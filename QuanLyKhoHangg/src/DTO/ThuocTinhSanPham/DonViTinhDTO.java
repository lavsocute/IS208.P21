package DTO.ThuocTinhSanPham;

public class DonViTinhDTO {
    private int madonvitinh;
    private String tendonvitinh;
    
    public DonViTinhDTO() {
        
    }

    public DonViTinhDTO(int madonvitinh, String tendonvitinh) {
        this.madonvitinh = madonvitinh;
        this.tendonvitinh = tendonvitinh;
    }

    public int getMadonvitinh() {
        return madonvitinh;
    }

    public void setMadonvitinh(int madonvitinh) {
        this.madonvitinh = madonvitinh;
    }

    public String getTendonvitinh() {
        return tendonvitinh;
    }

    public void setTendonvitinh(String tendonvitinh) {
        this.tendonvitinh = tendonvitinh;
    }

    @Override
    public String toString() {
        return "DonViTinhDTO{" + "madonvitinh=" + madonvitinh + ", tendonvitinh=" + tendonvitinh + '}';
    }
}