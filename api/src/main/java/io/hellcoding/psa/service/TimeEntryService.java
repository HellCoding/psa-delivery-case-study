package io.hellcoding.psa.service;

import io.hellcoding.psa.api.dto.Dtos.CreateTimeEntryRequest;
import io.hellcoding.psa.api.dto.Dtos.UpdateTimeEntryRequest;
import io.hellcoding.psa.api.error.DomainException;
import io.hellcoding.psa.api.error.ResourceNotFoundException;
import io.hellcoding.psa.domain.ApprovalStatus;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.ProjectTask;
import io.hellcoding.psa.domain.TimeEntry;
import io.hellcoding.psa.domain.TimesheetStatus;
import io.hellcoding.psa.domain.WeeklyTimesheet;
import io.hellcoding.psa.repository.EmployeeRepository;
import io.hellcoding.psa.repository.ProjectMemberRepository;
import io.hellcoding.psa.repository.ProjectRepository;
import io.hellcoding.psa.repository.ProjectTaskRepository;
import io.hellcoding.psa.repository.TimeEntryRepository;
import io.hellcoding.psa.repository.WeeklyTimesheetRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimeEntryService {

    private static final BigDecimal DAILY_LIMIT = new BigDecimal("24.0");

    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final WeeklyTimesheetRepository weeklyTimesheetRepository;
    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryService(
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository,
            ProjectTaskRepository projectTaskRepository,
            ProjectMemberRepository projectMemberRepository,
            WeeklyTimesheetRepository weeklyTimesheetRepository,
            TimeEntryRepository timeEntryRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.weeklyTimesheetRepository = weeklyTimesheetRepository;
        this.timeEntryRepository = timeEntryRepository;
    }

    @Transactional
    public TimeEntry create(CreateTimeEntryRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.employeeId()));
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + request.projectId()));
        ProjectTask task = projectTaskRepository.findById(request.taskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + request.taskId()));

        validateEmployee(employee);
        validateProjectAssignment(employee, project, request.entryDate());
        validateTask(project, task);
        validateEntryDate(project, request.entryDate());
        validateHours(request.hours());
        ensureWeekIsEditable(employee.getId(), request.entryDate());
        ensureDailyLimit(employee.getId(), request.entryDate(), request.hours(), null);

        return timeEntryRepository.save(new TimeEntry(
                employee,
                project,
                task,
                request.entryDate(),
                request.hours(),
                employee.getHourlyRate()
        ));
    }

    @Transactional
    public TimeEntry update(Long entryId, UpdateTimeEntryRequest request) {
        TimeEntry entry = timeEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("TimeEntry not found: " + entryId));
        validateHours(request.hours());
        ensureWeekIsEditable(entry.getEmployee().getId(), entry.getEntryDate());
        ensureDailyLimit(entry.getEmployee().getId(), entry.getEntryDate(), request.hours(), entry.getId());
        entry.updateHours(request.hours());
        return entry;
    }

    @Transactional
    public void delete(Long entryId) {
        TimeEntry entry = timeEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResourceNotFoundException("TimeEntry not found: " + entryId));
        ensureWeekIsEditable(entry.getEmployee().getId(), entry.getEntryDate());
        timeEntryRepository.delete(entry);
    }

    @Transactional(readOnly = true)
    public List<TimeEntry> listByEmployeeWeek(Long employeeId, LocalDate weekStartDate) {
        LocalDate weekStart = WeekUtils.weekStart(weekStartDate);
        return timeEntryRepository.findByEmployeeIdAndEntryDateBetweenOrderByEntryDateAscIdAsc(
                employeeId,
                weekStart,
                WeekUtils.weekEnd(weekStart)
        );
    }

    private void validateEmployee(Employee employee) {
        if (employee.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new DomainException("Only approved employees can create time entries");
        }
    }

    private void validateProjectAssignment(Employee employee, Project project, LocalDate entryDate) {
        if (projectMemberRepository.countActiveAssignment(project.getId(), employee.getId(), entryDate) == 0) {
            throw new DomainException("Employee is not assigned to the project on the entry date");
        }
    }

    private void validateTask(Project project, ProjectTask task) {
        if (!task.getProject().getId().equals(project.getId())) {
            throw new DomainException("Task does not belong to the project");
        }
        if (!task.isActive()) {
            throw new DomainException("Inactive task cannot receive time entries");
        }
    }

    private void validateEntryDate(Project project, LocalDate entryDate) {
        if (entryDate.isAfter(LocalDate.now())) {
            throw new DomainException("Future time entries are not allowed");
        }
        if (entryDate.isBefore(project.getStartDate()) || entryDate.isAfter(project.getEndDate())) {
            throw new DomainException("Entry date must be within the project period");
        }
    }

    private void validateHours(BigDecimal hours) {
        if (hours.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Hours must be positive");
        }
        if (hours.compareTo(DAILY_LIMIT) > 0) {
            throw new DomainException("Hours cannot exceed 24 per day");
        }
        try {
            hours.multiply(BigDecimal.valueOf(2)).toBigIntegerExact();
        } catch (ArithmeticException ex) {
            throw new DomainException("Hours must be entered in 0.5 increments");
        }
    }

    private void ensureDailyLimit(Long employeeId, LocalDate entryDate, BigDecimal hours, Long excludeId) {
        BigDecimal currentHours = timeEntryRepository.sumHoursForEmployeeOnDate(employeeId, entryDate, excludeId);
        if (currentHours.add(hours).compareTo(DAILY_LIMIT) > 0) {
            throw new DomainException("Daily total hours cannot exceed 24");
        }
    }

    private void ensureWeekIsEditable(Long employeeId, LocalDate entryDate) {
        LocalDate weekStart = WeekUtils.weekStart(entryDate);
        weeklyTimesheetRepository.findByEmployeeIdAndWeekStartDate(employeeId, weekStart)
                .map(WeeklyTimesheet::getStatus)
                .filter(status -> status == TimesheetStatus.SUBMITTED || status == TimesheetStatus.APPROVED)
                .ifPresent(status -> {
                    throw new DomainException("Submitted or approved weeks cannot be edited");
                });
    }
}
