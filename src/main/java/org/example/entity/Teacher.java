package org.example.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho giáo viên dạy thêm
 */
public class Teacher {
    private Long id;                // ID
    private String teacherCode;      // Mã giáo viên
    private String fullName;         // Họ và tên
    private LocalDate dateOfBirth;   // Ngày sinh
    private String gender;           // Giới tính
    private String phone;            // Số điện thoại
    private String email;            // Email
    private String address;          // Địa chỉ
    private String specialization;   // Chuyên môn (Toán, Lý, Hóa, Anh...)
    private String qualification;    // Trình độ (Cử nhân, Thạc sĩ, Tiến sĩ...)
    private Integer yearsOfExperience; // Số năm kinh nghiệm
    private String status;           // ACTIVE, INACTIVE
    private String notes;            // Ghi chú
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Teacher() {
    }

    public Teacher(Long id, String teacherCode, String fullName, LocalDate dateOfBirth, 
                   String gender, String phone, String email, String address, 
                   String specialization, String qualification, Integer yearsOfExperience,
                   String status) {
        this.id = id;
        this.teacherCode = teacherCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.specialization = specialization;
        this.qualification = qualification;
        this.yearsOfExperience = yearsOfExperience;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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
        return "Teacher{" +
                "id=" + id +
                ", teacherCode='" + teacherCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", qualification='" + qualification + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", status='" + status + '\'' +
                '}';
    }
}
