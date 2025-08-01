 package com.example.mysrms.backend;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Add User (No serialNumber in query)
    public void addUser(User user) throws SQLException {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getUserRole());
            stmt.executeUpdate();
        }
    }

    // Get All Users (Dynamic serialNumber)
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT username, password, role FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int serialNumber = 1;
            while (rs.next()) {
                User user = new User();
                user.setSerialNumber(serialNumber++); // Dynamic assignment
                user.setUserName(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setUserRole(rs.getString("role"));
                users.add(user);
            }
        }
        return users;
    }

    // Get User by username (Primary key)
    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setOriginalUserName(username);
                    user.setUserName(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setUserRole(rs.getString("role"));
                }
            }
        }
        return user;
    }

    // Update User
    public void updateUser(User user, String originalUserName) throws SQLException {
        String query = "UPDATE users SET username = ?, password = ?, role = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getUserRole());
            stmt.setString(4, user.getOriginalUserName());
            stmt.executeUpdate();
        }
    }

    // Delete User by username (Primary key)
    public void deleteUser(String username) throws SQLException {
        String query = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }
}