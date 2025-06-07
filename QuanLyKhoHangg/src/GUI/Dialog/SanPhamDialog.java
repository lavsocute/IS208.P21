package GUI.Dialog;

import BUS.DonViTinhBUS;
import BUS.ThoiGianBHBUS;
import BUS.KhuVucKhoBUS;
import BUS.LoaiSanPhamBUS;
import BUS.ThuongHieuBUS;
import BUS.XuatXuBUS;
import BUS.SanPhamBUS;
import DAO.DonViTinhDAO;
import DAO.KhuVucKhoDAO;
import DAO.LoaiSanPhamDAO;
import DAO.SanPhamDAO;
import DAO.ThoiGianBHDAO;
import DAO.ThuongHieuDAO;
import DAO.XuatXuDAO;
import DTO.SanPhamDTO;

import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Component.InputImage;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.SelectForm;
import GUI.Panel.SanPham;
import helper.Formater;
import helper.Validation;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.PlainDocument;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;



public final class SanPhamDialog extends JDialog implements ActionListener {

    private HeaderTitle titlePage;
    private JPanel pninfosanpham, pnbottom, pnCenter, pninfosanphamright;
    private ButtonCustom btnHuyBo, btnThem, btnSua;
    InputForm tenSP, mota, soluongton, gianhap, giaban;
    SelectForm donvitinh, xuatxu, thoigianbaohanh, loaisanpham;
    SelectForm thuonghieu, khuvuc;
    InputImage hinhanh;
    DefaultTableModel tblModel;
    GUI.Panel.SanPham jpSP;
    
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);    // SteelBlue
private static final Color HOVER_COLOR = new Color(100, 150, 200);
private static final Color SELECTED_COLOR = new Color(50, 100, 150);
private static final Color BACKGROUND_COLOR = new Color(240, 245, 250);
private static final Color BORDER_COLOR = new Color(200, 210, 220);
private static final Color TEXT_COLOR = new Color(30, 30, 30);
private static final Color SECONDARY_TEXT_COLOR = new Color(100, 110, 120);
    
    
    public SanPhamBUS spBUS = new SanPhamBUS();
    public ArrayList<DTO.SanPhamDTO> listSP = spBUS.getAll();

    KhuVucKhoBUS kvkhoBus = new KhuVucKhoBUS();
    ThuongHieuBUS thuonghieuBus = new ThuongHieuBUS();
    DonViTinhBUS donViTinhBUS = new DonViTinhBUS();
    XuatXuBUS xuatXuBUS = new XuatXuBUS();
    ThoiGianBHBUS thoiGianBHBUS = new ThoiGianBHBUS();
    LoaiSanPhamBUS loaiSPBus = new LoaiSanPhamBUS();

    SanPhamDTO sp;
    String[] arrkhuvuc;
    String[] arrthuonghieu;
    String[] arrDVT;
    String[] arrXX;
    String[] arrTGBH;
    String[] arrLoaiSP;

    int masp;
    String type;

    public void init(SanPham jpSP) {
        this.jpSP = jpSP;
        masp = jpSP.spBUS.getAutoIncrement();
        arrkhuvuc = kvkhoBus.getArrTenKhuVuc();
        arrthuonghieu = thuonghieuBus.getArrTenThuongHieu();
        arrDVT = donViTinhBUS.getArrTenDonViTinh();
        arrXX = xuatXuBUS.getArrTenXuatXu();
        arrTGBH = thoiGianBHBUS.getArrThoiGianBH();
        arrLoaiSP = loaiSPBus.getArrTenLoaiSP();
    }

    public SanPhamDialog(SanPham jpSP, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.type = type;
        init(jpSP);
        initComponents(title, type);
    }

    public SanPhamDialog(SanPham jpSP, JFrame owner, String title, boolean modal, String type, SanPhamDTO sp) {
        super(owner, title, modal);
        this.type = type;
        init(jpSP);
        this.sp = sp;
        initComponents(title, type);
    }

    // Trong phương thức initComponents, cập nhật các thành phần giao diện
