package org.example;

import org.example.config.DatabaseConfig;
import org.example.entity.Account;
import org.example.entity.Student;
import org.example.repository.AccountRepository;
import org.example.repository.impl.AccountRepositoryImpl;
import org.example.service.StudentService;
import org.example.service.impl.StudentServiceImpl;
import org.example.view.LoginForm;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Set Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Khởi tạo database
        System.out.println("=== Khởi động ứng dụng Quản Lý Dạy Thêm ===");
        DatabaseConfig.getInstance();
        
        // Khởi tạo dữ liệu mặc định
        initializeDefaultData();
        
        System.out.println("\n=== Ứng dụng đã sẵn sàng ===");
        System.out.println("Database location: " + DatabaseConfig.getDatabasePath());
        
        // Hiển thị màn hình đăng nhập
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
    
    /**
     * Khởi tạo dữ liệu mặc định (tài khoản admin và học sinh mẫu)
     */
    private static void initializeDefaultData() {
        createDefaultAccount();
        testAddStudent();
    }
    
    /**
     * Tạo tài khoản admin mặc định
     */
    private static void createDefaultAccount() {
        try {
            AccountRepository accountRepository = new AccountRepositoryImpl();
            
            // Kiểm tra xem đã có tài khoản admin chưa
            if (accountRepository.findByUsername("admin") == null) {
                System.out.println("\nTạo tài khoản admin mặc định...");
                
                Account adminAccount = new Account();
                adminAccount.setUsername("admin");
                adminAccount.setPassword("admin123");
                adminAccount.setRole("ADMIN");
                adminAccount.setActive(true);
                
                accountRepository.save(adminAccount);
                System.out.println("✓ Đã tạo tài khoản admin");
                System.out.println("  Username: admin");
                System.out.println("  Password: admin123");
            } else {
                System.out.println("\nTài khoản admin đã tồn tại");
            }
            
            // Tạo thêm tài khoản user mẫu
            if (accountRepository.findByUsername("user") == null) {
                Account userAccount = new Account();
                userAccount.setUsername("user");
                userAccount.setPassword("user123");
                userAccount.setRole("USER");
                userAccount.setActive(true);
                
                accountRepository.save(userAccount);
                System.out.println("✓ Đã tạo tài khoản user");
                System.out.println("  Username: user");
                System.out.println("  Password: user123");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo tài khoản mặc định: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testAddStudent() {
        try {
            StudentService studentService = new StudentServiceImpl();
            
            // Kiểm tra xem đã có học sinh mẫu chưa
            List<Student> existingStudents = studentService.getAllStudents();
            
            if (existingStudents.isEmpty()) {
                System.out.println("\nThêm dữ liệu học sinh mẫu...");
                
                // Tạo học sinh mẫu
                Student student1 = new Student();
                student1.setStudentCode("HS001");
                student1.setFullName("Nguyễn Văn An");
                student1.setDateOfBirth(LocalDate.of(2010, 5, 15));
                student1.setGender("Nam");
                student1.setPhone("0123456789");
                student1.setAddress("123 Đường ABC, Huế");
                student1.setParentName("Nguyễn Văn A");
                student1.setParentPhone("0987654321");
                student1.setStatus("ACTIVE");
                student1.setNotes("Học sinh giỏi");
                
                studentService.createStudent(student1);
                System.out.println("✓ Đã thêm: " + student1.getFullName());
                
                Student student2 = new Student();
                student2.setStudentCode("HS002");
                student2.setFullName("Trần Thị Bình");
                student2.setDateOfBirth(LocalDate.of(2011, 8, 20));
                student2.setGender("Nữ");
                student2.setPhone("0123456788");
                student2.setAddress("456 Đường XYZ, Huế");
                student2.setParentName("Trần Văn B");
                student2.setParentPhone("0987654322");
                student2.setStatus("ACTIVE");
                
                studentService.createStudent(student2);
                System.out.println("✓ Đã thêm: " + student2.getFullName());
            } else {
                System.out.println("\nĐã có " + existingStudents.size() + " học sinh trong database");
                for (Student s : existingStudents) {
                    System.out.println("  - " + s.getStudentCode() + ": " + s.getFullName());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}