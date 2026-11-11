package com.tripsafe.dto;

import java.time.LocalDate;

public class HolidayDto {
    private Long id;
    private LocalDate date;
    private String name;
    private String description;

    public HolidayDto() {}

    public HolidayDto(Long id, LocalDate date, String name, String description) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
