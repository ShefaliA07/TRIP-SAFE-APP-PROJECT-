package com.tripsafe.controller;

import com.tripsafe.dto.MaxSafeDurationRequest;
import com.tripsafe.dto.MaxSafeDurationResponse;
import com.tripsafe.dto.TripProjectionResponse;
import com.tripsafe.dto.TripRequest;
import com.tripsafe.model.Trip;
import com.tripsafe.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/project")
    public ResponseEntity<TripProjectionResponse> projectTripRisk(@Valid @RequestBody TripRequest request) {
        TripProjectionResponse response = tripService.calculateProjection(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TripProjectionResponse> planAndSaveTrip(@Valid @RequestBody TripRequest request) {
        request.setSaveTrip(true);
        TripProjectionResponse response = tripService.calculateProjection(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/max-safe-duration")
    public ResponseEntity<MaxSafeDurationResponse> findMaxSafeDuration(@Valid @RequestBody MaxSafeDurationRequest request) {
        MaxSafeDurationResponse response = tripService.findMaxSafeDuration(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllSavedTrips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        return tripService.getTripById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
