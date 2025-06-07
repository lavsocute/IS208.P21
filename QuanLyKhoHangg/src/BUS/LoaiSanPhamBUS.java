package BUS;

import DAO.LoaiSanPhamDAO;
import DTO.LoaiSanPhamDTO;
import java.util.ArrayList;

public class LoaiSanPhamBUS {

    private final LoaiSanPhamDAO lspDAO = LoaiSanPhamDAO.getInstance();
    private ArrayList<LoaiSanPhamDTO> listLSP = new ArrayList<>();

    public LoaiSanPhamBUS() {
        listLSP = lspDAO.selectAll();
    }

    public ArrayList<LoaiSanPhamDTO> getAll() {
        return this.listLSP;
    }

    public LoaiSanPhamDTO getByIndex(int index) {
        return this.listLSP.get(index);
    }

    public int getIndexByMaLSP(int maloai) {
        int i = 0;
        int vitri = -1;
        while (i < this.listLSP.size() && vitri == -1) {
            if (listLSP.get(i).getMaloai() == maloai) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

    public Boolean add(LoaiSanPhamDTO lsp) {
        boolean check = lspDAO.insert(lsp) != 0;
        if (check) {
            this.listLSP.add(lsp);
        }
        return check;
    }

    public Boolean delete(LoaiSanPhamDTO lsp) {
        boolean check = lspDAO.delete(Integer.toString(lsp.getMaloai())) != 0;
        if (check) {
            this.listLSP.remove(lsp);
        }
        return check;
    }

    public Boolean update(LoaiSanPhamDTO lsp) {
        boolean check = lspDAO.update(lsp) != 0;
        if (check) {
            this.listLSP.set(getIndexByMaLSP(lsp.getMaloai()), lsp);
        }
        return check;
    }

    public ArrayList<LoaiSanPhamDTO> search(String text) {
        text = text.toLowerCase();
        ArrayList<LoaiSanPhamDTO> result = new ArrayList<>();
        for (LoaiSanPhamDTO i : this.listLSP) {
            if (Integer.toString(i.getMaloai()).toLowerCase().contains(text) || 
                i.getTenloai().toLowerCase().contains(text) ||
                i.getMota().toLowerCase().contains(text)) {
                result.add(i);
            }
        }
        return result;
    }

    public String[] getArrTenLoaiSP() {
        int size = listLSP.size();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = listLSP.get(i).getTenloai();
        }
        return result;
    }

    public String getTenLoaiSP(int maloai) {
        return this.listLSP.get(this.getIndexByMaLSP(maloai)).getTenloai();
    }

    public boolean checkDup(String tenloai) {
        boolean check = true;
        int i = 0;
        while (i < this.listLSP.size() && check) {
            if (this.listLSP.get(i).getTenloai().equalsIgnoreCase(tenloai)) {
                check = false;
            } else {
                i++;
            }
        }
        return check;
    }

    public int getAutoIncrement() {
        return lspDAO.getAutoIncrement();
    }
}