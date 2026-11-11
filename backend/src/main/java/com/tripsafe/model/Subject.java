package com.tripsafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Entity representing an academic subject with attendance records.
 * Demonstrates OOP Encapsulation with private state and explicit business logic.
 */
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject name cannot be blank")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String code;

    @Min(value = 0, message = "Classes conducted cannot be negative")
    @Column(nullable = false)
    private int classesConducted;

    @Min(value = 0, message = "Classes attended cannot be negative")
    @Column(nullable = false)
    private int classesAttended;

    @DecimalMin(value = "0.0", message = "Minimum attendance cannot be less than 0%")
    @DecimalMax(value = "100.0", message = "Minimum attendance cannot exceed 100%")
    @Column(nullable = false)
    private double minAttendancePercentage = 75.0;

    @Column(length = 30)
    private String color = "#4f46e5"; // Indigo default for timetable badge

    // Default constructor required by JPA
    public Subject() {
    }

    // Parameterized constructor
    public Subject(String name, String code, int classesConducted, int classesAttended, double minAttendancePercentage, String color) {
        validateAttendanceValues(classesConducted, classesAttended);
        this.name = name;
        this.code = code;
        this.classesConducted = classesConducted;
        this.classesAttended = classesAttended;
        this.minAttendancePercentage = minAttendancePercentage;
        this.color = (color != null && !color.isBlank()) ? color : "#4f46e5";
    }

    /**
     * Validates that attendance figures comply with physical constraints.
     */
    public static void validateAttendanceValues(int conducted, int attended) {
        if (conducted < 0) {
            throw new IllegalArgumentException("Classes conducted cannot be negative.");
        }
        if (attended < 0) {
            throw new IllegalArgumentException("Classes attended cannot be negative.");
        }
        if (attended > conducted) {
            throw new IllegalArgumentException("Classes attended (" + attended + ") cannot exceed classes conducted (" + conducted + ").");
        }
    }

    /**
     * Calculates the student's current attendance percentage.
     * Formula: (Classes Attended / Classes Conducted) * 100.
     * Handles zero classes conducted gracefully (defaults to 100% since no classes missed yet).
     *
     * @return current attendance percentage as an unrounded double
     */
    public double calculateCurrentAttendancePercentage() {
        if (classesConducted == 0) {
            return 100.0;
        }
        return ((double) classesAttended / (double) classesConducted) * 100.0;
    }

    /**
     * Checks if current attendance is at or above the minimum requirement.
     */
    public boolean isCurrentlySafe() {
        return calculateCurrentAttendancePercentage() >= minAttendancePercentage;
    }

    // Getters and Setters with Encapsulation
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getClassesConducted() {
        return classesConducted;
    }

    public void setClassesConducted(int classesConducted) {
        validateAttendanceValues(classesConducted, this.classesAttended);
        this.classesConducted = classesConducted;
    }

    public int getClassesAttended() {
        return classesAttended;
    }

    public void setClassesAttended(int classesAttended) {
        validateAttendanceValues(this.classesConducted, classesAttended);
        this.classesAttended = classesAttended;
    }

    public double getMinAttendancePercentage() {
        return minAttendancePercentage;
    }

    public void setMinAttendancePercentage(double minAttendancePercentage) {
        this.minAttendancePercentage = minAttendancePercentage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject subject)) return false;
        return Objects.equals(id, subject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", conducted=" + classesConducted +
                ", attended=" + classesAttended +
                ", minReq=" + minAttendancePercentage + "%" +
                '}';
    }
}
