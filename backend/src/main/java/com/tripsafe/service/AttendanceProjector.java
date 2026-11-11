package com.tripsafe.service;

import com.tripsafe.model.Subject;
import org.springframework.stereotype.Component;

/**
 * Core OOP Calculation Engine for TripSafe.
 * Encapsulates attendance projection formulas and mathematical rules.
 */
@Component
public class AttendanceProjector {

    /**
     * Calculates the current attendance percentage for a subject.
     * Formula: (Classes Attended / Classes Conducted) * 100.
     *
     * @param attended number of classes attended
     * @param conducted number of classes conducted
     * @return current percentage as an unrounded double. Returns 100.0 if conducted is 0.
     */
    public double calculateCurrentPercentage(int attended, int conducted) {
        if (conducted < 0 || attended < 0) {
            throw new IllegalArgumentException("Class counts cannot be negative.");
        }
        if (attended > conducted) {
            throw new IllegalArgumentException("Attended classes (" + attended + ") cannot exceed conducted classes (" + conducted + ").");
        }
        if (conducted == 0) {
            return 100.0;
        }
        return ((double) attended / (double) conducted) * 100.0;
    }

    /**
     * Calculates projected attendance percentage after expected missed classes.
     * Formula: Classes Attended / (Classes Conducted + Expected Missed Classes) * 100.
     *
     * For this project, expected missed classes count as additional conducted classes
     * that the student will not attend.
     *
     * @param attended classes already attended
     * @param conducted classes already conducted
     * @param missed expected classes missed during the trip
     * @return projected attendance percentage as an unrounded double
     */
    public double calculateProjectedPercentage(int attended, int conducted, int missed) {
        if (missed < 0) {
            throw new IllegalArgumentException("Missed classes cannot be negative.");
        }
        int totalConductedAfterTrip = conducted + missed;
        if (totalConductedAfterTrip == 0) {
            return 100.0;
        }
        return ((double) attended / (double) totalConductedAfterTrip) * 100.0;
    }

    /**
     * Convenience method calculating projected attendance directly from a Subject entity and missed count.
     */
    public double calculateProjectedPercentage(Subject subject, int missed) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject entity cannot be null.");
        }
        return calculateProjectedPercentage(subject.getClassesAttended(), subject.getClassesConducted(), missed);
    }
}
