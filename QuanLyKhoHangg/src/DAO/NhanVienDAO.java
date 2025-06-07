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
import java.sql.Statement;
import DTO.NhanVienDTO;

public class NhanVienDAO implements DAOinterface<NhanVienDTO> {
    public static NhanVienDAO getInstance() {
        return new NhanVienDAO();
    }

    @Override
public int insert(NhanVienDTO t) {
    int result = 0;
    try {
        Connection con = JDBCUtil.getConnection();

        // First get the next available ID
        int nextId = getNextAvailableId(con);

        String sql = "INSERT INTO nhanvien(manv, hoten, gioitinh, sdt, ngaysinh, trangthai, email) " +
                     "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, nextId);
        pst.setString(2, t.getHoten());
        pst.setInt(3, t.getGioitinh());
        pst.setString(4, t.getSdt());
        pst.setDate(5, new java.sql.Date(t.getNgaysinh().getTime()));
        pst.setInt(6, t.getTrangthai());
        pst.setString(7, t.getEmail());

        result = pst.executeUpdate();
        JDBCUtil.closeConnection(con);
    } catch (SQLException ex) {
        Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
}

private int getNextAvailableId(Connection con) throws SQLException {
    // Get current max ID
    String getMaxSql = "SELECT NVL(MAX(manv), 0) FROM nhanvien";
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(getMaxSql);
    int maxManv = 0;
    if (rs.next()) {
        maxManv = rs.getInt(1);
    }
    return maxManv + 1;
}

    private void dongBoSequence(Connection con) throws SQLException {
    // 1. Lấy MAX(manv) hiện tại
    String getMaxSql = "SELECT NVL(MAX(manv), 0) FROM nhanvien";
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(getMaxSql);
    int maxManv = 0;
    if (rs.next()) {
        maxManv = rs.getInt(1);
    }

    // 2. Lấy giá trị hiện tại của sequence (without incrementing)
    String seqValSql = "SELECT nhanvien_seq.CURRVAL FROM dual";
    try {
        rs = stmt.executeQuery(seqValSql);
        int seqVal = 0;
        if (rs.next()) {
            seqVal = rs.getInt(1);
        }

        // 3. Nếu sequence < max(manv), thì điều chỉnh sequence
        if (seqVal <= maxManv) {
            // Calculate how much we need to increment
            int incrementBy = maxManv - seqVal + 1;
            String alterSql = "ALTER SEQUENCE nhanvien_seq INCREMENT BY " + incrementBy;
            stmt.execute(alterSql);
            
            // Get next value (which will now be maxManv + 1)
            stmt.executeQuery("SELECT nhanvien_seq.NEXTVAL FROM dual");
            
            // Reset increment back to 1
            stmt.execute("ALTER SEQUENCE nhanvien_seq INCREMENT BY 1");
        }
    } catch (SQLException e) {
        // If CURRVAL was not yet defined for this session
        if (e.getErrorCode() == 8002) { // ORA-08002
            // Sequence has never been used, no need to sync
        } else {
            throw e;
        }
    }
}
    @Override
    public int update(NhanVienDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE nhanvien SET hoten=?, gioitinh=?, ngaysinh=?, sdt=?, trangthai=?, email=? WHERE manv=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getHoten());
            pst.setInt(2, t.getGioitinh());
            pst.setDate(3, new java.sql.Date(t.getNgaysinh().getTime()));
            pst.setString(4, t.getSdt());
            pst.setInt(5, t.getTrangthai());
            pst.setString(6, t.getEmail());
            pst.setInt(7, t.getManv());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE nhanvien SET trangthai = -1 WHERE manv = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien WHERE trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int manv = rs.getInt("manv");
                String hoten = rs.getString("hoten");
                int gioitinh = rs.getInt("gioitinh");
                Date ngaysinh = rs.getDate("ngaysinh");
                String sdt = rs.getString("sdt");
                int trangthai = rs.getInt("trangthai");
                String email = rs.getString("email");
                NhanVienDTO nv = new NhanVienDTO(manv, hoten, gioitinh, ngaysinh, sdt, trangthai, email);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    public ArrayList<NhanVienDTO> selectAlll() {
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int manv = rs.getInt("manv");
                String hoten = rs.getString("hoten");
                int gioitinh = rs.getInt("gioitinh");
                Date ngaysinh = rs.getDate("ngaysinh");
                String sdt = rs.getString("sdt");
                int trangthai = rs.getInt("trangthai");
                String email = rs.getString("email");
                NhanVienDTO nv = new NhanVienDTO(manv, hoten, gioitinh, ngaysinh, sdt, trangthai, email);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    public ArrayList<NhanVienDTO> selectAllNV() {
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien nv WHERE nv.trangthai = 1 " +
                         "AND NOT EXISTS (SELECT * FROM taikhoan tk WHERE nv.manv=tk.manv)";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                int manv = rs.getInt("manv");
                String hoten = rs.getString("hoten");
                int gioitinh = rs.getInt("gioitinh");
                Date ngaysinh = rs.getDate("ngaysinh");
                String sdt = rs.getString("sdt");
                int trangthai = rs.getInt("trangthai");
                String email = rs.getString("email");
                NhanVienDTO nv = new NhanVienDTO(manv, hoten, gioitinh, ngaysinh, sdt, trangthai, email);
                result.add(nv);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public NhanVienDTO selectById(String t) {
        NhanVienDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien WHERE manv=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                int manv = rs.getInt("manv");
                String hoten = rs.getString("hoten");
                int gioitinh = rs.getInt("gioitinh");
                Date ngaysinh = rs.getDate("ngaysinh");
                String sdt = rs.getString("sdt");
                int trangthai = rs.getInt("trangthai");
                String email = rs.getString("email");
                result = new NhanVienDTO(manv, hoten, gioitinh, ngaysinh, sdt, trangthai, email);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    public NhanVienDTO selectByEmail(String t) {
        NhanVienDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhanvien WHERE email=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                int manv = rs.getInt("manv");
                String hoten = rs.getString("hoten");
                int gioitinh = rs.getInt("gioitinh");
                Date ngaysinh = rs.getDate("ngaysinh");
                String sdt = rs.getString("sdt");
                int trangthai = rs.getInt("trangthai");
                String email = rs.getString("email");
                result = new NhanVienDTO(manv, hoten, gioitinh, ngaysinh, sdt, trangthai, email);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT nhanvien_seq.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhanVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}