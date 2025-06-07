package DAO;

import DTO.ChiTietSanPhamDTO;
import config.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChiTietSanPhamDAO implements DAOinterface<ChiTietSanPhamDTO> {
    
    public static ChiTietSanPhamDAO getInstance() {
        return new ChiTietSanPhamDAO();
    }

    @Override
    public int insert(ChiTietSanPhamDTO t) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "INSERT INTO ctsanpham(machitiet, masanpham, maphieunhap, maphieuxuat, tinhtrang) VALUES (?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, t.getMaChiTiet());
            pst.setInt(2, t.getMaSanPham());
            
            if (t.getMaPhieuNhap() != null) {
                pst.setInt(3, t.getMaPhieuNhap());
            } else {
                pst.setNull(3, java.sql.Types.INTEGER);
            }
            
            if (t.getMaPhieuXuat() != null) {
                pst.setInt(4, t.getMaPhieuXuat());
            } else {
                pst.setNull(4, java.sql.Types.INTEGER);
            }
            
            pst.setInt(5, t.getTinhTrang());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public int insertMultiple(ArrayList<ChiTietSanPhamDTO> list) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            con.setAutoCommit(false);
            String sql = "INSERT INTO ctsanpham(machitiet, masanpham, maphieunhap, maphieuxuat, tinhtrang) VALUES (?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            
            for (ChiTietSanPhamDTO t : list) {
                pst.setString(1, t.getMaChiTiet());
                pst.setInt(2, t.getMaSanPham());
                
                if (t.getMaPhieuNhap() != null) {
                    pst.setInt(3, t.getMaPhieuNhap());
                } else {
                    pst.setNull(3, java.sql.Types.INTEGER);
                }
                
                if (t.getMaPhieuXuat() != null) {
                    pst.setInt(4, t.getMaPhieuXuat());
                } else {
                    pst.setNull(4, java.sql.Types.INTEGER);
                }
                
                pst.setInt(5, t.getTinhTrang());
                pst.addBatch();
            }
            
            int[] batchResults = pst.executeBatch();
            con.commit();
            result = batchResults.length;
        } catch (SQLException ex) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    JDBCUtil.closeConnection(con);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int update(ChiTietSanPhamDTO t) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "UPDATE ctsanpham SET masanpham=?, maphieunhap=?, maphieuxuat=?, tinhtrang=? WHERE machitiet=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMaSanPham());
            
            if (t.getMaPhieuNhap() != null) {
                pst.setInt(2, t.getMaPhieuNhap());
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }
            
            if (t.getMaPhieuXuat() != null) {
                pst.setInt(3, t.getMaPhieuXuat());
            } else {
                pst.setNull(3, java.sql.Types.INTEGER);
            }
            
            pst.setInt(4, t.getTinhTrang());
            pst.setString(5, t.getMaChiTiet());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public int updateXuat(ChiTietSanPhamDTO t) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "UPDATE ctsanpham SET maphieuxuat=?, tinhtrang=? WHERE machitiet=?";
            pst = con.prepareStatement(sql);
            
            if (t.getMaPhieuXuat() != null) {
                pst.setInt(1, t.getMaPhieuXuat());
            } else {
                pst.setNull(1, java.sql.Types.INTEGER);
            }
            
            pst.setInt(2, t.getTinhTrang());
            pst.setString(3, t.getMaChiTiet());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public int reset(ChiTietSanPhamDTO t) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "UPDATE ctsanpham SET maphieuxuat=NULL, tinhtrang=1 WHERE machitiet=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, t.getMaChiTiet());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            String sql = "UPDATE ctsanpham SET tinhtrang=0 WHERE machitiet=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public int deleteByPhieuNhap(int maphieunhap) {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "DELETE FROM ctsanpham WHERE maphieunhap=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, maphieunhap);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public ArrayList<ChiTietSanPhamDTO> selectAll() {
        ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ctsanpham";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                String machitiet = rs.getString("machitiet");
                int masanpham = rs.getInt("masanpham");
                int maphieunhap = rs.getInt("maphieunhap");
                int maphieuxuat = rs.getInt("maphieuxuat");
                int tinhtrang = rs.getInt("tinhtrang");
                
                ChiTietSanPhamDTO ct = new ChiTietSanPhamDTO(
                    machitiet, 
                    masanpham, 
                    rs.wasNull() ? null : maphieunhap, 
                    rs.wasNull() ? null : maphieuxuat, 
                    tinhtrang
                );
                result.add(ct);
            }
        } catch (SQLException e) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public ArrayList<ChiTietSanPhamDTO> selectByMaSanPham(int masanpham) {
        ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ctsanpham WHERE masanpham=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, masanpham);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                String machitiet = rs.getString("machitiet");
                int masp = rs.getInt("masanpham");
                int maphieunhap = rs.getInt("maphieunhap");
                int maphieuxuat = rs.getInt("maphieuxuat");
                int tinhtrang = rs.getInt("tinhtrang");
                
                ChiTietSanPhamDTO ct = new ChiTietSanPhamDTO(
                    machitiet, 
                    masp, 
                    rs.wasNull() ? null : maphieunhap, 
                    rs.wasNull() ? null : maphieuxuat, 
                    tinhtrang
                );
                result.add(ct);
            }
        } catch (SQLException e) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public ArrayList<ChiTietSanPhamDTO> selectByMaSanPhamAndStatus(int masanpham, int tinhtrang) {
        ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ctsanpham WHERE masanpham=? AND tinhtrang=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, masanpham);
            pst.setInt(2, tinhtrang);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                String machitiet = rs.getString("machitiet");
                int masp = rs.getInt("masanpham");
                int maphieunhap = rs.getInt("maphieunhap");
                int maphieuxuat = rs.getInt("maphieuxuat");
                int tt = rs.getInt("tinhtrang");
                
                ChiTietSanPhamDTO ct = new ChiTietSanPhamDTO(
                    machitiet, 
                    masp, 
                    rs.wasNull() ? null : maphieunhap, 
                    rs.wasNull() ? null : maphieuxuat, 
                    tt
                );
                result.add(ct);
            }
        } catch (SQLException e) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public ChiTietSanPhamDTO selectById(String machitiet) {
        ChiTietSanPhamDTO result = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ctsanpham WHERE machitiet=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, machitiet);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                String ma = rs.getString("machitiet");
                int masanpham = rs.getInt("masanpham");
                int maphieunhap = rs.getInt("maphieunhap");
                int maphieuxuat = rs.getInt("maphieuxuat");
                int tinhtrang = rs.getInt("tinhtrang");
                
                result = new ChiTietSanPhamDTO(
                    ma, 
                    masanpham, 
                    rs.wasNull() ? null : maphieunhap, 
                    rs.wasNull() ? null : maphieuxuat, 
                    tinhtrang
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) JDBCUtil.closeConnection(con);
            } catch (SQLException ex) {
                Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ArrayList<ChiTietSanPhamDTO> selectByMaPhieuNhap(int maphieunhap) {
    ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    try {
        con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM ctsanpham WHERE maphieunhap=?";
        pst = con.prepareStatement(sql);
        pst.setInt(1, maphieunhap);
        rs = pst.executeQuery();
        
        while (rs.next()) {
            String machitiet = rs.getString("machitiet");
            int masanpham = rs.getInt("masanpham");
            int mapn = rs.getInt("maphieunhap");
            int maphieuxuat = rs.getInt("maphieuxuat");
            int tinhtrang = rs.getInt("tinhtrang");
            
            ChiTietSanPhamDTO ct = new ChiTietSanPhamDTO(
                machitiet, 
                masanpham, 
                rs.wasNull() ? null : mapn, 
                rs.wasNull() ? null : maphieuxuat, 
                tinhtrang
            );
            result.add(ct);
        }
    } catch (SQLException e) {
        Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (con != null) JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return result;
}

public ArrayList<ChiTietSanPhamDTO> selectByMaPhieuXuat(int maphieuxuat) {
    ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    try {
        con = JDBCUtil.getConnection();
        String sql = "SELECT * FROM ctsanpham WHERE maphieuxuat=?";
        pst = con.prepareStatement(sql);
        pst.setInt(1, maphieuxuat);
        rs = pst.executeQuery();
        
        while (rs.next()) {
            String machitiet = rs.getString("machitiet");
            int masanpham = rs.getInt("masanpham");
            int maphieunhap = rs.getInt("maphieunhap");
            int mapx = rs.getInt("maphieuxuat");
            int tinhtrang = rs.getInt("tinhtrang");
            
            ChiTietSanPhamDTO ct = new ChiTietSanPhamDTO(
                machitiet, 
                masanpham, 
                rs.wasNull() ? null : maphieunhap, 
                rs.wasNull() ? null : mapx, 
                tinhtrang
            );
            result.add(ct);
        }
    } catch (SQLException e) {
        Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (con != null) JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietSanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return result;
}
}