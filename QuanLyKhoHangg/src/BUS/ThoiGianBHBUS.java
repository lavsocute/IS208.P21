/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ThoiGianBHDAO;
import DTO.ThuocTinhSanPham.ThoiGianBHDTO;
import java.util.ArrayList;

public class ThoiGianBHBUS {
    private ThoiGianBHDAO thoiGianBHDAO = new ThoiGianBHDAO();
    private ArrayList<ThoiGianBHDTO> listThoiGianBH = new ArrayList<>();

    public ThoiGianBHBUS() {
        this.listThoiGianBH = thoiGianBHDAO.selectAll();
    }

    public ArrayList<ThoiGianBHDTO> getAll() {
        return this.listThoiGianBH;
    }

    public String[] getArrThoiGianBH() {
        String[] result = new String[listThoiGianBH.size()];
        for (int i = 0; i < listThoiGianBH.size(); i++) {
            result[i] = listThoiGianBH.get(i).getTenthgianbh();
        }
        return result;
    }

    public ThoiGianBHDTO getByIndex(int index) {
        return this.listThoiGianBH.get(index);
    }

    public boolean add(ThoiGianBHDTO thoiGianBH) {
        boolean check = thoiGianBHDAO.insert(thoiGianBH) != 0;
        if (check) {
            this.listThoiGianBH.add(thoiGianBH);
        }
        return check;
    }

    public boolean delete(ThoiGianBHDTO thoiGianBH, int index) {
        boolean check = thoiGianBHDAO.delete(Integer.toString(thoiGianBH.getMathoigianbh())) != 0;
        if (check) {
            this.listThoiGianBH.remove(index);
        }
        return check;
    }

    public int getIndexByMaTGBH(int maTGBH) {
        int i = 0;
        int vitri = -1;
        while (i < this.listThoiGianBH.size() && vitri == -1) {
            if (listThoiGianBH.get(i).getMathoigianbh() == maTGBH) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public String getTenThoiGianBH(int maTGBH) {
        int index = this.getIndexByMaTGBH(maTGBH);
        return this.listThoiGianBH.get(index).getTenthgianbh();
    }

    public boolean update(ThoiGianBHDTO thoiGianBH) {
        boolean check = thoiGianBHDAO.update(thoiGianBH) != 0;
        if (check) {
            this.listThoiGianBH.set(getIndexByMaTGBH(thoiGianBH.getMathoigianbh()), thoiGianBH);
        }
        return check;
    }
    
    public boolean checkDup(String name) {
        boolean check = true;
        int i = 0;
        while (i < this.listThoiGianBH.size() && check) {
            if (this.listThoiGianBH.get(i).getTenthgianbh().toLowerCase().contains(name.toLowerCase())) {
                check = false;
            } else {
                i++;
            }
        }
        return check;
    }
}