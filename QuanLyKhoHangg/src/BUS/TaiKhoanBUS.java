package BUS;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoanDTO;
import java.util.ArrayList;

/**
 *
 * @author robot
 */
public class TaiKhoanBUS {
    private ArrayList<TaiKhoanDTO> listTaiKhoan;
    
    public TaiKhoanBUS(){
        this.listTaiKhoan = TaiKhoanDAO.getInstance().selectAll();
    }
    
    public ArrayList<TaiKhoanDTO> getTaiKhoanAll(){
        return listTaiKhoan;
    }
    
    public TaiKhoanDTO getTaiKhoan(int index){
        return listTaiKhoan.get(index);
    }
    
    public int getTaiKhoanByMaNV(int manv){
        int i = 0;
        int vitri = -1;
        while (i < this.listTaiKhoan.size() && vitri == -1) {
            if (listTaiKhoan.get(i).getManv() == manv) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }
    
    public TaiKhoanDTO getByMaNV(int manv) {
        int index = getTaiKhoanByMaNV(manv);
        return index != -1 ? listTaiKhoan.get(index) : null;
    }
    
    public boolean add(TaiKhoanDTO tk){
        boolean success = TaiKhoanDAO.getInstance().insert(tk) > 0;
        if(success) {
            listTaiKhoan.add(tk);
        }
        return success;
    }
    
    public boolean update(TaiKhoanDTO tk){
        int index = getTaiKhoanByMaNV(tk.getManv());
        if(index == -1) return false;
        
        boolean success = TaiKhoanDAO.getInstance().update(tk) > 0;
        if(success) {
            listTaiKhoan.set(index, tk);
        }
        return success;
    }
    
    public boolean delete(int manv){
        int index = getTaiKhoanByMaNV(manv);
        if(index == -1) return false;
        
        boolean success = TaiKhoanDAO.getInstance().delete(manv+"") > 0;
        if(success) {
            listTaiKhoan.remove(index);
        }
        return success;
    }
    
    public ArrayList<TaiKhoanDTO> search(String txt, String type) {
        ArrayList<TaiKhoanDTO> result = new ArrayList<>();
        txt = txt.toLowerCase();
        switch (type) {
            case "Tất cả" -> {
                for (TaiKhoanDTO i : listTaiKhoan) {
                    if (Integer.toString(i.getManv()).contains(txt) || i.getUsername().contains(txt) ) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhân viên" -> {
                for (TaiKhoanDTO i : listTaiKhoan) {
                    if (Integer.toString(i.getManv()).contains(txt)) {
                        result.add(i);
                    }
                }
            }
            case "Username" -> {
                for (TaiKhoanDTO i : listTaiKhoan) {
                    if (i.getUsername().toLowerCase().contains(txt)) {
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }
}