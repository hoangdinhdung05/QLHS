package org.example.repository;

import org.example.entity.Teacher;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface để thao tác với bảng teachers
 */
public interface TeacherRepository {
    
    /**
     * Lưu giáo viên mới
     */
    Teacher save(Teacher teacher);
    
    /**
     * Cập nhật thông tin giáo viên
     */
    Teacher update(Teacher teacher);
    
    /**
     * Xóa giáo viên theo ID
     */
    boolean delete(Long id);
    
    /**
     * Tìm giáo viên theo ID
     */
    Optional<Teacher> findById(Long id);
    
    /**
     * Tìm giáo viên theo mã giáo viên
     */
    Optional<Teacher> findByTeacherCode(String teacherCode);
    
    /**
     * Lấy tất cả giáo viên
     */
    List<Teacher> findAll();
    
    /**
     * Lấy giáo viên theo trạng thái
     */
    List<Teacher> findByStatus(String status);
    
    /**
     * Tìm kiếm giáo viên theo tên
     */
    List<Teacher> searchByName(String keyword);
    
    /**
     * Tìm kiếm giáo viên theo chuyên môn
     */
    List<Teacher> findBySpecialization(String specialization);
}
