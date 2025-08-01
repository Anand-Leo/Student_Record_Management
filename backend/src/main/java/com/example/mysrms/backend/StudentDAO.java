package com.example.mysrms.backend;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentDAO {

    // Method to add a new student
    public void addStudent(Student student) throws SQLException {
        String query = "INSERT INTO students (roll_number, name, birth_date, gender, section, branch_name, admission_year, college_name, university_name, phone_no)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student.getRoll_number());
            stmt.setString(2, student.getName());

            if(student.getBirthDate() != null) {
                stmt.setDate(3, Date.valueOf(student.getBirthDate()));
            } else {
                stmt.setNull(3, Types.DATE);  // Set SQL NULL for empty dates
            }

            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getSection());
            stmt.setString(6, student.getBranch_name());
            stmt.setInt(7, student.getAdmission_year());
            stmt.setString(8, student.getCollege_name());
            stmt.setString(9, student.getUniversity_name());
            stmt.setString(10, student.getPhone_no());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to add student: " + e.getMessage(), e);
        }
    }

    // Method to get all students
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT roll_number, name, birth_date, gender, section, branch_name, admission_year, college_name, university_name, phone_no FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int serialNumber = 1; // Start serial number from 1
            while (rs.next()) {
                Student student = new Student();
                student.setSerialNumber(serialNumber++); // Set the serial number
                student.setRoll_number(rs.getString("roll_number"));
                student.setName(rs.getString("name"));
                student.setBirthDate(rs.getDate("birth_date") != null ? rs.getDate("birth_date").toLocalDate() : null);  // Correct date handling
                student.setGender(rs.getString("gender"));
                student.setSection(rs.getString("section"));
                student.setBranch_name(rs.getString("branch_name"));
                student.setAdmission_year(rs.getInt("admission_year"));
                student.setCollege_name(rs.getString("college_name"));
                student.setUniversity_name(rs.getString("university_name"));
                student.setPhone_no(rs.getString("phone_no"));
                students.add(student);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch students: " + e.getMessage(), e);
        }
        return students;
    }

    // Method to get a student by roll_number
    public Student getStudentByRollNumber(String rollNumber) throws SQLException {
        Student student = null;
        String query = "SELECT * FROM students WHERE roll_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rollNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    student = new Student();
                    student.setOriginalRollNumber(rollNumber); // Set original roll number
                    student.setRoll_number(rs.getString("roll_number"));
                    student.setName(rs.getString("name"));
                    java.sql.Date birthDateSql = rs.getDate("birth_date");
                    LocalDate birthDate = (birthDateSql != null) ? birthDateSql.toLocalDate() : null;
                    student.setBirthDate(birthDate); // ✅ Safe null handling
                    student.setGender(rs.getString("gender"));
                    student.setSection(rs.getString("section"));
                    student.setBranch_name(rs.getString("branch_name"));
                    student.setRoll_number(rs.getString("roll_number"));
                    student.setAdmission_year(rs.getInt("admission_year"));
                    student.setCollege_name(rs.getString("college_name"));
                    student.setUniversity_name(rs.getString("university_name"));
                    student.setPhone_no(rs.getString("phone_no"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch student by roll number: " + e.getMessage(), e);
        }
        return student;
    }

    // Method to update a student
    public void updateStudent(Student student) throws SQLException {
        // Check if the new roll number already exists
        if (!student.getRoll_number().equals(student.getOriginalRollNumber())) {
            Student existingStudent = getStudentByRollNumber(student.getRoll_number());
            if (existingStudent != null) {
                throw new SQLException("Roll number already exists!");
            }
        }
        String query = "UPDATE students SET " +
                "name = ?, birth_date = ?, gender = ?, section = ?, branch_name = ?, " +
                "admission_year = ?, college_name = ?, university_name = ?, phone_no = ?, roll_number = ? " +
                "WHERE roll_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student.getName());
            if (student.getBirthDate() != null) {
                stmt.setDate(2, Date.valueOf(student.getBirthDate()));
            } else {
                stmt.setNull(2, Types.DATE); // Set SQL NULL
            }
            stmt.setString(3, student.getGender());
            stmt.setString(4, student.getSection());
            stmt.setString(5, student.getBranch_name());
            stmt.setInt(6, student.getAdmission_year());
            stmt.setString(7, student.getCollege_name());
            stmt.setString(8, student.getUniversity_name());
            stmt.setString(9,student.getPhone_no());
            stmt.setString(10, student.getRoll_number()); // New roll number
            stmt.setString(11, student.getOriginalRollNumber()); // WHERE clause uses original roll number
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update student: " + e.getMessage(), e);
        }
    }

    // Method to delete a student
    public void deleteStudent(String rollNumber) throws SQLException {
        String query = "DELETE FROM students WHERE roll_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rollNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to delete student: " + e.getMessage(), e);
        }
    }
}