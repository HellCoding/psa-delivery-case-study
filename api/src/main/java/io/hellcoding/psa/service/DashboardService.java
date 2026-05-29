package io.hellcoding.psa.service;

import io.hellcoding.psa.api.dto.Dtos.ProjectSummaryResponse;
import io.hellcoding.psa.api.error.DomainException;
import io.hellcoding.psa.api.error.ResourceNotFoundException;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.TimeEntry;
import io.hellcoding.psa.repository.ProjectMemberRepository;
import io.hellcoding.psa.repository.ProjectRepository;
import io.hellcoding.psa.repository.TimeEntryRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TimeEntryRepository timeEntryRepository;

    public DashboardService(
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            TimeEntryRepository timeEntryRepository
    ) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.timeEntryRepository = timeEntryRepository;
    }

    @Transactional(readOnly = true)
    public ProjectSummaryResponse projectSummary(Long projectId, LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new DomainException("to must be on or after from");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        List<TimeEntry> entries = timeEntryRepository.findByProjectIdAndEntryDateBetween(projectId, from, to);

        BigDecimal totalHours = entries.stream()
                .map(TimeEntry::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal costAmount = entries.stream()
                .map(TimeEntry::getCostAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal contractAmount = project.getBizOpty().getContractAmount();
        BigDecimal profitAmount = contractAmount.subtract(costAmount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal profitRate = percentage(profitAmount, contractAmount);
        BigDecimal progressRate = percentage(totalHours, project.getEstimatedHours());

        return new ProjectSummaryResponse(
                project.getId(),
                project.getCode(),
                project.getName(),
                project.getBizOpty().getClientName(),
                from,
                to,
                totalHours,
                costAmount,
                contractAmount,
                profitAmount,
                profitRate,
                progressRate,
                projectMemberRepository.countByProjectId(projectId)
        );
    }

    private BigDecimal percentage(BigDecimal numerator, BigDecimal denominator) {
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return numerator.multiply(new BigDecimal("100"))
                .divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
