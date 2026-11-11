package com.tripsafe.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class MaxSafeDurationRequest {
    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    private int searchHorizonDays = 30; // Search window in days

    public MaxSafeDurationRequest() {}

    public MaxSafeDurationRequest(LocalDate departureDate, int searchHorizonDays) {
        this.departureDate = departureDate;
        this.searchHorizonDays = searchHorizonDays > 0 ? searchHorizonDays : 30;
    }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public int getSearchHorizonDays() { return searchHorizonDays; }
    public void setSearchHorizonDays(int searchHorizonDays) { this.searchHorizonDays = searchHorizonDays; }
}
