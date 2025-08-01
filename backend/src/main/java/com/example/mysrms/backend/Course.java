package com.example.mysrms.backend;

public class Course {
    private String courseName;
    private String description;
    private String originalCourseName; // Add this field

    // Transient field for display purposes only
    private transient int serialNumber;

    // Getters and Setters
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getSerialNumber() { return serialNumber; }
    public void setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; }
    public String getOriginalCourseName() { return originalCourseName; }
    public void setOriginalCourseName(String originalCourseName) { this.originalCourseName = originalCourseName; }
}