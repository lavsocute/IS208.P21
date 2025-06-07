package DAO;

import DTO.ChiTietPhieuNhapDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;


public class ChiTietPhieuNhapDAO implements ChiTietInterface<ChiTietPhieuNhapDTO> {

    public static ChiTietPhieuNhapDAO getInstance() {
        return new ChiTietPhieuNhapDAO();
    }

    @Override
    public int insert(ArrayList<ChiTietPhieuNhapDTO> t) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            // Using Oracle SQL syntax
            String sql = "INSERT INTO ctphieunhap(maphieunhap, masanpham, soluong, dongia, hinhthucnhap) VALUES (?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            
            // Disable auto-commit to handle transaction
            con.setAutoCommit(false);
            
            for (ChiTietPhieuNhapDTO item : t) {
                pst.setInt(1, item.getMaPhieu());
                pst.setInt(2, item.getMaSanPham());
                pst.setInt(3, item.getSoLuong());
                pst.setDouble(4, item.getDonGia());
                pst.setInt(5, item.getPhuongthucnnhap());
                pst.addBatch(); // Add to batch for better performance
            }
            
            int[] batchResult = pst.executeBatch();
            result = batchResult.length;
            
            // Update product quantities
            for (ChiTietPhieuNhapDTO item : t) {
                SanPhamDAO.getInstance().updateSoLuongTon(item.getMaSanPham(), item.getSoLuong());
            }
            
            con.commit(); // Commit transaction
            
        } catch (SQLException ex) {
            try {
                if (con != null) {
                    con.rollback(); // Rollback if error occurs
                }
            } catch (SQLException ex1) {
                Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) {
                    con.setAutoCommit(true); // Reset auto-commit
                    JDBCUtil.closeConnection(con);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "DELETE FROM ctphieunhap WHERE maphieunhap = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int update(ArrayList<ChiTietPhieuNhapDTO> t, String pk) {
        int result = this.delete(pk);
        if (result != 0) {
            result = this.insert(t);
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietPhieuNhapDTO> selectAll(String t) {
        ArrayList<ChiTietPhieuNhapDTO> result = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ctphieunhap WHERE maphieunhap = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, t);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                int maphieu = rs.getInt("maphieunhap");
                int masanpham = rs.getInt("masanpham");
                int dongia = rs.getInt("dongia");
                int soluong = rs.getInt("soluong");
                int phuongthucnhap = rs.getInt("hinhthucnhap");
                ChiTietPhieuNhapDTO ctphieu = new ChiTietPhieuNhapDTO(phuongthucnhap, maphieu, masanpham, soluong, dongia);
                result.add(ctphieu);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}