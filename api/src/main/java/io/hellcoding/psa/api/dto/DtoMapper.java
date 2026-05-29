package io.hellcoding.psa.api.dto;

import io.hellcoding.psa.api.dto.Dtos.BizOptyResponse;
import io.hellcoding.psa.api.dto.Dtos.EmployeeResponse;
import io.hellcoding.psa.api.dto.Dtos.ProjectMemberResponse;
import io.hellcoding.psa.api.dto.Dtos.ProjectResponse;
import io.hellcoding.psa.api.dto.Dtos.ProjectTaskResponse;
import io.hellcoding.psa.api.dto.Dtos.TimeEntryResponse;
import io.hellcoding.psa.api.dto.Dtos.WeeklyTimesheetResponse;
import io.hellcoding.psa.domain.BizOpty;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.ProjectMember;
import io.hellcoding.psa.domain.ProjectTask;
import io.hellcoding.psa.domain.TimeEntry;
import io.hellcoding.psa.domain.WeeklyTimesheet;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static EmployeeResponse toEmployee(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getHourlyRate(),
                employee.getApprovalStatus()
        );
    }

    public static BizOptyResponse toBizOpty(BizOpty bizOpty) {
        return new BizOptyResponse(
                bizOpty.getId(),
                bizOpty.getName(),
                bizOpty.getClientName(),
                bizOpty.getDealStatus(),
                bizOpty.getContractAmount()
        );
    }

    public static ProjectResponse toProject(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getBizOpty().getId(),
                project.getBizOpty().getClientName(),
                project.getCode(),
                project.getName(),
                project.getStartDate(),
                project.getEndDate(),
                project.getBudget(),
                project.getEstimatedHours(),
                project.getStatus(),
                project.getProjectManager().getId()
        );
    }

    public static ProjectMemberResponse toProjectMember(ProjectMember member) {
        return new ProjectMemberResponse(
                member.getId(),
                member.getProject().getId(),
                member.getEmployee().getId(),
                member.getEmployee().getName(),
                member.getRole(),
                member.getStartDate(),
                member.getEndDate(),
                member.getAllocationPercent()
        );
    }

    public static ProjectTaskResponse toProjectTask(ProjectTask task) {
        return new ProjectTaskResponse(
                task.getId(),
                task.getProject().getId(),
                task.getName(),
                task.getDescription(),
                task.getEstimatedHours(),
                task.getSortOrder(),
                task.isActive()
        );
    }

    public static TimeEntryResponse toTimeEntry(TimeEntry entry) {
        Long weeklyTimesheetId = entry.getWeeklyTimesheet() == null ? null : entry.getWeeklyTimesheet().getId();
        return new TimeEntryResponse(
                entry.getId(),
                entry.getEmployee().getId(),
                entry.getProject().getId(),
                entry.getTask().getId(),
                weeklyTimesheetId,
                entry.getEntryDate(),
                entry.getHours(),
                entry.getHourlyRateSnapshot(),
                entry.getCostAmount(),
                entry.getStatus()
        );
    }

    public static WeeklyTimesheetResponse toWeeklyTimesheet(WeeklyTimesheet timesheet) {
        Long reviewedById = timesheet.getReviewedBy() == null ? null : timesheet.getReviewedBy().getId();
        return new WeeklyTimesheetResponse(
                timesheet.getId(),
                timesheet.getEmployee().getId(),
                timesheet.getWeekStartDate(),
                timesheet.getTotalHours(),
                timesheet.getStatus(),
                timesheet.getSubmittedAt(),
                timesheet.getReviewedAt(),
                reviewedById,
                timesheet.getRejectReason()
        );
    }
}
