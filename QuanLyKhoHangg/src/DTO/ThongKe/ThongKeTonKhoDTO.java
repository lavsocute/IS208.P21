package DTO.ThongKe;

import java.util.Objects;

public class ThongKeTonKhoDTO {
    int masp;
    String tensanpham;
    int tondauky;
    int nhaptrongky;
    int xuattrongky;
    int toncuoiky;

    public ThongKeTonKhoDTO() {
    }

    public ThongKeTonKhoDTO(int masp, String tensanpham, int tondauky, int nhaptrongky, int xuattrongky, int toncuoiky) {
        this.masp = masp;
        this.tensanpham = tensanpham;
        this.tondauky = tondauky;
        this.nhaptrongky = nhaptrongky;
        this.xuattrongky = xuattrongky;
        this.toncuoiky = toncuoiky;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public int getTondauky() {
        return tondauky;
    }

    public void setTondauky(int tondauky) {
        this.tondauky = tondauky;
    }

    public int getNhaptrongky() {
        return nhaptrongky;
    }

    public void setNhaptrongky(int nhaptrongky) {
        this.nhaptrongky = nhaptrongky;
    }

    public int getXuattrongky() {
        return xuattrongky;
    }

    public void setXuattrongky(int xuattrongky) {
        this.xuattrongky = xuattrongky;
    }

    public int getToncuoiky() {
        return toncuoiky;
    }

    public void setToncuoiky(int toncuoiky) {
        this.toncuoiky = toncuoiky;
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
        final ThongKeTonKhoDTO other = (ThongKeTonKhoDTO) obj;
        if (this.masp != other.masp) {
            return false;
        }
        if (this.tondauky != other.tondauky) {
            return false;
        }
        if (this.nhaptrongky != other.nhaptrongky) {
            return false;
        }
        if (this.xuattrongky != other.xuattrongky) {
            return false;
        }
        if (this.toncuoiky != other.toncuoiky) {
            return false;
        }
        return Objects.equals(this.tensanpham, other.tensanpham);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.masp;
        hash = 29 * hash + Objects.hashCode(this.tensanpham);
        hash = 29 * hash + this.tondauky;
        hash = 29 * hash + this.nhaptrongky;
        hash = 29 * hash + this.xuattrongky;
        hash = 29 * hash + this.toncuoiky;
        return hash;
    }

    @Override
    public String toString() {
        return "ThongKeTonKhoDTO{" + "masp=" + masp + ", tensanpham=" + tensanpham + ", tondauky=" + tondauky 
                + ", nhaptrongky=" + nhaptrongky + ", xuattrongky=" + xuattrongky + ", toncuoiky=" + toncuoiky + '}';
    }
}