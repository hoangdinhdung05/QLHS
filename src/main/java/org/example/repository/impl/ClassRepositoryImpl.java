package org.example.repository.impl;

import org.example.entity.ClassEntity;
import org.example.repository.ClassRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementation của ClassRepository với in-memory storage
 */
public class ClassRepositoryImpl implements ClassRepository {
    
    private final Map<Long, ClassEntity> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public ClassRepositoryImpl() {
        // Mock data
        initMockData();
    }
    
    private void initMockData() {
        addMockClass("LOP001", "Toán Nâng Cao Lớp 9", "Toán Học", "123 ng ABC, Hue", "T2, T4, T6 (18:00-19:30)", 150000, 30, 25, "ACTIVE", "GROUP");
        addMockClass("LOP002", "Văn Học Lớp 10", "Ngữ Văn", "456 ng XYZ, Hue", "T3, T5, T7 (19:00-20:30)", 130000, 25, 22, "ACTIVE", "GROUP");
        addMockClass("LOP003", "Tiếng Anh Giao Tiếp", "Tiếng Anh", "789 Tran Hung Dao, Hue", "T2, T4, T6 (17:00-18:30)", 180000, 20, 20, "FULL", "GROUP");
        addMockClass("LOP004", "Vật Lý Lớp 12", "Vật Lý", "12 Le Loi, Hue", "T3, T5 (18:30-20:00)", 160000, 20, 18, "ACTIVE", "GROUP");
        addMockClass("LOP005", "Hóa Học Lớp 11", "Hóa Học", "45 Nguyen Hue, Hue", "T2, T4 (17:30-19:00)", 155000, 18, 15, "ACTIVE", "GROUP");
        addMockClass("LOP006", "Sinh Học Lớp 12", "Sinh Học", "78 Hai Ba Trung, Hue", "T3, T6 (18:00-19:30)", 140000, 22, 20, "ACTIVE", "GROUP");
        addMockClass("LOP007", "Toán Ôn Thi THPT", "Toán Học", "90 Ba Trieu, Hue", "T7, CN (08:00-10:00)", 200000, 35, 30, "ACTIVE", "GROUP");
        addMockClass("LOP008", "Văn Ôn Thi THPT", "Ngữ Văn", "23 Ly Thuong Kiet, Hue", "T7, CN (10:30-12:00)", 180000, 30, 28, "ACTIVE", "GROUP");
        addMockClass("LOP009", "Tiếng Anh IELTS Foundation", "Tiếng Anh", "67 Phan Boi Chau, Hue", "T2, T4, T6 (19:30-21:00)", 250000, 18, 16, "ACTIVE", "SMALL_GROUP");
        addMockClass("LOP010", "Lập Trình Python", "Tin Học", "34 Dien Bien Phu, Hue", "T3, T5 (18:00-20:00)", 220000, 15, 12, "ACTIVE", "SMALL_GROUP");
    }
    
    private void addMockClass(String code, String name, String subject, String location, 
                             String schedule, double fee, int maxStudents, int currentStudents,
                             String status, String classType) {
        ClassEntity entity = new ClassEntity();
        Long id = idGenerator.getAndIncrement();
        entity.setId(id);
        entity.setClassCode(code);
        entity.setClassName(name);
        entity.setSubject(subject);
        entity.setLocation(location);
        entity.setSchedule(schedule);
        entity.setFeePerSession(BigDecimal.valueOf(fee));
        entity.setMaxStudents(maxStudents);
        entity.setCurrentStudents(currentStudents);
        entity.setStatus(status);
        entity.setClassType(classType);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        storage.put(id, entity);
    }
    
    @Override
    public ClassEntity save(ClassEntity classEntity) {
        if (classEntity.getId() == null) {
            classEntity.setId(idGenerator.getAndIncrement());
        }
        classEntity.setCreatedAt(LocalDateTime.now());
        classEntity.setUpdatedAt(LocalDateTime.now());
        storage.put(classEntity.getId(), classEntity);
        return classEntity;
    }
    
    @Override
    public ClassEntity update(ClassEntity classEntity) {
        if (classEntity.getId() == null || !storage.containsKey(classEntity.getId())) {
            throw new RuntimeException("Class không tồn tại");
        }
        classEntity.setUpdatedAt(LocalDateTime.now());
        storage.put(classEntity.getId(), classEntity);
        return classEntity;
    }
    
    @Override
    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }
    
    @Override
    public Optional<ClassEntity> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public Optional<ClassEntity> findByClassCode(String classCode) {
        return storage.values().stream()
                .filter(c -> c.getClassCode().equalsIgnoreCase(classCode))
                .findFirst();
    }
    
    @Override
    public List<ClassEntity> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public List<ClassEntity> findByStatus(String status) {
        return storage.values().stream()
                .filter(c -> status.equalsIgnoreCase(c.getStatus()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ClassEntity> searchByName(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return storage.values().stream()
                .filter(c -> c.getClassName().toLowerCase().contains(lowerKeyword) ||
                           c.getClassCode().toLowerCase().contains(lowerKeyword) ||
                           c.getSubject().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}
