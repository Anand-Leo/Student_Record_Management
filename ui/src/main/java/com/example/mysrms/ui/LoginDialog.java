package com.example.mysrms.ui;

import com.example.mysrms.backend.User;
import com.example.mysrms.backend.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO = new UserDAO(); // Add this
    private boolean authenticated = false; // Track authentication status

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);
        setLayout(new GridLayout(3, 2, 5, 5));
        setSize(300, 150);
        setLocationRelativeTo(parent);

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticate(username, password)) {
                authenticated = true; // Set authentication status
                dispose(); // Close the dialog
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(loginButton);
    }

    private boolean authenticate(String username, String password) {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user != null) {
                // return BCrypt.checkpw(password, user.getPassword()); // Verify hashed password
                return password.equals(user.getPassword()); // Plain text comparison (not secure)
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // Authentication failed
    }
    // Method to check if the user is authenticated
    public boolean isAuthenticated() {
        return authenticated;
    }
}