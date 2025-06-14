package GUI.Dialog;

import BUS.ChiTietSanPhamBUS;
import BUS.DonViTinhBUS;
import BUS.ThoiGianBHBUS;
import BUS.PhieuNhapBUS;
import BUS.PhieuXuatBUS;
import BUS.SanPhamBUS;
import DAO.DonViTinhDAO;
import DAO.ThoiGianBHDAO;
import DAO.KhachHangDAO;
import DAO.NhaCungCapDAO;
import DAO.NhanVienDAO;
import DAO.SanPhamDAO;
import DTO.ChiTietPhieuDTO;
import DTO.ChiTietSanPhamDTO;
import DTO.PhieuNhapDTO;
import DTO.PhieuXuatDTO;
import DTO.SanPhamDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import helper.Formater;
import helper.writePDF;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class ChiTietPhieuDialog extends JDialog implements ActionListener {

    HeaderTitle titlePage;
    JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_bottom_right, pnmain_bottom_left, pnmain_btn;
    InputForm txtMaPhieu, txtNhanVien, txtNhaCungCap, txtThoiGian;
    DefaultTableModel tblModel, tblModelImei;
    JTable table, tblImei;
    JScrollPane scrollTable, scrollTableImei;

    PhieuNhapDTO phieunhap;
    PhieuXuatDTO phieuxuat;
    
    SanPhamBUS sanphamBus = new SanPhamBUS();
    ChiTietSanPhamBUS ctspBus = new ChiTietSanPhamBUS();
    PhieuNhapBUS phieunhapBus;
    PhieuXuatBUS phieuxuatBus;

    ButtonCustom btnPdf, btnHuyBo;

    ArrayList<ChiTietPhieuDTO> chitietphieu;

    HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> chitietsanpham = new HashMap<>();

    public ChiTietPhieuDialog(JFrame owner, String title, boolean modal, PhieuNhapDTO phieunhapDTO) {
        super(owner, title, modal);
        this.phieunhap = phieunhapDTO;
        phieunhapBus = new PhieuNhapBUS();
        chitietphieu = phieunhapBus.getChiTietPhieu_Type(phieunhapDTO.getMaphieu());
        chitietsanpham = ctspBus.getChiTietSanPhamFromMaPN(phieunhapDTO.getMaphieu());
        initComponent(title);
        initPhieuNhap();
        loadDataTableChiTietPhieu(chitietphieu);
        this.setVisible(true);
    }

    public ChiTietPhieuDialog(JFrame owner, String title, boolean modal, PhieuXuatDTO phieuxuatDTO) {
        super(owner, title, modal);
        this.phieuxuat = phieuxuatDTO;
        phieuxuatBus = new PhieuXuatBUS();
        chitietphieu = phieuxuatBus.selectCTP(phieuxuatDTO.getMaphieu());
        chitietsanpham = ctspBus.getChiTietSanPhamFromMaPX(phieuxuatDTO.getMaphieu());
        initComponent(title);
        initPhieuXuat();
        loadDataTableChiTietPhieu(chitietphieu);
        this.setVisible(true);
    }

    public void initPhieuNhap() {
        txtMaPhieu.setText("PN" + Integer.toString(this.phieunhap.getMaphieu()));
        txtNhaCungCap.setText(NhaCungCapDAO.getInstance().selectById(phieunhap.getManhacungcap() + "").getTenncc());
        txtNhanVien.setText(NhanVienDAO.getInstance().selectById(phieunhap.getManguoitao() + "").getHoten());
        txtThoiGian.setText(Formater.FormatTime(phieunhap.getThoigiantao()));
    }

    public void initPhieuXuat() {
        txtMaPhieu.setText("PX" + Integer.toString(this.phieuxuat.getMaphieu()));
        txtNhaCungCap.setTitle("Khách hàng");
        txtNhaCungCap.setText(KhachHangDAO.getInstance().selectById(phieuxuat.getMakh() + "").getHoten());
        txtNhanVien.setText(NhanVienDAO.getInstance().selectById(phieuxuat.getManguoitao() + "").getHoten());
        txtThoiGian.setText(Formater.FormatTime(phieuxuat.getThoigiantao()));
    }

    public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuDTO> ctPhieu) {
    tblModel.setRowCount(0);
    int size = ctPhieu.size();
    for (int i = 0; i < size; i++) {
        SanPhamDTO sp = sanphamBus.getByMaSP(ctPhieu.get(i).getMaSanPham());
        if (sp == null) {
            // Handle missing product - either skip or show placeholder
            tblModel.addRow(new Object[]{
                i + 1, 
                ctPhieu.get(i).getMaSanPham(), 
                "[Sản phẩm không tồn tại]", 
                "", 
                "", 
                Formater.FormatVND(ctPhieu.get(i).getDonGia()), 
                ctPhieu.get(i).getSoLuong()
            });
            continue;
        }
        
        // Lấy thông tin đơn vị tính
        String donViTinh = "";
        try {
            donViTinh = DonViTinhDAO.getInstance().selectById(sp.getDonvitinh()+"").getTendonvitinh();
        } catch (Exception e) {
            donViTinh = "Không xác định";
        }
        
        // Lấy thông tin thời gian bảo hành
        String thoiGianBH = "";
        try {
            thoiGianBH = ThoiGianBHDAO.getInstance().selectById(sp.getThoigianbaohanh()+"").getTenthgianbh();
        } catch (Exception e) {
            thoiGianBH = "Không xác định";
        }
        
        tblModel.addRow(new Object[]{
            i + 1, 
            sp.getMasp(), 
            SanPhamDAO.getInstance().selectById(sp.getMasp()+"").getTensp(), 
            donViTinh,
            thoiGianBH, 
            Formater.FormatVND(ctPhieu.get(i).getDonGia()), 
            ctPhieu.get(i).getSoLuong()
        });
    }
}

    public void loadDataTableImei(ArrayList<ChiTietSanPhamDTO> dssp) {
        tblModelImei.setRowCount(0);
        int size = dssp.size();
        for (int i = 0; i < size; i++) {
            tblModelImei.addRow(new Object[]{
                i + 1, dssp.get(i).getMaChiTiet()
            });
        }
    }

    public void initComponent(String title) {
        this.setSize(new Dimension(1100, 500));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());

        pnmain = new JPanel(new BorderLayout());

        pnmain_top = new JPanel(new GridLayout(1, 4));
        txtMaPhieu = new InputForm("Mã phiếu");
        txtNhanVien = new InputForm("Nhân viên nhập");
        txtNhaCungCap = new InputForm("Nhà cung cấp");
        txtThoiGian = new InputForm("Thời gian tạo");

        txtMaPhieu.setEditable(false);
        txtNhanVien.setEditable(false);
        txtNhaCungCap.setEditable(false);
        txtThoiGian.setEditable(false);

        pnmain_top.add(txtMaPhieu);
        pnmain_top.add(txtNhanVien);
        pnmain_top.add(txtNhaCungCap);
        pnmain_top.add(txtThoiGian);

        pnmain_bottom = new JPanel(new BorderLayout(5, 5));
        pnmain_bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnmain_bottom.setBackground(Color.WHITE);

        pnmain_bottom_left = new JPanel(new GridLayout(1, 1));
        table = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên SP", "ĐVT", "Thời gian BH", "Đơn giá", "Số lượng"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        scrollTable.setViewportView(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = table.getSelectedRow();
                if (index != -1) {
                    loadDataTableImei(chitietsanpham.get(chitietphieu.get(index).getMaSanPham()));
                }
            }
        });
        pnmain_bottom_left.add(scrollTable);

        pnmain_bottom_right = new JPanel(new GridLayout(1, 1));
        pnmain_bottom_right.setPreferredSize(new Dimension(200, 10));
        tblImei = new JTable();
        scrollTableImei = new JScrollPane();
        tblModelImei = new DefaultTableModel();
        tblModelImei.setColumnIdentifiers(new String[]{"STT", "Mã CTSP"});
        tblImei.setModel(tblModelImei);
        tblImei.setFocusable(false);
        tblImei.setDefaultRenderer(Object.class, centerRenderer);
        tblImei.getColumnModel().getColumn(1).setPreferredWidth(170);
        scrollTableImei.setViewportView(tblImei);
        pnmain_bottom_right.add(scrollTableImei);

        pnmain_bottom.add(pnmain_bottom_left, BorderLayout.CENTER);
        pnmain_bottom.add(pnmain_bottom_right, BorderLayout.EAST);

        pnmain_btn = new JPanel(new FlowLayout());
        pnmain_btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnmain_btn.setBackground(Color.white);
        btnPdf = new ButtonCustom("Xuất file PDF", "success", 14);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnPdf.addActionListener(this);
        btnHuyBo.addActionListener(this);
        pnmain_btn.add(btnPdf);
        pnmain_btn.add(btnHuyBo);

        pnmain.add(pnmain_top, BorderLayout.NORTH);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);
        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnHuyBo) {
            dispose();
        }
        if (source == btnPdf) {
            writePDF w = new writePDF();
            if (this.phieuxuat != null) {
                w.writePX(phieuxuat.getMaphieu());
            }
            if (this.phieunhap != null) {
                w.writePN(phieunhap.getMaphieu());
            }
        }
    }
}
