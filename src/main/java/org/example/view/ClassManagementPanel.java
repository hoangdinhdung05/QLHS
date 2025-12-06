package org.example.view;

import org.example.entity.ClassEntity;
import org.example.service.ClassService;
import org.example.service.impl.ClassServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ClassManagementPanel extends JPanel {
    
    private final ClassService classService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    
    private final String[] columnNames = {
        "STT", "Mã Lớp", "Tên Lớp", "Môn Học", "Địa Chỉ", "Lịch Học", 
        "Học Phí/Buổi", "Sĩ Số", "Trạng Thái"
    };
    
    public ClassManagementPanel() {
        this.classService = new ClassServiceImpl();
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        
        initComponents();
        loadClasses();
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
        searchBtn.addActionListener(e -> searchClasses());
        
        statusFilter = new JComboBox<>(new String[]{"Tất cả", "Đang mở", "Đã đủ"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilter.addActionListener(e -> filterByStatus());
        
        topRow.add(new JLabel("Tìm lớp:"));
        topRow.add(searchField);
        topRow.add(searchBtn);
        topRow.add(Box.createHorizontalStrut(10));
        topRow.add(new JLabel("Trạng thái:"));
        topRow.add(statusFilter);
        
        // Bottom row: Actions
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        bottomRow.setOpaque(false);
        
        JButton addBtn = createStyledButton("➕ Thêm", new Color(40, 167, 69));
        addBtn.addActionListener(e -> showAddClassDialog());
        
        JButton editBtn = createStyledButton("✏️ Sửa", new Color(255, 193, 7));
        editBtn.addActionListener(e -> showEditClassDialog());
        
        JButton deleteBtn = createStyledButton("🗑️ Xóa", new Color(220, 53, 69));
        deleteBtn.addActionListener(e -> deleteClass());
        
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(108, 117, 125));
        refreshBtn.addActionListener(e -> loadClasses());
        
        JButton exportExcelBtn = createStyledButton("📊 Xuất Excel", new Color(23, 162, 184));
        exportExcelBtn.addActionListener(e -> exportToExcel());
        
        bottomRow.add(addBtn);
        bottomRow.add(editBtn);
        bottomRow.add(deleteBtn);
        bottomRow.add(refreshBtn);
        bottomRow.add(exportExcelBtn);
        
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
        header.setBackground(new Color(40, 167, 69));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        header.setOpaque(true);
        
        // Custom header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(new Color(40, 167, 69));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                setOpaque(true);
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Mã lớp
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Học phí
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Sĩ số
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer); // Trạng thái
        
        // Custom renderer for status
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                
                if (!isSelected) {
                    if ("Đang mở".equals(value)) {
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
        table.getColumnModel().getColumn(1).setPreferredWidth(90);  // Mã lớp
        table.getColumnModel().getColumn(2).setPreferredWidth(180); // Tên lớp
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Môn học
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Địa chỉ
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Lịch học
        table.getColumnModel().getColumn(6).setPreferredWidth(110); // Học phí
        table.getColumnModel().getColumn(7).setPreferredWidth(70);  // Sĩ số
        table.getColumnModel().getColumn(8).setPreferredWidth(90);  // Trạng thái
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        
        JLabel totalLabel = new JLabel("Tổng số lớp: 0");
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
    
    private void loadClasses() {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        try {
            List<ClassEntity> classes = classService.getAllClasses();
            
            int stt = 1;
            for (ClassEntity classEntity : classes) {
                String siSo = classEntity.getCurrentStudents() + "/" + classEntity.getMaxStudents();
                String status = "ACTIVE".equals(classEntity.getStatus()) ? "Đang mở" : 
                               "FULL".equals(classEntity.getStatus()) ? "Đã đủ" : "Đóng";
                
                Object[] row = {
                    stt++,
                    classEntity.getClassCode(),
                    classEntity.getClassName(),
                    classEntity.getSubject(),
                    classEntity.getLocation() != null ? classEntity.getLocation() : "",
                    classEntity.getSchedule() != null ? classEntity.getSchedule() : "",
                    currencyFormat.format(classEntity.getFeePerSession()),
                    siSo,
                    status
                };
                tableModel.addRow(row);
            }
            
            updateTotalLabel(classes.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách lớp: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchClasses() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadClasses();
            return;
        }
        
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        try {
            List<ClassEntity> classes = classService.searchClasses(keyword);
            
            int stt = 1;
            for (ClassEntity classEntity : classes) {
                String siSo = classEntity.getCurrentStudents() + "/" + classEntity.getMaxStudents();
                String status = "ACTIVE".equals(classEntity.getStatus()) ? "Đang mở" : 
                               "FULL".equals(classEntity.getStatus()) ? "Đã đủ" : "Đóng";
                
                Object[] row = {
                    stt++,
                    classEntity.getClassCode(),
                    classEntity.getClassName(),
                    classEntity.getSubject(),
                    classEntity.getLocation() != null ? classEntity.getLocation() : "",
                    classEntity.getSchedule() != null ? classEntity.getSchedule() : "",
                    currencyFormat.format(classEntity.getFeePerSession()),
                    siSo,
                    status
                };
                tableModel.addRow(row);
            }
            
            updateTotalLabel(classes.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filterByStatus() {
        String selected = (String) statusFilter.getSelectedItem();
        if ("Tất cả".equals(selected)) {
            loadClasses();
            return;
        }
        
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        try {
            String status = "Đang mở".equals(selected) ? "ACTIVE" : "FULL";
            List<ClassEntity> classes = classService.findByStatus(status);
            
            int stt = 1;
            for (ClassEntity classEntity : classes) {
                String siSo = classEntity.getCurrentStudents() + "/" + classEntity.getMaxStudents();
                String statusText = "ACTIVE".equals(classEntity.getStatus()) ? "Đang mở" : "Đã đủ";
                
                Object[] row = {
                    stt++,
                    classEntity.getClassCode(),
                    classEntity.getClassName(),
                    classEntity.getSubject(),
                    classEntity.getLocation() != null ? classEntity.getLocation() : "",
                    classEntity.getSchedule() != null ? classEntity.getSchedule() : "",
                    currencyFormat.format(classEntity.getFeePerSession()),
                    siSo,
                    statusText
                };
                tableModel.addRow(row);
            }
            
            updateTotalLabel(classes.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lọc: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAddClassDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm lớp học mới", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 600);
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
        JTextField subjectField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField scheduleField = new JTextField(20);
        JTextField feeField = new JTextField(20);
        JTextField maxStudentsField = new JTextField(20);
        JComboBox<String> classTypeBox = new JComboBox<>(new String[]{"GROUP", "SMALL_GROUP", "INDIVIDUAL"});
        
        // Add components
        addFormField(formPanel, gbc, 0, "Mã lớp: *", codeField);
        addFormField(formPanel, gbc, 1, "Tên lớp: *", nameField);
        addFormField(formPanel, gbc, 2, "Môn học: *", subjectField);
        addFormField(formPanel, gbc, 3, "Địa chỉ:", locationField);
        addFormField(formPanel, gbc, 4, "Lịch học:", scheduleField);
        addFormField(formPanel, gbc, 5, "Học phí/buổi: *", feeField);
        addFormField(formPanel, gbc, 6, "Sĩ số tối đa:", maxStudentsField);
        addFormField(formPanel, gbc, 7, "Loại lớp:", classTypeBox);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton saveBtn = createStyledButton("💾 Lưu", new Color(40, 167, 69));
        JButton cancelBtn = createStyledButton("❌ Hủy", new Color(108, 117, 125));
        
        saveBtn.addActionListener(e -> {
            try {
                // Validation
                if (codeField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mã lớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên lớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (feeField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập học phí!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create class
                ClassEntity classEntity = new ClassEntity();
                classEntity.setClassCode(codeField.getText().trim());
                classEntity.setClassName(nameField.getText().trim());
                classEntity.setSubject(subjectField.getText().trim());
                classEntity.setLocation(locationField.getText().trim());
                classEntity.setSchedule(scheduleField.getText().trim());
                classEntity.setFeePerSession(new BigDecimal(feeField.getText().trim()));
                classEntity.setMaxStudents(maxStudentsField.getText().trim().isEmpty() ? 30 : 
                    Integer.parseInt(maxStudentsField.getText().trim()));
                classEntity.setCurrentStudents(0);
                classEntity.setClassType((String) classTypeBox.getSelectedItem());
                classEntity.setStatus("ACTIVE");
                
                // Save
                classService.createClass(classEntity);
                
                JOptionPane.showMessageDialog(dialog, "Thêm lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadClasses();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ cho học phí và sĩ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
    
    private void showEditClassDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn lớp cần sửa!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String classCode = (String) tableModel.getValueAt(selectedRow, 1);
        
        try {
            ClassEntity classEntity = classService.findByClassCode(classCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học!"));
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin lớp học", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(600, 650);
            dialog.setLocationRelativeTo(this);
            
            // Form panel
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Fields with existing data
            JTextField codeField = new JTextField(20);
            codeField.setText(classEntity.getClassCode());
            codeField.setEditable(false);
            
            JTextField nameField = new JTextField(20);
            nameField.setText(classEntity.getClassName());
            
            JTextField subjectField = new JTextField(20);
            subjectField.setText(classEntity.getSubject());
            
            JTextField locationField = new JTextField(20);
            locationField.setText(classEntity.getLocation() != null ? classEntity.getLocation() : "");
            
            JTextField scheduleField = new JTextField(20);
            scheduleField.setText(classEntity.getSchedule() != null ? classEntity.getSchedule() : "");
            
            JTextField feeField = new JTextField(20);
            feeField.setText(classEntity.getFeePerSession().toString());
            
            JTextField maxStudentsField = new JTextField(20);
            maxStudentsField.setText(classEntity.getMaxStudents().toString());
            
            JTextField currentStudentsField = new JTextField(20);
            currentStudentsField.setText(classEntity.getCurrentStudents().toString());
            
            JComboBox<String> classTypeBox = new JComboBox<>(new String[]{"GROUP", "SMALL_GROUP", "INDIVIDUAL"});
            classTypeBox.setSelectedItem(classEntity.getClassType());
            
            JComboBox<String> statusBox = new JComboBox<>(new String[]{"ACTIVE", "FULL", "INACTIVE"});
            statusBox.setSelectedItem(classEntity.getStatus());
            
            // Add components
            addFormField(formPanel, gbc, 0, "Mã lớp: *", codeField);
            addFormField(formPanel, gbc, 1, "Tên lớp: *", nameField);
            addFormField(formPanel, gbc, 2, "Môn học: *", subjectField);
            addFormField(formPanel, gbc, 3, "Địa chỉ:", locationField);
            addFormField(formPanel, gbc, 4, "Lịch học:", scheduleField);
            addFormField(formPanel, gbc, 5, "Học phí/buổi: *", feeField);
            addFormField(formPanel, gbc, 6, "Sĩ số tối đa:", maxStudentsField);
            addFormField(formPanel, gbc, 7, "Sĩ số hiện tại:", currentStudentsField);
            addFormField(formPanel, gbc, 8, "Loại lớp:", classTypeBox);
            addFormField(formPanel, gbc, 9, "Trạng thái:", statusBox);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            
            // Button panel
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton saveBtn = createStyledButton("💾 Lưu", new Color(40, 167, 69));
            JButton cancelBtn = createStyledButton("❌ Hủy", new Color(108, 117, 125));
            
            saveBtn.addActionListener(e -> {
                try {
                    // Update class
                    classEntity.setClassName(nameField.getText().trim());
                    classEntity.setSubject(subjectField.getText().trim());
                    classEntity.setLocation(locationField.getText().trim());
                    classEntity.setSchedule(scheduleField.getText().trim());
                    classEntity.setFeePerSession(new BigDecimal(feeField.getText().trim()));
                    classEntity.setMaxStudents(Integer.parseInt(maxStudentsField.getText().trim()));
                    classEntity.setCurrentStudents(Integer.parseInt(currentStudentsField.getText().trim()));
                    classEntity.setClassType((String) classTypeBox.getSelectedItem());
                    classEntity.setStatus((String) statusBox.getSelectedItem());
                    
                    // Save
                    classService.updateClass(classEntity);
                    
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadClasses();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
    
    private void deleteClass() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn lớp cần xóa!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String classCode = (String) tableModel.getValueAt(selectedRow, 1);
        String className = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa lớp:\n" + classCode + " - " + className + "?\n\nLưu ý: Thao tác này không thể hoàn tác!",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ClassEntity classEntity = classService.findByClassCode(classCode)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học!"));
                
                boolean deleted = classService.deleteClass(classEntity.getId());
                
                if (deleted) {
                    JOptionPane.showMessageDialog(this,
                        "Xóa lớp học thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadClasses();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Không thể xóa lớp học!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi xóa lớp học: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToExcel() {
        try {
            List<ClassEntity> classes = classService.getAllClasses();
            
            if (classes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Không có dữ liệu để xuất!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file Excel");
            fileChooser.setSelectedFile(new java.io.File("DanhSachLopHoc_" + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                
                // Export using utility (would need to create this method)
                JOptionPane.showMessageDialog(this,
                    "Chức năng xuất Excel đang được phát triển...",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi xuất file Excel: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
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
    
    private void updateTotalLabel(int count) {
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel) components[0]).setText("Tổng số lớp: " + count);
        }
    }
}
