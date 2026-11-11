package com.tripsafe.controller;

import com.tripsafe.dto.HolidayDto;
import com.tripsafe.service.TimetableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@CrossOrigin(origins = "*")
public class HolidayController {

    private final TimetableService timetableService;

    public HolidayController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public ResponseEntity<List<HolidayDto>> getAllHolidays() {
        return ResponseEntity.ok(timetableService.getAllHolidays());
    }

    @PostMapping
    public ResponseEntity<HolidayDto> addHoliday(@Valid @RequestBody HolidayDto dto) {
        HolidayDto saved = timetableService.addHoliday(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long id) {
        timetableService.deleteHoliday(id);
        return ResponseEntity.noContent().build();
    }
}
