# TripSafe — Relational Database Schema & Data Modeling

The TripSafe database schema is normalized to 3rd Normal Form (3NF) to guarantee referential integrity and eliminate update anomalies.

---

## 1. Entity-Relationship Diagram (ERD)

```
+------------------------+          +------------------------+
|    student_profiles    |          |        holidays        |
+------------------------+          +------------------------+
| id (PK)                |          | id (PK)                |
| name                   |          | date (UNIQUE)          |
| roll_number            |          | name                   |
| college_name           |          | description            |
| hostel_block           |          +------------------------+
| default_min_attendance |
+------------------------+
                                    +------------------------+
+------------------------+          |         trips          |
|        subjects        |          +------------------------+
+------------------------+          | id (PK)                |
| id (PK)                |◄───┐     | trip_name              |
| name                   |    │     | departure_date         |
| code                   |    │     | return_date            |
| classes_conducted      |    │     | notes                  |
| classes_attended       |    │     | overall_status         |
| min_attendance_pct     |    │     | safe_subjects_count    |
| color                  |    │     | at_risk_subjects_count |
+------------------------+    │     | total_classes_missed   |
                              │     | projection_details_json|
+------------------------+    │     | created_at             |
|   timetable_entries    |    │     +------------------------+
+------------------------+    │
| id (PK)                |    │
| day_of_week            |    │
| period_index           |    │
| slot_time              |    │
| subject_id (FK)        |────┘ (ON DELETE CASCADE)
| UNIQUE(day, period)    |
+------------------------+
```

---

## 2. Table Specifications

### A. `subjects`
Stores academic coursework and attendance tallies.
- `id`: BIGINT AUTO_INCREMENT PRIMARY KEY
- `name`: VARCHAR(100) NOT NULL
- `code`: VARCHAR(20)
- `classes_conducted`: INT NOT NULL DEFAULT 0
- `classes_attended`: INT NOT NULL DEFAULT 0
- `min_attendance_percentage`: DOUBLE NOT NULL DEFAULT 75.0
- `color`: VARCHAR(30) DEFAULT '#4f46e5'
- Constraints:
  - `CHECK (classes_conducted >= 0 AND classes_attended >= 0)`
  - `CHECK (classes_attended <= classes_conducted)`

### B. `timetable_entries`
Maps weekly class schedule.
- `id`: BIGINT AUTO_INCREMENT PRIMARY KEY
- `day_of_week`: VARCHAR(20) NOT NULL (MONDAY - SUNDAY)
- `period_index`: INT NOT NULL (1 to 10)
- `slot_time`: VARCHAR(30) NOT NULL
- `subject_id`: BIGINT NOT NULL (FK referencing `subjects.id` ON DELETE CASCADE)
- Constraints:
  - `UNIQUE (day_of_week, period_index)` ensures no double-booking for the same period.

### C. `holidays`
Registered dates where classes are not held.
- `id`: BIGINT AUTO_INCREMENT PRIMARY KEY
- `date`: DATE NOT NULL UNIQUE
- `name`: VARCHAR(100) NOT NULL
- `description`: VARCHAR(255)

### D. `trips`
Persisted travel plans with frozen snapshot results.
- `id`: BIGINT AUTO_INCREMENT PRIMARY KEY
- `trip_name`: VARCHAR(150) NOT NULL
- `departure_date`: DATE NOT NULL
- `return_date`: DATE NOT NULL
- `notes`: VARCHAR(500)
- `overall_status`: VARCHAR(20) NOT NULL (`SAFE` or `AT_RISK`)
- `safe_subjects_count`: INT NOT NULL
- `at_risk_subjects_count`: INT NOT NULL
- `total_classes_missed`: INT NOT NULL
- `projection_details_json`: TEXT (JSON array snapshot)
- `created_at`: TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- Constraints:
  - `CHECK (return_date >= departure_date)`
