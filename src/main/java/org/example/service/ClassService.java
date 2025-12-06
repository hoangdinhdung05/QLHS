package org.example.service;

import org.example.entity.ClassEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service interface cho quản lý lớp học
 */
public interface ClassService {
    
    /**
     * Tạo lớp học mới
     */
    ClassEntity createClass(ClassEntity classEntity);
    
    /**
     * Cập nhật thông tin lớp học
     */
    ClassEntity updateClass(ClassEntity classEntity);
    
    /**
     * Xóa lớp học theo ID
     */
    boolean deleteClass(Long id);
    
    /**
     * Tìm lớp học theo ID
     */
    Optional<ClassEntity> findById(Long id);
    
    /**
     * Tìm lớp học theo mã lớp
     */
    Optional<ClassEntity> findByClassCode(String classCode);
    
    /**
     * Lấy tất cả lớp học
     */
    List<ClassEntity> getAllClasses();
    
    /**
     * Tìm kiếm lớp học theo tên hoặc mã
     */
    List<ClassEntity> searchClasses(String keyword);
    
    /**
     * Lấy lớp học theo trạng thái
     */
    List<ClassEntity> findByStatus(String status);
    
    /**
     * Lấy lớp học theo môn học
     */
    List<ClassEntity> findBySubject(String subject);
    
    /**
     * Lấy lớp học theo loại (INDIVIDUAL, SMALL_GROUP, GROUP)
     */
    List<ClassEntity> findByClassType(String classType);
}
