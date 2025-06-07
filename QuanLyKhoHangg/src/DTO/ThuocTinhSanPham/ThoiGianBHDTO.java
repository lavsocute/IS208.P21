package DTO.ThuocTinhSanPham;

public class ThoiGianBHDTO {
    private int mathoigianbh;
    private String tenthgianbh;
    
    public ThoiGianBHDTO() {
        
    }

    public ThoiGianBHDTO(int mathoigianbh, String tenthgianbh) {
        this.mathoigianbh = mathoigianbh;
        this.tenthgianbh = tenthgianbh;
    }

    public int getMathoigianbh() {
        return mathoigianbh;
    }

    public void setMathoigianbh(int mathoigianbh) {
        this.mathoigianbh = mathoigianbh;
    }

    public String getTenthgianbh() {
        return tenthgianbh;
    }

    public void setTenthgianbh(String tenthgianbh) {
        this.tenthgianbh = tenthgianbh;
    }

    @Override
    public String toString() {
        return "ThoiGianBHDTO{" + "mathoigianbh=" + mathoigianbh + ", tenthgianbh=" + tenthgianbh + '}';
    }
}