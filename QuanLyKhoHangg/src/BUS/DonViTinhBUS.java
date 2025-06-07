/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.DonViTinhDAO;
import DTO.ThuocTinhSanPham.DonViTinhDTO;
import java.util.ArrayList;

public class DonViTinhBUS {
    private DonViTinhDAO donViTinhDAO = new DonViTinhDAO();
    private ArrayList<DonViTinhDTO> listDonViTinh = new ArrayList<>();

    public DonViTinhBUS() {
        this.listDonViTinh = donViTinhDAO.selectAll();
    }

    public ArrayList<DonViTinhDTO> getAll() {
        return this.listDonViTinh;
    }

    public String[] getArrTenDonViTinh() {
        String[] result = new String[listDonViTinh.size()];
        for (int i = 0; i < listDonViTinh.size(); i++) {
            result[i] = listDonViTinh.get(i).getTendonvitinh();
        }
        return result;
    }

    public DonViTinhDTO getByIndex(int index) {
        return this.listDonViTinh.get(index);
    }

    public boolean add(DonViTinhDTO donViTinh) {
        boolean check = donViTinhDAO.insert(donViTinh) != 0;
        if (check) {
            this.listDonViTinh.add(donViTinh);
        }
        return check;
    }

    public boolean delete(DonViTinhDTO donViTinh, int index) {
        boolean check = donViTinhDAO.delete(Integer.toString(donViTinh.getMadonvitinh())) != 0;
        if (check) {
            this.listDonViTinh.remove(index);
        }
        return check;
    }

    public int getIndexByMaDVT(int maDVT) {
        int i = 0;
        int vitri = -1;
        while (i < this.listDonViTinh.size() && vitri == -1) {
            if (listDonViTinh.get(i).getMadonvitinh() == maDVT) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public String getTenDonViTinh(int maDVT) {
        int index = this.getIndexByMaDVT(maDVT);
        return this.listDonViTinh.get(index).getTendonvitinh();
    }

    public boolean update(DonViTinhDTO donViTinh) {
        boolean check = donViTinhDAO.update(donViTinh) != 0;
        if (check) {
            this.listDonViTinh.set(getIndexByMaDVT(donViTinh.getMadonvitinh()), donViTinh);
        }
        return check;
    }
    
    public boolean checkDup(String name) {
        boolean check = true;
        int i = 0;
        while (i < this.listDonViTinh.size() && check) {
            if (this.listDonViTinh.get(i).getTendonvitinh().toLowerCase().contains(name.toLowerCase())) {
                check = false;
            } else {
                i++;
            }
        }
        return check;
    }
}