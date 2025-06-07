package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCUtil {

    public static Connection getConnection() {
        Connection result = null;
        try {
            // Đăng ký Oracle Driver (không bắt buộc từ JDBC 4.0, nhưng có thể thêm)
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Các thông số kết nối
            String url = "jdbc:oracle:thin:@localhost:1521:orcl21"; // orcl là SID mặc định
            String userName = "system"; // Thay bằng tên user Oracle
            String password = "19122005"; // Thay bằng mật khẩu Oracle

            // Tạo kết nối
            result = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu Oracle!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Gỡ lỗi khi cần
        }
        return result;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeResources(PreparedStatement pst, Connection con) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public static void closeResultSet(ResultSet rs) {
    if (rs != null) {
        try {
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

public static void closePreparedStatement(PreparedStatement pst) {
    if (pst != null) {
        try {
            pst.close();
        } catch (SQLException e) {
            Logger.getLogger(JDBCUtil.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
}