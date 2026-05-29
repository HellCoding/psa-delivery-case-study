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
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
        name = "weekly_timesheets",
        uniqueConstraints = @UniqueConstraint(name = "uk_employee_week", columnNames = {"employee_id", "week_start_date"})
)
public class WeeklyTimesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "week_start_date", nullable = false)
    private LocalDate weekStartDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalHours = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimesheetStatus status = TimesheetStatus.DRAFT;

    private Instant submittedAt;

    private Instant reviewedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewed_by_id")
    private Employee reviewedBy;

    @Column(length = 1000)
    private String rejectReason;

    protected WeeklyTimesheet() {
    }

    public WeeklyTimesheet(Employee employee, LocalDate weekStartDate) {
        this.employee = employee;
        this.weekStartDate = weekStartDate;
    }

    public void submit(BigDecimal totalHours) {
        this.totalHours = totalHours;
        this.status = TimesheetStatus.SUBMITTED;
        this.submittedAt = Instant.now();
        this.reviewedAt = null;
        this.reviewedBy = null;
        this.rejectReason = null;
    }

    public void approve(Employee reviewer) {
        this.status = TimesheetStatus.APPROVED;
        this.reviewedAt = Instant.now();
        this.reviewedBy = reviewer;
        this.rejectReason = null;
    }

    public void reject(Employee reviewer, String rejectReason) {
        this.status = TimesheetStatus.REJECTED;
        this.reviewedAt = Instant.now();
        this.reviewedBy = reviewer;
        this.rejectReason = rejectReason;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public TimesheetStatus getStatus() {
        return status;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public Employee getReviewedBy() {
        return reviewedBy;
    }

    public String getRejectReason() {
        return rejectReason;
    }
}
