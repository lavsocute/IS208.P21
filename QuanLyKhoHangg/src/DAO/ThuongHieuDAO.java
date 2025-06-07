package DAO;

import DTO.ThuocTinhSanPham.ThuongHieuDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThuongHieuDAO implements DAOinterface<ThuongHieuDTO> {
    public static ThuongHieuDAO getInstance() {
        return new ThuongHieuDAO();
    }

    @Override
    public int insert(ThuongHieuDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO thuonghieu(mathuonghieu, tenthuonghieu, trangthai) VALUES (thuonghieu_seq.NEXTVAL, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenthuonghieu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThuongHieuDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(ThuongHieuDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE thuonghieu SET tenthuonghieu=? WHERE mathuonghieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenthuonghieu());
            pst.setInt(2, t.getMathuonghieu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThuongHieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE thuonghieu SET trangthai = 0 WHERE mathuonghieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThuongHieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<ThuongHieuDTO> selectAll() {
        ArrayList<ThuongHieuDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM thuonghieu WHERE trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mathuonghieu = rs.getInt("mathuonghieu");
                String tenthuonghieu = rs.getString("tenthuonghieu");
                
                ThuongHieuDTO lh = new ThuongHieuDTO(mathuonghieu, tenthuonghieu);
                result.add(lh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(ThuongHieuDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public ThuongHieuDTO selectById(String t) {
        ThuongHieuDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM thuonghieu WHERE mathuonghieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mathuonghieu = rs.getInt("mathuonghieu");
                String tenloaihang = rs.getString("tenthuonghieu");
                result = new ThuongHieuDTO(mathuonghieu, tenloaihang);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(ThuongHieuDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT thuonghieu_seq.CURRVAL FROM DUAL";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ThuongHieuDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}