package GUI.Component;

import DAO.NhanVienDAO;
import DTO.NhanVienDTO;
import DTO.TaiKhoanDTO;
import GUI.Main;
import GUI.Panel.*;
import GUI.Panel.ThongKe.ThongKe;
import GUI.Dialog.MyAccount;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuTaskbar extends JPanel {

    private TaiKhoanDTO taiKhoanDangNhap;
    // Panels
    private HomePage homePage;
    private SanPham sanPham;
    private QuanLyThuocTinhSP quanLyThuocTinhSP;
    private KhuVucKho quanLyKho;
    private PhieuNhap phieuNhap;
    private PhieuXuat phieuXuat;
    private KhachHang khachHang;
    private NhaCungCap nhaCungCap;
    private NhanVien nhanVien;
    private TaiKhoan taiKhoan;
    private ThongKe thongKe;

    // Menu items
        private final String[][] menuItems = {
            {"Trang chủ", "home.svg", "trangchu", "ALL"},
            {"Sản phẩm", "product.svg", "sanpham", "ALL"},
            {"Khu vực kho", "area.svg", "khuvuckho", "0,1,2,3"},
            {"Phiếu nhập", "import.svg", "nhaphang", "0,1,2"},
            {"Phiếu xuất", "export.svg", "xuathang", "0,1,3"},
            {"Khách hàng", "customer.svg", "khachhang", "0,1"},
            {"Nhà cung cấp", "supplier.svg", "nhacungcap", "0,1"},
            {"Nhân viên", "staff.svg", "nhanvien", "0"},
            {"Tài khoản", "account.svg", "taikhoan", "0"},
            {"Thống kê", "statistical.svg", "thongke", "0,1"},
            {"Đăng xuất", "log_out.svg", "dangxuat", "ALL"},};
    
    // Components
    private Main main;
    private TaiKhoanDTO user;
    private itemTaskbar[] listitem;
    private JLabel lblUsername, lblRole, lblIcon;
    private JScrollPane scrollPane;
    private JPanel pnlCenter, pnlTop, pnlBottom;
    private NhanVienDTO nhanVienDTO;
    
    // Slide menu variables
    private boolean isCollapsed = false;
    private Timer animationTimer;
    private int currentWidth;
    private final int COLLAPSED_WIDTH = 60;
    private final int EXPANDED_WIDTH = 250;
    private JButton toggleButton;

    // Màu chính (không phải trắng) phối với icon đen
private final Color primaryColor = new Color(70, 130, 180);    // SteelBlue - màu xanh dương trung tính
private final Color hoverColor = new Color(100, 150, 200);     // Xanh nhạt hơn khi hover
private final Color selectedColor = new Color(50, 100, 150);   // Xanh đậm khi chọn

// Màu nền phối với icon đen
private final Color backgroundColor = new Color(240, 245, 250); // Nền xanh rất nhạt
private final Color borderColor = new Color(200, 210, 220);     // Viền xám xanh nhạt

// Màu chữ và icon
private final Color textColor = new Color(30, 30, 30);          // Chữ đen nhạt (dễ đọc)
private final Color secondaryTextColor = new Color(100, 110, 120); // Chữ phụ xám xanh
private final Color iconColor = new Color(0, 0, 0);             // Icon đen (giữ nguyên)

    public MenuTaskbar(Main main, TaiKhoanDTO tk) {
        this.main = main;
        this.user = tk;
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(Integer.toString(tk.getManv()));
        initComponent();
    }

    public MenuTaskbar(TaiKhoanDTO taiKhoan) {
        this.user = taiKhoan;
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(Integer.toString(taiKhoan.getManv()));
        initComponent();
    }

    public MenuTaskbar() {
        this.user = new TaiKhoanDTO();
        this.user.setManv(1);
        this.user.setQuyen("STAFF");
        this.nhanVienDTO = new NhanVienDTO();
        nhanVienDTO.setHoten("Admin Test");
        nhanVienDTO.setGioitinh(1);
        initComponent();
    }

    private void initComponent() {
        listitem = new itemTaskbar[menuItems.length];
        this.setOpaque(true);
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.putClientProperty(FlatClientProperties.STYLE, "arc: 0");
        
        currentWidth = EXPANDED_WIDTH;
        this.setPreferredSize(new Dimension(currentWidth, 1400));

        // Top panel - user info
        pnlTop = createTopPanel();
        this.add(pnlTop, BorderLayout.NORTH);

        // Center panel - menu items
        pnlCenter = new JPanel();
        pnlCenter.setBackground(backgroundColor);
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        scrollPane = new JScrollPane(pnlCenter);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        this.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel - logout
        pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(backgroundColor);
        pnlBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderColor));
        this.add(pnlBottom, BorderLayout.SOUTH);

        // Create menu items
        for (int i = 0; i < menuItems.length; i++) {
            listitem[i] = new itemTaskbar(menuItems[i][1], menuItems[i][0]);

            boolean isLastItem = (i + 1 == menuItems.length);
            String requiredRoles = menuItems[i][3];

            if (user.getManhomquyen() == 0 || hasPermission(requiredRoles)) {
                listitem[i].setVisible(true);
            } else {
                listitem[i].setVisible(false);
            }

            if (isLastItem) {
                pnlBottom.add(listitem[i], BorderLayout.CENTER);
            } else {
                pnlCenter.add(listitem[i]);
            }
        }

        // Set first item as selected by default
        listitem[0].setBackground(selectedColor);
        listitem[0].setForeground(primaryColor);
        listitem[0].isSelected = true;

        // Add mouse listeners for menu items
        for (int i = 0; i < menuItems.length; i++) {
            final int index = i;
            listitem[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    pnlMenuTaskbarMousePress(evt);
                    handleMenuItemClick(index);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!listitem[index].isSelected) {
                        listitem[index].setBackground(hoverColor);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!listitem[index].isSelected) {
                        listitem[index].setBackground(backgroundColor);
                    }
                }
            });
        }
        
    }

    private void toggleMenu() {
        isCollapsed = !isCollapsed;
        int targetWidth = isCollapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH;
        
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentWidth < targetWidth) {
                    currentWidth += 5;
                    if (currentWidth > targetWidth) currentWidth = targetWidth;
                } else if (currentWidth > targetWidth) {
                    currentWidth -= 5;
                    if (currentWidth < targetWidth) currentWidth = targetWidth;
                }
                
                setPreferredSize(new Dimension(currentWidth, getHeight()));
                revalidate();
                
                if (currentWidth == targetWidth) {
                    animationTimer.stop();
                    updateMenuItemsVisibility();
                }
            }
        });
        animationTimer.start();
    }
    
    private void updateMenuItemsVisibility() {
    boolean showText = !isCollapsed;
    
    for (itemTaskbar item : listitem) {
        item.setShowText(showText);
        item.updateIconSize(isCollapsed);
    }
    
    lblUsername.setVisible(showText);
    lblRole.setVisible(showText);
    
    // Cập nhật toggle button icon
    
    revalidate();
    repaint();
}

    private JPanel createTopPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(backgroundColor);
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor),
            BorderFactory.createEmptyBorder(10, 15, 15, 15) // Giảm padding top xuống 10
    ));

    // Toggle button (right side)
    toggleButton = new JButton();
    toggleButton.setIcon(new FlatSVGIcon("./icon/menu.svg", 20, 20));
    toggleButton.setOpaque(false);
    toggleButton.setContentAreaFilled(false);
    toggleButton.setBorderPainted(false);
    toggleButton.setFocusPainted(false);
    toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    toggleButton.addActionListener(e -> toggleMenu());
    
    JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    togglePanel.setOpaque(false);
    togglePanel.add(toggleButton);
    panel.add(togglePanel, BorderLayout.NORTH); // Đặt toggle button ở phía Bắc

    // Main profile content (centered and pushed down)
    JPanel profileContainer = new JPanel();
    profileContainer.setOpaque(false);
    profileContainer.setLayout(new BoxLayout(profileContainer, BoxLayout.Y_AXIS));
    
    // Thêm khoảng trống phía trên để đẩy nội dung xuống dưới toggle button
    profileContainer.add(Box.createVerticalStrut(20)); // Điều chỉnh số pixel theo ý muốn

    // Avatar panel với căn giữa
    JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    avatarPanel.setOpaque(false);
    
    lblIcon = new JLabel();
    Integer gioiTinh = (nhanVienDTO != null) ? nhanVienDTO.getGioitinh() : null;
    String iconName = (gioiTinh == null) ? "man" : (gioiTinh == 1 ? "man" : "women");
    lblIcon.setIcon(new FlatSVGIcon("./icon/" + iconName + "_50px.svg"));
    lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    lblIcon.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent evt) {
            MyAccount ma = new MyAccount(main, MenuTaskbar.this, "Chỉnh sửa thông tin tài khoản", true);
            ma.setVisible(true);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            lblIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            lblIcon.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    });
    
    avatarPanel.add(lblIcon);
    profileContainer.add(avatarPanel);

    // User info panel
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setOpaque(false);
    infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Thêm padding top

    String name = (nhanVienDTO != null && nhanVienDTO.getHoten() != null) ? nhanVienDTO.getHoten() : "Người dùng";
    lblUsername = new JLabel(name);
    lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 17));
    lblUsername.setForeground(textColor);
    lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

    String role = (user != null) ? getRoleDisplayName(user.getManhomquyen()) : "Không xác định";
    lblRole = new JLabel(role);
    lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    lblRole.setForeground(secondaryTextColor);
    lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

    infoPanel.add(lblUsername);
    infoPanel.add(Box.createVerticalStrut(5));
    infoPanel.add(lblRole);
    
    profileContainer.add(infoPanel);

    // Center everything
    JPanel centerWrapper = new JPanel(new GridBagLayout());
    centerWrapper.setOpaque(false);
    centerWrapper.add(profileContainer);
    
    panel.add(centerWrapper, BorderLayout.CENTER);

    return panel;
}

    private String getRoleDisplayName(int manhomquyen) {
        switch (manhomquyen) {
            case 0: return "Admin";
            case 1: return "Quản lý kho";
            case 2: return "Nhân viên nhập hàng";
            case 3: return "Nhân viên xuất hàng";
            default: return "Không xác định";
        }
    }

    private void handleMenuItemClick(int index) {
        switch (menuItems[index][2]) {
            case "trangchu":
                homePage = new HomePage();
                main.setPanel(homePage);
                break;
            case "sanpham":
                sanPham = new SanPham(main);
                main.setPanel(sanPham);
                break;
            case "khuvuckho":
                quanLyKho = new KhuVucKho(main);
                main.setPanel(quanLyKho);
                break;
            case "nhaphang":
                phieuNhap = new PhieuNhap(main, nhanVienDTO);
                main.setPanel(phieuNhap);
                break;
            case "xuathang":
                phieuXuat = new PhieuXuat(main, user);
                main.setPanel(phieuXuat);
                break;
            case "khachhang":
                khachHang = new KhachHang(main);
                main.setPanel(khachHang);
                break;
            case "nhacungcap":
                nhaCungCap = new NhaCungCap(main);
                main.setPanel(nhaCungCap);
                break;
            case "nhanvien":
                nhanVien = new NhanVien(main);
                main.setPanel(nhanVien);
                break;
            case "taikhoan":
                taiKhoan = new TaiKhoan(main);
                main.setPanel(taiKhoan);
                break;
            case "thongke":
                thongKe = new ThongKe();
                main.setPanel(thongKe);
                break;
            case "dangxuat":
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    main.dispose();
                    new GUI.DangNhap().setVisible(true);
                }
                break;
        }
    }

    private boolean hasPermission(String requiredRoles) {
        if (requiredRoles.equalsIgnoreCase("ALL")) {
            return true;
        }
        String[] roles = requiredRoles.split(",");
        int userRole = user.getManhomquyen();
        for (String role : roles) {
            try {
                if (Integer.parseInt(role.trim()) == userRole) {
                    return true;
                }
            } catch (NumberFormatException e) {
                // Bỏ qua role sai định dạng
            }
        }
        return false;
    }

    private void pnlMenuTaskbarMousePress(MouseEvent evt) {
        for (int i = 0; i < menuItems.length; i++) {
            if (evt.getSource() == listitem[i]) {
                listitem[i].isSelected = true;
                listitem[i].setBackground(selectedColor);
                listitem[i].setForeground(primaryColor);
            } else {
                listitem[i].isSelected = false;
                listitem[i].setBackground(backgroundColor);
                listitem[i].setForeground(textColor);
            }
        }
    }

    public void resetChange() {
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(String.valueOf(nhanVienDTO.getManv()));
        updateUserInfo();
    }

    private void updateUserInfo() {
        lblUsername.setText(nhanVienDTO.getHoten());
        lblRole.setText(getRoleDisplayName(user.getManhomquyen()));
        lblIcon.setIcon(new FlatSVGIcon("./icon/" + (nhanVienDTO.getGioitinh() == 1 ? "man" : "woman") + "_50px.svg"));
    }

    private class itemTaskbar extends JPanel {
    private boolean isSelected = false;
    private JLabel lblText;
    private String iconName;
    private JLabel lblIcon;
    
    public itemTaskbar(String icon, String text) {
        this.iconName = icon;
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(15, 0)); // Sử dụng BorderLayout
        this.setPreferredSize(new Dimension(EXPANDED_WIDTH, 40)); // Luôn giữ kích thước như khi mở rộng
        this.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon - đặt ở phía Tây
        FlatSVGIcon svgIcon = new FlatSVGIcon("./icon/" + icon, 20, 20);
        lblIcon = new JLabel(svgIcon);
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        iconPanel.add(lblIcon, BorderLayout.CENTER);
        iconPanel.setPreferredSize(new Dimension(40, 40)); // Kích thước cố định cho icon
        this.add(iconPanel, BorderLayout.WEST);

        // Text - đặt ở trung tâm
        lblText = new JLabel(text);
        lblText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblText.setForeground(textColor);
        this.add(lblText, BorderLayout.CENTER);
    }
    
    public void setShowText(boolean show) {
        lblText.setVisible(show);
        // Không thay đổi layout hay kích thước khi ẩn/hiện text
        revalidate();
        repaint();
    }
    
    public void updateIconSize(boolean isCollapsed) {
        FlatSVGIcon svgIcon = new FlatSVGIcon("./icon/" + iconName, 
            isCollapsed ? 24 : 20, 
            isCollapsed ? 24 : 20);
        lblIcon.setIcon(svgIcon);
    }
    }
    public NhanVienDTO getNhanVienDTO() {
    return this.nhanVienDTO;
}
}