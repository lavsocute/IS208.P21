package DAO;

import DTO.LoaiSanPhamDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoaiSanPhamDAO implements DAOinterface<LoaiSanPhamDTO> {
    
    public static LoaiSanPhamDAO getInstance() {
        return new LoaiSanPhamDAO();
    }

    @Override
    public int insert(LoaiSanPhamDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            // Sử dụng sequence cho Oracle
            String sql = "INSERT INTO loaisanpham (maloai, tenloai, mota, trangthai) VALUES (loaisanpham_seq.NEXTVAL, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenloai());
            pst.setString(2, t.getMota());
            pst.setInt(3, t.isTrangthai() ? 1 : 0); // Oracle thường dùng số cho boolean
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(LoaiSanPhamDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE loaisanpham SET tenloai=?, mota=?, trangthai=? WHERE maloai=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenloai());
            pst.setString(2, t.getMota());
            pst.setInt(3, t.isTrangthai() ? 1 : 0);
            pst.setInt(4, t.getMaloai());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE loaisanpham SET trangthai = 0 WHERE maloai = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<LoaiSanPhamDTO> selectAll() {
        ArrayList<LoaiSanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM loaisanpham WHERE trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int maloai = rs.getInt("maloai");
                String tenloai = rs.getString("tenloai");
                String mota = rs.getString("mota");
                boolean trangthai = rs.getInt("trangthai") == 1;
                
                LoaiSanPhamDTO lsp = new LoaiSanPhamDTO(maloai, tenloai, mota, trangthai);
                result.add(lsp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public LoaiSanPhamDTO selectById(String t) {
        LoaiSanPhamDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM loaisanpham WHERE maloai=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int maloai = rs.getInt("maloai");
                String tenloai = rs.getString("tenloai");
                String mota = rs.getString("mota");
                boolean trangthai = rs.getInt("trangthai") == 1;
                
                result = new LoaiSanPhamDTO(maloai, tenloai, mota, trangthai);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Lấy giá trị hiện tại của sequence trong Oracle
            String sql = "SELECT loaisanpham_seq.CURRVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public ArrayList<LoaiSanPhamDTO> selectAllIncludingInactive() {
        ArrayList<LoaiSanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM loaisanpham";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int maloai = rs.getInt("maloai");
                String tenloai = rs.getString("tenloai");
                String mota = rs.getString("mota");
                boolean trangthai = rs.getInt("trangthai") == 1;
                
                LoaiSanPhamDTO lsp = new LoaiSanPhamDTO(maloai, tenloai, mota, trangthai);
                result.add(lsp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    // Phương thức mới để lấy ID tiếp theo từ sequence
    public int getNextId() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT loaisanpham_seq.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(LoaiSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}