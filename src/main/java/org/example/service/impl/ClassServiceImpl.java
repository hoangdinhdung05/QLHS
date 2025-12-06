package org.example.service.impl;

import org.example.entity.ClassEntity;
import org.example.repository.ClassRepository;
import org.example.repository.impl.ClassRepositoryImpl;
import org.example.service.ClassService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation của ClassService
 */
public class ClassServiceImpl implements ClassService {
    
    private final ClassRepository classRepository;
    
    public ClassServiceImpl() {
        this.classRepository = new ClassRepositoryImpl();
    }
    
    @Override
    public ClassEntity createClass(ClassEntity classEntity) {
        // Validation
        validateClass(classEntity);
        
        // Kiểm tra trùng mã lớp
        if (classRepository.findByClassCode(classEntity.getClassCode()).isPresent()) {
            throw new RuntimeException("Mã lớp đã tồn tại: " + classEntity.getClassCode());
        }
        
        // Set default values
        if (classEntity.getCurrentStudents() == null) {
            classEntity.setCurrentStudents(0);
        }
        if (classEntity.getStatus() == null) {
            classEntity.setStatus("ACTIVE");
        }
        
        return classRepository.save(classEntity);
    }
    
    @Override
    public ClassEntity updateClass(ClassEntity classEntity) {
        validateClass(classEntity);
        
        // Kiểm tra lớp có tồn tại không
        if (classEntity.getId() == null || classRepository.findById(classEntity.getId()).isEmpty()) {
            throw new RuntimeException("Lớp học không tồn tại");
        }
        
        // Kiểm tra trùng mã lớp (trừ chính nó)
        Optional<ClassEntity> existingClass = classRepository.findByClassCode(classEntity.getClassCode());
        if (existingClass.isPresent() && !existingClass.get().getId().equals(classEntity.getId())) {
            throw new RuntimeException("Mã lớp đã tồn tại: " + classEntity.getClassCode());
        }
        
        // Cập nhật trạng thái FULL nếu đủ học sinh
        if (classEntity.getCurrentStudents() != null && classEntity.getMaxStudents() != null) {
            if (classEntity.getCurrentStudents() >= classEntity.getMaxStudents()) {
                classEntity.setStatus("FULL");
            } else if ("FULL".equals(classEntity.getStatus())) {
                classEntity.setStatus("ACTIVE");
            }
        }
        
        return classRepository.update(classEntity);
    }
    
    @Override
    public boolean deleteClass(Long id) {
        return classRepository.delete(id);
    }
    
    @Override
    public Optional<ClassEntity> findById(Long id) {
        return classRepository.findById(id);
    }
    
    @Override
    public Optional<ClassEntity> findByClassCode(String classCode) {
        return classRepository.findByClassCode(classCode);
    }
    
    @Override
    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }
    
    @Override
    public List<ClassEntity> searchClasses(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllClasses();
        }
        return classRepository.searchByName(keyword.trim());
    }
    
    @Override
    public List<ClassEntity> findByStatus(String status) {
        return classRepository.findByStatus(status);
    }
    
    @Override
    public List<ClassEntity> findBySubject(String subject) {
        return getAllClasses().stream()
                .filter(c -> c.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClassEntity> findByClassType(String classType) {
        return getAllClasses().stream()
                .filter(c -> classType.equalsIgnoreCase(c.getClassType()))
                .collect(Collectors.toList());
    }
    
    /**
     * Validate thông tin lớp học
     */
    private void validateClass(ClassEntity classEntity) {
        if (classEntity == null) {
            throw new RuntimeException("Thông tin lớp học không được để trống");
        }
        
        if (classEntity.getClassCode() == null || classEntity.getClassCode().trim().isEmpty()) {
            throw new RuntimeException("Mã lớp không được để trống");
        }
        
        if (classEntity.getClassName() == null || classEntity.getClassName().trim().isEmpty()) {
            throw new RuntimeException("Tên lớp không được để trống");
        }
        
        if (classEntity.getSubject() == null || classEntity.getSubject().trim().isEmpty()) {
            throw new RuntimeException("Môn học không được để trống");
        }
        
        if (classEntity.getFeePerSession() == null || classEntity.getFeePerSession().doubleValue() <= 0) {
            throw new RuntimeException("Học phí phải lớn hơn 0");
        }
    }
}
