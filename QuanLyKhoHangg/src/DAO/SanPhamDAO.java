package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import config.JDBCUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import DTO.SanPhamDTO;
import DTO.ChiTietSanPhamDTO;

public class SanPhamDAO implements DAOinterface<SanPhamDTO> {

    public static SanPhamDAO getInstance() {
        return new SanPhamDAO();
    }

    @Override
    public int insert(SanPhamDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO SANPHAM (masp, tensp, hinhanh, maloai, mota, soluongton, gianhap, giaban, thoigianbaohanh, thuonghieu, xuatxu, donvitinh, khuvuckho, trangthai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMasp());
            pst.setString(2, t.getTensp());
            pst.setString(3, t.getHinhanh());
            pst.setInt(4, t.getMaloai());
            pst.setString(5, t.getMota());
            pst.setInt(6, t.getSoluongton());
            pst.setDouble(7, t.getGianhap());
            pst.setDouble(8, t.getGiaban());
            pst.setObject(9, t.getThoigianbaohanh());
            pst.setObject(10, t.getThuonghieu());
            pst.setObject(11, t.getXuatxu());
            pst.setObject(12, t.getDonvitinh());
            pst.setObject(13, t.getKhuvuckho());
            pst.setInt(14, t.getTrangthai());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(SanPhamDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE SANPHAM SET tensp=?, hinhanh=?, maloai=?, mota=?, soluongton=?, gianhap=?, giaban=?, thoigianbaohanh=?, thuonghieu=?, xuatxu=?, donvitinh=?, khuvuckho=?, trangthai=? WHERE masp=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTensp());
            pst.setString(2, t.getHinhanh());
            pst.setInt(3, t.getMaloai());
            pst.setString(4, t.getMota());
            pst.setInt(5, t.getSoluongton());
            pst.setDouble(6, t.getGianhap());
            pst.setDouble(7, t.getGiaban());
            pst.setObject(8, t.getThoigianbaohanh());
            pst.setObject(9, t.getThuonghieu());
            pst.setObject(10, t.getXuatxu());
            pst.setObject(11, t.getDonvitinh());
            pst.setObject(12, t.getKhuvuckho());
            pst.setInt(13, t.getTrangthai());
            pst.setInt(14, t.getMasp());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE sanpham SET trangthai=0 WHERE masp=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<SanPhamDTO> selectAll() {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int masp = rs.getInt("masp");
                String tensp = rs.getString("tensp");
                String hinhanh = rs.getString("hinhanh");
                int maloai = rs.getInt("maloai");
                String mota = rs.getString("mota");
                int soluongton = rs.getInt("soluongton");
                double gianhap = rs.getDouble("gianhap");
                double giaban = rs.getDouble("giaban");
                Integer thoigianbaohanh = rs.getObject("thoigianbaohanh") != null ? rs.getInt("thoigianbaohanh") : null;
                Integer thuonghieu = rs.getObject("thuonghieu") != null ? rs.getInt("thuonghieu") : null;
                Integer xuatxu = rs.getObject("xuatxu") != null ? rs.getInt("xuatxu") : null;
                Integer donvitinh = rs.getObject("donvitinh") != null ? rs.getInt("donvitinh") : null;
                Integer khuvuckho = rs.getObject("khuvuckho") != null ? rs.getInt("khuvuckho") : null;
                int trangthai = rs.getInt("trangthai");
                
                SanPhamDTO sp = new SanPhamDTO(masp, tensp, hinhanh, maloai, mota, soluongton, 
                                             gianhap, giaban, thoigianbaohanh, thuonghieu, 
                                             xuatxu, donvitinh, khuvuckho, trangthai);
                result.add(sp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public SanPhamDTO selectById(String t) {
        SanPhamDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE masp=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int masp = rs.getInt("masp");
                String tensp = rs.getString("tensp");
                String hinhanh = rs.getString("hinhanh");
                int maloai = rs.getInt("maloai");
                String mota = rs.getString("mota");
                int soluongton = rs.getInt("soluongton");
                double gianhap = rs.getDouble("gianhap");
                double giaban = rs.getDouble("giaban");
                Integer thoigianbaohanh = rs.getObject("thoigianbaohanh") != null ? rs.getInt("thoigianbaohanh") : null;
                Integer thuonghieu = rs.getObject("thuonghieu") != null ? rs.getInt("thuonghieu") : null;
                Integer xuatxu = rs.getObject("xuatxu") != null ? rs.getInt("xuatxu") : null;
                Integer donvitinh = rs.getObject("donvitinh") != null ? rs.getInt("donvitinh") : null;
                Integer khuvuckho = rs.getObject("khuvuckho") != null ? rs.getInt("khuvuckho") : null;
                int trangthai = rs.getInt("trangthai");
                
                result = new SanPhamDTO(masp, tensp, hinhanh, maloai, mota, soluongton, 
                                      gianhap, giaban, thoigianbaohanh, thuonghieu, 
                                      xuatxu, donvitinh, khuvuckho, trangthai);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT seq_sanpham.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updateSoLuongTon(int masp, int soluong) {
    int result = 0;
    try {
        // Kiểm tra số lượng không âm trước khi cập nhật
        if (soluong < 0) {
            throw new IllegalArgumentException("Số lượng tồn không được âm: " + soluong);
        }
        
        Connection con = JDBCUtil.getConnection();
        String sql = "UPDATE SANPHAM SET soluongton=? WHERE masp=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, soluong);
        pst.setInt(2, masp);
        result = pst.executeUpdate();
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, 
            "Lỗi khi cập nhật số lượng tồn cho sản phẩm " + masp + 
            " với giá trị " + soluong, ex);
    } catch (IllegalArgumentException ex) {
        Logger.getLogger(SanPhamDAO.class.getName()).log(Level.WARNING, ex.getMessage());
    }
    return result;
}

    public ArrayList<SanPhamDTO> selectByStatus(int status) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM SANPHAM WHERE trangthai=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, status);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int masp = rs.getInt("masp");
                String tensp = rs.getString("tensp");
                String hinhanh = rs.getString("hinhanh");
                int maloai = rs.getInt("maloai");
                String mota = rs.getString("mota");
                int soluongton = rs.getInt("soluongton");
                double gianhap = rs.getDouble("gianhap");
                double giaban = rs.getDouble("giaban");
                Integer thoigianbaohanh = rs.getObject("thoigianbaohanh") != null ? rs.getInt("thoigianbaohanh") : null;
                Integer thuonghieu = rs.getObject("thuonghieu") != null ? rs.getInt("thuonghieu") : null;
                Integer xuatxu = rs.getObject("xuatxu") != null ? rs.getInt("xuatxu") : null;
                Integer donvitinh = rs.getObject("donvitinh") != null ? rs.getInt("donvitinh") : null;
                Integer khuvuckho = rs.getObject("khuvuckho") != null ? rs.getInt("khuvuckho") : null;
                int trangthai = rs.getInt("trangthai");
                
                SanPhamDTO sp = new SanPhamDTO(masp, tensp, hinhanh, maloai, mota, soluongton, 
                                             gianhap, giaban, thoigianbaohanh, thuonghieu, 
                                             xuatxu, donvitinh, khuvuckho, trangthai);
                result.add(sp);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    public boolean isMaChiTietExists(ArrayList<ChiTietSanPhamDTO> arr) {
    if (arr == null || arr.isEmpty()) {
        return false;
    }
    
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    try {
        con = JDBCUtil.getConnection();
        // Sử dụng IN clause để kiểm tra nhiều mã cùng lúc
        StringBuilder sql = new StringBuilder("SELECT machitiet FROM ctsanpham WHERE machitiet IN (");
        for (int i = 0; i < arr.size(); i++) {
            sql.append(i == 0 ? "?" : ",?");
        }
        sql.append(")");
        
        pst = con.prepareStatement(sql.toString());
        for (int i = 0; i < arr.size(); i++) {
            pst.setString(i + 1, arr.get(i).getMaChiTiet().trim());
        }
        
        rs = pst.executeQuery();
        
        // Nếu có bất kỳ kết quả nào trả về => tồn tại ít nhất 1 mã
        boolean exists = rs.next();
        
        // Debug: In ra các mã đã tồn tại
        if (exists) {
            System.out.println("==== DEBUG: Các mã đã tồn tại trong kho ====");
            do {
                System.out.println("- " + rs.getString("machitiet"));
            } while (rs.next());
        }
        
        return exists;
        
    } catch (SQLException ex) {
        Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        return true; // Trả về true nếu có lỗi để ngăn thêm dữ liệu không an toàn
    } finally {
        JDBCUtil.closeResultSet(rs);
        JDBCUtil.closePreparedStatement(pst);
        JDBCUtil.closeConnection(con);
    }
}
    public int countChiTietSanPhamByMaSP(int masp) {
    String sql = "SELECT COUNT(*) FROM ctsanpham WHERE masanpham = ?";
    try {
        Connection con = (Connection) JDBCUtil.getConnection();
        PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
        pst.setInt(1, masp);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}
}