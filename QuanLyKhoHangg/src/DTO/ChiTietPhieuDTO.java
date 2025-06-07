package DTO;

import java.util.Objects;


public class ChiTietPhieuDTO {
    private int maPhieu;           // Mã phiếu (nhập/xuất)
    private int maSanPham;         // Mã sản phẩm (tham chiếu tới SanPhamDTO)
    private int soLuong;           // Số lượng sản phẩm
    private double donGia;         // Đơn giá (nên dùng double để lưu giá)
    public ChiTietPhieuDTO() {
    }

    public ChiTietPhieuDTO(int maPhieu, int maSanPham, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getter và Setter
    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    // Tính thành tiền
    public double getThanhTien() {
        return soLuong * donGia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhieu, maSanPham);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChiTietPhieuDTO that = (ChiTietPhieuDTO) obj;
        return maPhieu == that.maPhieu && 
               maSanPham == that.maSanPham;
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDTO{" +
                "maPhieu=" + maPhieu +
                ", maSanPham=" + maSanPham +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                '}';
    }
}