package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import config.JDBCUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import DTO.KhuVucKhoDTO;

public class KhuVucKhoDAO implements DAOinterface<KhuVucKhoDTO> {

    public static KhuVucKhoDAO getInstance() {
        return new KhuVucKhoDAO();
    }

    @Override
    public int insert(KhuVucKhoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO khuvuckho(makhuvuc, tenkhuvuc, ghichu, trangthai) VALUES (?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMakhuvuc());
            pst.setString(2, t.getTenkhuvuc());
            pst.setString(3, t.getGhichu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(KhuVucKhoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE khuvuckho SET tenkhuvuc=?, ghichu=? WHERE makhuvuc=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenkhuvuc());
            pst.setString(2, t.getGhichu());
            pst.setInt(3, t.getMakhuvuc());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE khuvuckho SET trangthai = 0 WHERE makhuvuc = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<KhuVucKhoDTO> selectAll() {
        ArrayList<KhuVucKhoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM khuvuckho WHERE trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int makhuvuc = rs.getInt("makhuvuc");
                String tenkhuvuc = rs.getString("tenkhuvuc");
                String ghichu = rs.getString("ghichu");
                KhuVucKhoDTO kvk = new KhuVucKhoDTO(makhuvuc, tenkhuvuc, ghichu);
                result.add(kvk);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public KhuVucKhoDTO selectById(String t) {
        KhuVucKhoDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM khuvuckho WHERE makhuvuc=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int makhuvuc = rs.getInt("makhuvuc");
                String tenkhuvuc = rs.getString("tenkhuvuc");
                String ghichu = rs.getString("ghichu");
                result = new KhuVucKhoDTO(makhuvuc, tenkhuvuc, ghichu);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Oracle uses sequences for auto-increment
            // You need to know the sequence name for your table
            String sql = "SELECT seq_khuvuckho.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhuVucKhoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}