package com.example.pbl6app.Models;
/*
 * Created by tuyen.dang on 11/9/2022
 */

public class WorkerDetail extends Worker {

    private String address;
    private String phone;
    private String workingTime;
    private boolean isActive;

    public WorkerDetail(int id, String name, String linkIMG, int idTypeOfJob, String nameTypeOfJob, float rate, int totalReviews, String address, String phone, String workingTime, boolean isActive) {
        super(id, name, linkIMG, idTypeOfJob, nameTypeOfJob, rate, totalReviews);
        this.address = address;
        this.phone = phone;
        this.workingTime = workingTime;
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
