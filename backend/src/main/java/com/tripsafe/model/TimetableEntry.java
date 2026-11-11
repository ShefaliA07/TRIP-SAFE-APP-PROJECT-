package com.tripsafe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Entity representing a timetable slot in the student's weekly schedule.
 * Maps a specific day of week and period index to a Subject.
 */
@Entity
@Table(name = "timetable_entries", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"day_of_week", "period_index"})
})
public class TimetableEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Day of week is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeekEnum dayOfWeek;

    @Min(value = 1, message = "Period index must be at least 1")
    @Max(value = 10, message = "Period index cannot exceed 10")
    @Column(name = "period_index", nullable = false)
    private int periodIndex;

    @NotBlank(message = "Slot time label is required")
    @Column(name = "slot_time", nullable = false, length = 30)
    private String slotTime; // e.g. "09:00 - 10:00 AM"

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Subject subject;

    public TimetableEntry() {
    }

    public TimetableEntry(DayOfWeekEnum dayOfWeek, int periodIndex, String slotTime, Subject subject) {
        this.dayOfWeek = dayOfWeek;
        this.periodIndex = periodIndex;
        this.slotTime = slotTime;
        this.subject = subject;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeekEnum getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(int periodIndex) {
        this.periodIndex = periodIndex;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableEntry that)) return false;
        return periodIndex == that.periodIndex && dayOfWeek == that.dayOfWeek;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, periodIndex);
    }
}
