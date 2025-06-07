package DAO;

import DTO.TaiKhoanDTO;
import config.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaiKhoanDAO implements DAOinterface<TaiKhoanDTO> {

    public static TaiKhoanDAO getInstance() {
        return new TaiKhoanDAO();
    }

    @Override
    public int insert(TaiKhoanDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            // id là IDENTITY, tự động tăng
            String sql = "INSERT INTO TAIKHOAN (manv, username, matkhau, manhomquyen, trangthai) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getManv());
            pst.setString(2, t.getUsername());
            pst.setString(3, t.getMatkhau());
            pst.setInt(4, t.getManhomquyen());  // quyen VARCHAR2
            pst.setInt(5, t.getTrangthai());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(TaiKhoanDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE TAIKHOAN SET username = ?, matkhau = ?, manhomquyen = ?, trangthai = ? WHERE manv = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getUsername());
            pst.setString(2, t.getMatkhau());
            pst.setInt(3, t.getManhomquyen());
            pst.setInt(4, t.getTrangthai());
            pst.setInt(5, t.getManv());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updatePassByEmail(String email, String password) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            // Cách làm: update TAIKHOAN set matkhau = ? where manv in (select manv from NHANVIEN where email = ?)
            String sql = "UPDATE TAIKHOAN SET matkhau = ? WHERE manv IN (SELECT manv FROM NHANVIEN WHERE email = ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, password);
            pst.setString(2, email);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public TaiKhoanDTO selectByEmail(String email) {
        TaiKhoanDTO tk = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT tk.id, tk.manv, tk.username, tk.matkhau, tk.manhomquyen, tk.trangthai " +
                         "FROM TAIKHOAN tk JOIN NHANVIEN nv ON tk.manv = nv.manv WHERE nv.email = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tk = new TaiKhoanDTO(
                    rs.getInt("manv"),
                    rs.getString("username"),
                    rs.getString("matkhau"),
                    rs.getInt("manhomquyen"),
                    rs.getInt("trangthai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk;
    }

    public int sendOtp(String email, String otp) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            // Giả sử bạn thêm cột otp VARCHAR2(10) vào bảng TAIKHOAN (nếu chưa có thì phải thêm)
            String sql = "UPDATE TAIKHOAN SET otp = ? WHERE manv IN (SELECT manv FROM NHANVIEN WHERE email = ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, otp);
            pst.setString(2, email);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean checkOtp(String email, String otp) {
        boolean check = false;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT 1 FROM TAIKHOAN tk JOIN NHANVIEN nv ON tk.manv = nv.manv WHERE nv.email = ? AND tk.otp = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, otp);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                check = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE TAIKHOAN SET trangthai = -1 WHERE manv = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(t));
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<TaiKhoanDTO> selectAll() {
        ArrayList<TaiKhoanDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT manv, username, matkhau, manhomquyen, trangthai FROM TAIKHOAN WHERE trangthai IN (0,1)";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                TaiKhoanDTO tk = new TaiKhoanDTO(
                    rs.getInt("manv"),
                    rs.getString("username"),
                    rs.getString("matkhau"),
                    rs.getInt("manhomquyen"),
                    rs.getInt("trangthai")
                );
                result.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public TaiKhoanDTO selectById(String t) {
        TaiKhoanDTO result = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT manv, username, matkhau, manhomquyen, trangthai FROM TAIKHOAN WHERE manv = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(t));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new TaiKhoanDTO(
                    rs.getInt("manv"),
                    rs.getString("username"),
                    rs.getString("matkhau"),
                    rs.getInt("manhomquyen"),
                    rs.getInt("trangthai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TaiKhoanDTO selectByUsername(String username) {
        TaiKhoanDTO result = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT manv, username, matkhau, manhomquyen, trangthai FROM TAIKHOAN WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new TaiKhoanDTO(
                    rs.getInt("manv"),
                    rs.getString("username"),
                    rs.getString("matkhau"),
                    rs.getInt("manhomquyen"),
                    rs.getInt("trangthai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        // Oracle không có AUTO_INCREMENT như MySQL
        // Có thể lấy giá trị sequence nếu bạn dùng sequence để tạo id (chưa có)
        return -1;
    }
}
