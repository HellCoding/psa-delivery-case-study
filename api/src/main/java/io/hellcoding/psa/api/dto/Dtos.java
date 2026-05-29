package io.hellcoding.psa.api.dto;

import io.hellcoding.psa.domain.ApprovalStatus;
import io.hellcoding.psa.domain.DealStatus;
import io.hellcoding.psa.domain.ProjectStatus;
import io.hellcoding.psa.domain.TimeEntryStatus;
import io.hellcoding.psa.domain.TimesheetStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

public final class Dtos {

    private Dtos() {
    }

    public record CreateEmployeeRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @NotNull @Positive BigDecimal hourlyRate
    ) {
    }

    public record EmployeeResponse(
            Long id,
            String name,
            String email,
            BigDecimal hourlyRate,
            ApprovalStatus approvalStatus
    ) {
    }

    public record CreateBizOptyRequest(
            @NotBlank String name,
            @NotBlank String clientName,
            @NotNull DealStatus dealStatus,
            @NotNull @PositiveOrZero BigDecimal contractAmount
    ) {
    }

    public record BizOptyResponse(
            Long id,
            String name,
            String clientName,
            DealStatus dealStatus,
            BigDecimal contractAmount
    ) {
    }

    public record CreateProjectRequest(
            @NotNull Long bizOptyId,
            @NotBlank String code,
            @NotBlank String name,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate,
            @NotNull @PositiveOrZero BigDecimal budget,
            @NotNull @Positive BigDecimal estimatedHours,
            @NotNull Long projectManagerId
    ) {
    }

    public record ProjectResponse(
            Long id,
            Long bizOptyId,
            String clientName,
            String code,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal budget,
            BigDecimal estimatedHours,
            ProjectStatus status,
            Long projectManagerId
    ) {
    }

    public record AddProjectMemberRequest(
            @NotNull Long employeeId,
            @NotBlank String role,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate,
            @NotNull @DecimalMin("1.0") @DecimalMax("200.0") BigDecimal allocationPercent
    ) {
    }

    public record ProjectMemberResponse(
            Long id,
            Long projectId,
            Long employeeId,
            String employeeName,
            String role,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal allocationPercent
    ) {
    }

    public record CreateTaskRequest(
            @NotBlank String name,
            String description,
            @NotNull @Positive BigDecimal estimatedHours,
            @NotNull @PositiveOrZero Integer sortOrder
    ) {
    }

    public record ProjectTaskResponse(
            Long id,
            Long projectId,
            String name,
            String description,
            BigDecimal estimatedHours,
            Integer sortOrder,
            boolean active
    ) {
    }

    public record CreateTimeEntryRequest(
            @NotNull Long employeeId,
            @NotNull Long projectId,
            @NotNull Long taskId,
            @NotNull LocalDate entryDate,
            @NotNull @Positive BigDecimal hours
    ) {
    }

    public record UpdateTimeEntryRequest(
            @NotNull @Positive BigDecimal hours
    ) {
    }

    public record TimeEntryResponse(
            Long id,
            Long employeeId,
            Long projectId,
            Long taskId,
            Long weeklyTimesheetId,
            LocalDate entryDate,
            BigDecimal hours,
            BigDecimal hourlyRateSnapshot,
            BigDecimal costAmount,
            TimeEntryStatus status
    ) {
    }

    public record SubmitTimesheetRequest(
            @NotNull Long employeeId,
            @NotNull LocalDate weekStartDate
    ) {
    }

    public record ReviewTimesheetRequest(
            @NotNull Long reviewerId,
            String rejectReason
    ) {
    }

    public record WeeklyTimesheetResponse(
            Long id,
            Long employeeId,
            LocalDate weekStartDate,
            BigDecimal totalHours,
            TimesheetStatus status,
            Instant submittedAt,
            Instant reviewedAt,
            Long reviewedById,
            String rejectReason
    ) {
    }

    public record ProjectSummaryResponse(
            Long projectId,
            String projectCode,
            String projectName,
            String clientName,
            LocalDate from,
            LocalDate to,
            BigDecimal totalHours,
            BigDecimal costAmount,
            BigDecimal contractAmount,
            BigDecimal profitAmount,
            BigDecimal profitRate,
            BigDecimal progressRate,
            long activeMembers
    ) {
    }

    public record ApiErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path,
            Map<String, String> fieldErrors
    ) {
    }
}
