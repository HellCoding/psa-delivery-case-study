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
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "time_entries")
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private ProjectTask task;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "weekly_timesheet_id")
    private WeeklyTimesheet weeklyTimesheet;

    @Column(nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hours;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal hourlyRateSnapshot;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal costAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeEntryStatus status = TimeEntryStatus.DRAFT;

    protected TimeEntry() {
    }

    public TimeEntry(
            Employee employee,
            Project project,
            ProjectTask task,
            LocalDate entryDate,
            BigDecimal hours,
            BigDecimal hourlyRateSnapshot
    ) {
        this.employee = employee;
        this.project = project;
        this.task = task;
        this.entryDate = entryDate;
        this.hourlyRateSnapshot = hourlyRateSnapshot;
        updateHours(hours);
    }

    public void updateHours(BigDecimal hours) {
        this.hours = hours;
        this.costAmount = hours.multiply(hourlyRateSnapshot).setScale(2, RoundingMode.HALF_UP);
        this.status = TimeEntryStatus.DRAFT;
    }

    public void submit(WeeklyTimesheet weeklyTimesheet) {
        this.weeklyTimesheet = weeklyTimesheet;
        this.status = TimeEntryStatus.SUBMITTED;
    }

    public void approve() {
        this.status = TimeEntryStatus.APPROVED;
    }

    public void reject() {
        this.status = TimeEntryStatus.REJECTED;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Project getProject() {
        return project;
    }

    public ProjectTask getTask() {
        return task;
    }

    public WeeklyTimesheet getWeeklyTimesheet() {
        return weeklyTimesheet;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public BigDecimal getHours() {
        return hours;
    }

    public BigDecimal getHourlyRateSnapshot() {
        return hourlyRateSnapshot;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public TimeEntryStatus getStatus() {
        return status;
    }
}
