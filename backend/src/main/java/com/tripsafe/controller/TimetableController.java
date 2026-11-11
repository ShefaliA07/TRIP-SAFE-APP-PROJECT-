package com.tripsafe.controller;

import com.tripsafe.dto.TimetableEntryDto;
import com.tripsafe.model.DayOfWeekEnum;
import com.tripsafe.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public ResponseEntity<List<TimetableEntryDto>> getAllEntries() {
        return ResponseEntity.ok(timetableService.getAllTimetableEntries());
    }

    @GetMapping("/day/{day}")
    public ResponseEntity<List<TimetableEntryDto>> getEntriesByDay(@PathVariable DayOfWeekEnum day) {
        return ResponseEntity.ok(timetableService.getEntriesByDay(day));
    }

    @PostMapping("/slot")
    public ResponseEntity<TimetableEntryDto> assignSlot(@Valid @RequestBody TimetableEntryDto dto) {
        TimetableEntryDto saved = timetableService.assignSlot(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/slot/{id}")
    public ResponseEntity<Void> removeSlot(@PathVariable Long id) {
        timetableService.removeSlot(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/slot/{day}/{periodIndex}")
    public ResponseEntity<Void> removeSlotByDayAndPeriod(@PathVariable DayOfWeekEnum day, @PathVariable int periodIndex) {
        timetableService.removeSlotByDayAndPeriod(day, periodIndex);
        return ResponseEntity.noContent().build();
    }
}
