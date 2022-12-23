package com.example.pbl6app.Models;
/*
 * Created by tuyen.dang on 11/9/2022
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WorkerDetail extends Worker {

    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("workingTime")
    private String workingTime;
    @SerializedName("workerStatus")
    private int workerStatus;

    public WorkerDetail(String id, String name, String linkIMG, Rate rate, int totalReviews, ArrayList<JobInfo> listJobInfo, String address, String phone, String workingTime, int workerStatus) {
        super(id, name, linkIMG, rate, totalReviews, listJobInfo);
        this.address = address;
        this.phone = phone;
        this.workingTime = workingTime;
        this.workerStatus = workerStatus;
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

    public void  setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public int isWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(int workerStatus) {
        this.workerStatus = workerStatus;
    }
}
