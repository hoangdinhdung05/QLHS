package org.example.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity đại diện cho một buổi học
 */
public class LessonSession {
    private Long id;
    private Long classId;
    private LocalDate lessonDate;
    private String lessonTime;
    private String lessonTopic;
    private String notes;
    private LocalDateTime createdAt;

    // Thông tin thêm từ join (không lưu DB)
    private String className;
    private String subject;

    public LessonSession() {
    }

    public LessonSession(Long id, Long classId, LocalDate lessonDate, 
                        String lessonTime, String lessonTopic, String notes) {
        this.id = id;
        this.classId = classId;
        this.lessonDate = lessonDate;
        this.lessonTime = lessonTime;
        this.lessonTopic = lessonTopic;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public LocalDate getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDate lessonDate) {
        this.lessonDate = lessonDate;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public String getLessonTopic() {
        return lessonTopic;
    }

    public void setLessonTopic(String lessonTopic) {
        this.lessonTopic = lessonTopic;
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

    @Override
    public String toString() {
        return "LessonSession{" +
                "id=" + id +
                ", classId=" + classId +
                ", lessonDate=" + lessonDate +
                ", lessonTime='" + lessonTime + '\'' +
                ", lessonTopic='" + lessonTopic + '\'' +
                '}';
    }
}