public void initComponents(String title, String type) {
    this.setSize(new Dimension(1150, 480));
    this.setLayout(new BorderLayout(0, 0));
    this.getContentPane().setBackground(BACKGROUND_COLOR);
    
    // Cập nhật titlePage
    titlePage = new HeaderTitle(title.toUpperCase());
    titlePage.setBackground(PRIMARY_COLOR);
    titlePage.setForeground(Color.WHITE);
    titlePage.setFont(new Font("Segoe UI", Font.BOLD, 18));

    pnCenter = new JPanel(new BorderLayout());
    pnCenter.setBackground(BACKGROUND_COLOR);
    pnCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    pninfosanpham = new JPanel(new GridLayout(4, 3, 10, 10));
    pninfosanpham.setBackground(BACKGROUND_COLOR);
    pninfosanpham.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    pnCenter.add(pninfosanpham, BorderLayout.CENTER);

    pninfosanphamright = new JPanel(new BorderLayout());
    pninfosanphamright.setBackground(BACKGROUND_COLOR);
    pninfosanphamright.setPreferredSize(new Dimension(300, 600));
    pninfosanphamright.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    pnCenter.add(pninfosanphamright, BorderLayout.WEST);

    // Cập nhật các InputForm với style mới
    tenSP = createStyledInputForm("Tên sản phẩm");
    mota = createStyledInputForm("Mô tả sản phẩm");
    soluongton = createStyledInputForm("Số lượng tồn");
    PlainDocument slton = (PlainDocument)soluongton.getTxtForm().getDocument();
    slton.setDocumentFilter(new NumericDocumentFilter());
    
    gianhap = createStyledInputForm("Giá nhập");
    PlainDocument giaNhapDoc = (PlainDocument) gianhap.getTxtForm().getDocument();
    giaNhapDoc.setDocumentFilter(new NumericDocumentFilter());
    
    giaban = createStyledInputForm("Giá bán");
    PlainDocument gia = (PlainDocument)giaban.getTxtForm().getDocument();
    gia.setDocumentFilter(new NumericDocumentFilter());
    
    // Cập nhật SelectForm với style mới
    xuatxu = createStyledSelectForm("Xuất xứ", arrXX);
    donvitinh = createStyledSelectForm("Đơn vị tính", arrDVT);
    thoigianbaohanh = createStyledSelectForm("Thời gian BH", arrTGBH);
    thuonghieu = createStyledSelectForm("Thương hiệu", arrthuonghieu);
    khuvuc = createStyledSelectForm("Khu vực kho", arrkhuvuc);
    loaisanpham = createStyledSelectForm("Loại sản phẩm", arrLoaiSP);
    
    // Cập nhật InputImage
    hinhanh = new InputImage("Hình minh họa");
    hinhanh.setBackground(BACKGROUND_COLOR);
    hinhanh.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

    // Thêm các component vào panel
    pninfosanpham.add(tenSP);
    pninfosanpham.add(mota);
    pninfosanpham.add(soluongton);
    pninfosanpham.add(gianhap);
    pninfosanpham.add(giaban);
    pninfosanpham.add(xuatxu);
    pninfosanpham.add(donvitinh);
    pninfosanpham.add(thoigianbaohanh);
    pninfosanpham.add(thuonghieu);
    pninfosanpham.add(khuvuc);
    pninfosanpham.add(loaisanpham);
    pninfosanphamright.add(hinhanh, BorderLayout.CENTER);

    // Cập nhật panel bottom
    pnbottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    pnbottom.setBorder(new EmptyBorder(20, 0, 10, 20));
    pnbottom.setBackground(BACKGROUND_COLOR);

    btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
    btnHuyBo.setPreferredSize(new Dimension(120, 40));
    btnHuyBo.addActionListener(this);
    pnbottom.add(btnHuyBo);

    // Thêm nút Thêm/Sửa tùy theo chế độ
    if (type.equals("create")) {
        btnThem = new ButtonCustom("Thêm sản phẩm", "success", 14);
        btnThem.setPreferredSize(new Dimension(150, 40));
        btnThem.addActionListener(this);
        pnbottom.add(btnThem);
    } else if (type.equals("update")) {
        btnSua = new ButtonCustom("Cập nhật", "success", 14);
        btnSua.setPreferredSize(new Dimension(150, 40));
        btnSua.addActionListener(this);
        pnbottom.add(btnSua);
    }

    pnCenter.add(pnbottom, BorderLayout.SOUTH);

    if (type.equals("view")) {
        setInfo(sp);
        setEnable(false);
        btnHuyBo.setText("Đóng");
        if (btnThem != null) btnThem.setVisible(false);
        if (btnSua != null) btnSua.setVisible(false);
    } else if (type.equals("update")) {
        setInfo(sp);
        setEnable(true);
    }

    this.add(titlePage, BorderLayout.NORTH);
    this.add(pnCenter, BorderLayout.CENTER);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
}

