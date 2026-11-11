package com.tripsafe.dto;

import com.tripsafe.model.RiskStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaxSafeDurationResponse {
    private LocalDate departureDate;
    private LocalDate recommendedReturnDate;
    private int maxSafeDays;
    private boolean departureDateSafe;
    private boolean reachedHorizonWithoutFailure;
    private int searchHorizonDays;
    private String limitingSubjectName;
    private String limitingSubjectCode;
    private double limitingSubjectMargin;
    private String explanation;
    private boolean noClassesMissed;
    private List<SubjectProjectionResult> projectedResultsAtRecommendedDate = new ArrayList<>();

    public MaxSafeDurationResponse() {}

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getRecommendedReturnDate() { return recommendedReturnDate; }
    public void setRecommendedReturnDate(LocalDate recommendedReturnDate) { this.recommendedReturnDate = recommendedReturnDate; }

    public int getMaxSafeDays() { return maxSafeDays; }
    public void setMaxSafeDays(int maxSafeDays) { this.maxSafeDays = maxSafeDays; }

    public boolean isDepartureDateSafe() { return departureDateSafe; }
    public void setDepartureDateSafe(boolean departureDateSafe) { this.departureDateSafe = departureDateSafe; }

    public boolean isReachedHorizonWithoutFailure() { return reachedHorizonWithoutFailure; }
    public void setReachedHorizonWithoutFailure(boolean reachedHorizonWithoutFailure) { this.reachedHorizonWithoutFailure = reachedHorizonWithoutFailure; }

    public int getSearchHorizonDays() { return searchHorizonDays; }
    public void setSearchHorizonDays(int searchHorizonDays) { this.searchHorizonDays = searchHorizonDays; }

    public String getLimitingSubjectName() { return limitingSubjectName; }
    public void setLimitingSubjectName(String limitingSubjectName) { this.limitingSubjectName = limitingSubjectName; }

    public String getLimitingSubjectCode() { return limitingSubjectCode; }
    public void setLimitingSubjectCode(String limitingSubjectCode) { this.limitingSubjectCode = limitingSubjectCode; }

    public double getLimitingSubjectMargin() { return limitingSubjectMargin; }
    public void setLimitingSubjectMargin(double limitingSubjectMargin) { this.limitingSubjectMargin = limitingSubjectMargin; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public boolean isNoClassesMissed() { return noClassesMissed; }
    public void setNoClassesMissed(boolean noClassesMissed) { this.noClassesMissed = noClassesMissed; }

    public List<SubjectProjectionResult> getProjectedResultsAtRecommendedDate() { return projectedResultsAtRecommendedDate; }
    public void setProjectedResultsAtRecommendedDate(List<SubjectProjectionResult> projectedResultsAtRecommendedDate) { this.projectedResultsAtRecommendedDate = projectedResultsAtRecommendedDate; }
}
