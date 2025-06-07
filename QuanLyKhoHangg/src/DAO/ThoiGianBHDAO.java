package DAO;

import DTO.ThuocTinhSanPham.ThoiGianBHDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThoiGianBHDAO implements DAOinterface<ThoiGianBHDTO> {

    public static ThoiGianBHDAO getInstance() {
        return new ThoiGianBHDAO();
    }

    @Override
    public int insert(ThoiGianBHDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO thoigianbh (mathoigianbh, tenthgianbh, trangthai) VALUES (?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMathoigianbh());
            pst.setString(2, t.getTenthgianbh());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThoiGianBHDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(ThoiGianBHDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE thoigianbh SET tenthgianbh=? WHERE mathoigianbh=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenthgianbh());
            pst.setInt(2, t.getMathoigianbh());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThoiGianBHDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE thoigianbh SET trangthai = 0 WHERE mathoigianbh = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThoiGianBHDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<ThoiGianBHDTO> selectAll() {
        ArrayList<ThoiGianBHDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM thoigianbh WHERE trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mathoigianbh = rs.getInt("mathoigianbh");
                String tenthgianbh = rs.getString("tenthgianbh");
                ThoiGianBHDTO tg = new ThoiGianBHDTO(mathoigianbh, tenthgianbh);
                result.add(tg);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(ThoiGianBHDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public ThoiGianBHDTO selectById(String t) {
        ThoiGianBHDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM thoigianbh WHERE mathoigianbh=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int mathoigianbh = rs.getInt("mathoigianbh");
                String tenthgianbh = rs.getString("tenthgianbh");
                result = new ThoiGianBHDTO(mathoigianbh, tenthgianbh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(ThoiGianBHDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Oracle uses sequences instead of AUTO_INCREMENT
            // Assuming you have a sequence named THOIGIANBH_SEQ
            String sql = "SELECT THOIGIANBH_SEQ.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThoiGianBHDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}