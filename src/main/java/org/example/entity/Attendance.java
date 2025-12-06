package org.example.entity;

import java.time.LocalDateTime;

/**
 * Entity đại diện cho điểm danh học sinh trong buổi học
 */
public class Attendance {
    private Long id;
    private Long lessonSessionId;
    private Long studentId;
    private String status; // PRESENT, ABSENT, LATE
    private String notes;
    private LocalDateTime createdAt;

    // Thông tin thêm từ join (không lưu DB)
    private String studentName;
    private String studentCode;

    public Attendance() {
    }

    public Attendance(Long id, Long lessonSessionId, Long studentId, String status, String notes) {
        this.id = id;
        this.lessonSessionId = lessonSessionId;
        this.studentId = studentId;
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

    public Long getLessonSessionId() {
        return lessonSessionId;
    }

    public void setLessonSessionId(Long lessonSessionId) {
        this.lessonSessionId = lessonSessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", lessonSessionId=" + lessonSessionId +
                ", studentId=" + studentId +
                ", status='" + status + '\'' +
                '}';
    }
}
