package io.hellcoding.psa.service;

import io.hellcoding.psa.api.dto.Dtos.AddProjectMemberRequest;
import io.hellcoding.psa.api.dto.Dtos.CreateBizOptyRequest;
import io.hellcoding.psa.api.dto.Dtos.CreateProjectRequest;
import io.hellcoding.psa.api.dto.Dtos.CreateTaskRequest;
import io.hellcoding.psa.api.error.DomainException;
import io.hellcoding.psa.api.error.ResourceNotFoundException;
import io.hellcoding.psa.domain.BizOpty;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.ProjectMember;
import io.hellcoding.psa.domain.ProjectTask;
import io.hellcoding.psa.repository.BizOptyRepository;
import io.hellcoding.psa.repository.EmployeeRepository;
import io.hellcoding.psa.repository.ProjectMemberRepository;
import io.hellcoding.psa.repository.ProjectRepository;
import io.hellcoding.psa.repository.ProjectTaskRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final BizOptyRepository bizOptyRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectTaskRepository projectTaskRepository;

    public ProjectService(
            BizOptyRepository bizOptyRepository,
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            ProjectTaskRepository projectTaskRepository
    ) {
        this.bizOptyRepository = bizOptyRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectTaskRepository = projectTaskRepository;
    }

    @Transactional
    public BizOpty createBizOpty(CreateBizOptyRequest request) {
        return bizOptyRepository.save(new BizOpty(
                request.name(),
                request.clientName(),
                request.dealStatus(),
                request.contractAmount()
        ));
    }

    @Transactional(readOnly = true)
    public List<BizOpty> listBizOpties() {
        return bizOptyRepository.findAll();
    }

    @Transactional
    public Project createProject(CreateProjectRequest request) {
        if (projectRepository.existsByCode(request.code())) {
            throw new DomainException("Project code already exists: " + request.code());
        }
        if (request.endDate().isBefore(request.startDate())) {
            throw new DomainException("Project endDate must be on or after startDate");
        }
        BizOpty bizOpty = bizOptyRepository.findById(request.bizOptyId())
                .orElseThrow(() -> new ResourceNotFoundException("BizOpty not found: " + request.bizOptyId()));
        Employee projectManager = employeeRepository.findById(request.projectManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Project manager not found: " + request.projectManagerId()));

        return projectRepository.save(new Project(
                bizOpty,
                request.code(),
                request.name(),
                request.startDate(),
                request.endDate(),
                request.budget(),
                request.estimatedHours(),
                projectManager
        ));
    }

    @Transactional(readOnly = true)
    public List<Project> listProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
    }

    @Transactional
    public ProjectMember addMember(Long projectId, AddProjectMemberRequest request) {
        Project project = getProject(projectId);
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.employeeId()));
        if (projectMemberRepository.existsByProjectIdAndEmployeeId(projectId, request.employeeId())) {
            throw new DomainException("Employee is already assigned to this project");
        }
        if (request.endDate().isBefore(request.startDate())) {
            throw new DomainException("Member endDate must be on or after startDate");
        }
        if (request.startDate().isBefore(project.getStartDate()) || request.endDate().isAfter(project.getEndDate())) {
            throw new DomainException("Member assignment period must be within the project period");
        }
        validateAllocation(request.allocationPercent());
        return projectMemberRepository.save(new ProjectMember(
                project,
                employee,
                request.role(),
                request.startDate(),
                request.endDate(),
                request.allocationPercent()
        ));
    }

    @Transactional
    public ProjectTask createTask(Long projectId, CreateTaskRequest request) {
        Project project = getProject(projectId);
        return projectTaskRepository.save(new ProjectTask(
                project,
                request.name(),
                request.description(),
                request.estimatedHours(),
                request.sortOrder()
        ));
    }

    @Transactional(readOnly = true)
    public List<ProjectTask> listTasks(Long projectId) {
        return projectTaskRepository.findByProjectIdAndActiveTrueOrderBySortOrderAsc(projectId);
    }

    private void validateAllocation(BigDecimal allocationPercent) {
        if (allocationPercent.compareTo(BigDecimal.ONE) < 0 || allocationPercent.compareTo(new BigDecimal("200")) > 0) {
            throw new DomainException("Allocation percent must be between 1 and 200");
        }
    }
}
