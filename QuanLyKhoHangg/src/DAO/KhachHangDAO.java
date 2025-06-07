package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import config.JDBCUtil;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import DTO.KhachHangDTO;

public class KhachHangDAO implements DAOinterface<KhachHangDTO> {

    public static KhachHangDAO getInstance() {
        return new KhachHangDAO();
    }

    @Override
    public int insert(KhachHangDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO KhachHang(maKH, hoten, diachi, sdt, ngaythamgia, trangthai) "
                    + "VALUES (khachhang_seq.NEXTVAL, ?, ?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getHoten());
            pst.setString(2, t.getDiachi());
            pst.setString(3, t.getSdt());
            pst.setDate(4, new java.sql.Date(t.getNgaythamgia().getTime()));

            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(KhachHangDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE KhachHang SET hoten=?, diachi=?, sdt=?, ngaythamgia=? WHERE maKH=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getHoten());
            pst.setString(2, t.getDiachi());
            pst.setString(3, t.getSdt());
            pst.setDate(4, new java.sql.Date(t.getNgaythamgia().getTime()));
            pst.setInt(5, t.getMaKH());

            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE KhachHang SET trangthai=0 WHERE maKH=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

//    @Override
//    public ArrayList<KhachHangDTO> selectAll() {
//        ArrayList<KhachHangDTO> result = new ArrayList<>();
//        try {
//            Connection con = JDBCUtil.getConnection();
//            String sql = "SELECT * FROM KhachHang WHERE trangthai=1";
//            PreparedStatement pst = con.prepareStatement(sql);
//            ResultSet rs = pst.executeQuery();
//            
//            while(rs.next()){
//                int maKH = rs.getInt("maKH");
//                String hoten = rs.getString("hoten");
//                String diachi = rs.getString("diachi");
//                String sdt = rs.getString("sdt");
//                Date ngaythamgia = rs.getDate("ngaythamgia");
//                KhachHangDTO kh = new KhachHangDTO(maKH, hoten, sdt, diachi, ngaythamgia);
//                result.add(kh);
//            }
//            JDBCUtil.closeConnection(con);
//        } catch (Exception e) {
//            System.out.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
//        }
//        return result;
//    }
    @Override
    public ArrayList<KhachHangDTO> selectAll() {
        ArrayList<KhachHangDTO> result = new ArrayList<>();
        Connection con = null;
        try {
            con = JDBCUtil.getConnection();
            System.out.println("Kết nối database thành công");

            String sql = "SELECT * FROM KhachHang WHERE trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            System.out.println("Đang thực thi query: " + sql);

            while (rs.next()) {
                int maKH = rs.getInt("maKH");
                String hoten = rs.getString("hoten");
                String diachi = rs.getString("diachi");
                String sdt = rs.getString("sdt");
                Date ngaythamgia = rs.getDate("ngaythamgia");

                System.out.println("Đọc dữ liệu: " + maKH + " - " + hoten);

                KhachHangDTO kh = new KhachHangDTO(maKH, hoten, sdt, diachi, ngaythamgia);
                result.add(kh);
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong selectAll: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(con);
        }

        System.out.println("Tổng số bản ghi trả về: " + result.size());
        return result;
    }

    @Override
    public KhachHangDTO selectById(String t) {
        KhachHangDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM KhachHang WHERE maKH=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int maKH = rs.getInt("maKH");
                String hoten = rs.getString("hoten");
                String diachi = rs.getString("diachi");
                String sdt = rs.getString("sdt");
                Date ngaythamgia = rs.getDate("ngaythamgia");
                result = new KhachHangDTO(maKH, hoten, sdt, diachi, ngaythamgia);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy khách hàng theo ID: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // Lấy giá trị tiếp theo từ sequence
            String sql = "SELECT khachhang_seq.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(KhachHangDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
