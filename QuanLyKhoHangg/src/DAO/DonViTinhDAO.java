package DAO;

import DTO.ThuocTinhSanPham.DonViTinhDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DonViTinhDAO implements DAOinterface<DonViTinhDTO> {

    public static DonViTinhDAO getInstance() {
        return new DonViTinhDAO();
    }

    @Override
    public int insert(DonViTinhDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO donvitinh (madonvitinh, tendonvitinh) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMadonvitinh());
            pst.setString(2, t.getTendonvitinh());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DonViTinhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(DonViTinhDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE donvitinh SET tendonvitinh=? WHERE madonvitinh=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTendonvitinh());
            pst.setInt(2, t.getMadonvitinh());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DonViTinhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM donvitinh WHERE madonvitinh=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DonViTinhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<DonViTinhDTO> selectAll() {
        ArrayList<DonViTinhDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM donvitinh";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int madonvitinh = rs.getInt("madonvitinh");
                String tendonvitinh = rs.getString("tendonvitinh");
                DonViTinhDTO dvt = new DonViTinhDTO(madonvitinh, tendonvitinh);
                result.add(dvt);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(DonViTinhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public DonViTinhDTO selectById(String t) {
        DonViTinhDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM donvitinh WHERE madonvitinh=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int madonvitinh = rs.getInt("madonvitinh");
                String tendonvitinh = rs.getString("tendonvitinh");
                result = new DonViTinhDTO(madonvitinh, tendonvitinh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(DonViTinhDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Oracle uses sequences for auto-increment
            // Assuming the sequence is named donvitinh_seq
            String sql = "SELECT donvitinh_seq.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DonViTinhDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}