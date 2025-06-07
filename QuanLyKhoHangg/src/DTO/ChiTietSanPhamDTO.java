package DTO;

import java.util.Objects;

/**
 *
 * @author Tran Nhat Sinh
 */
public class ChiTietSanPhamDTO {
    private String maChiTiet;       // Mã chi tiết sản phẩm (thay thế IMEI)
    private int maSanPham;          // Mã sản phẩm (tham chiếu tới SanPhamDTO)
    private Integer maPhieuNhap;    // Mã phiếu nhập (null nếu chưa nhập)
    private Integer maPhieuXuat;    // Mã phiếu xuất (null nếu chưa xuất)
    private int tinhTrang;          // 0: hỏng, 1: trong kho, 2: đã bán...

    public ChiTietSanPhamDTO() {
    }

    public ChiTietSanPhamDTO(String maChiTiet, int maSanPham, Integer maPhieuNhap, 
                           Integer maPhieuXuat, int tinhTrang) {
        this.maChiTiet = maChiTiet;
        this.maSanPham = maSanPham;
        this.maPhieuNhap = maPhieuNhap;
        this.maPhieuXuat = maPhieuXuat;
        this.tinhTrang = tinhTrang;
    }

    // Getter và Setter
    public String getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(String maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public Integer getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(Integer maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public Integer getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(Integer maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
    @Override
    public int hashCode() {
        return Objects.hash(maChiTiet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChiTietSanPhamDTO that = (ChiTietSanPhamDTO) obj;
        return Objects.equals(maChiTiet, that.maChiTiet);
    }

    @Override
    public String toString() {
        return "ChiTietSanPhamDTO{" +
                "maChiTiet='" + maChiTiet + '\'' +
                ", maSanPham=" + maSanPham +
                ", maPhieuNhap=" + maPhieuNhap +
                ", maPhieuXuat=" + maPhieuXuat +
                ", tinhTrang=" + tinhTrang +
                '}';
    }
}