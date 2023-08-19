package com.example.btl_adr;

public class Task {
    private int maTask;
    private String userTask;
    private String tenTask;
    private String moTaTask;
    private String loaiTask;
    private String thoiGianTask;
    private int trangThaiTask;

    public Task(int maTask, String userTask, String tenTask, String moTaTask, String loaiTask,
                String thoiGianTask, int trangThaiTask) {
        this.maTask = maTask;
        this.userTask = userTask;
        this.tenTask = tenTask;
        this.moTaTask = moTaTask;
        this.loaiTask = loaiTask;
        this.thoiGianTask = thoiGianTask;
        this.trangThaiTask = trangThaiTask;
    }

    public int getMaTask() {
        return maTask;
    }

    public void setMaTask(int maTask) {
        this.maTask = maTask;
    }

    public String getUserTask() {
        return userTask;
    }

    public void setUserTask(String userTask) {
        this.userTask = userTask;
    }

    public String getTenTask() {
        return tenTask;
    }

    public void setTenTask(String tenTask) {
        this.tenTask = tenTask;
    }

    public String getMoTaTask() {
        return moTaTask;
    }

    public void setMoTaTask(String moTaTask) {
        this.moTaTask = moTaTask;
    }

    public String getLoaiTask() {
        return loaiTask;
    }

    public void setLoaiTask(String loaiTask) {
        this.loaiTask = loaiTask;
    }

    public String getThoiGianTask() {
        return thoiGianTask;
    }

    public void setThoiGianTask(String thoiGianTask) {
        this.thoiGianTask = thoiGianTask;
    }

    public int getTrangThaiTask() {
        return trangThaiTask;
    }

    public void setTrangThaiTask(int trangThaiTask) {
        this.trangThaiTask = trangThaiTask;
    }
}