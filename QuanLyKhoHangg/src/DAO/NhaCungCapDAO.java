package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import config.JDBCUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import DTO.NhaCungCapDTO;

public class NhaCungCapDAO implements DAOinterface<NhaCungCapDTO> {
    public static NhaCungCapDAO getInstance() {
        return new NhaCungCapDAO();
    }

    @Override
    public int insert(NhaCungCapDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO nhacungcap(manhacungcap, tennhacungcap, diachi, email, sdt, trangthai) VALUES (?,?,?,?,?,1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMancc());
            pst.setString(2, t.getTenncc());
            pst.setString(3, t.getDiachi());
            pst.setString(4, t.getEmail());
            pst.setString(5, t.getSdt());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(NhaCungCapDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE nhacungcap SET tennhacungcap=?, diachi=?, email=?, sdt=? WHERE manhacungcap=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenncc());
            pst.setString(2, t.getDiachi());
            pst.setString(3, t.getEmail());
            pst.setString(4, t.getSdt());
            pst.setInt(5, t.getMancc());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE nhacungcap SET trangthai = 0 WHERE manhacungcap = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<NhaCungCapDTO> selectAll() {
        ArrayList<NhaCungCapDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhacungcap WHERE trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mancc = rs.getInt("manhacungcap");
                String tenncc = rs.getString("tennhacungcap");
                String diachi = rs.getString("diachi");
                String email = rs.getString("email");
                String sdt = rs.getString("sdt");
                NhaCungCapDTO ncc = new NhaCungCapDTO(mancc, tenncc, diachi, email, sdt);
                result.add(ncc);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public NhaCungCapDTO selectById(String t) {
        NhaCungCapDTO result = null;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM nhacungcap WHERE manhacungcap=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int mancc = rs.getInt("manhacungcap");
                String tenncc = rs.getString("tennhacungcap");
                String diachi = rs.getString("diachi");
                String email = rs.getString("email");
                String sdt = rs.getString("sdt");
                
                result = new NhaCungCapDTO(mancc, tenncc, diachi, email, sdt);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            // For Oracle, we use sequences to get the next value
            String sql = "SELECT nhacungcap_seq.NEXTVAL FROM dual";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(NhaCungCapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}