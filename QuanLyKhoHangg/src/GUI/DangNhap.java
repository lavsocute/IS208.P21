package GUI;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import helper.BCrypt;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.miginfocom.swing.MigLayout;

public class DangNhap extends JFrame implements KeyListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton cmdLogin;

    public DangNhap() {
        initComponent();
    }

    private void initComponent() {
        // Thiết lập cửa sổ chính
        this.setSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("Đăng nhập hệ thống");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel chính sử dụng MigLayout
        JPanel mainPanel = new JPanel(new MigLayout("fill,insets 20", "[center]", "[center]"));
        
        // Panel đăng nhập
        JPanel loginPanel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));
        loginPanel.putClientProperty(FlatClientProperties.STYLE, 
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        // Thiết lập các thành phần
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cmdLogin = new JButton("ĐĂNG NHẬP");
        
        // Thiết lập style cho các thành phần
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, 
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");
        
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập tên đăng nhập");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");

        // Tiêu đề và mô tả
        JLabel lbTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG");
        JLabel description = new JLabel("Vui lòng nhập thông tin tài khoản");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, 
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        // Thêm các thành phần vào panel đăng nhập
        loginPanel.add(lbTitle);
        loginPanel.add(description);
        loginPanel.add(new JLabel("Tên đăng nhập"), "gapy 8");
        loginPanel.add(txtUsername);
        loginPanel.add(new JLabel("Mật khẩu"), "gapy 8");
        loginPanel.add(txtPassword);
        loginPanel.add(cmdLogin, "gapy 10");
        
        // Thêm panel đăng nhập vào panel chính
        mainPanel.add(loginPanel);
        
        // Thêm panel chính vào frame
        this.add(mainPanel, BorderLayout.CENTER);
        
        // Xử lý sự kiện
        cmdLogin.addActionListener(e -> {
            try {
                checkLogin();
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(DangNhap.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        txtUsername.addKeyListener(this);
        txtPassword.addKeyListener(this);
    }

    public void checkLogin() throws UnsupportedLookAndFeelException {
        String usernameCheck = txtUsername.getText();
        String passwordCheck = new String(txtPassword.getPassword());
        
        if (usernameCheck.isEmpty() || passwordCheck.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin đầy đủ", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        TaiKhoanDTO tk = TaiKhoanDAO.getInstance().selectByUsername(usernameCheck);
        
        if (tk == null) {
            JOptionPane.showMessageDialog(this, "Tài khoản không tồn tại", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else if (tk.getTrangthai() == 0) {
            JOptionPane.showMessageDialog(this, "Tài khoản đã bị khóa", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else if (!BCrypt.checkpw(passwordCheck, tk.getMatkhau())) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không chính xác", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        } else {
            this.dispose();
            Main main = new Main(tk);
            main.setVisible(true);
        }
    }

    public static void main(String[] args) {
        // Thiết lập giao diện FlatLaf
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);
        
        // Hiển thị cửa sổ đăng nhập
        EventQueue.invokeLater(() -> {
            DangNhap login = new DangNhap();
            login.setVisible(true);
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                checkLogin();
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(DangNhap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}