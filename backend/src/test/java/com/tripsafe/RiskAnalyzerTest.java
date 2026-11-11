package com.tripsafe;

import com.tripsafe.dto.SubjectProjectionResult;
import com.tripsafe.model.RiskStatus;
import com.tripsafe.service.RiskAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RiskAnalyzer Unit Tests - Threshold and Multi-Subject Integrity")
public class RiskAnalyzerTest {

    private RiskAnalyzer riskAnalyzer;

    @BeforeEach
    public void setUp() {
        riskAnalyzer = new RiskAnalyzer();
    }

    @Test
    @DisplayName("Evaluate Status: Projected exactly equal to required (75.0% >= 75.0%) is SAFE")
    public void testExactlyAtThresholdIsSafe() {
        RiskStatus status = riskAnalyzer.evaluateStatus(75.0, 75.0);
        assertEquals(RiskStatus.SAFE, status, "Attendance exactly equal to required must be SAFE");
    }

    @Test
    @DisplayName("Evaluate Status: Projected below required (74.074% < 75.0%) is AT_RISK")
    public void testBelowThresholdIsAtRisk() {
        RiskStatus status = riskAnalyzer.evaluateStatus(74.07407, 75.0);
        assertEquals(RiskStatus.AT_RISK, status, "Attendance below required must be AT_RISK");
    }

    @Test
    @DisplayName("Overall Trip Rule: A trip is AT_RISK if ANY individual subject falls below threshold, even if average is high")
    public void testSingleSubjectFailureFailsEntireTrip() {
        List<SubjectProjectionResult> results = new ArrayList<>();

        // Subject 1: High safe attendance
        SubjectProjectionResult s1 = new SubjectProjectionResult();
        s1.setSubjectName("Computer Networks");
        s1.setProjectedAttendancePercentage(95.0);
        s1.setStatus(RiskStatus.SAFE);
        results.add(s1);

        // Subject 2: High safe attendance
        SubjectProjectionResult s2 = new SubjectProjectionResult();
        s2.setSubjectName("Database Management");
        s2.setProjectedAttendancePercentage(90.0);
        s2.setStatus(RiskStatus.SAFE);
        results.add(s2);

        // Subject 3: High safe attendance
        SubjectProjectionResult s3 = new SubjectProjectionResult();
        s3.setSubjectName("Software Engineering");
        s3.setProjectedAttendancePercentage(92.0);
        s3.setStatus(RiskStatus.SAFE);
        results.add(s3);

        // Subject 4: AT_RISK subject (74.07%)
        SubjectProjectionResult s4 = new SubjectProjectionResult();
        s4.setSubjectName("Data Structures & Algorithms");
        s4.setProjectedAttendancePercentage(74.07);
        s4.setStatus(RiskStatus.AT_RISK);
        results.add(s4);

        // Average attendance is (95 + 90 + 92 + 74.07) / 4 = 87.77% (Very high!)
        // BUT strict college rule requires trip to be marked AT_RISK
        RiskStatus overall = riskAnalyzer.evaluateOverallTripStatus(results);

        assertEquals(RiskStatus.AT_RISK, overall,
                "Overall trip MUST be flagged AT_RISK when even one subject is at risk, regardless of high average attendance.");
    }

    @Test
    @DisplayName("Overall Trip Rule: A trip is SAFE only when all subjects are SAFE")
    public void testAllSubjectsSafeMakesTripSafe() {
        List<SubjectProjectionResult> results = new ArrayList<>();

        SubjectProjectionResult s1 = new SubjectProjectionResult();
        s1.setStatus(RiskStatus.SAFE);
        results.add(s1);

        SubjectProjectionResult s2 = new SubjectProjectionResult();
        s2.setStatus(RiskStatus.SAFE);
        results.add(s2);

        RiskStatus overall = riskAnalyzer.evaluateOverallTripStatus(results);
        assertEquals(RiskStatus.SAFE, overall);
    }
}
