package com.example.btl_adr;

public class user {

    private String taikhoan;
    private String matkhau;
    private String hoten;

    public user(String taikhoan, String matkhau, String hoten) {
        this.taikhoan = taikhoan;
        this.matkhau = matkhau;
        this.hoten = hoten;
    }
    public String toString() {
        return " TK " + taikhoan + " MK " + matkhau + " TÊN " + hoten;
    }

    public String getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(String taikhoan) {
        this.taikhoan = taikhoan;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }
}
