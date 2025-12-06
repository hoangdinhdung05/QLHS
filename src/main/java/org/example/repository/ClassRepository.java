package org.example.repository;

import org.example.entity.ClassEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho ClassEntity
 */
public interface ClassRepository {
    ClassEntity save(ClassEntity classEntity);
    ClassEntity update(ClassEntity classEntity);
    boolean delete(Long id);
    Optional<ClassEntity> findById(Long id);
    Optional<ClassEntity> findByClassCode(String classCode);
    List<ClassEntity> findAll();
    List<ClassEntity> findByStatus(String status);
    List<ClassEntity> searchByName(String keyword);
}
