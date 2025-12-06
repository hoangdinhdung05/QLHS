package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class PaymentManagementPanel extends JPanel {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JComboBox<String> monthFilter;
    private JComboBox<String> yearFilter;
    private JComboBox<String> classFilter;
    
    private final String[] columnNames = {
        "Mã Thanh Toán", "Mã HS", "Họ và Tên", "Lớp Học", 
        "Tháng/Năm", "Số Buổi", "Tổng Tiền", "Đã Trả", "Còn Lại", "Trạng Thái"
    };
    
    public PaymentManagementPanel() {
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
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.Y_AXIS));
        toolbar.setOpaque(false);
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Title row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        titleRow.setOpaque(false);
        
        JLabel titleLabel = new JLabel("💰 THANH TOÁN HỌC PHÍ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(72, 69, 130));
        titleRow.add(titleLabel);
        
        // Search row
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchRow.setOpaque(false);
        
        searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchField.setToolTipText("Tìm theo mã HS hoặc tên");
        
        JButton searchBtn = createStyledButton("🔍 Tìm", new Color(100, 149, 237));
        
        searchRow.add(new JLabel("Tìm:"));
        searchRow.add(searchField);
        searchRow.add(searchBtn);
        
        // Filter row
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filterRow.setOpaque(false);
        
        classFilter = new JComboBox<>(new String[]{
            "Tất cả lớp", "Toán Lớp 9", "Văn Lớp 10", 
            "Anh Giao Tiếp", "Lý Lớp 12"
        });
        classFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classFilter.setPreferredSize(new Dimension(140, 28));
        
        monthFilter = new JComboBox<>(new String[]{
            "Tất cả", "T12", "T11", "T10", "T9"
        });
        monthFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        monthFilter.setPreferredSize(new Dimension(80, 28));
        
        yearFilter = new JComboBox<>(new String[]{"2025", "2024"});
        yearFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearFilter.setPreferredSize(new Dimension(75, 28));
        
        statusFilter = new JComboBox<>(new String[]{
            "Tất cả", "Chưa trả", "Một phần", "Đã đủ"
        });
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilter.setPreferredSize(new Dimension(100, 28));
        
        filterRow.add(new JLabel("Lớp:"));
        filterRow.add(classFilter);
        filterRow.add(new JLabel("Tháng:"));
        filterRow.add(monthFilter);
        filterRow.add(new JLabel("Năm:"));
        filterRow.add(yearFilter);
        filterRow.add(new JLabel("TT:"));
        filterRow.add(statusFilter);
        
        // Action row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        actionRow.setOpaque(false);
        
        JButton recordPaymentBtn = createStyledButton("➕ Ghi TT", new Color(40, 167, 69));
        JButton viewDetailBtn = createStyledButton("👁️ Chi Tiết", new Color(0, 123, 255));
        JButton exportBtn = createStyledButton("📊 Excel", new Color(23, 162, 184));
        JButton refreshBtn = createStyledButton("🔄 Refresh", new Color(108, 117, 125));
        
        actionRow.add(recordPaymentBtn);
        actionRow.add(viewDetailBtn);
        actionRow.add(exportBtn);
        actionRow.add(refreshBtn);
        
        toolbar.add(titleRow);
        toolbar.add(searchRow);
        toolbar.add(filterRow);
        toolbar.add(actionRow);
        
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
        header.setBackground(new Color(255, 140, 0));
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
                setBackground(new Color(255, 140, 0));
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
        
        // Center alignment for specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
        
        // Right align for money columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        
        // Custom renderer for status
        table.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                
                if (!isSelected) {
                    if ("Đã trả đủ".equals(value)) {
                        c.setBackground(new Color(212, 237, 218));
                        c.setForeground(new Color(21, 87, 36));
                    } else if ("Trả một phần".equals(value)) {
                        c.setBackground(new Color(255, 243, 205));
                        c.setForeground(new Color(133, 100, 4));
                    } else {
                        c.setBackground(new Color(248, 215, 218));
                        c.setForeground(new Color(114, 28, 36));
                    }
                }
                return c;
            }
        });
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(120);
        table.getColumnModel().getColumn(8).setPreferredWidth(120);
        table.getColumnModel().getColumn(9).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Left: Total count
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        
        JLabel totalLabel = new JLabel("Tổng số bản ghi: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(new Color(70, 70, 90));
        leftPanel.add(totalLabel);
        
        // Right: Summary statistics
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setOpaque(false);
        
        JLabel totalAmountLabel = new JLabel("Tổng phải thu: 0 ₫");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalAmountLabel.setForeground(new Color(220, 53, 69));
        
        JLabel paidLabel = new JLabel("Đã thu: 0 ₫");
        paidLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        paidLabel.setForeground(new Color(40, 167, 69));
        
        JLabel remainingLabel = new JLabel("Còn lại: 0 ₫");
        remainingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        remainingLabel.setForeground(new Color(255, 193, 7));
        
        rightPanel.add(totalAmountLabel);
        rightPanel.add(new JLabel(" | "));
        rightPanel.add(paidLabel);
        rightPanel.add(new JLabel(" | "));
        rightPanel.add(remainingLabel);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
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
        Object[][] mockPayments = {
            {"TT001", "HS001", "Nguyễn Văn An", "Toán Nâng Cao Lớp 9", "12/2025", 12, 
             currencyFormat.format(1800000), currencyFormat.format(1800000), currencyFormat.format(0), "Đã trả đủ"},
            
            {"TT002", "HS002", "Trần Thị Bình", "Văn Học Lớp 10", "12/2025", 12, 
             currencyFormat.format(1560000), currencyFormat.format(1000000), currencyFormat.format(560000), "Trả một phần"},
            
            {"TT003", "HS003", "Lê Văn Cường", "Tiếng Anh Giao Tiếp", "12/2025", 12, 
             currencyFormat.format(2160000), currencyFormat.format(0), currencyFormat.format(2160000), "Chưa trả"},
            
            {"TT004", "HS004", "Phạm Thị Dung", "Vật Lý Lớp 12", "12/2025", 8, 
             currencyFormat.format(1280000), currencyFormat.format(1280000), currencyFormat.format(0), "Đã trả đủ"},
            
            {"TT005", "HS005", "Hoàng Văn Em", "Hóa Học Lớp 11", "12/2025", 8, 
             currencyFormat.format(1240000), currencyFormat.format(600000), currencyFormat.format(640000), "Trả một phần"},
            
            {"TT006", "HS006", "Đặng Thị Phương", "Sinh Học Lớp 12", "12/2025", 8, 
             currencyFormat.format(1120000), currencyFormat.format(0), currencyFormat.format(1120000), "Chưa trả"},
            
            {"TT007", "HS007", "Vũ Văn Giang", "Toán Ôn Thi THPT", "12/2025", 8, 
             currencyFormat.format(1600000), currencyFormat.format(1600000), currencyFormat.format(0), "Đã trả đủ"},
            
            {"TT008", "HS008", "Bùi Thị Hoa", "Văn Ôn Thi THPT", "12/2025", 8, 
             currencyFormat.format(1440000), currencyFormat.format(1440000), currencyFormat.format(0), "Đã trả đủ"},
            
            {"TT009", "HS009", "Trịnh Văn Ích", "Tiếng Anh IELTS", "12/2025", 12, 
             currencyFormat.format(3000000), currencyFormat.format(1500000), currencyFormat.format(1500000), "Trả một phần"},
            
            {"TT010", "HS010", "Ngô Thị Kim", "Lập Trình Python", "12/2025", 8, 
             currencyFormat.format(1760000), currencyFormat.format(0), currencyFormat.format(1760000), "Chưa trả"}
        };
        
        long totalAmount = 0;
        long paidAmount = 0;
        long remainingAmount = 0;
        
        for (Object[] row : mockPayments) {
            tableModel.addRow(row);
            
            // Calculate totals (remove currency formatting for calculation)
            String totalStr = ((String) row[6]).replaceAll("[^0-9]", "");
            String paidStr = ((String) row[7]).replaceAll("[^0-9]", "");
            String remainStr = ((String) row[8]).replaceAll("[^0-9]", "");
            
            totalAmount += Long.parseLong(totalStr);
            paidAmount += Long.parseLong(paidStr);
            remainingAmount += Long.parseLong(remainStr);
        }
        
        updateSummary(mockPayments.length, totalAmount, paidAmount, remainingAmount);
    }
    
    private void updateSummary(int count, long total, long paid, long remaining) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        JPanel bottomPanel = (JPanel) getComponent(2);
        
        // Update left panel (count)
        JPanel leftPanel = (JPanel) bottomPanel.getComponent(0);
        JLabel totalLabel = (JLabel) leftPanel.getComponent(0);
        totalLabel.setText("Tổng số bản ghi: " + count);
        
        // Update right panel (amounts)
        JPanel rightPanel = (JPanel) bottomPanel.getComponent(1);
        JLabel totalAmountLabel = (JLabel) rightPanel.getComponent(0);
        JLabel paidLabel = (JLabel) rightPanel.getComponent(2);
        JLabel remainingLabel = (JLabel) rightPanel.getComponent(4);
        
        totalAmountLabel.setText("Tổng phải thu: " + currencyFormat.format(total));
        paidLabel.setText("Đã thu: " + currencyFormat.format(paid));
        remainingLabel.setText("Còn lại: " + currencyFormat.format(remaining));
    }
}
