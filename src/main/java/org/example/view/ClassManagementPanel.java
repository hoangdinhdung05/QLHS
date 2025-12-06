package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ClassManagementPanel extends JPanel {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    
    private final String[] columnNames = {
        "Mã Lớp", "Tên Lớp", "Môn Học", "Học Phí/Buổi", 
        "Lịch Học", "Sĩ Số", "Trạng Thái"
    };
    
    public ClassManagementPanel() {
        setLayout(new BorderLayout(0, 15));
        setOpaque(false);
        
        initComponents();
        loadMockData();
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
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Left: Search
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton searchBtn = createStyledButton("🔍 Tìm Kiếm", new Color(100, 149, 237));
        
        leftPanel.add(new JLabel("Tìm kiếm lớp:"));
        leftPanel.add(searchField);
        leftPanel.add(searchBtn);
        
        // Right: Actions
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        JButton addBtn = createStyledButton("➕ Thêm Lớp", new Color(40, 167, 69));
        JButton editBtn = createStyledButton("✏️ Sửa", new Color(255, 193, 7));
        JButton viewBtn = createStyledButton("👁️ Xem Chi Tiết", new Color(23, 162, 184));
        JButton refreshBtn = createStyledButton("🔄 Làm Mới", new Color(108, 117, 125));
        
        rightPanel.add(addBtn);
        rightPanel.add(editBtn);
        rightPanel.add(viewBtn);
        rightPanel.add(refreshBtn);
        
        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);
        
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
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(40, 167, 69));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        
        // Custom renderer for status
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
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
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        
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
    
    private void loadMockData() {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        // Mock data
        Object[][] mockClasses = {
            {"LOP001", "Toán Nâng Cao Lớp 9", "Toán Học", currencyFormat.format(150000), "T2, T4, T6 (18:00-19:30)", 25, "Đang mở"},
            {"LOP002", "Văn Học Lớp 10", "Ngữ Văn", currencyFormat.format(130000), "T3, T5, T7 (19:00-20:30)", 22, "Đang mở"},
            {"LOP003", "Tiếng Anh Giao Tiếp", "Tiếng Anh", currencyFormat.format(180000), "T2, T4, T6 (17:00-18:30)", 20, "Đang mở"},
            {"LOP004", "Vật Lý Lớp 12", "Vật Lý", currencyFormat.format(160000), "T3, T5 (18:30-20:00)", 18, "Đang mở"},
            {"LOP005", "Hóa Học Lớp 11", "Hóa Học", currencyFormat.format(155000), "T2, T4 (17:30-19:00)", 15, "Đang mở"},
            {"LOP006", "Sinh Học Lớp 12", "Sinh Học", currencyFormat.format(140000), "T3, T6 (18:00-19:30)", 20, "Đang mở"},
            {"LOP007", "Toán Ôn Thi THPT", "Toán Học", currencyFormat.format(200000), "T7, CN (08:00-10:00)", 30, "Đang mở"},
            {"LOP008", "Văn Ôn Thi THPT", "Ngữ Văn", currencyFormat.format(180000), "T7, CN (10:30-12:00)", 28, "Đang mở"},
            {"LOP009", "Tiếng Anh IELTS Foundation", "Tiếng Anh", currencyFormat.format(250000), "T2, T4, T6 (19:30-21:00)", 16, "Đang mở"},
            {"LOP010", "Lập Trình Python", "Tin Học", currencyFormat.format(220000), "T3, T5 (18:00-20:00)", 12, "Đang mở"}
        };
        
        for (Object[] row : mockClasses) {
            tableModel.addRow(row);
        }
        
        updateTotalLabel(mockClasses.length);
    }
    
    private void updateTotalLabel(int count) {
        Component[] components = ((JPanel) getComponent(2)).getComponents();
        if (components.length > 0 && components[0] instanceof JLabel) {
            ((JLabel) components[0]).setText("Tổng số lớp: " + count);
        }
    }
}
