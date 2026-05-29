package io.hellcoding.psa.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

final class WeekUtils {

    private WeekUtils() {
    }

    static LocalDate weekStart(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    static LocalDate weekEnd(LocalDate weekStart) {
        return weekStart.plusDays(6);
    }
}
