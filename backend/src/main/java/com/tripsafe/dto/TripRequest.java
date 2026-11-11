package com.tripsafe.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TripRequest {
    private String tripName;

    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;

    @NotNull(message = "Return date is required")
    private LocalDate returnDate;

    private String notes;
    private boolean saveTrip = false;

    public TripRequest() {}

    public TripRequest(String tripName, LocalDate departureDate, LocalDate returnDate, String notes, boolean saveTrip) {
        this.tripName = tripName;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.notes = notes;
        this.saveTrip = saveTrip;
    }

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isSaveTrip() { return saveTrip; }
    public void setSaveTrip(boolean saveTrip) { this.saveTrip = saveTrip; }
}
