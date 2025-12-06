package org.example.repository;

import org.example.entity.Attendance;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Attendance
 */
public interface AttendanceRepository {
    Attendance save(Attendance attendance);
    Attendance update(Attendance attendance);
    boolean delete(Long id);
    Optional<Attendance> findById(Long id);
    List<Attendance> findByLessonSessionId(Long lessonSessionId);
    List<Attendance> findByStudentId(Long studentId);
    Optional<Attendance> findByLessonAndStudent(Long lessonSessionId, Long studentId);
    int countPresentByStudentAndMonth(Long studentId, Long classId, int year, int month);
}
