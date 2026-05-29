package io.hellcoding.psa.repository;

import io.hellcoding.psa.domain.TimeEntry;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

    List<TimeEntry> findByEmployeeIdAndEntryDateBetweenOrderByEntryDateAscIdAsc(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<TimeEntry> findByProjectIdAndEntryDateBetween(Long projectId, LocalDate startDate, LocalDate endDate);

    List<TimeEntry> findByWeeklyTimesheetId(Long weeklyTimesheetId);

    @Query("""
            select coalesce(sum(t.hours), 0)
            from TimeEntry t
            where t.employee.id = :employeeId
              and t.entryDate = :entryDate
              and (:excludeId is null or t.id <> :excludeId)
            """)
    BigDecimal sumHoursForEmployeeOnDate(
            @Param("employeeId") Long employeeId,
            @Param("entryDate") LocalDate entryDate,
            @Param("excludeId") Long excludeId
    );
}
