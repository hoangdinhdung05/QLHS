package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AttendanceManagementPanel extends JPanel {
    
    private JTable sessionTable;
    private JTable attendanceTable;
    private DefaultTableModel sessionTableModel;
    private DefaultTableModel attendanceTableModel;
    private JComboBox<String> classFilter;
    private JComboBox<String> monthFilter;
    private JComboBox<String> yearFilter;
    
    private final String[] sessionColumns = {
        "Mã Buổi", "Lớp Học", "Ngày Học", "Giờ Học", "Chủ Đề", "Trạng Thái"
    };
    
    private final String[] attendanceColumns = {
        "Mã HS", "Họ và Tên", "Có Mặt", "Vắng", "Muộn", "Ghi Chú"
    };
    
    public AttendanceManagementPanel() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        
        initComponents();
        loadMockData();
    }
    
    private void initComponents() {
        // Main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerSize(10);
        splitPane.setOpaque(false);
        
        // Top: Lesson sessions
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);
        topPanel.add(createSessionToolbar(), BorderLayout.NORTH);
        topPanel.add(createSessionTablePanel(), BorderLayout.CENTER);
        
        // Bottom: Attendance details
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(createAttendanceToolbar(), BorderLayout.NORTH);
        bottomPanel.add(createAttendanceTablePanel(), BorderLayout.CENTER);
        
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createSessionToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Title row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        titleRow.setOpaque(false);
        
        JLabel titleLabel = new JLabel("📅 LỊCH HỌC VÀ ĐIỂM DANH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(72, 69, 130));
        titleRow.add(titleLabel);
        
        // Filter row
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filterRow.setOpaque(false);
        
        classFilter = new JComboBox<>(new String[]{
            "Tất cả lớp", "Toán Lớp 9", "Văn Lớp 10", 
            "Anh Giao Tiếp", "Lý Lớp 12"
        });
        classFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classFilter.setPreferredSize(new Dimension(150, 28));
        
        monthFilter = new JComboBox<>(new String[]{
            "Tháng 12", "Tháng 11", "Tháng 10", "Tháng 9"
        });
        monthFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        monthFilter.setPreferredSize(new Dimension(100, 28));
        
        yearFilter = new JComboBox<>(new String[]{"2025", "2024"});
        yearFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearFilter.setPreferredSize(new Dimension(80, 28));
        
        filterRow.add(new JLabel("Lớp:"));
        filterRow.add(classFilter);
        filterRow.add(new JLabel("Tháng:"));
        filterRow.add(monthFilter);
        filterRow.add(new JLabel("Năm:"));
        filterRow.add(yearFilter);
        
        // Action row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        actionRow.setOpaque(false);
        
        JButton addSessionBtn = createStyledButton("➕ Thêm", new Color(40, 167, 69));
        JButton editSessionBtn = createStyledButton("✏️ Sửa", new Color(255, 193, 7));
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(108, 117, 125));
        
        actionRow.add(addSessionBtn);
        actionRow.add(editSessionBtn);
        actionRow.add(refreshBtn);
        
        toolbar.add(titleRow);
        toolbar.add(filterRow);
        toolbar.add(actionRow);
        
        return toolbar;
    }
    
    private JPanel createSessionTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        sessionTableModel = new DefaultTableModel(sessionColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        sessionTable = new JTable(sessionTableModel);
        sessionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sessionTable.setRowHeight(35);
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sessionTable.setShowGrid(true);
        sessionTable.setGridColor(new Color(240, 240, 245));
        
        // Header style
        JTableHeader header = sessionTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(138, 43, 226));
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
                setBackground(new Color(138, 43, 226));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                setOpaque(true);
                return this;
            }
        };
        
        for (int i = 0; i < sessionTable.getColumnCount(); i++) {
            sessionTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        sessionTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        sessionTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        sessionTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        sessionTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        
        // Status renderer
        sessionTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                
                if (!isSelected) {
                    if ("Đã điểm danh".equals(value)) {
                        c.setBackground(new Color(212, 237, 218));
                        c.setForeground(new Color(21, 87, 36));
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else {
                        c.setBackground(new Color(255, 243, 205));
                        c.setForeground(new Color(133, 100, 4));
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }
                }
                return c;
            }
        });
        
        // Column widths
        sessionTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        sessionTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        sessionTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        sessionTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        sessionTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        sessionTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        
        // Selection listener
        sessionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadAttendanceForSelectedSession();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(sessionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAttendanceToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Title row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        titleRow.setOpaque(false);
        
        JLabel titleLabel = new JLabel("👥 DANH SÁCH ĐIỂM DANH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(72, 69, 130));
        titleRow.add(titleLabel);
        
        // Action row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        actionRow.setOpaque(false);
        
        JButton markAllPresentBtn = createStyledButton("✓ Có Mặt", new Color(40, 167, 69));
        JButton saveBtn = createStyledButton("💾 Lưu", new Color(0, 123, 255));
        JButton exportBtn = createStyledButton("📊 Excel", new Color(23, 162, 184));
        
        actionRow.add(markAllPresentBtn);
        actionRow.add(saveBtn);
        actionRow.add(exportBtn);
        
        toolbar.add(titleRow);
        toolbar.add(actionRow);
        
        return toolbar;
    }
    
    private JPanel createAttendanceTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        attendanceTableModel = new DefaultTableModel(attendanceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 2 && column <= 4; // Cho phép edit checkbox
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column >= 2 && column <= 4) {
                    return Boolean.class;
                }
                return String.class;
            }
        };
        
        attendanceTable = new JTable(attendanceTableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attendanceTable.setRowHeight(35);
        attendanceTable.setShowGrid(true);
        attendanceTable.setGridColor(new Color(240, 240, 245));
        
        // Header style
        JTableHeader header = attendanceTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(138, 43, 226));
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
                setBackground(new Color(138, 43, 226));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                setOpaque(true);
                return this;
            }
        };
        
        for (int i = 0; i < attendanceTable.getColumnCount(); i++) {
            attendanceTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        attendanceTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        // Column widths
        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        attendanceTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(5).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom info
        JPanel bottomInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomInfo.setBackground(Color.WHITE);
        
        JLabel infoLabel = new JLabel("Chọn một buổi học ở trên để xem danh sách điểm danh");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(120, 120, 140));
        bottomInfo.add(infoLabel);
        
        panel.add(bottomInfo, BorderLayout.SOUTH);
        
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
    
    private void loadMockData() {
        sessionTableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        
        Object[][] mockSessions = {
            {"BH001", "Toán Nâng Cao Lớp 9", today.minusDays(5).format(formatter), "18:00-19:30", "Bài toán về phương trình bậc hai", "Đã điểm danh"},
            {"BH002", "Toán Nâng Cao Lớp 9", today.minusDays(3).format(formatter), "18:00-19:30", "Luyện tập phương trình", "Đã điểm danh"},
            {"BH003", "Văn Học Lớp 10", today.minusDays(2).format(formatter), "19:00-20:30", "Phân tích văn bản: Vợ Nhặt", "Đã điểm danh"},
            {"BH004", "Tiếng Anh Giao Tiếp", today.minusDays(1).format(formatter), "17:00-18:30", "Unit 5: Daily routines", "Đã điểm danh"},
            {"BH005", "Toán Nâng Cao Lớp 9", today.format(formatter), "18:00-19:30", "Hệ phương trình", "Chưa điểm danh"},
            {"BH006", "Vật Lý Lớp 12", today.plusDays(1).format(formatter), "18:30-20:00", "Dao động điều hòa", "Chưa điểm danh"},
            {"BH007", "Văn Học Lớp 10", today.plusDays(2).format(formatter), "19:00-20:30", "Luyện viết văn nghị luận", "Chưa điểm danh"}
        };
        
        for (Object[] row : mockSessions) {
            sessionTableModel.addRow(row);
        }
    }
    
    private void loadAttendanceForSelectedSession() {
        int selectedRow = sessionTable.getSelectedRow();
        if (selectedRow == -1) {
            attendanceTableModel.setRowCount(0);
            return;
        }
        
        String sessionId = (String) sessionTableModel.getValueAt(selectedRow, 0);
        String sessionStatus = (String) sessionTableModel.getValueAt(selectedRow, 5);
        
        attendanceTableModel.setRowCount(0);
        
        // Mock attendance data
        if ("Đã điểm danh".equals(sessionStatus)) {
            Object[][] mockAttendance = {
                {"HS001", "Nguyễn Văn An", true, false, false, ""},
                {"HS002", "Trần Thị Bình", true, false, false, ""},
                {"HS003", "Lê Văn Cường", false, true, false, "Không phép"},
                {"HS004", "Phạm Thị Dung", true, false, false, ""},
                {"HS005", "Hoàng Văn Em", false, false, true, "Đến muộn 15 phút"}
            };
            
            for (Object[] row : mockAttendance) {
                attendanceTableModel.addRow(row);
            }
        } else {
            // Chưa điểm danh - load danh sách học sinh với checkbox trống
            Object[][] mockStudents = {
                {"HS001", "Nguyễn Văn An", false, false, false, ""},
                {"HS002", "Trần Thị Bình", false, false, false, ""},
                {"HS003", "Lê Văn Cường", false, false, false, ""},
                {"HS004", "Phạm Thị Dung", false, false, false, ""},
                {"HS005", "Hoàng Văn Em", false, false, false, ""}
            };
            
            for (Object[] row : mockStudents) {
                attendanceTableModel.addRow(row);
            }
        }
    }
}
