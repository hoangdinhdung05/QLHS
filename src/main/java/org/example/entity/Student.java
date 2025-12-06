package org.example.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho học sinh trong hệ thống quản lý dạy thêm
 */
public class Student {
    private Long id;
    private String studentCode;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String address;
    private String parentName;
    private String parentPhone;
    private String status; // ACTIVE, INACTIVE
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Student() {
    }

    public Student(Long id, String studentCode, String fullName, LocalDate dateOfBirth,
                   String gender, String phone, String address, String parentName, 
                   String parentPhone, String status, String notes) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", parentPhone='" + parentPhone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

