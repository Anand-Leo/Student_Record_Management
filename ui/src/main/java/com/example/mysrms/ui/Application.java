package com.example.mysrms.ui;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        // Ensure UI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Show the LoginDialog first
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            // If login is successful, open the MainFrame
            if (loginDialog.isAuthenticated()) {
                MainFrame frame = new MainFrame();
                frame.setVisible(true); // Make the frame visible
                frame.setLocationRelativeTo(null); // Center the window
            } else {
                System.exit(0); // Exit if login fails
            }
        });

    }
}