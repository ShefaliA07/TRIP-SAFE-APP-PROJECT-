package com.tripsafe.dto;

import com.tripsafe.model.RiskStatus;

public class SubjectProjectionResult {
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String color;
    private int classesConductedBefore;
    private int classesAttended;
    private double currentAttendancePercentage;
    private int expectedMissedClasses;
    private int classesConductedAfter;
    private double projectedAttendancePercentage;
    private double requiredAttendancePercentage;
    private RiskStatus status;
    private double riskMargin; // projected - required (negative means deficit)
    private String explanation;

    public SubjectProjectionResult() {}

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getClassesConductedBefore() { return classesConductedBefore; }
    public void setClassesConductedBefore(int classesConductedBefore) { this.classesConductedBefore = classesConductedBefore; }

    public int getClassesAttended() { return classesAttended; }
    public void setClassesAttended(int classesAttended) { this.classesAttended = classesAttended; }

    public double getCurrentAttendancePercentage() { return currentAttendancePercentage; }
    public void setCurrentAttendancePercentage(double currentAttendancePercentage) { this.currentAttendancePercentage = currentAttendancePercentage; }

    public int getExpectedMissedClasses() { return expectedMissedClasses; }
    public void setExpectedMissedClasses(int expectedMissedClasses) { this.expectedMissedClasses = expectedMissedClasses; }

    public int getClassesConductedAfter() { return classesConductedAfter; }
    public void setClassesConductedAfter(int classesConductedAfter) { this.classesConductedAfter = classesConductedAfter; }

    public double getProjectedAttendancePercentage() { return projectedAttendancePercentage; }
    public void setProjectedAttendancePercentage(double projectedAttendancePercentage) { this.projectedAttendancePercentage = projectedAttendancePercentage; }

    public double getRequiredAttendancePercentage() { return requiredAttendancePercentage; }
    public void setRequiredAttendancePercentage(double requiredAttendancePercentage) { this.requiredAttendancePercentage = requiredAttendancePercentage; }

    public RiskStatus getStatus() { return status; }
    public void setStatus(RiskStatus status) { this.status = status; }

    public double getRiskMargin() { return riskMargin; }
    public void setRiskMargin(double riskMargin) { this.riskMargin = riskMargin; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
