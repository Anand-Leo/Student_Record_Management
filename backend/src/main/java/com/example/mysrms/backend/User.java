package com.example.mysrms.backend;

public class User {
    private String userName;
    private String password;
    private String userRole;
    private String originalUserName; // For updates
    private transient int serialNumber; // Display-only field

    // Getters & Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public int getSerialNumber() { return serialNumber; }
    public void setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; }
    public String getOriginalUserName() { return originalUserName; }
    public void setOriginalUserName(String originalUserName) {
        this.originalUserName = originalUserName;
    }
}