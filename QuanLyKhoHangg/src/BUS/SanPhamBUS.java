package BUS;

import DAO.SanPhamDAO;
import DTO.SanPhamDTO;
import DTO.ChiTietSanPhamDTO;
import DAO.ThuongHieuDAO;
import DAO.LoaiSanPhamDAO;
import DAO.XuatXuDAO;
import DAO.KhuVucKhoDAO;
import DTO.ChiTietSanPhamDTO;
import java.util.ArrayList;

public class SanPhamBUS {

    private final SanPhamDAO spDAO = new SanPhamDAO();
    private ArrayList<SanPhamDTO> listSP = new ArrayList<>();

    public SanPhamBUS() {
        listSP = spDAO.selectAll();
    }

    public ArrayList<SanPhamDTO> getAll() {
        return this.listSP;
    }

    public SanPhamDTO getByIndex(int index) {
        return this.listSP.get(index);
    }

    public SanPhamDTO getByMaSP(int masp) {
        for (SanPhamDTO sp : listSP) {
            if (sp.getMasp() == masp) {
                return sp;
            }
        }
        return null;
    }

    public int getIndexByMaSP(int masanpham) {
        for (int i = 0; i < listSP.size(); i++) {
            if (listSP.get(i).getMasp() == masanpham) {
                return i;
            }
        }
        return -1;
    }

    public Boolean add(SanPhamDTO sp) {
        boolean check = spDAO.insert(sp) != 0;
        if (check) {
            this.listSP.add(sp);
        }
        return check;
    }

    public Boolean delete(SanPhamDTO sp) {
        boolean check = spDAO.delete(Integer.toString(sp.getMasp())) != 0;
        if (check) {
            this.listSP.remove(sp);
        }
        return check;
    }

    public Boolean update(SanPhamDTO sp) {
        boolean check = spDAO.update(sp) != 0;
        if (check) {
            this.listSP.set(getIndexByMaSP(sp.getMasp()), sp);
        }
        return check;
    }

    public ArrayList<SanPhamDTO> getByMaKhuVuc(int makv) {
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        for (SanPhamDTO sp : this.listSP) {
            if (sp.getKhuvuckho() == makv) {
                result.add(sp);
            }
        }
        return result;
    }

    public ArrayList<SanPhamDTO> search(String text, String type) {
        text = text.toLowerCase();
        ArrayList<SanPhamDTO> result = new ArrayList<>();
        
        for (SanPhamDTO sp : this.listSP) {
            switch (type) {
                case "Tất cả":
                    if (Integer.toString(sp.getMasp()).toLowerCase().contains(text) ||
                        sp.getTensp().toLowerCase().contains(text) ||
                        getThuongHieuName(sp.getThuonghieu()).toLowerCase().contains(text) ||
                        getLoaiSanPhamName(sp.getMaloai()).toLowerCase().contains(text) ||
                        getXuatXuName(sp.getXuatxu()).toLowerCase().contains(text) ||
                        getKhuVucKhoName(sp.getKhuvuckho()).toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
                case "Mã SP":
                    if (Integer.toString(sp.getMasp()).toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
                case "Tên sản phẩm":
                    if (sp.getTensp().toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
                case "Thương hiệu":
                    if (getThuongHieuName(sp.getThuonghieu()).toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
                case "Loại sản phẩm":
                    if (getLoaiSanPhamName(sp.getMaloai()).toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
                case "Xuất xứ":
                    if (getXuatXuName(sp.getXuatxu()).toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
                case "Khu vực kho":
                    if (getKhuVucKhoName(sp.getKhuvuckho()).toLowerCase().contains(text)) {
                        result.add(sp);
                    }
                    break;
            }
        }
        return result;
    }

    // Các phương thức helper để lấy tên từ ID
    private String getThuongHieuName(Integer mathuonghieu) {
        if (mathuonghieu == null) return "";
        return ThuongHieuDAO.getInstance().selectById(mathuonghieu.toString()).getTenthuonghieu();
    }

    private String getLoaiSanPhamName(Integer maloai) {
        if (maloai == null) return "";
        return LoaiSanPhamDAO.getInstance().selectById(maloai.toString()).getTenloai();
    }

    private String getXuatXuName(Integer maxuatxu) {
        if (maxuatxu == null) return "";
        return XuatXuDAO.getInstance().selectById(maxuatxu.toString()).getTenxuatxu();
    }

    private String getKhuVucKhoName(Integer makhuvuc) {
        if (makhuvuc == null) return "";
        return KhuVucKhoDAO.getInstance().selectById(makhuvuc.toString()).getTenkhuvuc();
    }
    public int getAutoIncrement() {
    return spDAO.getAutoIncrement();
}
    public int getTotalQuantity() {
        int total = 0;
        for (SanPhamDTO sp : this.listSP) {
            total += sp.getSoluongton();
        }
        return total;
    }
    public boolean isMaChiTietExists(ArrayList<ChiTietSanPhamDTO> arr) {
    return spDAO.isMaChiTietExists(arr);
}
    public int countChiTietSanPhamByMaSP(int masp) {
    return spDAO.countChiTietSanPhamByMaSP(masp);
}
}