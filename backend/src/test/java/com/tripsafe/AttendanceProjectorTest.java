package com.tripsafe;

import com.tripsafe.service.AttendanceProjector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AttendanceProjector Unit Tests - Mathematical Accuracy")
public class AttendanceProjectorTest {

    private AttendanceProjector projector;

    @BeforeEach
    public void setUp() {
        projector = new AttendanceProjector();
    }

    @Test
    @DisplayName("Verify Core Specification: 60/75 attended yields 80.0% current attendance")
    public void testCurrentAttendanceCalculation() {
        int attended = 60;
        int conducted = 75;

        double currentPct = projector.calculateCurrentPercentage(attended, conducted);

        assertEquals(80.0, currentPct, 0.0001, "Current attendance should be exactly 80.0%");
    }

    @Test
    @DisplayName("Verify Core Specification: 60 attended, 75 conducted, +5 missed yields 75.0% projected attendance")
    public void testProjectedAttendanceFiveMissedClasses() {
        int attended = 60;
        int conducted = 75;
        int missed = 5;

        // 60 / (75 + 5) * 100 = 60 / 80 * 100 = 75.0%
        double projectedPct = projector.calculateProjectedPercentage(attended, conducted, missed);

        assertEquals(75.0, projectedPct, 0.0001, "Projected attendance should be exactly 75.0%");
    }

    @Test
    @DisplayName("Verify Core Specification: 60 attended, 75 conducted, +6 missed yields ~74.07% projected attendance")
    public void testProjectedAttendanceSixMissedClasses() {
        int attended = 60;
        int conducted = 75;
        int missed = 6;

        // 60 / (75 + 6) * 100 = 60 / 81 * 100 = 74.07407...%
        double projectedPct = projector.calculateProjectedPercentage(attended, conducted, missed);

        assertEquals(74.074, projectedPct, 0.001, "Projected attendance should be approximately 74.074%");
        assertTrue(projectedPct < 75.0, "Projected attendance must be strictly below 75%");
    }

    @Test
    @DisplayName("Handle Zero Conducted Classes Gracefully Without Division Errors")
    public void testZeroConductedClasses() {
        double currentPct = projector.calculateCurrentPercentage(0, 0);
        assertEquals(100.0, currentPct, 0.0001, "Zero conducted classes should yield 100% default safely");

        double projectedPct = projector.calculateProjectedPercentage(0, 0, 0);
        assertEquals(100.0, projectedPct, 0.0001);
    }

    @Test
    @DisplayName("Reject Invalid Inputs: Attended greater than conducted")
    public void testAttendedGreaterThanConductedThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            projector.calculateCurrentPercentage(10, 5);
        });
    }

    @Test
    @DisplayName("Reject Negative Class Counts")
    public void testNegativeClassCountsThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            projector.calculateCurrentPercentage(-1, 10);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            projector.calculateProjectedPercentage(10, 10, -2);
        });
    }
}
