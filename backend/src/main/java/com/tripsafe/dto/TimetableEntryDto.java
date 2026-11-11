package com.tripsafe.dto;

import com.tripsafe.model.DayOfWeekEnum;

public class TimetableEntryDto {
    private Long id;
    private DayOfWeekEnum dayOfWeek;
    private int periodIndex;
    private String slotTime;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String color;

    public TimetableEntryDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DayOfWeekEnum getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeekEnum dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public int getPeriodIndex() { return periodIndex; }
    public void setPeriodIndex(int periodIndex) { this.periodIndex = periodIndex; }

    public String getSlotTime() { return slotTime; }
    public void setSlotTime(String slotTime) { this.slotTime = slotTime; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
