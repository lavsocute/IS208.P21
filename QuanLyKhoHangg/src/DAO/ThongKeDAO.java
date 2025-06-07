package DAO;

import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTonKhoDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import DTO.ThongKe.ThongKeDoanhThuDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;

public class ThongKeDAO {

    public static ThongKeDAO getInstance() {
        return new ThongKeDAO();
    }

    // Phương thức thống kê tồn kho (giữ nguyên vì không liên quan đến doanh thu/chi phí)
    public static HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> getThongKeTonKho(String text, Date timeStart, Date timeEnd) {
        HashMap<Integer, ArrayList<ThongKeTonKhoDTO>> result = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeEnd.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
                         WITH nhap AS (
                           SELECT sanpham.masp, SUM(ctphieunhap.soluong) AS sl_nhap
                           FROM ctphieunhap
                           JOIN phieunhap ON phieunhap.maphieunhap = ctphieunhap.maphieunhap
                           JOIN sanpham ON ctphieunhap.masanpham = sanpham.masp
                           WHERE phieunhap.thoigian BETWEEN ? AND ?
                           GROUP BY sanpham.masp
                         ),
                         xuat AS (
                           SELECT sanpham.masp, SUM(ctphieuxuat.soluong) AS sl_xuat
                           FROM ctphieuxuat
                           JOIN phieuxuat ON phieuxuat.maphieuxuat = ctphieuxuat.maphieuxuat
                           JOIN sanpham ON ctphieuxuat.masanpham = sanpham.masp
                           WHERE phieuxuat.thoigian BETWEEN ? AND ?
                           GROUP BY sanpham.masp
                         ),
                         nhap_dau AS (
                           SELECT sanpham.masp, SUM(ctphieunhap.soluong) AS sl_nhap_dau
                           FROM phieunhap
                           JOIN ctphieunhap ON phieunhap.maphieunhap = ctphieunhap.maphieunhap
                           JOIN sanpham ON ctphieunhap.masanpham = sanpham.masp
                           WHERE phieunhap.thoigian < ?
                           GROUP BY sanpham.masp
                         ),
                         xuat_dau AS (
                           SELECT sanpham.masp, SUM(ctphieuxuat.soluong) AS sl_xuat_dau
                           FROM phieuxuat
                           JOIN ctphieuxuat ON phieuxuat.maphieuxuat = ctphieuxuat.maphieuxuat
                           JOIN sanpham ON ctphieuxuat.masanpham = sanpham.masp
                           WHERE phieuxuat.thoigian < ?
                           GROUP BY sanpham.masp
                         ),
                         dau_ky AS (
                           SELECT
                             sanpham.masp,
                             NVL(nhap_dau.sl_nhap_dau, 0) - NVL(xuat_dau.sl_xuat_dau, 0) AS soluongdauky
                           FROM sanpham
                           LEFT JOIN nhap_dau ON sanpham.masp = nhap_dau.masp
                           LEFT JOIN xuat_dau ON sanpham.masp = xuat_dau.masp
                         ),
                         temp_table AS (
                           SELECT 
                             sanpham.masp, 
                             sanpham.tensp, 
                             dau_ky.soluongdauky, 
                             NVL(nhap.sl_nhap, 0) AS soluongnhap, 
                             NVL(xuat.sl_xuat, 0) AS soluongxuat, 
                             (dau_ky.soluongdauky + NVL(nhap.sl_nhap, 0) - NVL(xuat.sl_xuat, 0)) AS soluongcuoiky
                           FROM dau_ky
                           LEFT JOIN nhap ON dau_ky.masp = nhap.masp
                           LEFT JOIN xuat ON dau_ky.masp = xuat.masp
                           JOIN sanpham ON sanpham.masp = dau_ky.masp
                         )
                         SELECT * FROM temp_table
                         WHERE tensp LIKE ? OR masp LIKE ?
                         ORDER BY masp""";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setTimestamp(1, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));
            pst.setTimestamp(3, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(4, new Timestamp(calendar.getTimeInMillis()));
            pst.setTimestamp(5, new Timestamp(timeStart.getTime()));
            pst.setTimestamp(6, new Timestamp(timeStart.getTime()));
            pst.setString(7, "%" + text + "%");
            pst.setString(8, "%" + text + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int masp = rs.getInt("masp");
                String tensp = rs.getString("tensp");
                int soluongdauky = rs.getInt("soluongdauky");
                int soluongnhap = rs.getInt("soluongnhap");
                int soluongxuat = rs.getInt("soluongxuat");
                int soluongcuoiky = rs.getInt("soluongcuoiky");
                
                ThongKeTonKhoDTO p = new ThongKeTonKhoDTO(
                    masp, 
                    tensp, 
                    soluongdauky, 
                    soluongnhap, 
                    soluongxuat, 
                    soluongcuoiky
                );
                result.computeIfAbsent(masp, k -> new ArrayList<>()).add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoTungNam(int year_start, int year_end) {
        ArrayList<ThongKeDoanhThuDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            
            String sql = 
                "WITH years AS (" +
                "    SELECT ? + LEVEL - 1 AS year " +
                "    FROM DUAL " +
                "    CONNECT BY LEVEL <= ? - ? + 1" +
                ") " +
                "SELECT " +
                "    years.year AS nam, " +
                "    NVL((SELECT SUM(tongtien) FROM phieunhap WHERE EXTRACT(YEAR FROM thoigian) = years.year), 0) AS chiphi, " +
                "    NVL((SELECT SUM(tongtien) FROM phieuxuat WHERE EXTRACT(YEAR FROM thoigian) = years.year), 0) AS doanhthu " +
                "FROM years " +
                "ORDER BY years.year";

            PreparedStatement pst = con.prepareStatement(sql);
            
            pst.setInt(1, year_start);
            pst.setInt(2, year_end);
            pst.setInt(3, year_start);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int thoigian = rs.getInt("nam");
                Long chiphi = rs.getLong("chiphi");
                Long doanhthu = rs.getLong("doanhthu");
                ThongKeDoanhThuDTO x = new ThongKeDoanhThuDTO(thoigian, chiphi, doanhthu, doanhthu - chiphi);
                result.add(x);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTheoThangDTO> getThongKeTheoThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT months.month AS thang, \n" +
                         "       NVL((SELECT SUM(tongtien) FROM phieunhap WHERE EXTRACT(MONTH FROM thoigian) = months.month AND EXTRACT(YEAR FROM thoigian) = ?), 0) AS chiphi,\n" +
                         "       NVL((SELECT SUM(tongtien) FROM phieuxuat WHERE EXTRACT(MONTH FROM thoigian) = months.month AND EXTRACT(YEAR FROM thoigian) = ?), 0) AS doanhthu\n" +
                         "FROM (\n" +
                         "       SELECT 1 AS month FROM dual\n" +
                         "       UNION ALL SELECT 2 FROM dual\n" +
                         "       UNION ALL SELECT 3 FROM dual\n" +
                         "       UNION ALL SELECT 4 FROM dual\n" +
                         "       UNION ALL SELECT 5 FROM dual\n" +
                         "       UNION ALL SELECT 6 FROM dual\n" +
                         "       UNION ALL SELECT 7 FROM dual\n" +
                         "       UNION ALL SELECT 8 FROM dual\n" +
                         "       UNION ALL SELECT 9 FROM dual\n" +
                         "       UNION ALL SELECT 10 FROM dual\n" +
                         "       UNION ALL SELECT 11 FROM dual\n" +
                         "       UNION ALL SELECT 12 FROM dual\n" +
                         "     ) months\n" +
                         "ORDER BY months.month";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, nam);
            pst.setInt(2, nam);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("thang");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTheoThangDTO thongke = new ThongKeTheoThangDTO(thang, chiphi, doanhthu, loinhuan);
                result.add(thongke);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTungNgayTrongThang(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT \n" +
                         "  days.day AS ngay, \n" +
                         "  NVL((SELECT SUM(tongtien) FROM phieunhap WHERE TRUNC(thoigian) = days.day), 0) AS chiphi, \n" +
                         "  NVL((SELECT SUM(tongtien) FROM phieuxuat WHERE TRUNC(thoigian) = days.day), 0) AS doanhthu\n" +
                         "FROM (\n" +
                         "  SELECT TO_DATE(?, 'YYYY-MM-DD') + LEVEL - 1 AS day\n" +
                         "  FROM dual\n" +
                         "  CONNECT BY LEVEL <= TO_NUMBER(TO_CHAR(LAST_DAY(TO_DATE(?, 'YYYY-MM-DD')), 'DD'))\n" +
                         ") days\n" +
                         "ORDER BY days.day";
            String dateStr = nam + "-" + thang + "-01";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, dateStr);
            pst.setString(2, dateStr);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
                       WITH dates AS (
                         SELECT TRUNC(SYSDATE) - 7 + LEVEL - 1 AS date_value
                         FROM dual
                         CONNECT BY LEVEL <= 7
                       )
                       SELECT
                         dates.date_value AS ngay,
                         NVL((SELECT SUM(tongtien) FROM phieuxuat WHERE TRUNC(thoigian) = dates.date_value), 0) AS doanhthu,
                         NVL((SELECT SUM(tongtien) FROM phieunhap WHERE TRUNC(thoigian) = dates.date_value), 0) AS chiphi
                       FROM dates
                       ORDER BY dates.date_value""";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeTuNgayDenNgay(String star, String end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = """
                       SELECT 
                         day_range.day_date AS ngay,
                         NVL((SELECT SUM(tongtien) FROM phieunhap WHERE TRUNC(thoigian) = day_range.day_date), 0) AS chiphi,
                         NVL((SELECT SUM(tongtien) FROM phieuxuat WHERE TRUNC(thoigian) = day_range.day_date), 0) AS doanhthu
                       FROM (
                         SELECT TO_DATE(?, 'YYYY-MM-DD') + LEVEL - 1 AS day_date
                         FROM dual
                         CONNECT BY LEVEL <= TO_DATE(?, 'YYYY-MM-DD') - TO_DATE(?, 'YYYY-MM-DD') + 1
                       ) day_range
                       ORDER BY day_range.day_date""";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, star);
            pst.setString(2, end);
            pst.setString(3, star);
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Date ngay = rs.getDate("ngay");
                int chiphi = rs.getInt("chiphi");
                int doanhthu = rs.getInt("doanhthu");
                int loinhuan = doanhthu - chiphi;
                ThongKeTungNgayTrongThangDTO tn = new ThongKeTungNgayTrongThangDTO(ngay, chiphi, doanhthu, loinhuan);
                result.add(tn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}