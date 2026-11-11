package com.tripsafe.dto;

import com.tripsafe.model.Trip;
import java.util.ArrayList;
import java.util.List;

public class DashboardSummaryDto {
    private int totalSubjects;
    private double averageAttendance;
    private int safeSubjectsCount;
    private int atRiskSubjectsCount;
    private Trip upcomingTrip;
    private List<SubjectDto> subjects = new ArrayList<>();
    private List<Trip> recentTrips = new ArrayList<>();

    public DashboardSummaryDto() {}

    public int getTotalSubjects() { return totalSubjects; }
    public void setTotalSubjects(int totalSubjects) { this.totalSubjects = totalSubjects; }

    public double getAverageAttendance() { return averageAttendance; }
    public void setAverageAttendance(double averageAttendance) { this.averageAttendance = averageAttendance; }

    public int getSafeSubjectsCount() { return safeSubjectsCount; }
    public void setSafeSubjectsCount(int safeSubjectsCount) { this.safeSubjectsCount = safeSubjectsCount; }

    public int getAtRiskSubjectsCount() { return atRiskSubjectsCount; }
    public void setAtRiskSubjectsCount(int atRiskSubjectsCount) { this.atRiskSubjectsCount = atRiskSubjectsCount; }

    public Trip getUpcomingTrip() { return upcomingTrip; }
    public void setUpcomingTrip(Trip upcomingTrip) { this.upcomingTrip = upcomingTrip; }

    public List<SubjectDto> getSubjects() { return subjects; }
    public void setSubjects(List<SubjectDto> subjects) { this.subjects = subjects; }

    public List<Trip> getRecentTrips() { return recentTrips; }
    public void setRecentTrips(List<Trip> recentTrips) { this.recentTrips = recentTrips; }
}
