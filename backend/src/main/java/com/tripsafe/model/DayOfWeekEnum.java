package com.tripsafe.model;

import java.time.DayOfWeek;

/**
 * Days of the week supported in the college timetable.
 */
public enum DayOfWeekEnum {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DayOfWeekEnum fromJavaDayOfWeek(DayOfWeek dayOfWeek) {
        return DayOfWeekEnum.valueOf(dayOfWeek.name());
    }
}
