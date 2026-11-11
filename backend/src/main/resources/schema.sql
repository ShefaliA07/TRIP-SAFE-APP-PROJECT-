-- ===================================================================
-- TripSafe Database DDL Schema (MySQL / ANSI SQL compatible)
-- ===================================================================

-- 1. Student Profile Table
CREATE TABLE IF NOT EXISTS student_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_number VARCHAR(50),
    college_name VARCHAR(100),
    hostel_block VARCHAR(50),
    default_min_attendance DOUBLE NOT NULL DEFAULT 75.0
);

-- 2. Subjects Table
CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20),
    classes_conducted INT NOT NULL DEFAULT 0,
    classes_attended INT NOT NULL DEFAULT 0,
    min_attendance_percentage DOUBLE NOT NULL DEFAULT 75.0,
    color VARCHAR(30) DEFAULT '#4f46e5',
    CONSTRAINT chk_classes_positive CHECK (classes_conducted >= 0 AND classes_attended >= 0),
    CONSTRAINT chk_attended_le_conducted CHECK (classes_attended <= classes_conducted)
);

-- 3. Weekly Timetable Entries Table
CREATE TABLE IF NOT EXISTS timetable_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL,
    period_index INT NOT NULL,
    slot_time VARCHAR(30) NOT NULL,
    subject_id BIGINT NOT NULL,
    CONSTRAINT uq_day_period UNIQUE (day_of_week, period_index),
    CONSTRAINT fk_timetable_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- 4. Institutional Holidays & Cancelled Classes Table
CREATE TABLE IF NOT EXISTS holidays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- 5. Saved Trips Table
CREATE TABLE IF NOT EXISTS trips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_name VARCHAR(150) NOT NULL,
    departure_date DATE NOT NULL,
    return_date DATE NOT NULL,
    notes VARCHAR(500),
    overall_status VARCHAR(20) NOT NULL,
    safe_subjects_count INT NOT NULL,
    at_risk_subjects_count INT NOT NULL,
    total_classes_missed INT NOT NULL,
    projection_details_json TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_trip_dates CHECK (return_date >= departure_date)
);
