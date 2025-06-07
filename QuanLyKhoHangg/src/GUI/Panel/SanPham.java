package GUI.Panel;

import BUS.SanPhamBUS;
import DAO.*;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Main;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietSanPhamDialog;
import GUI.Dialog.SanPhamDialog;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import helper.JTableExporter;
import helper.Formater;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;

public final class SanPham extends JPanel implements ActionListener, ItemListener {
    // Các thành phần giao diện
    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    JTable tableSanPham;
    JScrollPane scrollTableSanPham;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    Main m;
    public SanPhamBUS spBUS = new SanPhamBUS();
    
    public ArrayList<DTO.SanPhamDTO> listSP = spBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);
    Color PrimaryColor = new Color(106, 90, 205); // Màu tím
    Color SecondaryColor = new Color(240, 240, 245); // Màu nền nhạt

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Khởi tạo các thành phần
        initTable();
        initPadding();

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        // Panel chức năng (chứa nút thêm/sửa/xóa và tìm kiếm)
        initFunctionBar();
        
        // Panel hiển thị bảng sản phẩm
        initMainPanel();
    }

    private void initTable() {
        tableSanPham = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModel = new DefaultTableModel();
        tableSanPham.setShowGrid(false);
        tableSanPham.setIntercellSpacing(new Dimension(0, 0));
        tableSanPham.setRowHeight(35);
        tableSanPham.getTableHeader().setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 14));
        tableSanPham.getTableHeader().setBackground(PrimaryColor);
        tableSanPham.getTableHeader().setForeground(Color.white);
        String[] header = {"Mã SP", "Tên sản phẩm", "Số lượng tồn", "Giá bán", 
                          "Thương hiệu", "Loại sản phẩm", "Xuất xứ", 
                          "Đơn vị tính", "Khu vực kho", "Thời gian BH"};
        
        tblModel.setColumnIdentifiers(header);
        tableSanPham.setModel(tblModel);
        scrollTableSanPham.setViewportView(tableSanPham);
        
        // Căn giữa nội dung các cột (trừ cột tên sản phẩm)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tableSanPham.getColumnModel();
        for (int i = 0; i < header.length; i++) {
            if (i != 1) columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableSanPham.setFocusable(false);
        tableSanPham.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableSanPham, 2, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tableSanPham, 3, TableSorter.VND_CURRENCY_COMPARATOR);
        tableSanPham.setDefaultEditor(Object.class, null);
    }

    private void initFunctionBar() {
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Khởi tạo panel chứa các nút chức năng
        JPanel pnlFunction = new JPanel(new GridBagLayout());
        pnlFunction.setBorder(new EmptyBorder(0, 10, 0, 10));
        pnlFunction.setOpaque(false);

        // Thêm các nút chức năng (thêm/sửa/xóa)
        String[] actions = {"create", "update", "delete", "detail", "phone", "export"};
        mainFunction = new MainFunction(actions);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlFunction.add(mainFunction, gbc);

        // Thêm sự kiện cho các nút
        for (String action : actions) {
            mainFunction.btn.get(action).addActionListener(this);
        }

        // Khởi tạo thanh tìm kiếm
        initSearchPanel();

        // Thêm cả hai panel vào functionBar
        functionBar.add(pnlFunction);
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);
    }

    private void initSearchPanel() {
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã SP", "Tên sản phẩm", 
                                                 "Thương hiệu", "Loại sản phẩm", 
                                                 "Xuất xứ", "Khu vực kho"});
        search.cbxChoose.addItemListener(this);
        
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchProduct();
            }
        });

        search.btnReset.addActionListener(e -> {
            search.txtSearchForm.setText("");
            listSP = spBUS.getAll();
            loadDataTalbe(listSP);
        });
    }

    private void initMainPanel() {
        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.add(scrollTableSanPham, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    private void searchProduct() {
        String type = (String) search.cbxChoose.getSelectedItem();
        String txt = search.txtSearchForm.getText();
        listSP = spBUS.search(txt, type);
        loadDataTalbe(listSP);
    }

    public SanPham(Main m) {
        this.m = m;
        initComponent();
        loadDataTalbe(listSP);
    }

    public void loadDataTalbe(ArrayList<DTO.SanPhamDTO> result) {
        tblModel.setRowCount(0);
        for (DTO.SanPhamDTO sp : result) {
            tblModel.addRow(new Object[]{
                sp.getMasp(), 
                sp.getTensp(), 
                sp.getSoluongton(),
                Formater.FormatVND(sp.getGiaban()),
                getThuongHieuName(sp.getThuonghieu()),
                getLoaiSanPhamName(sp.getMaloai()),
                getXuatXuName(sp.getXuatxu()),
                getDonViTinhName(sp.getDonvitinh()),
                getKhuVucKhoName(sp.getKhuvuckho()),
                getThoiGianBHName(sp.getThoigianbaohanh())
            });
        }
    }

    // Các phương thức hỗ trợ lấy tên từ ID
    private String getThuongHieuName(Integer id) {
        return id != null ? ThuongHieuDAO.getInstance().selectById(id.toString()).getTenthuonghieu() : "";
    }
    
    private String getLoaiSanPhamName(int id) {
        return LoaiSanPhamDAO.getInstance().selectById(String.valueOf(id)).getTenloai();
    }
    
    private String getXuatXuName(Integer id) {
        return id != null ? XuatXuDAO.getInstance().selectById(id.toString()).getTenxuatxu() : "";
    }
    
    private String getDonViTinhName(Integer id) {
        return id != null ? DonViTinhDAO.getInstance().selectById(id.toString()).getTendonvitinh() : "";
    }
    
    private String getKhuVucKhoName(Integer id) {
        return id != null ? KhuVucKhoDAO.getInstance().selectById(id.toString()).getTenkhuvuc() : "";
    }
    
    private String getThoiGianBHName(Integer id) {
        return id != null ? ThoiGianBHDAO.getInstance().selectById(id.toString()).getTenthgianbh() : "";
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == search.cbxChoose) {
            searchProduct();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            new SanPhamDialog(this, owner, "Thêm sản phẩm mới", true, "create");
        } 
        else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = getRowSelected();
            if (index != -1) {
                new SanPhamDialog(this, owner, "Chỉnh sửa sản phẩm", true, "update", listSP.get(index));
                refreshData();
            }
        } 
        else if (e.getSource() == mainFunction.btn.get("delete")) {
            deleteProduct();
        } 
        else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = getRowSelected();
            if (index != -1) {
                new SanPhamDialog(this, owner, "Xem chi tiết sản phẩm", true, "view", listSP.get(index));
            }
        } 
        else if (e.getSource() == mainFunction.btn.get("phone")) {
            int index = getRowSelected();
            if (index != -1) {
                new ChiTietSanPhamDialog(owner, "Chi tiết sản phẩm", true, listSP.get(index));
            }
        } 
        else if (e.getSource() == mainFunction.btn.get("export")) {
            exportToExcel();
        }
    }

    private void deleteProduct() {
        int index = getRowSelected();
        if (index != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, 
                    "Bạn có chắc chắn muốn xóa sản phẩm này?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                spBUS.delete(listSP.get(index));
                refreshData();
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
            }
        }
    }

    private void exportToExcel() {
        try {
            JTableExporter.exportJTableToExcel(tableSanPham);
            JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage());
            Logger.getLogger(SanPham.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshData() {
        listSP = spBUS.getAll();
        loadDataTalbe(listSP);
    }

    public int getRowSelected() {
        int index = tableSanPham.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm");
        }
        return index;
    }

    private void initPadding() {
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);

        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);
    }
}