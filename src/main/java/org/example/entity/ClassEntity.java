package org.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho một lớp học dạy thêm offline
 */
public class ClassEntity {
    private Long id;
    private String classCode;           // Mã lớp học
    private String className;           // Tên lớp học
    private String subject;             // Môn học
    private String teacherName;         // Tên giáo viên
    private String location;            // Địa điểm dạy
    private String timeSlot;            // Khung giờ (VD: "18:00-20:00")
    private String dayOfWeek;           // Thứ trong tuần (VD: "2,4,6" hoặc "3,5,7")
    private Integer maxStudents;        // Số lượng học sinh tối đa (1-2 học sinh hoặc nhóm)
    private Integer currentStudents;    // Số học sinh hiện tại
    private BigDecimal feePerSession;   // Học phí mỗi buổi
    private String schedule;            // Lịch học tổng quan
    private String status;              // ACTIVE, INACTIVE, FULL
    private String classType;           // INDIVIDUAL (1-1), SMALL_GROUP (1-2), GROUP (3-10)
    private String notes;               // Ghi chú
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ClassEntity() {
    }

    public ClassEntity(Long id, String classCode, String className, String subject, 
                      BigDecimal feePerSession, String schedule, String status, String notes) {
        this.id = id;
        this.classCode = classCode;
        this.className = className;
        this.subject = subject;
        this.feePerSession = feePerSession;
        this.schedule = schedule;
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

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getFeePerSession() {
        return feePerSession;
    }

    public void setFeePerSession(BigDecimal feePerSession) {
        this.feePerSession = feePerSession;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(Integer currentStudents) {
        this.currentStudents = currentStudents;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "id=" + id +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", subject='" + subject + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", location='" + location + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", maxStudents=" + maxStudents +
                ", currentStudents=" + currentStudents +
                ", feePerSession=" + feePerSession +
                ", status='" + status + '\'' +
                ", classType='" + classType + '\'' +
                '}';
    }
}
