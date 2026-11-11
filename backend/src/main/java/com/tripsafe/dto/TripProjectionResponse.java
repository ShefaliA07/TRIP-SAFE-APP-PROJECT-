package com.tripsafe.dto;

import com.tripsafe.model.RiskStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripProjectionResponse {
    private String tripName;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private long durationDays;
    private RiskStatus overallStatus;
    private int safeSubjectsCount;
    private int atRiskSubjectsCount;
    private int totalMissedClasses;
    private List<SubjectProjectionResult> subjectProjections = new ArrayList<>();
    private String bottleneckSubjectName;
    private List<String> mostAffectedSubjects = new ArrayList<>();
    private String summaryMessage;
    private String recommendationMessage;
    private Long savedTripId;

    public TripProjectionResponse() {}

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public long getDurationDays() { return durationDays; }
    public void setDurationDays(long durationDays) { this.durationDays = durationDays; }

    public RiskStatus getOverallStatus() { return overallStatus; }
    public void setOverallStatus(RiskStatus overallStatus) { this.overallStatus = overallStatus; }

    public int getSafeSubjectsCount() { return safeSubjectsCount; }
    public void setSafeSubjectsCount(int safeSubjectsCount) { this.safeSubjectsCount = safeSubjectsCount; }

    public int getAtRiskSubjectsCount() { return atRiskSubjectsCount; }
    public void setAtRiskSubjectsCount(int atRiskSubjectsCount) { this.atRiskSubjectsCount = atRiskSubjectsCount; }

    public int getTotalMissedClasses() { return totalMissedClasses; }
    public void setTotalMissedClasses(int totalMissedClasses) { this.totalMissedClasses = totalMissedClasses; }

    public List<SubjectProjectionResult> getSubjectProjections() { return subjectProjections; }
    public void setSubjectProjections(List<SubjectProjectionResult> subjectProjections) { this.subjectProjections = subjectProjections; }

    public String getBottleneckSubjectName() { return bottleneckSubjectName; }
    public void setBottleneckSubjectName(String bottleneckSubjectName) { this.bottleneckSubjectName = bottleneckSubjectName; }

    public List<String> getMostAffectedSubjects() { return mostAffectedSubjects; }
    public void setMostAffectedSubjects(List<String> mostAffectedSubjects) { this.mostAffectedSubjects = mostAffectedSubjects; }

    public String getSummaryMessage() { return summaryMessage; }
    public void setSummaryMessage(String summaryMessage) { this.summaryMessage = summaryMessage; }

    public String getRecommendationMessage() { return recommendationMessage; }
    public void setRecommendationMessage(String recommendationMessage) { this.recommendationMessage = recommendationMessage; }

    public Long getSavedTripId() { return savedTripId; }
    public void setSavedTripId(Long savedTripId) { this.savedTripId = savedTripId; }
}
