# TripSafe — System Architecture & Java OOP Design Specification

## 1. Executive Architecture Overview

TripSafe is constructed as an enterprise-grade, three-tier full-stack application designed specifically for college hostel students to evaluate travel attendance risk.

```
+-----------------------------------------------------------------------+
|                         PRESENTATION LAYER                            |
|  - React 18 with TypeScript                                           |
|  - Tailwind CSS Responsive Design System                              |
|  - Lucide Icons & SVG Responsive Risk Visualizations                  |
|  - Dual Mode: Spring Boot REST API Client & Standalone Engine Fallback|
+-----------------------------------------------------------------------+
                                  │  REST APIs (JSON / HTTP)
                                  ▼
+-----------------------------------------------------------------------+
|                        BUSINESS LOGIC LAYER                           |
|  - Spring Boot 3.2+ REST Controllers with Jakarta Validation          |
|  - AttendanceProjector: Mathematical projection calculator            |
|  - RiskAnalyzer: Single-subject and multi-subject collegiate rule     |
|  - TripService: Trip planning, persistence, and safe horizon search   |
|  - TimetableService: Weekly slot resolution & holiday exclusion       |
+-----------------------------------------------------------------------+
                                  │  Spring Data JPA / Hibernate
                                  ▼
+-----------------------------------------------------------------------+
|                            DATA LAYER                                 |
|  - Entities: Subject, TimetableEntry, Holiday, Trip, StudentProfile   |
|  - Repositories: JpaRepository interfaces with derived queries        |
|  - Relational Persistence: Persistent Embedded H2 / MySQL             |
+-----------------------------------------------------------------------+
```

---

## 2. Core Java Object-Oriented Programming (OOP) Principles

The backend source code strictly reflects academic and enterprise OOP standards:

### A. Encapsulation
- **Data Hiding:** All entity fields (`Subject.name`, `classesConducted`, `classesAttended`, `minAttendancePercentage`) are `private`.
- **Integrity Validation:** Setters enforce invariants:
  - Classes attended cannot exceed classes conducted: `attended <= conducted`.
  - Negative class counts are rejected via `validateAttendanceValues()`.
  - Return date cannot precede departure date in `Trip.validateDates()`.

### B. Abstraction
- The complexity of mathematical formulas, holiday deduplication, and consecutive calendar iteration is abstracted away from API controllers.
- Controllers invoke clean high-level methods:
  - `tripService.calculateProjection(tripRequest)`
  - `tripService.findMaxSafeDuration(maxSafeRequest)`
  - `attendanceProjector.calculateProjectedPercentage(attended, conducted, missed)`

### C. Association & Domain Modeling
- **Many-to-One Association:** `TimetableEntry` maintains a foreign key relationship to `Subject`. Deleting a subject triggers cascade cleanup across timetable slots.
- **Auditable Value Persistence:** `Trip` stores a JSON serialized snapshot of `projectionDetailsJson` so historical projections remain permanently verifiable even if timetable configurations change later.

### D. Separation of Concerns (SoC)
- **Presentation (`controller/`):** Validates input DTOs, converts domain exceptions into HTTP 400 Bad Request responses via `GlobalExceptionHandler`.
- **Domain Services (`service/`):**
  - `AttendanceProjector`: Pure mathematical engine with zero database dependencies.
  - `RiskAnalyzer`: Pure rule evaluation engine.
  - `TimetableService`: Calendar and holiday resolution.
  - `TripService`: High-level business transaction orchestrator.
- **Persistence (`repository/`):** Spring Data JPA repositories handling query generation and transactional database operations.

---

## 3. Mathematical Formula & College Risk Model

### 1. Current Attendance Calculation
$$\text{Current Attendance (\%)} = \begin{cases} 
100.0 & \text{if } \text{Classes Conducted} = 0 \\
\left(\frac{\text{Classes Attended}}{\text{Classes Conducted}}\right) \times 100 & \text{if } \text{Classes Conducted} > 0 
\end{cases}$$

### 2. Projected Attendance Calculation
For this project, expected missed classes count as additional conducted classes that the student will not attend:
$$\text{Projected Attendance (\%)} = \left(\frac{\text{Classes Attended}}{\text{Classes Conducted} + \text{Expected Missed Classes}}\right) \times 100$$

### 3. Subject Risk Evaluation
- **SAFE:** $\text{Projected Attendance (\%)} \ge \text{Minimum Required Percentage}$ (e.g. 75.0%)
- **AT RISK:** $\text{Projected Attendance (\%)} < \text{Minimum Required Percentage}$

### 4. Strict Overall Trip Risk Rule
College academic committees do not grant exam eligibility based on high aggregate averages if a student is debarred in even one core subject. Therefore:
$$\text{Trip Status} = \begin{cases} 
\text{SAFE} & \text{if and only if } \forall s \in \text{Subjects}: s.\text{status} = \text{SAFE} \\
\text{AT RISK} & \text{if } \exists s \in \text{Subjects}: s.\text{status} = \text{AT RISK}
\end{cases}$$

---

## 4. "Find My Maximum Safe Trip" Horizon Search Algorithm

The maximum safe travel feature operates via iterative day-by-day evaluation:
1. **Input:** Departure Date $D$, Search Horizon $H$ (default 30 days).
2. **Iteration:** For $k = 0, 1, 2, \dots, H-1$:
   - Candidate Return Date $R_k = D + k \text{ days}$.
   - Resolve scheduled classes in $[D, R_k]$ excluding registered holidays.
   - Project attendance for every subject.
   - If any subject falls below its required threshold:
     - If $k = 0$: Flag departure date itself as unsafe ($0$ safe days).
     - If $k > 0$: The preceding date $R_{k-1}$ is the exact maximum safe return date.
3. **Output:** Recommended return date, maximum safe consecutive days, bottleneck subject identification, and projection snapshot.
