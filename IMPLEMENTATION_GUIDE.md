# Hướng dẫn Implementation Chi Tiết

## 📚 Mục lục nhanh
1. [Repository Implementation](#1-repository-implementation)
2. [Service Implementation](#2-service-implementation)  
3. [UI Implementation](#3-ui-implementation)
4. [Income Calculation](#4-income-calculation)
5. [Export Features](#5-export-features)

---

## 1. Repository Implementation

### ClassRepositoryImpl.java (Mẫu)

```java
package org.example.repository.impl;

import org.example.config.DatabaseConfig;
import org.example.entity.ClassEntity;
import org.example.repository.ClassRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassRepositoryImpl implements ClassRepository {
    
    private final Connection connection;

    public ClassRepositoryImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }

    @Override
    public ClassEntity save(ClassEntity classEntity) {
        String sql = """
            INSERT INTO classes (class_code, class_name, subject, fee_per_session, 
                               schedule, status, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, classEntity.getClassCode());
            stmt.setString(2, classEntity.getClassName());
            stmt.setString(3, classEntity.getSubject());
            stmt.setBigDecimal(4, classEntity.getFeePerSession());
            stmt.setString(5, classEntity.getSchedule());
            stmt.setString(6, classEntity.getStatus() != null ? classEntity.getStatus() : "ACTIVE");
            stmt.setString(7, classEntity.getNotes());

            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    classEntity.setId(rs.getLong(1));
                }
            }
            
            return classEntity;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm lớp học: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClassEntity> findAll() {
        String sql = "SELECT * FROM classes ORDER BY created_at DESC";
        List<ClassEntity> classes = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                classes.add(mapResultSetToClass(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách lớp học: " + e.getMessage(), e);
        }

        return classes;
    }

    private ClassEntity mapResultSetToClass(ResultSet rs) throws SQLException {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(rs.getLong("id"));
        classEntity.setClassCode(rs.getString("class_code"));
        classEntity.setClassName(rs.getString("class_name"));
        classEntity.setSubject(rs.getString("subject"));
        classEntity.setFeePerSession(rs.getBigDecimal("fee_per_session"));
        classEntity.setSchedule(rs.getString("schedule"));
        classEntity.setStatus(rs.getString("status"));
        classEntity.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            classEntity.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return classEntity;
    }
    
    // Implement các methods khác tương tự...
}
```

---

## 2. Service Implementation

### IncomeServiceImpl.java

```java
package org.example.service.impl;

import org.example.repository.AttendanceRepository;
import org.example.repository.impl.AttendanceRepositoryImpl;
import org.example.service.IncomeService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IncomeServiceImpl implements IncomeService {

    private final Connection connection;
    private final AttendanceRepository attendanceRepository;

    public IncomeServiceImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
        this.attendanceRepository = new AttendanceRepositoryImpl();
    }

    /**
     * Tính thu nhập của một học sinh trong tháng
     */
    public StudentMonthlyIncome calculateStudentMonthlyIncome(Long studentId, int year, int month) {
        String sql = """
            SELECT 
                s.id as student_id,
                s.student_code,
                s.full_name,
                c.id as class_id,
                c.class_name,
                c.subject,
                c.fee_per_session,
                COUNT(a.id) as total_sessions,
                (COUNT(a.id) * c.fee_per_session) as total_income
            FROM students s
            JOIN class_students cs ON s.id = cs.student_id
            JOIN classes c ON cs.class_id = c.id
            JOIN lesson_sessions ls ON c.id = ls.class_id
            JOIN attendances a ON ls.id = a.lesson_session_id AND a.student_id = s.id
            WHERE s.id = ?
              AND a.status = 'PRESENT'
              AND strftime('%Y', ls.lesson_date) = ?
              AND strftime('%m', ls.lesson_date) = ?
            GROUP BY s.id, c.id
            """;

        StudentMonthlyIncome income = new StudentMonthlyIncome();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            stmt.setString(2, String.valueOf(year));
            stmt.setString(3, String.format("%02d", month));
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                income.setStudentId(rs.getLong("student_id"));
                income.setStudentCode(rs.getString("student_code"));
                income.setStudentName(rs.getString("full_name"));
                income.setClassName(rs.getString("class_name"));
                income.setSubject(rs.getString("subject"));
                income.setFeePerSession(rs.getBigDecimal("fee_per_session"));
                income.setTotalSessions(rs.getInt("total_sessions"));
                income.setTotalIncome(rs.getBigDecimal("total_income"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tính thu nhập: " + e.getMessage(), e);
        }
        
        return income;
    }

    /**
     * Tính tổng thu nhập toàn bộ trong tháng
     */
    public MonthlyIncomeReport calculateTotalMonthlyIncome(int year, int month) {
        String sql = """
            SELECT 
                COUNT(DISTINCT ls.id) as total_lessons,
                COUNT(a.id) as total_attendances,
                SUM(c.fee_per_session) as total_income
            FROM lesson_sessions ls
            JOIN classes c ON ls.class_id = c.id
            JOIN attendances a ON ls.id = a.lesson_session_id
            WHERE a.status = 'PRESENT'
              AND strftime('%Y', ls.lesson_date) = ?
              AND strftime('%m', ls.lesson_date) = ?
            """;

        MonthlyIncomeReport report = new MonthlyIncomeReport();
        report.setYear(year);
        report.setMonth(month);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(year));
            stmt.setString(2, String.format("%02d", month));
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                report.setTotalLessons(rs.getInt("total_lessons"));
                report.setTotalAttendances(rs.getInt("total_attendances"));
                report.setTotalIncome(rs.getBigDecimal("total_income"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tính tổng thu nhập: " + e.getMessage(), e);
        }
        
        return report;
    }

    // Inner classes cho data models
    public static class StudentMonthlyIncome {
        private Long studentId;
        private String studentCode;
        private String studentName;
        private String className;
        private String subject;
        private BigDecimal feePerSession;
        private int totalSessions;
        private BigDecimal totalIncome;
        
        // Getters and Setters...
    }

    public static class MonthlyIncomeReport {
        private int year;
        private int month;
        private int totalLessons;
        private int totalAttendances;
        private BigDecimal totalIncome;
        
        // Getters and Setters...
    }
}
```

---

## 3. UI Implementation

### StudentManagementFrame.java (Mẫu đầy đủ)

```java
package org.example.view;

import org.example.entity.Student;
import org.example.service.StudentService;
import org.example.service.impl.StudentServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentManagementFrame extends JFrame {

    private final StudentService studentService;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JTextField txtStudentCode;
    private JTextField txtFullName;
    private JTextField txtDateOfBirth;
    private JComboBox<String> cmbGender;
    private JTextField txtPhone;
    private JTextArea txtAddress;
    private JTextField txtParentName;
    private JTextField txtParentPhone;
    private JComboBox<String> cmbStatus;
    private JTextArea txtNotes;

    public StudentManagementFrame() {
        this.studentService = new StudentServiceImpl();
        initializeUI();
        loadStudents();
    }

    private void initializeUI() {
        setTitle("Quản Lý Học Sinh");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Top panel - Search
        mainPanel.add(createSearchPanel(), BorderLayout.NORTH);

        // Center - Split between table and form
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(700);
        splitPane.setLeftComponent(createTablePanel());
        splitPane.setRightComponent(createFormPanel());
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom panel - Buttons
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        panel.add(new JLabel("Tìm theo tên hoặc mã:"));
        
        txtSearch = new JTextField(25);
        // Font lớn cho người lớn tuổi
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(txtSearch);

        JButton btnSearch = new JButton("🔍 Tìm kiếm");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.addActionListener(e -> searchStudents());
        panel.add(btnSearch);

        JButton btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadStudents());
        panel.add(btnRefresh);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách học sinh"));

        // Table
        String[] columns = {"ID", "Mã HS", "Họ tên", "Ngày sinh", "Giới tính", 
                           "SĐT", "SĐT Phụ huynh", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTable.setRowHeight(30);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Event khi chọn row
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin học sinh"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        // Mã học sinh
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblStudentCode = new JLabel("Mã học sinh:");
        lblStudentCode.setFont(labelFont);
        panel.add(lblStudentCode, gbc);
        
        gbc.gridx = 1;
        txtStudentCode = new JTextField(20);
        txtStudentCode.setFont(fieldFont);
        panel.add(txtStudentCode, gbc);

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblFullName = new JLabel("Họ tên:");
        lblFullName.setFont(labelFont);
        panel.add(lblFullName, gbc);
        
        gbc.gridx = 1;
        txtFullName = new JTextField(20);
        txtFullName.setFont(fieldFont);
        panel.add(txtFullName, gbc);

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblDateOfBirth = new JLabel("Ngày sinh (dd/MM/yyyy):");
        lblDateOfBirth.setFont(labelFont);
        panel.add(lblDateOfBirth, gbc);
        
        gbc.gridx = 1;
        txtDateOfBirth = new JTextField(20);
        txtDateOfBirth.setFont(fieldFont);
        panel.add(txtDateOfBirth, gbc);

        // Giới tính
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblGender = new JLabel("Giới tính:");
        lblGender.setFont(labelFont);
        panel.add(lblGender, gbc);
        
        gbc.gridx = 1;
        cmbGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cmbGender.setFont(fieldFont);
        panel.add(cmbGender, gbc);

        // Số điện thoại
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setFont(labelFont);
        panel.add(lblPhone, gbc);
        
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        txtPhone.setFont(fieldFont);
        panel.add(txtPhone, gbc);

        // Địa chỉ
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblAddress = new JLabel("Địa chỉ:");
        lblAddress.setFont(labelFont);
        panel.add(lblAddress, gbc);
        
        gbc.gridx = 1;
        txtAddress = new JTextArea(3, 20);
        txtAddress.setFont(fieldFont);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtAddress), gbc);

        // Tên phụ huynh
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblParentName = new JLabel("Tên phụ huynh:");
        lblParentName.setFont(labelFont);
        panel.add(lblParentName, gbc);
        
        gbc.gridx = 1;
        txtParentName = new JTextField(20);
        txtParentName.setFont(fieldFont);
        panel.add(txtParentName, gbc);

        // SĐT phụ huynh
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel lblParentPhone = new JLabel("SĐT phụ huynh:");
        lblParentPhone.setFont(labelFont);
        panel.add(lblParentPhone, gbc);
        
        gbc.gridx = 1;
        txtParentPhone = new JTextField(20);
        txtParentPhone.setFont(fieldFont);
        panel.add(txtParentPhone, gbc);

        // Trạng thái
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel lblStatus = new JLabel("Trạng thái:");
        lblStatus.setFont(labelFont);
        panel.add(lblStatus, gbc);
        
        gbc.gridx = 1;
        cmbStatus = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        cmbStatus.setFont(fieldFont);
        panel.add(cmbStatus, gbc);

        // Ghi chú
        gbc.gridx = 0; gbc.gridy = 9;
        JLabel lblNotes = new JLabel("Ghi chú:");
        lblNotes.setFont(labelFont);
        panel.add(lblNotes, gbc);
        
        gbc.gridx = 1;
        txtNotes = new JTextArea(3, 20);
        txtNotes.setFont(fieldFont);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtNotes), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        JButton btnAdd = new JButton("➕ Thêm mới");
        btnAdd.setFont(buttonFont);
        btnAdd.setPreferredSize(new Dimension(150, 40));
        btnAdd.addActionListener(e -> addStudent());
        panel.add(btnAdd);

        JButton btnUpdate = new JButton("✏️ Cập nhật");
        btnUpdate.setFont(buttonFont);
        btnUpdate.setPreferredSize(new Dimension(150, 40));
        btnUpdate.addActionListener(e -> updateStudent());
        panel.add(btnUpdate);

        JButton btnDelete = new JButton("🗑️ Xóa");
        btnDelete.setFont(buttonFont);
        btnDelete.setPreferredSize(new Dimension(150, 40));
        btnDelete.addActionListener(e -> deleteStudent());
        panel.add(btnDelete);

        JButton btnClear = new JButton("🔄 Làm mới form");
        btnClear.setFont(buttonFont);
        btnClear.setPreferredSize(new Dimension(170, 40));
        btnClear.addActionListener(e -> clearForm());
        panel.add(btnClear);

        return panel;
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentService.getAllStudents();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Student student : students) {
            Object[] row = {
                student.getId(),
                student.getStudentCode(),
                student.getFullName(),
                student.getDateOfBirth() != null ? student.getDateOfBirth().format(formatter) : "",
                student.getGender(),
                student.getPhone(),
                student.getParentPhone(),
                student.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void searchStudents() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadStudents();
            return;
        }

        tableModel.setRowCount(0);
        List<Student> students = studentService.searchStudents(keyword);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Student student : students) {
            Object[] row = {
                student.getId(),
                student.getStudentCode(),
                student.getFullName(),
                student.getDateOfBirth() != null ? student.getDateOfBirth().format(formatter) : "",
                student.getGender(),
                student.getPhone(),
                student.getParentPhone(),
                student.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            studentService.getStudentById(id).ifPresent(student -> {
                txtStudentCode.setText(student.getStudentCode());
                txtFullName.setText(student.getFullName());
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                txtDateOfBirth.setText(student.getDateOfBirth() != null ? 
                                      student.getDateOfBirth().format(formatter) : "");
                
                cmbGender.setSelectedItem(student.getGender());
                txtPhone.setText(student.getPhone());
                txtAddress.setText(student.getAddress());
                txtParentName.setText(student.getParentName());
                txtParentPhone.setText(student.getParentPhone());
                cmbStatus.setSelectedItem(student.getStatus());
                txtNotes.setText(student.getNotes());
            });
        }
    }

    private void addStudent() {
        try {
            Student student = createStudentFromForm();
            studentService.createStudent(student);
            
            JOptionPane.showMessageDialog(this, 
                "Thêm học sinh thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            loadStudents();
            clearForm();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn học sinh cần cập nhật!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            Student student = createStudentFromForm();
            student.setId(id);
            
            studentService.updateStudent(student);
            
            JOptionPane.showMessageDialog(this, 
                "Cập nhật thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            loadStudents();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn học sinh cần xóa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa học sinh này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                studentService.deleteStudent(id);
                
                JOptionPane.showMessageDialog(this, 
                    "Xóa thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadStudents();
                clearForm();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Student createStudentFromForm() {
        Student student = new Student();
        student.setStudentCode(txtStudentCode.getText().trim());
        student.setFullName(txtFullName.getText().trim());
        
        // Parse date
        String dobStr = txtDateOfBirth.getText().trim();
        if (!dobStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            student.setDateOfBirth(LocalDate.parse(dobStr, formatter));
        }
        
        student.setGender((String) cmbGender.getSelectedItem());
        student.setPhone(txtPhone.getText().trim());
        student.setAddress(txtAddress.getText().trim());
        student.setParentName(txtParentName.getText().trim());
        student.setParentPhone(txtParentPhone.getText().trim());
        student.setStatus((String) cmbStatus.getSelectedItem());
        student.setNotes(txtNotes.getText().trim());
        
        return student;
    }

    private void clearForm() {
        txtStudentCode.setText("");
        txtFullName.setText("");
        txtDateOfBirth.setText("");
        cmbGender.setSelectedIndex(0);
        txtPhone.setText("");
        txtAddress.setText("");
        txtParentName.setText("");
        txtParentPhone.setText("");
        cmbStatus.setSelectedIndex(0);
        txtNotes.setText("");
        studentTable.clearSelection();
    }
}
```

---

## 4. Income Calculation

### Query tính thu nhập chi tiết

```sql
-- Tính thu nhập của 1 học sinh trong 1 lớp cụ thể trong tháng
SELECT 
    s.id as student_id,
    s.student_code,
    s.full_name,
    c.id as class_id,
    c.class_name,
    c.fee_per_session,
    COUNT(a.id) as sessions_attended,
    (COUNT(a.id) * c.fee_per_session) as total_income
FROM students s
JOIN class_students cs ON s.id = cs.student_id
JOIN classes c ON cs.class_id = c.id
JOIN lesson_sessions ls ON c.id = ls.class_id
JOIN attendances a ON ls.id = a.lesson_session_id AND a.student_id = s.id
WHERE s.id = ?
  AND c.id = ?
  AND a.status = 'PRESENT'
  AND strftime('%Y-%m', ls.lesson_date) = '2024-12'
GROUP BY s.id, c.id;
```

---

## 5. Export Features

### Export Excel với Apache POI

Đã implement trong `ExcelExporter.java`. Để sử dụng:

```java
// Export danh sách học sinh
List<Student> students = studentService.getAllStudents();
ExcelExporter.exportStudentsToExcel(students, "C:/Users/YourName/Desktop/students.xlsx");

// Export báo cáo thu nhập
List<ExcelExporter.IncomeReport> reports = getIncomeReports();
ExcelExporter.exportIncomeReportToExcel(reports, "report.xlsx", 12, 2024);
```

### Backup Database

```java
import org.example.util.BackupUtil;

// Mở dialog backup
BackupUtil.showBackupDialog();

// Hoặc backup trực tiếp
String targetDir = "C:/Backup";
boolean success = BackupUtil.backupDatabase(targetDir);
```

---

## 🎯 Checklist Implementation

### Phase 1: Core Foundation
- [x] Setup DatabaseConfig với SQLite
- [x] Tạo tất cả Entity classes
- [x] Implement StudentRepository đầy đủ
- [ ] Implement ClassRepository đầy đủ
- [ ] Implement LessonSessionRepository đầy đủ
- [ ] Implement AttendanceRepository đầy đủ

### Phase 2: Business Logic
- [x] StudentService với validation
- [ ] ClassService với business rules
- [ ] LessonService
- [ ] IncomeService (tính toán thu nhập)

### Phase 3: User Interface
- [x] StudentManagementFrame (CRUD đầy đủ)
- [ ] ClassManagementFrame
- [ ] LessonManagementFrame
- [ ] AttendanceFrame (điểm danh)
- [ ] IncomeReportFrame
- [ ] MainFrame (menu chính)

### Phase 4: Advanced Features
- [x] ExcelExporter (xuất Excel)
- [ ] PDFGenerator (xuất PDF)
- [x] BackupUtil (backup/restore)
- [ ] Email notification (optional)

### Phase 5: Testing & Deployment
- [ ] Test từng chức năng
- [ ] Fix bugs
- [ ] Build với Maven Shade
- [ ] Tạo installer với jpackage

---

## 💡 Tips & Best Practices

1. **Database Connection**: Luôn dùng Singleton pattern cho DatabaseConfig
2. **Exception Handling**: Wrap SQLException thành RuntimeException với message rõ ràng
3. **UI Font Size**: Dùng font size >= 14 cho người lớn tuổi
4. **Button Icons**: Dùng emoji để UI thân thiện hơn
5. **Validation**: Validate ở Service layer, không validate ở Repository
6. **Date Format**: Dùng dd/MM/yyyy cho người Việt Nam
7. **Backup**: Nhắc user backup định kỳ (1 tuần 1 lần)
8. **Performance**: Dùng PreparedStatement để tránh SQL Injection

---

Hoàn thành! File này cung cấp đầy đủ code mẫu và hướng dẫn implementation chi tiết.
