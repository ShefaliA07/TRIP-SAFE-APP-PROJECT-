package com.tripsafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripsafe.dto.TripProjectionResponse;
import com.tripsafe.dto.TripRequest;
import com.tripsafe.model.RiskStatus;
import com.tripsafe.model.Subject;
import com.tripsafe.repository.SubjectRepository;
import com.tripsafe.repository.TripRepository;
import com.tripsafe.service.AttendanceProjector;
import com.tripsafe.service.RiskAnalyzer;
import com.tripsafe.service.TimetableService;
import com.tripsafe.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("TripService Unit Tests - End-to-End Projection Calculation")
public class TripServiceTest {

    private SubjectRepository subjectRepository;
    private TimetableService timetableService;
    private AttendanceProjector attendanceProjector;
    private RiskAnalyzer riskAnalyzer;
    private TripRepository tripRepository;
    private ObjectMapper objectMapper;
    private TripService tripService;

    @BeforeEach
    public void setUp() {
        subjectRepository = Mockito.mock(SubjectRepository.class);
        timetableService = Mockito.mock(TimetableService.class);
        attendanceProjector = new AttendanceProjector();
        riskAnalyzer = new RiskAnalyzer();
        tripRepository = Mockito.mock(TripRepository.class);
        objectMapper = new ObjectMapper();

        tripService = new TripService(
                subjectRepository,
                timetableService,
                attendanceProjector,
                riskAnalyzer,
                tripRepository,
                objectMapper
        );
    }

    @Test
    @DisplayName("Verify Full Specification Pipeline: Subject with 60/75, 5 missed classes remains SAFE")
    public void testTripWithFiveMissedClassesSafe() {
        Subject dsa = new Subject("Data Structures", "CS301", 75, 60, 75.0, "#4f46e5");
        dsa.setId(1L);

        when(subjectRepository.findAll()).thenReturn(List.of(dsa));
        when(timetableService.countExpectedMissedClassesPerSubject(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Map.of(1L, 5));

        LocalDate dep = LocalDate.of(2026, 11, 2);
        LocalDate ret = LocalDate.of(2026, 11, 6);
        TripRequest request = new TripRequest("Weekend Getaway", dep, ret, "Visiting home", false);

        TripProjectionResponse response = tripService.calculateProjection(request);

        assertNotNull(response);
        assertEquals(RiskStatus.SAFE, response.getOverallStatus());
        assertEquals(1, response.getSafeSubjectsCount());
        assertEquals(0, response.getAtRiskSubjectsCount());
        assertEquals(75.0, response.getSubjectProjections().get(0).getProjectedAttendancePercentage(), 0.001);
    }

    @Test
    @DisplayName("Verify Full Specification Pipeline: Subject with 60/75, 6 missed classes becomes AT_RISK")
    public void testTripWithSixMissedClassesAtRisk() {
        Subject dsa = new Subject("Data Structures", "CS301", 75, 60, 75.0, "#4f46e5");
        dsa.setId(1L);

        when(subjectRepository.findAll()).thenReturn(List.of(dsa));
        when(timetableService.countExpectedMissedClassesPerSubject(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Map.of(1L, 6));

        LocalDate dep = LocalDate.of(2026, 11, 2);
        LocalDate ret = LocalDate.of(2026, 11, 7);
        TripRequest request = new TripRequest("Extended Weekend", dep, ret, "Festival trip", false);

        TripProjectionResponse response = tripService.calculateProjection(request);

        assertNotNull(response);
        assertEquals(RiskStatus.AT_RISK, response.getOverallStatus(), "Overall trip must be AT_RISK");
        assertEquals(0, response.getSafeSubjectsCount());
        assertEquals(1, response.getAtRiskSubjectsCount());
        assertTrue(response.getSubjectProjections().get(0).getProjectedAttendancePercentage() < 75.0);
    }
}
