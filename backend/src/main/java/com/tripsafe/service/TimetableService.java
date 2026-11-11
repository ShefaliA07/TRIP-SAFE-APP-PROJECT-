package com.tripsafe.service;

import com.tripsafe.dto.HolidayDto;
import com.tripsafe.dto.TimetableEntryDto;
import com.tripsafe.model.DayOfWeekEnum;
import com.tripsafe.model.Holiday;
import com.tripsafe.model.Subject;
import com.tripsafe.model.TimetableEntry;
import com.tripsafe.repository.HolidayRepository;
import com.tripsafe.repository.SubjectRepository;
import com.tripsafe.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;
    private final HolidayRepository holidayRepository;

    public TimetableService(TimetableRepository timetableRepository,
                            SubjectRepository subjectRepository,
                            HolidayRepository holidayRepository) {
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
        this.holidayRepository = holidayRepository;
    }

    public List<TimetableEntryDto> getAllTimetableEntries() {
        return timetableRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TimetableEntryDto> getEntriesByDay(DayOfWeekEnum dayOfWeek) {
        return timetableRepository.findByDayOfWeekOrderByPeriodIndexAsc(dayOfWeek).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TimetableEntryDto assignSlot(TimetableEntryDto dto) {
        if (dto.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Day of week must be provided.");
        }
        if (dto.getPeriodIndex() < 1 || dto.getPeriodIndex() > 10) {
            throw new IllegalArgumentException("Period index must be between 1 and 10.");
        }

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + dto.getSubjectId()));

        Optional<TimetableEntry> existingOpt = timetableRepository.findByDayOfWeekAndPeriodIndex(
                dto.getDayOfWeek(), dto.getPeriodIndex());

        TimetableEntry entry;
        if (existingOpt.isPresent()) {
            entry = existingOpt.get();
            entry.setSubject(subject);
            entry.setSlotTime(dto.getSlotTime() != null ? dto.getSlotTime() : entry.getSlotTime());
        } else {
            entry = new TimetableEntry(
                    dto.getDayOfWeek(),
                    dto.getPeriodIndex(),
                    dto.getSlotTime() != null ? dto.getSlotTime() : ("Period " + dto.getPeriodIndex()),
                    subject
            );
        }

        TimetableEntry saved = timetableRepository.save(entry);
        return mapToDto(saved);
    }

    public void removeSlot(Long id) {
        timetableRepository.deleteById(id);
    }

    public void removeSlotByDayAndPeriod(DayOfWeekEnum day, int periodIndex) {
        timetableRepository.findByDayOfWeekAndPeriodIndex(day, periodIndex)
                .ifPresent(timetableRepository::delete);
    }

    /**
     * Counts scheduled classes per subject for a trip's date range (inclusive),
     * completely excluding dates recorded as institutional holidays or cancelled classes.
     *
     * @param departureDate start of trip (inclusive)
     * @param returnDate end of trip (inclusive)
     * @return Map of subjectId to count of scheduled missed classes
     */
    public Map<Long, Integer> countExpectedMissedClassesPerSubject(LocalDate departureDate, LocalDate returnDate) {
        Map<Long, Integer> countMap = new HashMap<>();

        // Fetch all holidays in or overlapping the range
        Set<LocalDate> holidayDates = holidayRepository.findByDateBetweenOrderByDateAsc(departureDate, returnDate)
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        // Cache all timetable entries mapped by DayOfWeekEnum
        List<TimetableEntry> allEntries = timetableRepository.findAll();
        Map<DayOfWeekEnum, List<TimetableEntry>> entriesByDay = allEntries.stream()
                .collect(Collectors.groupingBy(TimetableEntry::getDayOfWeek));

        LocalDate current = departureDate;
        while (!current.isAfter(returnDate)) {
            // If this date is a registered holiday, skip it - no classes missed!
            if (!holidayDates.contains(current)) {
                DayOfWeekEnum dayOfWeek = DayOfWeekEnum.fromJavaDayOfWeek(current.getDayOfWeek());
                List<TimetableEntry> dayClasses = entriesByDay.getOrDefault(dayOfWeek, Collections.emptyList());

                for (TimetableEntry entry : dayClasses) {
                    Long subjId = entry.getSubject().getId();
                    countMap.put(subjId, countMap.getOrDefault(subjId, 0) + 1);
                }
            }
            current = current.plusDays(1);
        }

        return countMap;
    }

    // Holiday Management
    public List<HolidayDto> getAllHolidays() {
        return holidayRepository.findAll().stream()
                .map(h -> new HolidayDto(h.getId(), h.getDate(), h.getName(), h.getDescription()))
                .collect(Collectors.toList());
    }

    public HolidayDto addHoliday(HolidayDto dto) {
        if (dto.getDate() == null) {
            throw new IllegalArgumentException("Holiday date cannot be null.");
        }
        Holiday holiday = new Holiday(dto.getDate(), dto.getName().trim(), dto.getDescription());
        Holiday saved = holidayRepository.save(holiday);
        return new HolidayDto(saved.getId(), saved.getDate(), saved.getName(), saved.getDescription());
    }

    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    private TimetableEntryDto mapToDto(TimetableEntry entry) {
        TimetableEntryDto dto = new TimetableEntryDto();
        dto.setId(entry.getId());
        dto.setDayOfWeek(entry.getDayOfWeek());
        dto.setPeriodIndex(entry.getPeriodIndex());
        dto.setSlotTime(entry.getSlotTime());
        if (entry.getSubject() != null) {
            dto.setSubjectId(entry.getSubject().getId());
            dto.setSubjectName(entry.getSubject().getName());
            dto.setSubjectCode(entry.getSubject().getCode());
            dto.setColor(entry.getSubject().getColor());
        }
        return dto;
    }
}
