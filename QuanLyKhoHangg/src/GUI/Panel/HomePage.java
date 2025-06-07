package GUI.Panel;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class HomePage extends JPanel {
    private Image backgroundImage;
    private static final Color TITLE_COLOR = new Color(0, 51, 102); // Màu xanh đậm chuyên nghiệp
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 100); // Màu bóng đổ

    public HomePage() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1100, 600));
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/quanlykhohang.jpg"));
            // Giữ nguyên tỷ lệ ảnh nhưng chiều ngang chiếm 90% màn hình
            int newWidth = (int)(getPreferredSize().width * 0.9);
            backgroundImage = icon.getImage().getScaledInstance(newWidth, -1, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            e.printStackTrace();
            setBackground(new Color(240, 240, 240)); // Màu nền nhạt khi không có ảnh
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Vẽ nền gradient chuyên nghiệp
        Graphics2D g2d = (Graphics2D)g;
        GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 240, 240), 0, getHeight(), new Color(220, 220, 220));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Vẽ hình ảnh căn giữa với chiều ngang lớn
        if (backgroundImage != null) {
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);
            int x = (getWidth() - imgWidth) / 2 + 10;
            int y = (getHeight() - imgHeight) / 2 + 20 ; // Dịch lên trên 30px
            
            // Thêm hiệu ứng bóng đổ cho ảnh
            g2d.setColor(SHADOW_COLOR);
            g2d.fillRoundRect(x + 5, y + 5, imgWidth, imgHeight, 10, 10);
            
            // Vẽ ảnh với bo góc
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new RoundRectangle2D.Float(x, y, imgWidth, imgHeight, 10, 10));
            g2d.drawImage(backgroundImage, x, y, this);
            g2d.setClip(null);
        }
        
        // Vẽ tiêu đề với hiệu ứng bóng đổ
        String title = "HỆ THỐNG QUẢN LÝ KHO HÀNG";
        Font titleFont = new Font("Segoe UI", Font.BOLD, 42);
        g2d.setFont(titleFont);
        
        // Tính toán vị trí
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleX = (getWidth() - titleWidth) / 2;
        int titleY = 80;
        
        // Vẽ bóng đổ
        g2d.setColor(SHADOW_COLOR);
        g2d.drawString(title, titleX + 3, titleY + 3);
        
        // Vẽ tiêu đề chính
        g2d.setColor(TITLE_COLOR);
        g2d.drawString(title, titleX, titleY);
        
        
    }
}