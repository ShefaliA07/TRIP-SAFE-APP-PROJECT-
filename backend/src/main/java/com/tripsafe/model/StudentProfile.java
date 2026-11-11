package com.tripsafe.model;

import jakarta.persistence.*;

/**
 * Entity representing the student's profile and hostel preferences.
 */
@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name = "Alex Mercer";

    @Column(length = 50)
    private String rollNumber = "21CS042";

    @Column(length = 100)
    private String collegeName = "National Institute of Technology";

    @Column(length = 50)
    private String hostelBlock = "Block C, Room 314";

    @Column(nullable = false)
    private double defaultMinAttendance = 75.0;

    public StudentProfile() {
    }

    public StudentProfile(String name, String rollNumber, String collegeName, String hostelBlock, double defaultMinAttendance) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.collegeName = collegeName;
        this.hostelBlock = hostelBlock;
        this.defaultMinAttendance = defaultMinAttendance;
    }

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

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getHostelBlock() {
        return hostelBlock;
    }

    public void setHostelBlock(String hostelBlock) {
        this.hostelBlock = hostelBlock;
    }

    public double getDefaultMinAttendance() {
        return defaultMinAttendance;
    }

    public void setDefaultMinAttendance(double defaultMinAttendance) {
        this.defaultMinAttendance = defaultMinAttendance;
    }
}
