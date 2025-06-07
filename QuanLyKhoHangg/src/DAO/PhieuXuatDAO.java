package DAO;

import DTO.ChiTietPhieuDTO;
import DTO.ChiTietSanPhamDTO;
import DTO.PhieuXuatDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhieuXuatDAO implements DAOinterface<PhieuXuatDTO> {
    
    public static PhieuXuatDAO getInstance(){
        return new PhieuXuatDAO();
    }

    @Override
    public int insert(PhieuXuatDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "INSERT INTO phieuxuat(maphieuxuat, tongtien, nguoitao, makhachhang, trangthai, thoigian) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMaphieu());
            pst.setLong(2, t.getTongTien());
            pst.setInt(3, t.getManguoitao());
            pst.setInt(4, t.getMakh());
            pst.setInt(5, t.getTrangthai());
            pst.setTimestamp(6, t.getThoigiantao());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(PhieuXuatDTO t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE phieuxuat SET thoigian=?, tongtien=?, trangthai=? WHERE maphieuxuat=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setTimestamp(1, t.getThoigiantao());
            pst.setLong(2, t.getTongTien());
            pst.setInt(3, t.getTrangthai());
            pst.setInt(4, t.getMaphieu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "UPDATE phieuxuat SET trangthai = 0 WHERE maphieuxuat = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
public ArrayList<PhieuXuatDTO> selectAll() {
    ArrayList<PhieuXuatDTO> result = new ArrayList<>();
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    try {
        con = JDBCUtil.getConnection();
        System.out.println("Kết nối database thành công");
        
        String sql = "SELECT * FROM phieuxuat ORDER BY maphieuxuat DESC";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();
        
        System.out.println("Đang thực hiện truy vấn...");
        
        while(rs.next()) {
            System.out.println("Đã tìm thấy bản ghi");
            int maphieu = rs.getInt("maphieuxuat");
            Timestamp thoigiantao = rs.getTimestamp("thoigian");
            int makh = rs.getInt("makhachhang");
            int nguoitao = rs.getInt("nguoitao");
            long tongtien = rs.getLong("tongtien");
            int trangthai = rs.getInt("trangthai");
            
            System.out.println("Thông tin phiếu: " + maphieu + ", " + thoigiantao + ", " + tongtien);
            
            PhieuXuatDTO phieuxuat = new PhieuXuatDTO(makh, maphieu, nguoitao, thoigiantao, tongtien, trangthai);
            result.add(phieuxuat);
        }
    } catch (SQLException e) {
        System.out.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
    
    System.out.println("Tổng số phiếu xuất lấy được: " + result.size());
    return result;
}

    @Override
    public PhieuXuatDTO selectById(String t) {
        PhieuXuatDTO result = null;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM phieuxuat WHERE maphieuxuat=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int maphieu = rs.getInt("maphieuxuat");
                Timestamp thoigiantao = rs.getTimestamp("thoigian");
                int makh = rs.getInt("makhachhang");
                int nguoitao = rs.getInt("nguoitaophieuxuat");
                long tongtien = rs.getLong("tongtien");
                int trangthai = rs.getInt("trangthai");
                result = new PhieuXuatDTO(makh, maphieu, nguoitao, thoigiantao, tongtien, trangthai);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }
    
    public PhieuXuatDTO cancel(int phieu) {
        PhieuXuatDTO result = null;
        try {
            ArrayList<ChiTietSanPhamDTO> chitietsanpham = ChiTietSanPhamDAO.getInstance().selectByMaPhieuXuat(phieu);
            ArrayList<ChiTietPhieuDTO> chitietphieu = ChiTietPhieuXuatDAO.getInstance().selectAll(phieu+"");
            ChiTietPhieuXuatDAO.getInstance().reset(chitietphieu);
            for (ChiTietSanPhamDTO chiTietSanPhamDTO : chitietsanpham) {
                ChiTietSanPhamDAO.getInstance().reset(chiTietSanPhamDTO);
            }
            deletePhieu(phieu);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
    
    public int deletePhieu(int t) {
        int result = 0 ;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "DELETE FROM phieuxuat WHERE maphieuxuat = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<PhieuXuatDTO> selectAllofKH(int makh) {
        ArrayList<PhieuXuatDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            String sql = "SELECT * FROM phieuxuat WHERE makhachhang=? ORDER BY maphieuxuat DESC";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setInt(1, makh);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while(rs.next()){
                int maphieu = rs.getInt("maphieuxuat");
                Timestamp thoigiantao = rs.getTimestamp("thoigian");
                int kh = rs.getInt("makhachhang");
                int nguoitao = rs.getInt("nguoitaophieuxuat");
                long tongtien = rs.getLong("tongtien");
                int trangthai = rs.getInt("trangthai");
                PhieuXuatDTO phieuxuat = new PhieuXuatDTO(kh, maphieu, nguoitao, thoigiantao, tongtien, trangthai);
                result.add(phieuxuat);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = (Connection) JDBCUtil.getConnection();
            // Sử dụng sequence trong Oracle thay vì AUTO_INCREMENT
            String sql = "SELECT phieuxuat_seq.NEXTVAL FROM dual";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuXuatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}