package com.example.mysrms.backend;

import java.time.LocalDate;

public class Student {

    private transient int serialNumber; // Transient field for S. No.
    private String roll_number; // Primary key
    private String originalRollNumber; // Add this field
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String phone_no;
    private String branch_name;
    private int admission_year;
    private String college_name;
    private String university_name;
    private String section;


    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public String getBranch_name() { return branch_name; }
    public void setBranch_name(String branch_name) { this.branch_name = branch_name; }
    public String getRoll_number() { return roll_number; }
    public void setRoll_number(String roll_number) { this.roll_number = roll_number; }
    public String getOriginalRollNumber() { return originalRollNumber; }
    public void setOriginalRollNumber(String originalRollNumber) { this.originalRollNumber = originalRollNumber; }
    public int getAdmission_year() { return admission_year; }
    public void setAdmission_year(int admission_year) { this.admission_year = admission_year; }
    public String getCollege_name() { return college_name; }
    public void setCollege_name(String college_name) { this.college_name = college_name; }
    public String getUniversity_name() { return university_name; }
    public void setUniversity_name(String university_name) { this.university_name = university_name; }
    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public int getSerialNumber() { return serialNumber; }
    public void setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; }

    // Method to clear input fields
    public void clearInputFields() {
        this.name = "";
        this.birthDate = null;
        this.gender = "";
        this.phone_no = ""; // Clear section field
        this.branch_name = "";
        this.roll_number = "";
        this.admission_year = 0;
        this.college_name = "";
        this.university_name = "";

    }
}