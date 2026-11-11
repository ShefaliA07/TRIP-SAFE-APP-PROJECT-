# TripSafe — Calculation & Testing Verification Report

This document reports on the unit test cases developed for the Java Spring Boot business logic and mathematical calculation engine.

---

## 1. Specification Test Case: Attendance Drop & Threshold Boundary

### Scenario Specification:
A student has attended **60 out of 75** classes in a subject requiring a minimum attendance of **75%**.

### A. Current Attendance
$$\text{Current Attendance} = \left(\frac{60}{75}\right) \times 100 = 80.0\%$$
- **Threshold Check:** $80.0\% \ge 75.0\%$
- **Result:** SAFE (Status: `SAFE`, Cushion: $+5.0\%$)

### B. Planned Trip with 5 Missed Classes
$$\text{Projected Attendance} = \left(\frac{60}{75 + 5}\right) \times 100 = \left(\frac{60}{80}\right) \times 100 = 75.0\%$$
- **Threshold Check:** $75.0\% \ge 75.0\%$
- **Result:** SAFE (Status: `SAFE`, Cushion: $0.0\%$)
- **Unit Test Method:** `AttendanceProjectorTest.testProjectedAttendanceFiveMissedClasses()`

### C. Planned Trip with 6 Missed Classes
$$\text{Projected Attendance} = \left(\frac{60}{75 + 6}\right) \times 100 = \left(\frac{60}{81}\right) \times 100 \approx 74.07407\%$$
- **Threshold Check:** $74.074\% < 75.0\%$
- **Result:** AT RISK (Status: `AT_RISK`, Deficit: $-0.926\%$)
- **Unit Test Method:** `AttendanceProjectorTest.testProjectedAttendanceSixMissedClasses()`

---

## 2. Multi-Subject Collegiate Risk Rule Test

### Scenario Specification:
A student has 4 subjects with strong attendance (90% to 95%) and 1 subject with projected attendance of 74.07%.

- Subject 1: Computer Networks — 95% (SAFE)
- Subject 2: Database Systems — 90% (SAFE)
- Subject 3: Software Engineering — 92% (SAFE)
- Subject 4: Operating Systems — 91% (SAFE)
- Subject 5: Data Structures — 74.07% (AT RISK)

$$\text{Average Aggregate Attendance} = \frac{95 + 90 + 92 + 91 + 74.07}{5} = 88.41\% \quad (\text{High!})$$

- **Strict College Policy:** In academic institutions, a high average does NOT excuse attendance debarment in individual core subjects. If any single subject falls below 75%, the entire trip is deemed unsafe.
- **TripSafe Risk Analyzer Output:** `RiskStatus.AT_RISK`
- **Unit Test Method:** `RiskAnalyzerTest.testSingleSubjectFailureFailsEntireTrip()`

---

## 3. Boundary & Error Conditions Verified

| Test Condition | Input Values | Expected Behavior | Unit Test Method |
|---|---|---|---|
| Zero Conducted Classes | Attended: 0, Conducted: 0 | Returns `100.0%` without division by zero error | `testZeroConductedClasses` |
| Attended > Conducted | Attended: 10, Conducted: 5 | Throws `IllegalArgumentException` | `testAttendedGreaterThanConductedThrowsException` |
| Negative Counts | Conducted: -1, Attended: 5 | Throws `IllegalArgumentException` | `testNegativeClassCountsThrowException` |
| Departure Date = Return Date | 1 Day Trip | Evaluates 1 day inclusively | `TripServiceTest` |
| Trip Start > Return Date | Dep: Nov 10, Ret: Nov 5 | Throws `IllegalArgumentException` | `Trip.validateDates` |
| Holiday Overlap | Trip covers 5 days, 1 is a holiday | Timetable skips holiday, counts only 4 days | `TimetableService.countExpectedMissedClassesPerSubject` |
