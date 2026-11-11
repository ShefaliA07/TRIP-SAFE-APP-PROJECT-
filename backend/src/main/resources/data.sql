-- ===================================================================
-- TripSafe Sample Demo Data
-- ===================================================================

-- Initial Student Profile
INSERT INTO student_profiles (name, roll_number, college_name, hostel_block, default_min_attendance)
VALUES ('Arjun Sharma', '21CS108', 'Apex Institute of Technology', 'Himalaya Hostel, Room 402', 75.0);

-- Subjects with Realistic Attendance
INSERT INTO subjects (id, name, code, classes_conducted, classes_attended, min_attendance_percentage, color)
VALUES 
(1, 'Data Structures & Algorithms', 'CS301', 75, 60, 75.0, '#4f46e5'),
(2, 'Database Management Systems', 'CS302', 55, 48, 75.0, '#0891b2'),
(3, 'Operating Systems', 'CS303', 65, 52, 75.0, '#059669'),
(4, 'Computer Networks', 'CS304', 50, 39, 75.0, '#d97706'),
(5, 'Software Engineering', 'CS305', 45, 37, 75.0, '#7c3aed');

-- Timetable Entries (Monday to Friday)
INSERT INTO timetable_entries (day_of_week, period_index, slot_time, subject_id) VALUES
('MONDAY', 1, '09:00 - 10:00', 1),
('MONDAY', 2, '10:15 - 11:15', 2),
('MONDAY', 3, '11:30 - 12:30', 3),
('MONDAY', 4, '02:00 - 03:00', 4),

('TUESDAY', 1, '09:00 - 10:00', 5),
('TUESDAY', 2, '10:15 - 11:15', 1),
('TUESDAY', 3, '11:30 - 12:30', 2),
('TUESDAY', 4, '02:00 - 03:00', 3),

('WEDNESDAY', 1, '09:00 - 10:00', 4),
('WEDNESDAY', 2, '10:15 - 11:15', 5),
('WEDNESDAY', 3, '11:30 - 12:30', 1),
('WEDNESDAY', 4, '02:00 - 03:00', 2),

('THURSDAY', 1, '09:00 - 10:00', 3),
('THURSDAY', 2, '10:15 - 11:15', 4),
('THURSDAY', 3, '11:30 - 12:30', 5),
('THURSDAY', 4, '02:00 - 03:00', 1),

('FRIDAY', 1, '09:00 - 10:00', 2),
('FRIDAY', 2, '10:15 - 11:15', 3),
('FRIDAY', 3, '11:30 - 12:30', 4),
('FRIDAY', 4, '02:00 - 03:00', 5);

-- Sample Institutional Holiday
INSERT INTO holidays (date, name, description)
VALUES ('2026-11-05', 'College Foundation Day', 'Annual sports & cultural celebration day - no instructional classes');