// Phương thức helper để tạo InputForm với style mới
private InputForm createStyledInputForm(String title) {
    InputForm inputForm = new InputForm(title);
    inputForm.setBackground(BACKGROUND_COLOR);
    inputForm.getTxtForm().setBackground(Color.WHITE);
    inputForm.getTxtForm().setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    inputForm.getTxtForm().setForeground(TEXT_COLOR);
    inputForm.getLblTitle().setForeground(SECONDARY_TEXT_COLOR);
    inputForm.getLblTitle().setFont(new Font("Segoe UI", Font.PLAIN, 13));
    return inputForm;
}

// Phương thức helper để tạo SelectForm với style mới
private SelectForm createStyledSelectForm(String title, String[] items) {
    SelectForm selectForm = new SelectForm(title, items);
    selectForm.setBackground(BACKGROUND_COLOR);
    selectForm.getCbb().setBackground(Color.WHITE);
    selectForm.getCbb().setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR, 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    selectForm.getCbb().setForeground(TEXT_COLOR);
    selectForm.getLblTitle().setForeground(SECONDARY_TEXT_COLOR);
    selectForm.getLblTitle().setFont(new Font("Segoe UI", Font.PLAIN, 13));
    return selectForm;
}

    private void setEnable(boolean enable) {
    tenSP.setEditable(enable);
    mota.setEditable(enable);
    soluongton.setEditable(enable);
    gianhap.setEditable(enable);  // Thêm dòng này
    giaban.setEditable(enable);
    xuatxu.setEnabled(enable);
    donvitinh.setEnabled(enable);
    thoigianbaohanh.setEnabled(enable);
    thuonghieu.setEnabled(enable);
    khuvuc.setEnabled(enable);
    loaisanpham.setEnabled(enable);  // Thêm dòng này
    hinhanh.setEnabled(enable);
}

    public String addImage(String urlImg) {
        Random randomGenerator = new Random();
        int ram = randomGenerator.nextInt(1000);
        File sourceFile = new File(urlImg);
        String destPath = "./src/img_product";
        File destFolder = new File(destPath);
        String newName = ram + sourceFile.getName();
        try {
            Path dest = Paths.get(destFolder.getPath(), newName);
            Files.copy(sourceFile.toPath(), dest);
        } catch (IOException e) {
        }
        return newName;
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnHuyBo) {
        dispose();
    } else if (e.getSource() == btnThem && type.equals("create")) {
        try {
            if (validateCardOne()) {
                SanPhamDTO sp = getInfo();
                if (jpSP.spBUS.add(sp)) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                    listSP = spBUS.getAll();
                    jpSP.loadDataTalbe(listSP);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại! Kiểm tra lại dữ liệu.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm: " + ex.getMessage());
        }
    } else if (e.getSource() == btnSua && type.equals("update")) {
        try {
            if (validateCardOne()) {
                SanPhamDTO sp = getInfo();
                if (jpSP.spBUS.update(sp)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                    listSP = spBUS.getAll();
                    jpSP.loadDataTalbe(listSP);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại! Kiểm tra lại dữ liệu.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật sản phẩm: " + ex.getMessage());
        }
    }
}

     public SanPhamDTO getInfo() {
    String hinhanh = this.hinhanh.getUrl_img();
    if (hinhanh == null || hinhanh.isEmpty()) {
        hinhanh = "default.png";
    } else if (type.equals("update") && !hinhanh.equals(sp.getHinhanh())) { 
        // Only copy new image if in update mode and image is different
        hinhanh = addImage(hinhanh);
    } else if (type.equals("create")) {
        // For new products, always copy the image if one was selected
        hinhanh = addImage(hinhanh);
    }
    
    // Rest of the method remains the same
    String vtensp = tenSP.getText();
    String vmota = mota.getText();
    int vsoluongton = Integer.parseInt(soluongton.getText());
    double vgianhap = Double.parseDouble(gianhap.getText());
    double vgiaban = Double.parseDouble(giaban.getText());
    
    // Lấy các giá trị từ combobox
    Integer vxuatxu = (xuatxu.getSelectedIndex() >= 0) ? 
            xuatXuBUS.getAll().get(xuatxu.getSelectedIndex()).getMaxuatxu() : null;
    Integer vdonvitinh = (donvitinh.getSelectedIndex() >= 0) ? 
            donViTinhBUS.getAll().get(donvitinh.getSelectedIndex()).getMadonvitinh() : null;
    Integer vthoigianbaohanh = (thoigianbaohanh.getSelectedIndex() >= 0) ? 
            thoiGianBHBUS.getAll().get(thoigianbaohanh.getSelectedIndex()).getMathoigianbh() : null;
    Integer vthuonghieu = (thuonghieu.getSelectedIndex() >= 0) ? 
            thuonghieuBus.getAll().get(thuonghieu.getSelectedIndex()).getMathuonghieu() : null;
    Integer khuvuckho = (khuvuc.getSelectedIndex() >= 0) ? 
            kvkhoBus.getAll().get(khuvuc.getSelectedIndex()).getMakhuvuc() : null;
    int loaisanpham = (this.loaisanpham.getSelectedIndex() >= 0) ? 
            loaiSPBus.getAll().get(this.loaisanpham.getSelectedIndex()).getMaloai() : 0;
    
    int productId = (type.equals("create")) ? jpSP.spBUS.getAutoIncrement() : this.sp.getMasp();
    int trangthai = (type.equals("create")) ? 1 : this.sp.getTrangthai();
    
    return new SanPhamDTO(productId, vtensp, hinhanh, loaisanpham, vmota, vsoluongton, 
                        vgianhap, vgiaban, vthoigianbaohanh, vthuonghieu, vxuatxu, 
                        vdonvitinh, khuvuckho, trangthai);
}
    public void setInfo(SanPhamDTO sp) {
    hinhanh.setUrl_img(sp.getHinhanh());
    tenSP.setText(sp.getTensp());
    mota.setText(sp.getMota());
    soluongton.setText(Integer.toString(sp.getSoluongton()));
    
    // Format giá trị double để hiển thị
    gianhap.setText(String.format("%.0f", sp.getGianhap()));
    giaban.setText(String.format("%.0f", sp.getGiaban()));
    
    // Set selected items with validation
    if (sp.getXuatxu() != null) xuatxu.setSelectedItem(sp.getXuatxu());
    if (sp.getDonvitinh() != null) donvitinh.setSelectedItem(sp.getDonvitinh());
    if (sp.getThoigianbaohanh() != null) thoigianbaohanh.setSelectedItem(sp.getThoigianbaohanh());
    
    // For these, we need to check if the index is valid
    int thuongHieuIndex = thuonghieuBus.getIndexByMaLH(sp.getThuonghieu());
    if (thuongHieuIndex >= 0 && thuongHieuIndex < thuonghieu.getItemCount()) {
        thuonghieu.setSelectedIndex(thuongHieuIndex);
    }
    
    int khuvucIndex = jpSP.spBUS.getIndexByMaSP(sp.getKhuvuckho());
    if (khuvucIndex >= 0 && khuvucIndex < khuvuc.getItemCount()) {
        khuvuc.setSelectedIndex(khuvucIndex);
    }
    
    int loaiSPIndex = loaiSPBus.getIndexByMaLSP(sp.getMaloai());
    if (loaiSPIndex >= 0 && loaiSPIndex < loaisanpham.getItemCount()) {
        loaisanpham.setSelectedIndex(loaiSPIndex);
    } else {
        // Handle case where index is invalid
        loaisanpham.setSelectedIndex(-1); // or 0 for first item
    }
}

    public boolean validateCardOne() {
        if (Validation.isEmpty(tenSP.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!");
            return false;
        }
        
        if (Validation.isEmpty(mota.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mô tả sản phẩm!");
            return false;
        }
        
        try {
            int quantity = Integer.parseInt(soluongton.getText());
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng tồn phải lớn hơn hoặc bằng 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng tồn phải là số nguyên!");
            return false;
        }
        
        try {
            double importPrice = Double.parseDouble(gianhap.getText());
            if (importPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập phải là số!");
            return false;
        }
        
        try {
            double price = Double.parseDouble(giaban.getText());
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số!");
            return false;
        }
        
        return true;
    }
    
    
}