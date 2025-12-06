package org.example.view;

import org.example.view.layout.Footer;
import org.example.view.layout.Header;
import org.example.view.layout.SideBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private final Header header;
    private final SideBar sideBar;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;

    private String currentModuleTitle = "Quản Lý Học Sinh";

    // Card keys
    private static final String SCREEN_STUDENTS = "students";
    private static final String SCREEN_CLASSES = "classes";
    private static final String SCREEN_ATTENDANCE = "attendance";
    private static final String SCREEN_PAYMENTS = "payments";
    private static final String SCREEN_REPORTS = "reports";
    private static final String SCREEN_SETTINGS = "settings";

    public MainFrame(String username, String role) {
        setTitle("Hệ Thống Quản Lý Dạy Thêm - HUE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Header & Sidebar
        header = new Header(username, role);
        sideBar = new SideBar(role);

        // Background content
        JPanel bgContent = new JPanel(new BorderLayout());
        bgContent.setBackground(new Color(237, 239, 252));
        bgContent.setBorder(new EmptyBorder(18, 24, 18, 24));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);

        initScreens();
        bgContent.add(contentPanel, BorderLayout.CENTER);

        Footer footer = new Footer();

        add(sideBar, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(bgContent, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        initMenuActions();
        initHeaderActions();
    }

    // ===== Tạo các màn hình mock =====
    private void initScreens() {
        contentPanel.add(new StudentManagementPanel(), SCREEN_STUDENTS);
        contentPanel.add(new ClassManagementPanel(), SCREEN_CLASSES);
        contentPanel.add(new AttendanceManagementPanel(), SCREEN_ATTENDANCE);
        contentPanel.add(new PaymentManagementPanel(), SCREEN_PAYMENTS);
        contentPanel.add(new DashboardPanel(), SCREEN_REPORTS);
        contentPanel.add(createModuleScreen("Cài Đặt Hệ Thống"), SCREEN_SETTINGS);

        // Default
        showScreen(SCREEN_STUDENTS, "Quản Lý Học Sinh");
    }

    private JPanel createModuleScreen(String title) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 22, 22, 22));

        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(new Color(120, 120, 160));

        card.add(lbl, BorderLayout.CENTER);
        return card;
    }

    // ===== Gán action cho sidebar =====
    private void initMenuActions() {

        JButton btn;

        btn = sideBar.getMenu("Quản Lý Học Sinh");
        if (btn != null) {
            btn.addActionListener(e ->
                    showScreen(SCREEN_STUDENTS, "Quản Lý Học Sinh"));
        }

        btn = sideBar.getMenu("Quản Lý Lớp Học");
        if (btn != null) {
            btn.addActionListener(e ->
                    showScreen(SCREEN_CLASSES, "Quản Lý Lớp Học"));
        }

        btn = sideBar.getMenu("Điểm Danh");
        if (btn != null) {
            btn.addActionListener(e ->
                    showScreen(SCREEN_ATTENDANCE, "Điểm Danh & Học Phí"));
        }

        btn = sideBar.getMenu("Thanh Toán");
        if (btn != null) {
            btn.addActionListener(e ->
                    showScreen(SCREEN_PAYMENTS, "Thanh Toán & Học Phí"));
        }

        btn = sideBar.getMenu("Báo Cáo");
        if (btn != null) {
            btn.addActionListener(e ->
                    showScreen(SCREEN_REPORTS, "Báo Cáo & Thống Kê"));
        }

        btn = sideBar.getMenu("Cài Đặt");
        if (btn != null) {
            btn.addActionListener(e ->
                    showScreen(SCREEN_SETTINGS, "Cài Đặt Hệ Thống"));
        }

        // Logout
        btn = sideBar.getMenu("Đăng xuất");
        if (btn != null) {
            btn.addActionListener(e -> {
                new LoginForm().setVisible(true);
                dispose();
            });
        }
    }

    // ===== Hành vi nút Thêm/Sửa/Export trên header (tạm mock) =====
    private void initHeaderActions() {
        header.getBtnAdd().addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Thực hiện chức năng [Thêm] tại module: " + currentModuleTitle,
                        "Action", JOptionPane.INFORMATION_MESSAGE));

        header.getBtnEdit().addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Thực hiện chức năng [Sửa] tại module: " + currentModuleTitle,
                        "Action", JOptionPane.INFORMATION_MESSAGE));

        header.getBtnExport().addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Thực hiện chức năng [Xuất Excel] tại module: " + currentModuleTitle,
                        "Action", JOptionPane.INFORMATION_MESSAGE));
    }

    // ===== Đổi màn hình + cập nhật header + sidebar =====
    private void showScreen(String cardKey, String moduleTitle) {
        currentModuleTitle = moduleTitle;
        header.setModuleTitle(moduleTitle);
        sideBar.setActiveMenu(moduleTitle);
        cardLayout.show(contentPanel, cardKey);
    }

    // Panel bo tròn có bóng nhẹ dùng cho mọi màn hình
    static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // bóng nhẹ
            graphics.setColor(new Color(0, 0, 0, 18));
            graphics.fillRoundRect(4, 4, width - 8, height - 8,
                    arcs.width, arcs.height);

            // thân panel
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, width - 8, height - 8,
                    arcs.width, arcs.height);
        }
    }

    // Test nhanh (chạy riêng, không qua login)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new MainFrame("admin", "ADMIN").setVisible(true)
        );
    }
}
