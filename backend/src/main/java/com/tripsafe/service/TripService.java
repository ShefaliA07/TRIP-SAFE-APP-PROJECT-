package com.tripsafe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripsafe.dto.*;
import com.tripsafe.model.RiskStatus;
import com.tripsafe.model.Subject;
import com.tripsafe.model.Trip;
import com.tripsafe.repository.SubjectRepository;
import com.tripsafe.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripService {

    private final SubjectRepository subjectRepository;
    private final TimetableService timetableService;
    private final AttendanceProjector attendanceProjector;
    private final RiskAnalyzer riskAnalyzer;
    private final TripRepository tripRepository;
    private final ObjectMapper objectMapper;

    public TripService(SubjectRepository subjectRepository,
                       TimetableService timetableService,
                       AttendanceProjector attendanceProjector,
                       RiskAnalyzer riskAnalyzer,
                       TripRepository tripRepository,
                       ObjectMapper objectMapper) {
        this.subjectRepository = subjectRepository;
        this.timetableService = timetableService;
        this.attendanceProjector = attendanceProjector;
        this.riskAnalyzer = riskAnalyzer;
        this.tripRepository = tripRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Calculates projected attendance and risk status for a planned trip.
     */
    public TripProjectionResponse calculateProjection(TripRequest request) {
        Trip.validateDates(request.getDepartureDate(), request.getReturnDate());

        List<Subject> subjects = subjectRepository.findAll();
        long durationDays = ChronoUnit.DAYS.between(request.getDepartureDate(), request.getReturnDate()) + 1;

        Map<Long, Integer> missedMap = timetableService.countExpectedMissedClassesPerSubject(
                request.getDepartureDate(), request.getReturnDate());

        List<SubjectProjectionResult> projectionResults = new ArrayList<>();
        int safeCount = 0;
        int atRiskCount = 0;
        int totalMissedClasses = 0;

        SubjectProjectionResult bottleneck = null;

        for (Subject subject : subjects) {
            int missed = missedMap.getOrDefault(subject.getId(), 0);
            totalMissedClasses += missed;

            double currentPct = attendanceProjector.calculateCurrentPercentage(
                    subject.getClassesAttended(), subject.getClassesConducted());
            double projectedPct = attendanceProjector.calculateProjectedPercentage(
                    subject.getClassesAttended(), subject.getClassesConducted(), missed);

            RiskStatus status = riskAnalyzer.evaluateStatus(projectedPct, subject.getMinAttendancePercentage());
            double margin = projectedPct - subject.getMinAttendancePercentage();

            if (status == RiskStatus.SAFE) {
                safeCount++;
            } else {
                atRiskCount++;
            }

            SubjectProjectionResult result = new SubjectProjectionResult();
            result.setSubjectId(subject.getId());
            result.setSubjectName(subject.getName());
            result.setSubjectCode(subject.getCode());
            result.setColor(subject.getColor());
            result.setClassesConductedBefore(subject.getClassesConducted());
            result.setClassesAttended(subject.getClassesAttended());
            result.setCurrentAttendancePercentage(currentPct);
            result.setExpectedMissedClasses(missed);
            result.setClassesConductedAfter(subject.getClassesConducted() + missed);
            result.setProjectedAttendancePercentage(projectedPct);
            result.setRequiredAttendancePercentage(subject.getMinAttendancePercentage());
            result.setStatus(status);
            result.setRiskMargin(margin);
            result.setExplanation(riskAnalyzer.generateSubjectExplanation(
                    subject.getName(), currentPct, projectedPct, subject.getMinAttendancePercentage(), missed));

            projectionResults.add(result);

            // Track bottleneck (the subject with lowest margin)
            if (bottleneck == null || margin < bottleneck.getRiskMargin()) {
                bottleneck = result;
            }
        }

        RiskStatus overallStatus = riskAnalyzer.evaluateOverallTripStatus(projectionResults);

        TripProjectionResponse response = new TripProjectionResponse();
        response.setTripName(request.getTripName() != null && !request.getTripName().isBlank() ? request.getTripName() : "Planned Trip");
        response.setDepartureDate(request.getDepartureDate());
        response.setReturnDate(request.getReturnDate());
        response.setDurationDays(durationDays);
        response.setOverallStatus(overallStatus);
        response.setSafeSubjectsCount(safeCount);
        response.setAtRiskSubjectsCount(atRiskCount);
        response.setTotalMissedClasses(totalMissedClasses);
        response.setSubjectProjections(projectionResults);

        if (bottleneck != null) {
            response.setBottleneckSubjectName(bottleneck.getSubjectName());
        }

        List<String> affected = projectionResults.stream()
                .filter(p -> p.getExpectedMissedClasses() > 0)
                .sorted(Comparator.comparingDouble(SubjectProjectionResult::getRiskMargin))
                .map(SubjectProjectionResult::getSubjectName)
                .collect(Collectors.toList());
        response.setMostAffectedSubjects(affected);

        // Generate summary and recommendations
        if (overallStatus == RiskStatus.SAFE) {
            response.setSummaryMessage("Trip is Safe! All " + safeCount + " subjects maintain required attendance.");
            response.setRecommendationMessage("You can travel without risking minimum attendance requirements. Have a safe journey!");
        } else {
            response.setSummaryMessage(String.format("HIGH RISK WARNING: %d subject(s) will drop below the required attendance threshold!", atRiskCount));
            response.setRecommendationMessage(String.format("Recommendation: Shorten your trip or adjust departure dates. Bottleneck subject '%s' will drop to %.1f%% (required %.0f%%).",
                    bottleneck != null ? bottleneck.getSubjectName() : "Subject",
                    bottleneck != null ? bottleneck.getProjectedAttendancePercentage() : 0.0,
                    bottleneck != null ? bottleneck.getRequiredAttendancePercentage() : 75.0));
        }

        // Persist trip if requested
        if (request.isSaveTrip()) {
            Trip trip = new Trip(response.getTripName(), request.getDepartureDate(), request.getReturnDate(), request.getNotes());
            trip.setOverallStatus(overallStatus);
            trip.setSafeSubjectsCount(safeCount);
            trip.setAtRiskSubjectsCount(atRiskCount);
            trip.setTotalClassesMissed(totalMissedClasses);
            try {
                trip.setProjectionDetailsJson(objectMapper.writeValueAsString(projectionResults));
            } catch (Exception e) {
                trip.setProjectionDetailsJson("[]");
            }
            Trip saved = tripRepository.save(trip);
            response.setSavedTripId(saved.getId());
        }

        return response;
    }

    /**
     * Finds the maximum consecutive safe travel duration starting from a given departure date.
     * Evaluates candidate return dates sequentially up to the search horizon.
     */
    public MaxSafeDurationResponse findMaxSafeDuration(MaxSafeDurationRequest request) {
        LocalDate departureDate = request.getDepartureDate();
        if (departureDate == null) {
            throw new IllegalArgumentException("Departure date cannot be null.");
        }

        int horizon = request.getSearchHorizonDays() > 0 ? request.getSearchHorizonDays() : 30;
        List<Subject> subjects = subjectRepository.findAll();

        MaxSafeDurationResponse response = new MaxSafeDurationResponse();
        response.setDepartureDate(departureDate);
        response.setSearchHorizonDays(horizon);

        if (subjects.isEmpty()) {
            response.setDepartureDateSafe(true);
            response.setMaxSafeDays(horizon);
            response.setRecommendedReturnDate(departureDate.plusDays(horizon - 1));
            response.setExplanation("No subjects are configured. Travel duration is unconstrained.");
            return response;
        }

        LocalDate lastSafeReturnDate = null;
        List<SubjectProjectionResult> lastSafeProjections = null;
        SubjectProjectionResult failureBottleneck = null;

        for (int dayOffset = 0; dayOffset < horizon; dayOffset++) {
            LocalDate candidateReturnDate = departureDate.plusDays(dayOffset);
            TripRequest testReq = new TripRequest("Max Safe Test", departureDate, candidateReturnDate, "", false);
            TripProjectionResponse testResp = calculateProjection(testReq);

            if (testResp.getOverallStatus() == RiskStatus.SAFE) {
                lastSafeReturnDate = candidateReturnDate;
                lastSafeProjections = testResp.getSubjectProjections();
            } else {
                // Trip became at risk on this date
                if (dayOffset == 0) {
                    // Even the departure date itself is unsafe!
                    response.setDepartureDateSafe(false);
                    response.setMaxSafeDays(0);
                    response.setRecommendedReturnDate(departureDate);
                    response.setProjectedResultsAtRecommendedDate(testResp.getSubjectProjections());
                    response.setLimitingSubjectName(testResp.getBottleneckSubjectName());
                    response.setExplanation("CRITICAL: Even travelling on the departure date (" + departureDate + ") alone will breach attendance in '" +
                            testResp.getBottleneckSubjectName() + "'. Safe duration is 0 days.");
                    return response;
                }

                // Previous day was the maximum safe date!
                failureBottleneck = testResp.getSubjectProjections().stream()
                        .filter(p -> p.getStatus() == RiskStatus.AT_RISK)
                        .min(Comparator.comparingDouble(SubjectProjectionResult::getRiskMargin))
                        .orElse(null);
                break;
            }
        }

        if (lastSafeReturnDate != null) {
            int safeDays = (int) ChronoUnit.DAYS.between(departureDate, lastSafeReturnDate) + 1;
            response.setDepartureDateSafe(true);
            response.setMaxSafeDays(safeDays);
            response.setRecommendedReturnDate(lastSafeReturnDate);
            response.setProjectedResultsAtRecommendedDate(lastSafeProjections);

            if (safeDays == horizon) {
                response.setReachedHorizonWithoutFailure(true);
                response.setExplanation("All subjects remain Safe across the full " + horizon + "-day search horizon up to " + lastSafeReturnDate + ".");
            } else {
                String limiter = failureBottleneck != null ? failureBottleneck.getSubjectName() : "a subject";
                response.setLimitingSubjectName(limiter);
                response.setExplanation(String.format("You can safely travel for %d day(s) until %s. Extending to %s triggers risk in '%s'.",
                        safeDays, lastSafeReturnDate, lastSafeReturnDate.plusDays(1), limiter));
            }
        }

        return response;
    }

    public List<Trip> getAllSavedTrips() {
        return tripRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
