package com.ScholarShare.entity;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String role; // student or admin
    private String status;
    private String profilePic;
    private Timestamp createdAt;

   public User(){}

    public User(int userId, String fullName, String email, String phone, String password, String role, String status, String profilePic, Timestamp createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.status = status;
        this.profilePic = profilePic;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
       return "admin".equals(this.role);
    }

    public boolean isStudent() {
       return "student".equals(this.role);
    }

    public boolean isActive(){
       return "active".equals(this.status);
    }
    @Override
    public String toString() {
        return "User{userId=" + userId + ", fullName='" + fullName +
                "', email='" + email + "', role='" + role +
                "', status='" + status + "'}";
    }

}
