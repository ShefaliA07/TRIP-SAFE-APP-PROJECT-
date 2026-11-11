package com.tripsafe.controller;

import com.tripsafe.dto.DashboardSummaryDto;
import com.tripsafe.dto.SubjectDto;
import com.tripsafe.model.RiskStatus;
import com.tripsafe.model.Trip;
import com.tripsafe.service.AttendanceService;
import com.tripsafe.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final AttendanceService attendanceService;
    private final TripService tripService;

    public DashboardController(AttendanceService attendanceService, TripService tripService) {
        this.attendanceService = attendanceService;
        this.tripService = tripService;
    }

    @GetMapping
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
        List<SubjectDto> subjects = attendanceService.getAllSubjects();
        List<Trip> allTrips = tripService.getAllSavedTrips();

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setTotalSubjects(subjects.size());

        if (!subjects.isEmpty()) {
            double totalPct = subjects.stream()
                    .mapToDouble(SubjectDto::getCurrentAttendancePercentage)
                    .sum();
            summary.setAverageAttendance(totalPct / subjects.size());

            int safe = (int) subjects.stream().filter(s -> s.getCurrentStatus() == RiskStatus.SAFE).count();
            summary.setSafeSubjectsCount(safe);
            summary.setAtRiskSubjectsCount(subjects.size() - safe);
        } else {
            summary.setAverageAttendance(0.0);
            summary.setSafeSubjectsCount(0);
            summary.setAtRiskSubjectsCount(0);
        }

        summary.setSubjects(subjects);

        // Upcoming trip: first trip with departure date >= today
        LocalDate today = LocalDate.now();
        Trip upcoming = allTrips.stream()
                .filter(t -> !t.getDepartureDate().isBefore(today))
                .findFirst()
                .orElse(null);
        summary.setUpcomingTrip(upcoming);

        // Recent trips (up to 5)
        summary.setRecentTrips(allTrips.stream().limit(5).toList());

        return ResponseEntity.ok(summary);
    }
}
