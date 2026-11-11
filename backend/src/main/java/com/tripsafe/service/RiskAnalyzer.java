package com.tripsafe.service;

import com.tripsafe.dto.SubjectProjectionResult;
import com.tripsafe.model.RiskStatus;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Core OOP Risk Assessment Engine for TripSafe.
 * Encapsulates multi-subject risk rules and threshold compliance.
 */
@Component
public class RiskAnalyzer {

    // Floating-point precision epsilon to avoid rounding comparison artifacts
    private static final double EPSILON = 1e-9;

    /**
     * Determines whether a projected attendance percentage satisfies the minimum required percentage.
     *
     * @param projectedPercentage projected attendance
     * @param requiredPercentage required minimum threshold
     * @return RiskStatus.SAFE or RiskStatus.AT_RISK
     */
    public RiskStatus evaluateStatus(double projectedPercentage, double requiredPercentage) {
        // Safe if projected >= required (accounting for negligible floating point variance)
        if (projectedPercentage + EPSILON >= requiredPercentage) {
            return RiskStatus.SAFE;
        } else {
            return RiskStatus.AT_RISK;
        }
    }

    /**
     * Determines the overall trip risk status.
     * Strict College Rule: A trip is Safe ONLY if ALL individual subjects remain Safe.
     * If even ONE subject falls below its required threshold, the entire trip is AT RISK!
     *
     * @param results list of individual subject projections
     * @return RiskStatus.SAFE if every subject is safe; otherwise RiskStatus.AT_RISK
     */
    public RiskStatus evaluateOverallTripStatus(List<SubjectProjectionResult> results) {
        if (results == null || results.isEmpty()) {
            return RiskStatus.SAFE;
        }
        for (SubjectProjectionResult result : results) {
            if (result.getStatus() == RiskStatus.AT_RISK) {
                return RiskStatus.AT_RISK;
            }
        }
        return RiskStatus.SAFE;
    }

    /**
     * Generates a clear, descriptive explanation for an individual subject projection.
     */
    public String generateSubjectExplanation(String subjectName, double currentPct, double projectedPct, double requiredPct, int missed) {
        if (missed == 0) {
            return String.format("%s has no classes scheduled during this trip. Attendance remains steady at %.1f%%.", subjectName, currentPct);
        }
        if (projectedPct + EPSILON >= requiredPct) {
            double cushion = projectedPct - requiredPct;
            return String.format("Missing %d classes drops attendance from %.1f%% to %.1f%% (safe with a %.1f%% cushion above the %.0f%% requirement).",
                    missed, currentPct, projectedPct, cushion, requiredPct);
        } else {
            double deficit = requiredPct - projectedPct;
            return String.format("CRITICAL: Missing %d classes drops attendance from %.1f%% to %.1f%%, falling %.1f%% BELOW the %.0f%% requirement!",
                    missed, currentPct, projectedPct, deficit, requiredPct);
        }
    }
}
