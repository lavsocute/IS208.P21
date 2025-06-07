package DAO;

import DTO.ThuocTinhSanPham.XuatXuDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XuatXuDAO implements DAOinterface<XuatXuDTO> {

    public static XuatXuDAO getInstance() {
        return new XuatXuDAO();
    }

    @Override
    public int insert(XuatXuDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO xuatxu (maxuatxu, tenxuatxu, trangthai) VALUES (?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMaxuatxu());
            pst.setString(2, t.getTenxuatxu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(XuatXuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(XuatXuDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE xuatxu SET tenxuatxu=? WHERE maxuatxu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenxuatxu());
            pst.setInt(2, t.getMaxuatxu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(XuatXuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE xuatxu SET trangthai = 0 WHERE maxuatxu = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(XuatXuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<XuatXuDTO> selectAll() {
        ArrayList<XuatXuDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM xuatxu WHERE trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int maxuatxu = rs.getInt("maxuatxu");
                String tenxuatxu = rs.getString("tenxuatxu");
                XuatXuDTO ms = new XuatXuDTO(maxuatxu, tenxuatxu);
                result.add(ms);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(XuatXuDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public XuatXuDTO selectById(String t) {
        XuatXuDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM xuatxu WHERE maxuatxu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int maxuatxu = rs.getInt("maxuatxu");
                String tenxuatxu = rs.getString("tenxuatxu");
                result = new XuatXuDTO(maxuatxu, tenxuatxu);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(XuatXuDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Oracle uses sequences instead of AUTO_INCREMENT
            // Assuming you have a sequence named XUATXU_SEQ
            String sql = "SELECT XUATXU_SEQ.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(XuatXuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}