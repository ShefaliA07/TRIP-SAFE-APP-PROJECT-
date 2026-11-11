package com.tripsafe.dto;

import com.tripsafe.model.RiskStatus;

public class SubjectDto {
    private Long id;
    private String name;
    private String code;
    private int classesConducted;
    private int classesAttended;
    private double minAttendancePercentage;
    private String color;
    private double currentAttendancePercentage;
    private RiskStatus currentStatus;

    public SubjectDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getClassesConducted() { return classesConducted; }
    public void setClassesConducted(int classesConducted) { this.classesConducted = classesConducted; }

    public int getClassesAttended() { return classesAttended; }
    public void setClassesAttended(int classesAttended) { this.classesAttended = classesAttended; }

    public double getMinAttendancePercentage() { return minAttendancePercentage; }
    public void setMinAttendancePercentage(double minAttendancePercentage) { this.minAttendancePercentage = minAttendancePercentage; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public double getCurrentAttendancePercentage() { return currentAttendancePercentage; }
    public void setCurrentAttendancePercentage(double currentAttendancePercentage) { this.currentAttendancePercentage = currentAttendancePercentage; }

    public RiskStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(RiskStatus currentStatus) { this.currentStatus = currentStatus; }
}
