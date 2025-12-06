package org.example.repository;

import org.example.entity.Student;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Student
 */
public interface StudentRepository {
    Student save(Student student);
    Student update(Student student);
    boolean delete(Long id);
    Optional<Student> findById(Long id);
    Optional<Student> findByStudentCode(String studentCode);
    List<Student> findAll();
    List<Student> findByStatus(String status);
    List<Student> searchByName(String keyword);
    List<Student> findByClassId(Long classId);
}

