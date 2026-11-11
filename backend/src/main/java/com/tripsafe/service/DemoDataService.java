package com.tripsafe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripsafe.model.*;
import com.tripsafe.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DemoDataService {

    private final SubjectRepository subjectRepository;
    private final TimetableRepository timetableRepository;
    private final HolidayRepository holidayRepository;
    private final TripRepository tripRepository;
    private final StudentProfileRepository profileRepository;

    public DemoDataService(SubjectRepository subjectRepository,
                           TimetableRepository timetableRepository,
                           HolidayRepository holidayRepository,
                           TripRepository tripRepository,
                           StudentProfileRepository profileRepository) {
        this.subjectRepository = subjectRepository;
        this.timetableRepository = timetableRepository;
        this.holidayRepository = holidayRepository;
        this.tripRepository = tripRepository;
        this.profileRepository = profileRepository;
    }

    public void resetAllData() {
        tripRepository.deleteAll();
        holidayRepository.deleteAll();
        timetableRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    public void loadDemoData() {
        resetAllData();

        // 1. Setup Student Profile
        if (profileRepository.count() == 0) {
            profileRepository.save(new StudentProfile(
                    "Arjun Sharma", "21CS108", "Apex Institute of Technology", "Himalaya Hostel, Room 402", 75.0
            ));
        }

        // 2. Create Realistic Subjects
        // Subject 1: Data Structures & Algorithms (CS301) - 60/75 = 80.0%, min 75% (Direct match to verification spec!)
        Subject dsa = new Subject("Data Structures & Algorithms", "CS301", 75, 60, 75.0, "#4f46e5");
        // Subject 2: Database Management Systems (CS302) - 48/55 = 87.27%, min 75%
        Subject dbms = new Subject("Database Management Systems", "CS302", 55, 48, 75.0, "#0891b2");
        // Subject 3: Operating Systems (CS303) - 52/65 = 80.0%, min 75%
        Subject os = new Subject("Operating Systems", "CS303", 65, 52, 75.0, "#059669");
        // Subject 4: Computer Networks (CS304) - 39/50 = 78.0%, min 75%
        Subject cn = new Subject("Computer Networks", "CS304", 50, 39, 75.0, "#d97706");
        // Subject 5: Software Engineering (CS305) - 37/45 = 82.22%, min 75%
        Subject se = new Subject("Software Engineering", "CS305", 45, 37, 75.0, "#7c3aed");

        List<Subject> savedSubjects = subjectRepository.saveAll(List.of(dsa, dbms, os, cn, se));
        Subject s1 = savedSubjects.get(0); // DSA
        Subject s2 = savedSubjects.get(1); // DBMS
        Subject s3 = savedSubjects.get(2); // OS
        Subject s4 = savedSubjects.get(3); // CN
        Subject s5 = savedSubjects.get(4); // SE

        // 3. Weekly Timetable (Monday through Friday, 4 periods/day)
        List<TimetableEntry> entries = new ArrayList<>();
        // Monday
        entries.add(new TimetableEntry(DayOfWeekEnum.MONDAY, 1, "09:00 - 10:00", s1));
        entries.add(new TimetableEntry(DayOfWeekEnum.MONDAY, 2, "10:15 - 11:15", s2));
        entries.add(new TimetableEntry(DayOfWeekEnum.MONDAY, 3, "11:30 - 12:30", s3));
        entries.add(new TimetableEntry(DayOfWeekEnum.MONDAY, 4, "02:00 - 03:00", s4));

        // Tuesday
        entries.add(new TimetableEntry(DayOfWeekEnum.TUESDAY, 1, "09:00 - 10:00", s5));
        entries.add(new TimetableEntry(DayOfWeekEnum.TUESDAY, 2, "10:15 - 11:15", s1));
        entries.add(new TimetableEntry(DayOfWeekEnum.TUESDAY, 3, "11:30 - 12:30", s2));
        entries.add(new TimetableEntry(DayOfWeekEnum.TUESDAY, 4, "02:00 - 03:00", s3));

        // Wednesday
        entries.add(new TimetableEntry(DayOfWeekEnum.WEDNESDAY, 1, "09:00 - 10:00", s4));
        entries.add(new TimetableEntry(DayOfWeekEnum.WEDNESDAY, 2, "10:15 - 11:15", s5));
        entries.add(new TimetableEntry(DayOfWeekEnum.WEDNESDAY, 3, "11:30 - 12:30", s1));
        entries.add(new TimetableEntry(DayOfWeekEnum.WEDNESDAY, 4, "02:00 - 03:00", s2));

        // Thursday
        entries.add(new TimetableEntry(DayOfWeekEnum.THURSDAY, 1, "09:00 - 10:00", s3));
        entries.add(new TimetableEntry(DayOfWeekEnum.THURSDAY, 2, "10:15 - 11:15", s4));
        entries.add(new TimetableEntry(DayOfWeekEnum.THURSDAY, 3, "11:30 - 12:30", s5));
        entries.add(new TimetableEntry(DayOfWeekEnum.THURSDAY, 4, "02:00 - 03:00", s1));

        // Friday
        entries.add(new TimetableEntry(DayOfWeekEnum.FRIDAY, 1, "09:00 - 10:00", s2));
        entries.add(new TimetableEntry(DayOfWeekEnum.FRIDAY, 2, "10:15 - 11:15", s3));
        entries.add(new TimetableEntry(DayOfWeekEnum.FRIDAY, 3, "11:30 - 12:30", s4));
        entries.add(new TimetableEntry(DayOfWeekEnum.FRIDAY, 4, "02:00 - 03:00", s5));

        timetableRepository.saveAll(entries);

        // 4. Sample Academic Holidays
        LocalDate today = LocalDate.now();
        holidayRepository.saveAll(List.of(
                new Holiday(today.plusDays(10), "College Foundation Day", "Annual institutional celebration, non-instructional"),
                new Holiday(today.plusDays(24), "National Holiday", "Gazetted public holiday")
        ));

        // 5. Sample Past Trip
        Trip sampleTrip = new Trip("Diwali Festival Trip", today.minusDays(20), today.minusDays(16), "Travelled home for festive celebration");
        sampleTrip.setOverallStatus(RiskStatus.SAFE);
        sampleTrip.setSafeSubjectsCount(5);
        sampleTrip.setAtRiskSubjectsCount(0);
        sampleTrip.setTotalClassesMissed(6);
        sampleTrip.setProjectionDetailsJson("[]");
        tripRepository.save(sampleTrip);
    }
}
