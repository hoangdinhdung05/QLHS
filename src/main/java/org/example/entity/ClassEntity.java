package org.example.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho một lớp học
 */
public class ClassEntity {
    private Long id;
    private String classCode;
    private String className;
    private String subject;
    private BigDecimal feePerSession;
    private String schedule;
    private String status; // ACTIVE, INACTIVE
    private String notes;
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

    @Override
    public String toString() {
        return "ClassEntity{" +
                "id=" + id +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", subject='" + subject + '\'' +
                ", feePerSession=" + feePerSession +
                ", schedule='" + schedule + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
