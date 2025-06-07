package DAO;

import DTO.ChiTietPhieuDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

public class ChiTietPhieuXuatDAO implements ChiTietInterface<ChiTietPhieuDTO> {

    public static ChiTietPhieuXuatDAO getInstance() {
        return new ChiTietPhieuXuatDAO();
    }

    @Override
    public int insert(ArrayList<ChiTietPhieuDTO> t) {
        int result = 0;

            for (ChiTietPhieuDTO chiTiet : t) {
                try {
                    Connection con = (Connection) JDBCUtil.getConnection();
                    String sql = "INSERT INTO ctphieuxuat(maphieuxuat, masanpham, soluong, dongia) VALUES (?,?,?,?)";
                    PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
                    pst.setInt(1, chiTiet.getMaPhieu());
                    pst.setInt(2, chiTiet.getMaSanPham());
                    pst.setInt(3, chiTiet.getSoLuong());
                    int soluong = -(chiTiet.getSoLuong());
                    int change = SanPhamDAO.getInstance().updateSoLuongTon(chiTiet.getMaSanPham(), soluong);
                    pst.setDouble(4, chiTiet.getDonGia());
                    result = pst.executeUpdate();
                JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    
        }
   
    public int reset(ArrayList<ChiTietPhieuDTO> t) {
        int result = 0;
        for (ChiTietPhieuDTO chiTiet : t) {
            try {
                SanPhamDAO.getInstance().updateSoLuongTon(chiTiet.getMaSanPham(), +(chiTiet.getSoLuong()));
                delete(chiTiet.getMaPhieu() + "");
            } catch (Exception ex) {
                Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            String sql = "DELETE FROM ctphieuxuat WHERE maphieuxuat = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JDBCUtil.closeConnection(con);
        }
        return result;
    }

    @Override
    public int update(ArrayList<ChiTietPhieuDTO> t, String pk) {
        int result = 0;
        try {
            result = this.delete(pk);
            if (result != 0) {
                result = this.insert(t);
            }
        } catch (Exception ex) {
            Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietPhieuDTO> selectAll(String t) {
        ArrayList<ChiTietPhieuDTO> result = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ctphieuxuat WHERE maphieuxuat = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, t);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                int maphieu = rs.getInt("maphieuxuat");
                int maSanPham = rs.getInt("maSanPham");
                int dongia = rs.getInt("dongia");
                int soluong = rs.getInt("soluong");
                ChiTietPhieuDTO ctphieu = new ChiTietPhieuDTO(maphieu, maSanPham, soluong, dongia);
                result.add(ctphieu);
            }
        } catch (SQLException e) {
            Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ChiTietPhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JDBCUtil.closeConnection(con);
        }
        return result;
    }
}