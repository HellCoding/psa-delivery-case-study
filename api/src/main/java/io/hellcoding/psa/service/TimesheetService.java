package io.hellcoding.psa.service;

import io.hellcoding.psa.api.dto.Dtos.ReviewTimesheetRequest;
import io.hellcoding.psa.api.dto.Dtos.SubmitTimesheetRequest;
import io.hellcoding.psa.api.error.DomainException;
import io.hellcoding.psa.api.error.ResourceNotFoundException;
import io.hellcoding.psa.domain.ApprovalStatus;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.TimeEntry;
import io.hellcoding.psa.domain.TimesheetStatus;
import io.hellcoding.psa.domain.WeeklyTimesheet;
import io.hellcoding.psa.repository.EmployeeRepository;
import io.hellcoding.psa.repository.ProjectMemberRepository;
import io.hellcoding.psa.repository.TimeEntryRepository;
import io.hellcoding.psa.repository.WeeklyTimesheetRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimesheetService {

    private final EmployeeRepository employeeRepository;
    private final WeeklyTimesheetRepository weeklyTimesheetRepository;
    private final TimeEntryRepository timeEntryRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public TimesheetService(
            EmployeeRepository employeeRepository,
            WeeklyTimesheetRepository weeklyTimesheetRepository,
            TimeEntryRepository timeEntryRepository,
            ProjectMemberRepository projectMemberRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.weeklyTimesheetRepository = weeklyTimesheetRepository;
        this.timeEntryRepository = timeEntryRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Transactional
    public WeeklyTimesheet submit(SubmitTimesheetRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.employeeId()));
        if (employee.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new DomainException("Only approved employees can submit timesheets");
        }

        LocalDate weekStart = WeekUtils.weekStart(request.weekStartDate());
        WeeklyTimesheet timesheet = weeklyTimesheetRepository
                .findByEmployeeIdAndWeekStartDate(employee.getId(), weekStart)
                .orElseGet(() -> new WeeklyTimesheet(employee, weekStart));

        if (timesheet.getStatus() == TimesheetStatus.SUBMITTED || timesheet.getStatus() == TimesheetStatus.APPROVED) {
            throw new DomainException("Timesheet is already submitted or approved");
        }

        List<TimeEntry> entries = timeEntryRepository.findByEmployeeIdAndEntryDateBetweenOrderByEntryDateAscIdAsc(
                employee.getId(),
                weekStart,
                WeekUtils.weekEnd(weekStart)
        );
        BigDecimal totalHours = entries.stream()
                .map(TimeEntry::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        timesheet.submit(totalHours);
        WeeklyTimesheet saved = weeklyTimesheetRepository.save(timesheet);
        entries.forEach(entry -> entry.submit(saved));
        return saved;
    }

    @Transactional
    public WeeklyTimesheet approve(Long timesheetId, ReviewTimesheetRequest request) {
        WeeklyTimesheet timesheet = getSubmittedTimesheet(timesheetId);
        Employee reviewer = employeeRepository.findById(request.reviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found: " + request.reviewerId()));
        List<TimeEntry> entries = timeEntryRepository.findByWeeklyTimesheetId(timesheetId);
        validateReviewerCanReview(timesheet, reviewer, entries);

        timesheet.approve(reviewer);
        entries.forEach(TimeEntry::approve);
        return timesheet;
    }

    @Transactional
    public WeeklyTimesheet reject(Long timesheetId, ReviewTimesheetRequest request) {
        if (request.rejectReason() == null || request.rejectReason().isBlank()) {
            throw new DomainException("Reject reason is required");
        }
        WeeklyTimesheet timesheet = getSubmittedTimesheet(timesheetId);
        Employee reviewer = employeeRepository.findById(request.reviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found: " + request.reviewerId()));
        List<TimeEntry> entries = timeEntryRepository.findByWeeklyTimesheetId(timesheetId);
        validateReviewerCanReview(timesheet, reviewer, entries);

        timesheet.reject(reviewer, request.rejectReason().trim());
        entries.forEach(TimeEntry::reject);
        return timesheet;
    }

    @Transactional(readOnly = true)
    public WeeklyTimesheet get(Long timesheetId) {
        return weeklyTimesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet not found: " + timesheetId));
    }

    private WeeklyTimesheet getSubmittedTimesheet(Long timesheetId) {
        WeeklyTimesheet timesheet = weeklyTimesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet not found: " + timesheetId));
        if (timesheet.getStatus() != TimesheetStatus.SUBMITTED) {
            throw new DomainException("Only submitted timesheets can be reviewed");
        }
        return timesheet;
    }

    private void validateReviewerCanReview(WeeklyTimesheet timesheet, Employee reviewer, List<TimeEntry> entries) {
        if (entries.isEmpty()) {
            LocalDate weekStart = timesheet.getWeekStartDate();
            boolean reviewerManagesAnActiveAssignment = projectMemberRepository.countActiveAssignmentsManagedByReviewer(
                    timesheet.getEmployee().getId(),
                    reviewer.getId(),
                    weekStart,
                    WeekUtils.weekEnd(weekStart)
            ) > 0;
            if (!reviewerManagesAnActiveAssignment) {
                throw new DomainException("Reviewer must be the project manager for an active assignment in the submitted week");
            }
            return;
        }

        boolean reviewerIsPmForAllProjects = entries.stream()
                .map(TimeEntry::getProject)
                .map(Project::getProjectManager)
                .allMatch(projectManager -> projectManager.getId().equals(reviewer.getId()));
        if (!reviewerIsPmForAllProjects) {
            throw new DomainException("Reviewer must be the project manager for all submitted entries");
        }
    }
}
