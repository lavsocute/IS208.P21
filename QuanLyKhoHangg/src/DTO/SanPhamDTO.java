package DTO;

import java.util.Objects;

public class SanPhamDTO {
    private int masp;           // Mã sản phẩm
    private String tensp;       // Tên sản phẩm
    private String hinhanh;     // Đường dẫn hình ảnh
    private int maloai;         // Mã loại sản phẩm
    private String mota;        // Mô tả chi tiết
    private int soluongton;     // Số lượng tồn kho
    private double gianhap;     // Giá nhập (mới thêm)
    private double giaban;      // Giá bán
    private Integer thoigianbaohanh; // Mã thời gian bảo hành (có thể null)
    private Integer thuonghieu; // Mã thương hiệu (có thể null)
    private Integer xuatxu;     // Mã xuất xứ (có thể null)
    private Integer donvitinh;  // Mã đơn vị tính (có thể null)
    private Integer khuvuckho;  // Mã khu vực kho (có thể null)
    private int trangthai;      // Trạng thái (0: không hoạt động, 1: hoạt động)

    // Constructors
    public SanPhamDTO() {
    }

    public SanPhamDTO(int masp, String tensp, String hinhanh, int maloai, String mota, 
                     int soluongton, double gianhap, double giaban, Integer thoigianbaohanh, 
                     Integer thuonghieu, Integer xuatxu, Integer donvitinh, 
                     Integer khuvuckho, int trangthai) {
        this.masp = masp;
        this.tensp = tensp;
        this.hinhanh = hinhanh;
        this.maloai = maloai;
        this.mota = mota;
        this.soluongton = soluongton;
        this.gianhap = gianhap;
        this.giaban = giaban;
        this.thoigianbaohanh = thoigianbaohanh;
        this.thuonghieu = thuonghieu;
        this.xuatxu = xuatxu;
        this.donvitinh = donvitinh;
        this.khuvuckho = khuvuckho;
        this.trangthai = trangthai;
    }

    // Getters & Setters
    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getSoluongton() {
        return soluongton;
    }

    public void setSoluongton(int soluongton) {
        this.soluongton = soluongton;
    }

    public double getGianhap() {
        return gianhap;
    }

    public void setGianhap(double gianhap) {
        this.gianhap = gianhap;
    }

    public double getGiaban() {
        return giaban;
    }

    public void setGiaban(double giaban) {
        this.giaban = giaban;
    }

    public Integer getThoigianbaohanh() {
        return thoigianbaohanh;
    }

    public void setThoigianbaohanh(Integer thoigianbaohanh) {
        this.thoigianbaohanh = thoigianbaohanh;
    }

    public Integer getThuonghieu() {
        return thuonghieu;
    }

    public void setThuonghieu(Integer thuonghieu) {
        this.thuonghieu = thuonghieu;
    }

    public Integer getXuatxu() {
        return xuatxu;
    }

    public void setXuatxu(Integer xuatxu) {
        this.xuatxu = xuatxu;
    }

    public Integer getDonvitinh() {
        return donvitinh;
    }

    public void setDonvitinh(Integer donvitinh) {
        this.donvitinh = donvitinh;
    }

    public Integer getKhuvuckho() {
        return khuvuckho;
    }

    public void setKhuvuckho(Integer khuvuckho) {
        this.khuvuckho = khuvuckho;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SanPhamDTO that = (SanPhamDTO) obj;
        return masp == that.masp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(masp);
    }

    @Override
    public String toString() {
        return "SanPhamDTO{" +
                "masp=" + masp +
                ", tensp='" + tensp + '\'' +
                ", hinhanh='" + hinhanh + '\'' +
                ", maloai=" + maloai +
                ", mota='" + mota + '\'' +
                ", soluongton=" + soluongton +
                ", gianhap=" + gianhap +
                ", giaban=" + giaban +
                ", thoigianbaohanh=" + thoigianbaohanh +
                ", thuonghieu=" + thuonghieu +
                ", xuatxu=" + xuatxu +
                ", donvitinh=" + donvitinh +
                ", khuvuckho=" + khuvuckho +
                ", trangthai=" + trangthai +
                '}';
    }
}