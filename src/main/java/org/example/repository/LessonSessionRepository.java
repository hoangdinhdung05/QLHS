package org.example.repository;

import org.example.entity.LessonSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho LessonSession
 */
public interface LessonSessionRepository {
    LessonSession save(LessonSession lessonSession);
    LessonSession update(LessonSession lessonSession);
    boolean delete(Long id);
    Optional<LessonSession> findById(Long id);
    List<LessonSession> findAll();
    List<LessonSession> findByClassId(Long classId);
    List<LessonSession> findByDateRange(LocalDate startDate, LocalDate endDate);
    List<LessonSession> findByMonth(int year, int month);
}
