package DTO;

import java.util.Objects;

/**
 * Lớp DTO đại diện cho thông tin loại sản phẩm
 */
public class LoaiSanPhamDTO {
    private int maloai;         // Mã loại sản phẩm (PK)
    private String tenloai;     // Tên loại sản phẩm (điện thoại, laptop, đồ gia dụng,...)
    private String mota;        // Mô tả về loại sản phẩm
    private boolean trangthai;  // Trạng thái hoạt động (true - đang kinh doanh, false - ngừng kinh doanh)

    // Constructors
    public LoaiSanPhamDTO() {
    }

    public LoaiSanPhamDTO(int maloai, String tenloai, String mota, boolean trangthai) {
        this.maloai = maloai;
        this.tenloai = tenloai;
        this.mota = mota;
        this.trangthai = trangthai;
    }

    // Getters & Setters
    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }

    // Override equals(), hashCode(), toString()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoaiSanPhamDTO that = (LoaiSanPhamDTO) obj;
        return maloai == that.maloai;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maloai);
    }

    @Override
    public String toString() {
        return "LoaiSanPhamDTO{" +
                "maloai=" + maloai +
                ", tenloai='" + tenloai + '\'' +
                ", mota='" + mota + '\'' +
                ", trangthai=" + trangthai +
                '}';
    }
}