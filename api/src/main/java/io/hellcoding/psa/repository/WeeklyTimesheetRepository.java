package io.hellcoding.psa.repository;

import io.hellcoding.psa.domain.WeeklyTimesheet;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyTimesheetRepository extends JpaRepository<WeeklyTimesheet, Long> {

    Optional<WeeklyTimesheet> findByEmployeeIdAndWeekStartDate(Long employeeId, LocalDate weekStartDate);
}
