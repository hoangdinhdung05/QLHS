package org.example.service.impl;

import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.example.repository.impl.StudentRepositoryImpl;
import org.example.service.StudentService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của StudentService sử dụng Repository
 */
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl() {
        this.studentRepository = new StudentRepositoryImpl();
    }

    public Student createStudent(Student student) {
        // Validation
        validateStudent(student);
        
        // Kiểm tra trùng mã học sinh
        if (studentRepository.findByStudentCode(student.getStudentCode()).isPresent()) {
            throw new RuntimeException("Mã học sinh đã tồn tại: " + student.getStudentCode());
        }
        
        return studentRepository.save(student);
    }

    public Student updateStudentNew(Student student) {
        validateStudent(student);
        
        // Kiểm tra học sinh có tồn tại không
        if (student.getId() == null || studentRepository.findById(student.getId()).isEmpty()) {
            throw new RuntimeException("Học sinh không tồn tại");
        }
        
        // Kiểm tra trùng mã học sinh (trừ chính nó)
        Optional<Student> existingStudent = studentRepository.findByStudentCode(student.getStudentCode());
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(student.getId())) {
            throw new RuntimeException("Mã học sinh đã tồn tại: " + student.getStudentCode());
        }
        
        return studentRepository.update(student);
    }

    public boolean deleteStudentById(Long id) {
        return studentRepository.delete(id);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> findByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getActiveStudents() {
        return studentRepository.findByStatus("ACTIVE");
    }

    public List<Student> searchStudentsNew(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.searchByName(keyword.trim());
    }

    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findByClassId(classId);
    }

    public boolean markStudentAsInactive(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStatus("INACTIVE");
            studentRepository.update(student);
            return true;
        }
        return false;
    }

    /**
     * Validate thông tin học sinh
     */
    private void validateStudent(Student student) {
        if (student == null) {
            throw new RuntimeException("Thông tin học sinh không được để trống");
        }
        
        if (student.getStudentCode() == null || student.getStudentCode().trim().isEmpty()) {
            throw new RuntimeException("Mã học sinh không được để trống");
        }
        
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Tên học sinh không được để trống");
        }
        
        if (student.getParentPhone() == null || student.getParentPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại phụ huynh không được để trống");
        }
    }

    // ============ OLD INTERFACE METHODS (for backward compatibility) ============
    
    @Override
    public void addStudent(Student student) {
        createStudent(student);
    }

    @Override
    public void updateStudent(int index, Student student) {
        throw new UnsupportedOperationException("Use updateStudentNew(Student) instead");
    }

    @Override
    public void deleteStudent(int index) {
        throw new UnsupportedOperationException("Use deleteStudentById(Long id) instead");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean existsMaSV(String maSV, int ignoreIndex) {
        return findByStudentCode(maSV).isPresent();
    }

    @Override
    public boolean existsEmail(String email, int ignoreIndex) {
        return false; // Not implemented in new version
    }

    @Override
    public List<Student> searchByMaSV(String keyword) {
        return searchStudentsNew(keyword);
    }

    @Override
    public void sortByScoreDesc() {
        // Not implemented - no score field in current entity
        throw new UnsupportedOperationException("Sort by score not implemented");
    }
}
