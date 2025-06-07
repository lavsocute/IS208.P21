package DAO;

import DTO.ThuocTinhSanPham.MauSacDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import config.JDBCUtil;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author Tran Nhat Sinh
 */
public class MauSacDAO implements DAOinterface<MauSacDTO> {

    public static MauSacDAO getInstance() {
        return new MauSacDAO();
    }

    @Override
    public int insert(MauSacDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO mausac(mamau, tenmau, trangthai) VALUES (?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMamau());
            pst.setString(2, t.getTenmau());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(MauSacDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE mausac SET tenmau = ? WHERE mamau = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmau());
            pst.setInt(2, t.getMamau());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE mausac SET trangthai = 0 WHERE mamau = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<MauSacDTO> selectAll() {
        ArrayList<MauSacDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM mausac WHERE trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mamau = rs.getInt("mamau");
                String tenmau = rs.getString("tenmau");
                MauSacDTO ms = new MauSacDTO(mamau, tenmau);
                result.add(ms);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    public ArrayList<MauSacDTO> getAll() {
        ArrayList<MauSacDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM mausac";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mamau = rs.getInt("mamau");
                String tenmau = rs.getString("tenmau");
                MauSacDTO ms = new MauSacDTO(mamau, tenmau);
                result.add(ms);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public MauSacDTO selectById(String t) {
        MauSacDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM mausac WHERE mamau = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mamau = rs.getInt("mamau");
                String tenmau = rs.getString("tenmau");
                result = new MauSacDTO(mamau, tenmau);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Oracle uses sequences instead of auto-increment
            // First, get the sequence name for the mamau column
            String sequenceQuery = "SELECT sequence_name FROM user_sequences WHERE sequence_name LIKE 'MAUSAC_SEQ%'";
            PreparedStatement pst = con.prepareStatement(sequenceQuery);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String sequenceName = rs.getString(1);
                String sql = "SELECT " + sequenceName + ".NEXTVAL FROM dual";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
            } else {
                // If sequence doesn't exist, you might need to create one
                Logger.getLogger(MauSacDAO.class.getName()).log(Level.WARNING, "Sequence for MAUSAC not found");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MauSacDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}