package com.tripsafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity representing an institutional holiday or class cancellation.
 * Dates matching holidays will exclude timetable classes during trip calculation.
 */
@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Holiday date cannot be null")
    @Column(nullable = false, unique = true)
    private LocalDate date;

    @NotBlank(message = "Holiday name cannot be blank")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    public Holiday() {
    }

    public Holiday(LocalDate date, String name, String description) {
        this.date = date;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holiday holiday)) return false;
        return Objects.equals(date, holiday.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
