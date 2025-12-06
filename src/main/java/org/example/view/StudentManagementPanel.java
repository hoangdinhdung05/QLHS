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
        "Mã HS", "Họ và Tên", "Ngày Sinh", "Giới Tính", 
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
        
        bottomRow.add(addBtn);
        bottomRow.add(editBtn);
        bottomRow.add(deleteBtn);
        bottomRow.add(refreshBtn);
        
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
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã HS
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Ngày sinh
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Giới tính
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Trạng thái
        
        // Custom renderer for status
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã HS
        table.getColumnModel().getColumn(1).setPreferredWidth(180); // Họ tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Ngày sinh
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Giới tính
        table.getColumnModel().getColumn(4).setPreferredWidth(110); // SĐT
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // SĐT PH
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Trạng thái
        
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
        JOptionPane.showMessageDialog(this,
            "Chức năng thêm học sinh đang được phát triển...",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showEditStudentDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn học sinh cần sửa!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentCode = (String) tableModel.getValueAt(selectedRow, 0);
        JOptionPane.showMessageDialog(this,
            "Chức năng sửa thông tin học sinh " + studentCode + " đang được phát triển...",
            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn học sinh cần xóa!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentCode = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa học sinh:\n" + studentCode + " - " + studentName + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Chức năng xóa đang được phát triển...",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateTotalLabel(int count) {
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel) components[0]).setText("Tổng số học sinh: " + count);
        }
    }
}
