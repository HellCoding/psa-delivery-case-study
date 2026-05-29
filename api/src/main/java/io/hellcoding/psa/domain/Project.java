package io.hellcoding.psa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "biz_opty_id", nullable = false)
    private BizOpty bizOpty;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal budget;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.PLANNED;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_manager_id", nullable = false)
    private Employee projectManager;

    protected Project() {
    }

    public Project(
            BizOpty bizOpty,
            String code,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal budget,
            BigDecimal estimatedHours,
            Employee projectManager
    ) {
        this.bizOpty = bizOpty;
        this.code = code;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.estimatedHours = estimatedHours;
        this.projectManager = projectManager;
    }

    public Long getId() {
        return id;
    }

    public BizOpty getBizOpty() {
        return bizOpty;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public BigDecimal getEstimatedHours() {
        return estimatedHours;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public Employee getProjectManager() {
        return projectManager;
    }
}
