package org.example.view;

import org.example.entity.Student;
import org.example.service.StudentService;
import org.example.service.impl.StudentServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentManagementPanel extends JPanel {
    
    private final StudentService studentService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    
    private final String[] columnNames = {
        "STT", "Mã HS", "Họ và Tên", "Ngày Sinh", "Giới Tính", 
        "SĐT", "SĐT Phụ Huynh", "Trạng Thái"
    };
    
    public StudentManagementPanel() {
        this.studentService = new StudentServiceImpl();
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        
        initComponents();
        loadStudents();
    }
    
    private void initComponents() {
        // ===== TOP TOOLBAR =====
        JPanel toolbar = createToolbar();
        add(toolbar, BorderLayout.NORTH);
        
        // ===== TABLE PANEL =====
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Top row: Search
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topRow.setOpaque(false);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        
        JButton searchBtn = createStyledButton("🔍 Tìm", new Color(100, 149, 237));
        searchBtn.addActionListener(e -> searchStudents());
        
        statusFilter = new JComboBox<>(new String[]{"Tất cả", "Đang học", "Đã nghỉ"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilter.addActionListener(e -> filterByStatus());
        
        topRow.add(new JLabel("Tìm:"));
        topRow.add(searchField);
        topRow.add(searchBtn);
        topRow.add(Box.createHorizontalStrut(10));
        topRow.add(new JLabel("Trạng thái:"));
        topRow.add(statusFilter);
        
        // Bottom row: Actions
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        bottomRow.setOpaque(false);
        
        JButton addBtn = createStyledButton("➕ Thêm", new Color(40, 167, 69));
        addBtn.addActionListener(e -> showAddStudentDialog());
        
        JButton editBtn = createStyledButton("✏️ Sửa", new Color(255, 193, 7));
        editBtn.addActionListener(e -> showEditStudentDialog());
        
        JButton deleteBtn = createStyledButton("🗑️ Xóa", new Color(220, 53, 69));
        deleteBtn.addActionListener(e -> deleteStudent());
        
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(108, 117, 125));
        refreshBtn.addActionListener(e -> loadStudents());
        
        JButton exportBtn = createStyledButton("📊 Xuất Excel", new Color(23, 162, 184));
        exportBtn.addActionListener(e -> exportToExcel());
        
        JButton exportPdfBtn = createStyledButton("📄 Xuất PDF", new Color(220, 53, 69));
        exportPdfBtn.addActionListener(e -> exportToPdf());
        
        bottomRow.add(addBtn);
        bottomRow.add(editBtn);
        bottomRow.add(deleteBtn);
        bottomRow.add(refreshBtn);
        bottomRow.add(exportBtn);
        bottomRow.add(exportPdfBtn);
        
        toolbar.add(topRow);
        toolbar.add(bottomRow);
        
        return toolbar;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 245));
        
        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(100, 149, 237));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        header.setOpaque(true);
        
        // Make header text more visible - custom renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(100, 149, 237));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                setOpaque(true);
                return this;
            }
        };
        
        // Apply renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Center alignment for some columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Mã HS
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Ngày sinh
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Giới tính
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Trạng thái
        
        // Custom renderer for status
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                
                if (!isSelected) {
                    if ("ACTIVE".equals(value) || "Đang học".equals(value)) {
                        c.setBackground(new Color(212, 237, 218));
                        c.setForeground(new Color(21, 87, 36));
                    } else {
                        c.setBackground(new Color(248, 215, 218));
                        c.setForeground(new Color(114, 28, 36));
                    }
                }
                return c;
            }
        });
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Mã HS
        table.getColumnModel().getColumn(2).setPreferredWidth(180); // Họ tên
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Ngày sinh
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Giới tính
        table.getColumnModel().getColumn(5).setPreferredWidth(110); // SĐT
        table.getColumnModel().getColumn(6).setPreferredWidth(120); // SĐT PH
        table.getColumnModel().getColumn(7).setPreferredWidth(100); // Trạng thái
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        
        JLabel totalLabel = new JLabel("Tổng số học sinh: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(new Color(70, 70, 90));
        panel.add(totalLabel);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void loadStudents() {
        tableModel.setRowCount(0);
        
        try {
            List<Student> students = studentService.getAllStudents();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            int stt = 1;
            for (Student student : students) {
                Object[] row = {
                    stt++,
                    student.getStudentCode(),
                    student.getFullName(),
                    student.getDateOfBirth() != null ? student.getDateOfBirth().format(formatter) : "",
                    student.getGender(),
                    student.getPhone() != null ? student.getPhone() : "",
                    student.getParentPhone(),
                    "ACTIVE".equals(student.getStatus()) ? "Đang học" : "Đã nghỉ"
                };
                tableModel.addRow(row);
            }
            
            updateTotalLabel(students.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách học sinh: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchStudents() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadStudents();
            return;
        }
        
        tableModel.setRowCount(0);
        try {
            List<Student> students = studentService.searchByMaSV(keyword);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            int stt = 1;
            for (Student student : students) {
                Object[] row = {
                    stt++,
                    student.getStudentCode(),
                    student.getFullName(),
                    student.getDateOfBirth() != null ? student.getDateOfBirth().format(formatter) : "",
                    student.getGender(),
                    student.getPhone() != null ? student.getPhone() : "",
                    student.getParentPhone(),
                    "ACTIVE".equals(student.getStatus()) ? "Đang học" : "Đã nghỉ"
                };
                tableModel.addRow(row);
            }
            
            updateTotalLabel(students.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filterByStatus() {
        String selected = (String) statusFilter.getSelectedItem();
        if ("Tất cả".equals(selected)) {
            loadStudents();
            return;
        }
        
        tableModel.setRowCount(0);
        try {
            List<Student> students;
            if ("Đang học".equals(selected)) {
                students = studentService.getAllStudents().stream()
                    .filter(s -> "ACTIVE".equals(s.getStatus()))
                    .toList();
            } else {
                students = studentService.getAllStudents().stream()
                    .filter(s -> !"ACTIVE".equals(s.getStatus()))
                    .toList();
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Student student : students) {
                Object[] row = {
                    student.getStudentCode(),
                    student.getFullName(),
                    student.getDateOfBirth() != null ? student.getDateOfBirth().format(formatter) : "",
                    student.getGender(),
                    student.getPhone() != null ? student.getPhone() : "",
                    student.getParentPhone(),
                    "ACTIVE".equals(student.getStatus()) ? "Đang học" : "Đã nghỉ"
                };
                tableModel.addRow(row);
            }
            
            updateTotalLabel(students.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lọc: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm học sinh mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Fields
        JTextField codeField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField dobField = new JTextField(20); // dd/MM/yyyy
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField parentNameField = new JTextField(20);
        JTextField parentPhoneField = new JTextField(20);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Đang học", "Đã nghỉ"});
        
        // Add components
        addFormField(formPanel, gbc, 0, "Mã học sinh: *", codeField);
        addFormField(formPanel, gbc, 1, "Họ và tên: *", nameField);
        addFormField(formPanel, gbc, 2, "Ngày sinh (dd/MM/yyyy):", dobField);
        addFormField(formPanel, gbc, 3, "Giới tính:", genderBox);
        addFormField(formPanel, gbc, 4, "Số điện thoại:", phoneField);
        addFormField(formPanel, gbc, 5, "Địa chỉ:", addressField);
        addFormField(formPanel, gbc, 6, "Tên phụ huynh:", parentNameField);
        addFormField(formPanel, gbc, 7, "SĐT phụ huynh: *", parentPhoneField);
        addFormField(formPanel, gbc, 8, "Trạng thái:", statusBox);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveBtn = createStyledButton("💾 Lưu", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("❌ Hủy", new Color(108, 117, 125));
        
        saveBtn.addActionListener(e -> {
            try {
                // Validation
                if (codeField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mã học sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (parentPhoneField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập SĐT phụ huynh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create student
                Student student = new Student();
                student.setStudentCode(codeField.getText().trim());
                student.setFullName(nameField.getText().trim());
                
                // Parse date if provided
                if (!dobField.getText().trim().isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    student.setDateOfBirth(LocalDate.parse(dobField.getText().trim(), formatter));
                }
                
                student.setGender((String) genderBox.getSelectedItem());
                student.setPhone(phoneField.getText().trim());
                student.setAddress(addressField.getText().trim());
                student.setParentName(parentNameField.getText().trim());
                student.setParentPhone(parentPhoneField.getText().trim());
                student.setStatus("Đang học".equals(statusBox.getSelectedItem()) ? "ACTIVE" : "INACTIVE");
                
                // Save
                studentService.createStudent(student);
                
                JOptionPane.showMessageDialog(dialog, "Thêm học sinh thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadStudents();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showEditStudentDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn học sinh cần sửa!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentCode = (String) tableModel.getValueAt(selectedRow, 1); // Column 1 is now student code
        
        try {
            // Find student
            Student student = studentService.findByStudentCode(studentCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh!"));
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin học sinh", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(500, 550);
            dialog.setLocationRelativeTo(this);
            
            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Fields with existing data
            JTextField codeField = new JTextField(20);
            codeField.setText(student.getStudentCode());
            codeField.setEditable(false); // Don't allow changing code
            
            JTextField nameField = new JTextField(20);
            nameField.setText(student.getFullName());
            
            JTextField dobField = new JTextField(20);
            if (student.getDateOfBirth() != null) {
                dobField.setText(student.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            
            JComboBox<String> genderBox = new JComboBox<>(new String[]{"Nam", "Nữ"});
            genderBox.setSelectedItem(student.getGender());
            
            JTextField phoneField = new JTextField(20);
            phoneField.setText(student.getPhone() != null ? student.getPhone() : "");
            
            JTextField addressField = new JTextField(20);
            addressField.setText(student.getAddress() != null ? student.getAddress() : "");
            
            JTextField parentNameField = new JTextField(20);
            parentNameField.setText(student.getParentName() != null ? student.getParentName() : "");
            
            JTextField parentPhoneField = new JTextField(20);
            parentPhoneField.setText(student.getParentPhone());
            
            JComboBox<String> statusBox = new JComboBox<>(new String[]{"Đang học", "Đã nghỉ"});
            statusBox.setSelectedItem("ACTIVE".equals(student.getStatus()) ? "Đang học" : "Đã nghỉ");
            
            // Add components
            addFormField(formPanel, gbc, 0, "Mã học sinh: *", codeField);
            addFormField(formPanel, gbc, 1, "Họ và tên: *", nameField);
            addFormField(formPanel, gbc, 2, "Ngày sinh (dd/MM/yyyy):", dobField);
            addFormField(formPanel, gbc, 3, "Giới tính:", genderBox);
            addFormField(formPanel, gbc, 4, "Số điện thoại:", phoneField);
            addFormField(formPanel, gbc, 5, "Địa chỉ:", addressField);
            addFormField(formPanel, gbc, 6, "Tên phụ huynh:", parentNameField);
            addFormField(formPanel, gbc, 7, "SĐT phụ huynh: *", parentPhoneField);
            addFormField(formPanel, gbc, 8, "Trạng thái:", statusBox);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
            // Button panel
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton saveBtn = createStyledButton("💾 Lưu", new Color(40, 167, 69));
            JButton cancelBtn = createStyledButton("❌ Hủy", new Color(108, 117, 125));
            
            saveBtn.addActionListener(e -> {
                try {
                    // Validation
                    if (nameField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (parentPhoneField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập SĐT phụ huynh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Update student
                    student.setFullName(nameField.getText().trim());
                    
                    // Parse date if provided
                    if (!dobField.getText().trim().isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        student.setDateOfBirth(LocalDate.parse(dobField.getText().trim(), formatter));
                    } else {
                        student.setDateOfBirth(null);
                    }
                    
                    student.setGender((String) genderBox.getSelectedItem());
                    student.setPhone(phoneField.getText().trim());
                    student.setAddress(addressField.getText().trim());
                    student.setParentName(parentNameField.getText().trim());
                    student.setParentPhone(parentPhoneField.getText().trim());
                    student.setStatus("Đang học".equals(statusBox.getSelectedItem()) ? "ACTIVE" : "INACTIVE");
                    
                    // Save
                    studentService.updateStudentNew(student);
                    
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadStudents();
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            cancelBtn.addActionListener(e -> dialog.dispose());
            
            btnPanel.add(saveBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn học sinh cần xóa!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentCode = (String) tableModel.getValueAt(selectedRow, 1); // Column 1 is now student code
        String studentName = (String) tableModel.getValueAt(selectedRow, 2); // Column 2 is now student name
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa học sinh:\n" + studentCode + " - " + studentName + "?\n\nLưu ý: Thao tác này không thể hoàn tác!",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Find student and delete
                Student student = studentService.findByStudentCode(studentCode)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy học sinh!"));
                
                boolean deleted = studentService.deleteStudentById(student.getId());
                
                if (deleted) {
                    JOptionPane.showMessageDialog(this,
                        "Xóa học sinh thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa học sinh!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi xóa học sinh: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateTotalLabel(int count) {
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel) components[0]).setText("Tổng số học sinh: " + count);
        }
    }
    
    /**
     * Helper method to add form fields to dialog
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(field, gbc);
    }
    
    /**
     * Export student list to Excel
     */
    private void exportToExcel() {
        try {
            List<Student> students = studentService.getAllStudents();
            
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu để xuất!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");
            fileChooser.setSelectedFile(new java.io.File("DanhSachHocSinh_" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                
                // Export using ExcelExporter utility
                org.example.util.ExcelExporter.exportStudentsToExcel(students, filePath);
                
                int choice = JOptionPane.showConfirmDialog(this,
                    "Xuất file thành công!\nBạn có muốn mở file?",
                    "Thành công", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new java.io.File(filePath));
                }
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xuất file Excel: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Export student list to PDF
     */
    private void exportToPdf() {
        try {
            List<Student> students = studentService.getAllStudents();
            
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu để xuất!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create file chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file PDF");
            fileChooser.setSelectedFile(new java.io.File("DanhSachHocSinh_" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }
                
                // Export using PdfExporter utility
                org.example.util.PdfExporter.exportStudentsToPdf(students, filePath);
                
                int choice = JOptionPane.showConfirmDialog(this,
                    "Xuất file PDF thành công!\nBạn có muốn mở file?",
                    "Thành công", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new java.io.File(filePath));
                }
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xuất file PDF: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
