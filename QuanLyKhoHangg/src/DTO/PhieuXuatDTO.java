/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Tran Nhat Sinh
 */
public class PhieuXuatDTO extends PhieuDTO{
    private int maKh;

    public PhieuXuatDTO(int maKH) {
        this.maKh = maKH;
    }

    public PhieuXuatDTO(int maKh, int maphieu, int manguoitao, Timestamp thoigiantao, long tongTien, int trangthai) {
        super(maphieu, manguoitao, thoigiantao, tongTien, trangthai);
        this.maKh = maKh;
    }

    public int getMakh() {
        return maKh;
    }

    public void setMakh(int makh) {
        this.maKh = makh;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.maKh;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PhieuXuatDTO other = (PhieuXuatDTO) obj;
        return this.maKh == other.maKh;
    }

    @Override
    public String toString() {
        return "PhieuXuatDTO{" + "makh=" + maKh + '}';
    }

    
}
