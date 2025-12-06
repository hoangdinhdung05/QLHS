package org.example.view;

import org.example.dto.LoginRequest;
import org.example.entity.Account;
import org.example.service.AuthService;
import org.example.service.impl.AuthServiceImpl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm extends JFrame {

    private JTextField username;
    private JPasswordField password;
    private JButton btnLogin;
    private JLabel linkRegister;

    // service
    private final AuthService authService;

    public LoginForm() {
        this.authService = new AuthServiceImpl(); // inject service

        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 400);
        setMinimumSize(new Dimension(500, 350));
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        // ===== PANEL OUTER =====
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(245, 245, 250));
        container.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        add(container);

        // ===== TITLE =====
        JLabel title = new JLabel("QUẢN LÝ SINH VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(50, 50, 70));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        container.add(title, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 250));
        container.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblUsername, gbc);

        // Input Username
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        username = new JTextField(20);
        username.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        username.setPreferredSize(new Dimension(250, 35));
        formPanel.add(username, gbc);

        // Label Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblPassword, gbc);

        // Input Password
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        password = new JPasswordField(20);
        password.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        password.setPreferredSize(new Dimension(250, 35));
        formPanel.add(password, gbc);

        // ===== LOGIN BUTTON =====
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(250, 40));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // hover effect
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnLogin.setBackground(new Color(60, 120, 170));
            }
            public void mouseExited(MouseEvent evt) {
                btnLogin.setBackground(new Color(70, 130, 180));
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnLogin, gbc);

        // ===== REGISTER LINK =====
        linkRegister = new JLabel("<html><u>Chưa có tài khoản? Đăng ký ngay</u></html>");
        linkRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        linkRegister.setForeground(new Color(70, 130, 180));
        linkRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover effect for link
        linkRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterForm().setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                linkRegister.setForeground(new Color(50, 100, 150));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                linkRegister.setForeground(new Color(70, 130, 180));
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 8, 8);
        formPanel.add(linkRegister, gbc);

        // ===== LOGIN EVENT =====
        btnLogin.addActionListener(e -> onLogin());

        // enter để login
        getRootPane().setDefaultButton(btnLogin);
    }

    private void onLogin() {
        String user = username.getText().trim();
        String pass = new String(password.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ username và password!",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account account = authService.login(new LoginRequest(user, pass));

        if (account != null) {
            JOptionPane.showMessageDialog(this,
                    "Đăng nhập thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);

            new MainFrame(account.getUsername(), account.getRole()).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Sai tài khoản hoặc mật khẩu!",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}