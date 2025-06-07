package GUI.Dialog;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import GUI.Component.SelectForm;
import GUI.Panel.TaiKhoan;
import helper.BCrypt;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TaiKhoanDialog extends JDialog {

    private TaiKhoan taiKhoan;
    private HeaderTitle titlePage;
    private JPanel pnmain, pnbottom;
    private ButtonCustom btnThem, btnCapNhat, btnHuyBo;
    private InputForm username;
    private InputForm password;
    private SelectForm maNhomQuyen;
    private SelectForm trangthai;
    private int manv;
    private ArrayList<TaiKhoanDTO> listTK = TaiKhoanDAO.getInstance().selectAll();

    public TaiKhoanDialog(TaiKhoan taiKhoan, JFrame owner, String title, boolean modal, String type, int manv) {
        super(owner, title, modal);
        init(title, type);
        this.manv = manv;
        this.taiKhoan = taiKhoan;
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public TaiKhoanDialog(TaiKhoan taiKhoan, JFrame owner, String title, boolean modal, String type, TaiKhoanDTO tk) {
        super(owner, title, modal);
        init(title, type);
        this.manv = tk.getManv();
        this.taiKhoan = taiKhoan;
        username.setText(tk.getUsername());
        password.setPass(tk.getMatkhau());
        
        // Set selected nhóm quyền dựa trên giá trị số
        String nhomQuyenText = getNhomQuyenText(tk.getManhomquyen());
        maNhomQuyen.setSelectedItem(nhomQuyenText);
        
        trangthai.setSelectedIndex(tk.getTrangthai());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init(String title, String type) {
        this.setSize(new Dimension(500, 620));
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());
        pnmain = new JPanel(new GridLayout(4, 1, 5, 0));
        pnmain.setBackground(Color.white);
        
        username = new InputForm("Tên đăng nhập");
        password = new InputForm("Mật khẩu", "password");
        
        // Khởi tạo danh sách nhóm quyền cố định
        String[] nhomQuyenOptions = {"Admin (0)", "Quản lý kho (1)", "Nhân viên nhập hàng (2)", "Nhân viên xuất hàng (3)"};
        maNhomQuyen = new SelectForm("Nhóm quyền", nhomQuyenOptions);
        
        trangthai = new SelectForm("Trạng thái", new String[]{"Ngưng hoạt động", "Hoạt động"});
        
        pnmain.add(username);
        pnmain.add(password);
        pnmain.add(maNhomQuyen);
        pnmain.add(trangthai);
        
        pnbottom = new JPanel(new FlowLayout());
        pnbottom.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnbottom.setBackground(Color.white);
        
        btnThem = new ButtonCustom("Thêm tài khoản", "success", 14);
        btnCapNhat = new ButtonCustom("Lưu thông tin", "success", 14);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String tendangnhap = username.getText();
                    int check = 0;
                    for (TaiKhoanDTO i : listTK) {
                        if (i.getUsername().equalsIgnoreCase(tendangnhap)) {
                            check++;
                            break;
                        }
                    }
                    if (check == 0) {
                        String pass = BCrypt.hashpw(password.getPass(), BCrypt.gensalt(12));
                        int manhom = getSelectedNhomQuyenValue();
                        int tt = trangthai.getSelectedIndex();
                        TaiKhoanDTO tk = new TaiKhoanDTO(manv, tendangnhap, pass, manhom, tt);
                        TaiKhoanDAO.getInstance().insert(tk);
                        taiKhoan.taiKhoanBus.add(tk);
                        taiKhoan.loadTable(taiKhoan.taiKhoanBus.getTaiKhoanAll());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Tên tài khoản đã tồn tại. Vui lòng đổi tên khác!", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
                        username.requestFocus();
                    }
                }
            }
        });

        btnCapNhat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!username.getText().isEmpty()) {
                    String tendangnhap = username.getText();
                    String pass = password.getPass().isEmpty() ? "" : BCrypt.hashpw(password.getPass(), BCrypt.gensalt(12));
                    int manhom = getSelectedNhomQuyenValue();
                    int tt = trangthai.getSelectedIndex();
                    
                    TaiKhoanDTO tk = new TaiKhoanDTO(manv, tendangnhap, pass, manhom, tt);
                    TaiKhoanDAO.getInstance().update(tk);
                    taiKhoan.taiKhoanBus.update(tk);
                    taiKhoan.loadTable(taiKhoan.taiKhoanBus.getTaiKhoanAll());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng không để trống tên đăng nhập");
                }
            }
        });

        btnHuyBo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        switch (type) {
            case "create" ->
                pnbottom.add(btnThem);
            case "update" -> {
                pnmain.remove(password);
                pnbottom.add(btnCapNhat);
                password.setDisablePass();
            }
            case "view" -> {
                pnmain.remove(password);
                username.setEditable(false);
                maNhomQuyen.setDisable();
                trangthai.setDisable();
                this.setSize(new Dimension(500, 550));
            }
            default ->
                throw new AssertionError();
        }
        pnbottom.add(btnHuyBo);
        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.add(pnbottom, BorderLayout.SOUTH);
    }

    // Lấy giá trị số từ lựa chọn nhóm quyền
    private int getSelectedNhomQuyenValue() {
        String selected = (String) maNhomQuyen.getSelectedItem();
        // Lấy số trong ngoặc đơn
        return Integer.parseInt(selected.substring(selected.indexOf("(") + 1, selected.indexOf(")")));
    }

    // Chuyển mã nhóm quyền thành tên hiển thị
    private String getNhomQuyenText(int manhomquyen) {
        switch (manhomquyen) {
            case 0: return "Admin (0)";
            case 1: return "Quản lý kho (1)";
            case 2: return "Nhân viên nhập hàng (2)";
            case 3: return "Nhân viên xuất hàng (3)";
            default: return "Admin (0)";
        }
    }

    public boolean validateInput() {
        if (username.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống tên đăng nhập");
            return false;
        } else if (username.getText().length() < 6) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập ít nhất 6 kí tự");
            return false;
        } else if (password.getPass().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng không để trống mật khẩu");
            return false;
        } else if (password.getPass().length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu ít nhất 6 ký tự");
            return false;
        }
        return true;
    }
}