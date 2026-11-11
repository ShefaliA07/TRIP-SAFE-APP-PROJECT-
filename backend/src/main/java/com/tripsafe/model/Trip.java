package com.tripsafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entity representing a planned or historical student travel trip.
 * Persists the dates, overall risk status, and projected impact snapshot.
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Trip name or destination is required")
    @Column(nullable = false, length = 150)
    private String tripName;

    @NotNull(message = "Departure date is required")
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @NotNull(message = "Return date is required")
    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Column(length = 500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_status", nullable = false, length = 20)
    private RiskStatus overallStatus;

    @Column(name = "safe_subjects_count", nullable = false)
    private int safeSubjectsCount;

    @Column(name = "at_risk_subjects_count", nullable = false)
    private int atRiskSubjectsCount;

    @Column(name = "total_classes_missed", nullable = false)
    private int totalClassesMissed;

    @Lob
    @Column(name = "projection_details_json", columnDefinition = "TEXT")
    private String projectionDetailsJson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Trip() {
    }

    public Trip(String tripName, LocalDate departureDate, LocalDate returnDate, String notes) {
        validateDates(departureDate, returnDate);
        this.tripName = tripName;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.notes = notes;
        this.createdAt = LocalDateTime.now();
    }

    public static void validateDates(LocalDate departureDate, LocalDate returnDate) {
        if (departureDate == null || returnDate == null) {
            throw new IllegalArgumentException("Both departure date and return date must be provided.");
        }
        if (returnDate.isBefore(departureDate)) {
            throw new IllegalArgumentException("Return date (" + returnDate + ") cannot be before departure date (" + departureDate + ").");
        }
    }

    /**
     * Calculates the inclusive duration of the trip in days.
     */
    public long getDurationInDays() {
        if (departureDate == null || returnDate == null) return 0;
        return ChronoUnit.DAYS.between(departureDate, returnDate) + 1;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        validateDates(departureDate, this.returnDate);
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        validateDates(this.departureDate, returnDate);
        this.returnDate = returnDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public RiskStatus getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(RiskStatus overallStatus) {
        this.overallStatus = overallStatus;
    }

    public int getSafeSubjectsCount() {
        return safeSubjectsCount;
    }

    public void setSafeSubjectsCount(int safeSubjectsCount) {
        this.safeSubjectsCount = safeSubjectsCount;
    }

    public int getAtRiskSubjectsCount() {
        return atRiskSubjectsCount;
    }

    public void setAtRiskSubjectsCount(int atRiskSubjectsCount) {
        this.atRiskSubjectsCount = atRiskSubjectsCount;
    }

    public int getTotalClassesMissed() {
        return totalClassesMissed;
    }

    public void setTotalClassesMissed(int totalClassesMissed) {
        this.totalClassesMissed = totalClassesMissed;
    }

    public String getProjectionDetailsJson() {
        return projectionDetailsJson;
    }

    public void setProjectionDetailsJson(String projectionDetailsJson) {
        this.projectionDetailsJson = projectionDetailsJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
