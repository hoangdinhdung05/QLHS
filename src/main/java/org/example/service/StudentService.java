package org.example.service;

import org.example.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    /**
     * Lấy tất cả sinh viên
     */
    List<Student> getAllStudents();

    /**
     * Thêm sinh viên (new method)
     */
    Student createStudent(Student student);

    /**
     * Thêm sinh viên (old method - deprecated)
     */
    void addStudent(Student student);

    /**
     * Cập nhật sinh viên tại vị trí index
     */
    void updateStudent(int index, Student student);

    /**
     * Xóa sinh viên tại vị trí index
     */
    void deleteStudent(int index);

    /**
     * Xóa tất cả sinh viên
     */
    void deleteAll();

    /**
     * Kiểm tra mã sinh viên đã tồn tại (không phân biệt hoa thường)
     * @param maSV Mã sinh viên cần kiểm tra
     * @param ignoreIndex Chỉ số của sinh viên đang được chỉnh sửa (bỏ qua trong kiểm tra), -1 nếu không có
     */
    boolean existsMaSV(String maSV, int ignoreIndex);

    /**
     * Kiểm tra email đã tồn tại (không phân biệt hoa thường)
     * @param email Email cần kiểm tra
     * @param ignoreIndex Chỉ số của sinh viên đang được chỉnh sửa (bỏ qua trong kiểm tra), -1 nếu không có
     */
    boolean existsEmail(String email, int ignoreIndex);

    /**
     * Tìm theo mã SV (chứa keyword, không phân biệt hoa thường)
     */
    List<Student> searchByMaSV(String keyword);

    /**
     * Sắp xếp giảm dần theo điểm tổng kết
     */
    void sortByScoreDesc();
    
    // ============ NEW METHODS FOR CRUD OPERATIONS ============
    
    /**
     * Tìm học sinh theo mã học sinh
     */
    Optional<Student> findByStudentCode(String studentCode);
    
    /**
     * Cập nhật thông tin học sinh
     */
    Student updateStudentNew(Student student);
    
    /**
     * Xóa học sinh theo ID
     */
    boolean deleteStudentById(Long id);
}
