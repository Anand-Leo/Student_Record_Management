package com.example.mysrms.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Method to add a new course
    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO courses (course_name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getDescription());
            stmt.executeUpdate();
        }
    }

    // Method to get all courses
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT course_name, description FROM courses ORDER BY course_name"; // Order by name or another field
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int serialNumber = 1; // Start serial number from 1
            while (rs.next()) {
                Course course = new Course();
                course.setSerialNumber(serialNumber++); // Dynamic assignment
                course.setCourseName(rs.getString("course_name"));
                course.setDescription(rs.getString("description"));
                courses.add(course);
            }
        }
        return courses;
    }

    // Method to get a course by ID
    // In CourseDAO.getCourseByName()
    public Course getCourseByName(String courseName) throws SQLException {
        Course course = null;
        String query = "SELECT * FROM courses WHERE course_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courseName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    course = new Course();
                    course.setOriginalCourseName(courseName); // Store original name
                    course.setCourseName(rs.getString("course_name"));
                    course.setDescription(rs.getString("description"));
                }
            }
        }
        return course;
    }
    // Method to update a course
    public void updateCourse(Course course, String originalCourseName) throws SQLException {
        String query = "UPDATE courses SET course_name = ?, description = ? WHERE course_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getDescription());
            stmt.setString(3, originalCourseName); // Use original name to find the record
            stmt.executeUpdate();
        }
    }

    // Method to delete a course
    public void deleteCourse(String courseName) throws SQLException {
        String query = "DELETE FROM courses WHERE course_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, courseName);
            stmt.executeUpdate();
        }
    }
}