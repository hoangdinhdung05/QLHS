package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardPanel extends JPanel {
    
    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        
        initComponents();
    }
    
    private void initComponents() {
        // ===== TOP STATS CARDS =====
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.NORTH);
        
        // ===== CENTER CONTENT =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);
        
        JPanel chartPanel = createChartPanel();
        JPanel activityPanel = createActivityPanel();
        
        centerPanel.add(chartPanel);
        centerPanel.add(activityPanel);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        panel.add(createStatCard("Tổng Học Sinh", "150", "📚", new Color(100, 149, 237)));
        panel.add(createStatCard("Số Lớp Đang Mở", "10", "🏫", new Color(40, 167, 69)));
        panel.add(createStatCard("Doanh Thu Tháng", currencyFormat.format(45000000), "💰", new Color(255, 193, 7)));
        panel.add(createStatCard("Học Phí Chưa Thu", currencyFormat.format(8500000), "⚠️", new Color(220, 53, 69)));
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String icon, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon panel
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
        iconPanel.setPreferredSize(new Dimension(80, 80));
        iconPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(new Color(120, 120, 140));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(50, 50, 70));
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(valueLabel);
        
        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("📊 Biểu Đồ Doanh Thu 6 Tháng Gần Nhất");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(50, 50, 70));
        panel.add(title, BorderLayout.NORTH);
        
        // Mock chart (simple bar chart)
        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int padding = 40;
                int barWidth = 60;
                int gap = 20;
                
                // Sample data (in millions)
                int[] data = {35, 42, 38, 45, 43, 48};
                String[] months = {"T7", "T8", "T9", "T10", "T11", "T12"};
                
                // Draw bars
                int maxValue = 50;
                for (int i = 0; i < data.length; i++) {
                    int barHeight = (int) ((double) data[i] / maxValue * (height - padding * 2));
                    int x = padding + i * (barWidth + gap);
                    int y = height - padding - barHeight;
                    
                    // Bar
                    g2.setColor(new Color(100, 149, 237));
                    g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                    
                    // Value
                    g2.setColor(new Color(50, 50, 70));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    String value = data[i] + "M";
                    int strWidth = g2.getFontMetrics().stringWidth(value);
                    g2.drawString(value, x + (barWidth - strWidth) / 2, y - 5);
                    
                    // Month
                    g2.setColor(new Color(120, 120, 140));
                    strWidth = g2.getFontMetrics().stringWidth(months[i]);
                    g2.drawString(months[i], x + (barWidth - strWidth) / 2, height - padding + 20);
                }
            }
        };
        chartArea.setBackground(Color.WHITE);
        chartArea.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        panel.add(chartArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("🔔 Hoạt Động Gần Đây");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(50, 50, 70));
        panel.add(title, BorderLayout.NORTH);
        
        // Activity list
        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setBackground(Color.WHITE);
        activityList.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        addActivityItem(activityList, "Học sinh Nguyễn Văn An đã đăng ký lớp Toán Nâng Cao", "10 phút trước", new Color(100, 149, 237));
        addActivityItem(activityList, "Lớp Tiếng Anh Giao Tiếp đã bắt đầu buổi học", "25 phút trước", new Color(40, 167, 69));
        addActivityItem(activityList, "Học phí tháng 12 của Trần Thị Bình đã được thanh toán", "1 giờ trước", new Color(255, 193, 7));
        addActivityItem(activityList, "Lớp Vật Lý Lớp 12 sẽ bắt đầu vào 18:30 hôm nay", "2 giờ trước", new Color(23, 162, 184));
        addActivityItem(activityList, "3 học sinh mới đã đăng ký học hôm nay", "3 giờ trước", new Color(40, 167, 69));
        addActivityItem(activityList, "Nhắc nhở: 5 học sinh chưa nộp học phí tháng 12", "5 giờ trước", new Color(220, 53, 69));
        
        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addActivityItem(JPanel parent, String text, String time, Color dotColor) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 12, 0),
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 245))
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        // Dot indicator
        JPanel dotPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dotColor);
                g2.fillOval(0, 5, 10, 10);
            }
        };
        dotPanel.setPreferredSize(new Dimension(10, 20));
        dotPanel.setOpaque(false);
        
        // Text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textLabel.setForeground(new Color(50, 50, 70));
        
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(new Color(150, 150, 170));
        
        textPanel.add(textLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(timeLabel);
        
        item.add(dotPanel, BorderLayout.WEST);
        item.add(textPanel, BorderLayout.CENTER);
        
        parent.add(item);
    }
}
