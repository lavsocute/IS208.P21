package BUS;

import DAO.ChiTietSanPhamDAO;
import DTO.ChiTietSanPhamDTO;
import DTO.SanPhamDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class ChiTietSanPhamBUS {

    private final ChiTietSanPhamDAO ctspDAO = new ChiTietSanPhamDAO();
    public SanPhamBUS spbus = new SanPhamBUS();
    public ArrayList<SanPhamDTO> listsp;
    public ArrayList<ChiTietSanPhamDTO> listctsp = new ArrayList<>();

    public ChiTietSanPhamBUS() {

    }

    public ArrayList<ChiTietSanPhamDTO> getAllByMaSP(int masp) {
        listctsp = ctspDAO.selectByMaSanPham(masp);
        return listctsp;
    }

    public ArrayList<ChiTietSanPhamDTO> getAll() {
        return this.listctsp;
    }

    public ChiTietSanPhamDTO getByIndex(int index) {
        return this.listctsp.get(index);
    }

    public ArrayList<ChiTietSanPhamDTO> getAllCTSPbyMasp(int masp) {
        ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
        ArrayList<ChiTietSanPhamDTO> list = ctspDAO.selectByMaSanPham(masp);
        result.addAll(list);
        return result;
    }

    public HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> getChiTietSanPhamFromMaPN(int maphieunhap) {
        ArrayList<ChiTietSanPhamDTO> chitietsp = ctspDAO.selectByMaPhieuNhap(maphieunhap);
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> result = new HashMap<>();
        
        for (ChiTietSanPhamDTO i : chitietsp) {
            int maSP = i.getMaSanPham();
            SanPhamDTO sp = spbus.getByMaSP(maSP);
            
            // Kiểm tra null trước khi sử dụng
            if (sp != null) {
                if (!result.containsKey(maSP)) {
                    result.put(maSP, new ArrayList<>());
                }
                result.get(maSP).add(i);
            }
        }
        return result;
    }
    public HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> getChiTietSanPhamFromMaPX(int maphieuxuat) {
        ArrayList<ChiTietSanPhamDTO> chitietsp = ctspDAO.selectByMaPhieuXuat(maphieuxuat);
        HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> result = new HashMap<>();
        for (ChiTietSanPhamDTO i : chitietsp) {
            if (result.get(i.getMaSanPham()) == null) {
                result.put(i.getMaSanPham(), new ArrayList<>());
            }
        }
        for (ChiTietSanPhamDTO i : chitietsp) {
            result.get(i.getMaSanPham()).add(i);
        }
        return result;
    }

    public void Show(ArrayList<ChiTietSanPhamDTO> x) {
        for (ChiTietSanPhamDTO a : x) {
            System.out.println(a.getMaChiTiet());
        }
    }

    public boolean updateXuat(ArrayList<ChiTietSanPhamDTO> list) {
    // Tạo map để lưu số lượng cần giảm theo từng mã sản phẩm
    HashMap<Integer, Integer> quantityToReduce = new HashMap<>();
    
    // Cập nhật trạng thái từng chi tiết sản phẩm và tính số lượng giảm
    for (ChiTietSanPhamDTO ct : list) {
        // 1. Đánh dấu chi tiết sản phẩm đã xuất
        ct.setTinhTrang(0);
        
        // 2. Cập nhật vào database
        if (ctspDAO.updateXuat(ct) == 0) {
            return false; // Nếu có lỗi thì dừng ngay
        }
        
        // 3. Tính số lượng cần giảm cho sản phẩm tổng
        int masp = ct.getMaSanPham();
        quantityToReduce.put(masp, quantityToReduce.getOrDefault(masp, 0) + 1);
    }
    
    // Cập nhật số lượng tồn cho từng sản phẩm
    for (Map.Entry<Integer, Integer> entry : quantityToReduce.entrySet()) {
        int masp = entry.getKey();
        int quantity = entry.getValue();
        
        // Lấy thông tin sản phẩm
        SanPhamDTO sp = spbus.getByMaSP(masp);
        if (sp == null) {
            continue; // Bỏ qua nếu không tìm thấy sản phẩm
        }
        
        // Kiểm tra số lượng tồn đủ để giảm
        if (sp.getSoluongton() < quantity) {
            throw new RuntimeException("Số lượng tồn của sản phẩm " + masp + " không đủ");
        }
        
        // Cập nhật số lượng tồn
        sp.setSoluongton(sp.getSoluongton() - quantity);
        if (!spbus.update(sp)) {
            throw new RuntimeException("Lỗi khi cập nhật số lượng tồn cho sản phẩm " + masp);
        }
    }
    
    return true;
}

    public ArrayList<ChiTietSanPhamDTO> selectAllByMaPhieuXuat(int maphieu) {
        return ctspDAO.selectByMaPhieuXuat(maphieu);
    }

    public ArrayList<ChiTietSanPhamDTO> FilterPBvaTT(String text, int masp, int tinhtrang) {
    ArrayList<ChiTietSanPhamDTO> list = this.getAllCTSPbyMasp(masp);
    ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
    for (ChiTietSanPhamDTO i : list) {
        if (i.getMaSanPham() == masp && i.getTinhTrang() == tinhtrang && 
            (i.getMaChiTiet().toLowerCase().contains(text.toLowerCase()) || 
             (i.getMaPhieuNhap() != null && i.getMaPhieuNhap().toString().contains(text)) ||
             (i.getMaPhieuXuat() != null && i.getMaPhieuXuat().toString().contains(text)))) {
            result.add(i);
        }
    }
    return result;
}

public ArrayList<ChiTietSanPhamDTO> FilterPBvaAll(String text, int masp) {
    ArrayList<ChiTietSanPhamDTO> list = this.getAllCTSPbyMasp(masp);
    ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
    for (ChiTietSanPhamDTO i : list) {
        if (i.getMaSanPham() == masp && 
            (i.getMaChiTiet().toLowerCase().contains(text.toLowerCase()) || 
             (i.getMaPhieuNhap() != null && i.getMaPhieuNhap().toString().contains(text)) ||
             (i.getMaPhieuXuat() != null && i.getMaPhieuXuat().toString().contains(text)))) {
            result.add(i);
        }
    }
    return result;
}
}