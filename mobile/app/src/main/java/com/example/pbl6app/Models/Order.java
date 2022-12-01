package com.example.pbl6app.Models;

import com.google.gson.annotations.SerializedName;
public class Order {
    @SerializedName("id")
    private String id;
    @SerializedName("jobId")
    private String jobId;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("workerId")
    private String workerId;
    @SerializedName("note")
    private String note;
    @SerializedName("jobPrices")
    private int jobPrices;
    @SerializedName("creationTime")
    private String creationTime;
    @SerializedName("userAddress")
    private String userAddress;
    @SerializedName("userPoint")
    private String userPoint;
    @SerializedName("isPaid")
    private boolean isPaid;
    @SerializedName("status")
    private String status;
    @SerializedName("jobInfoName")
    private String jobInfoName;
    @SerializedName("workerName")
    private String workerName;

    public Order(String id, String jobId, String customerId, String workerId, String note, int jobPrices, String creationTime, String userAddress, String userPoint, boolean isPaid, String status, String jobInfoName, String workerName) {
        this.id = id;
        this.jobId = jobId;
        this.customerId = customerId;
        this.workerId = workerId;
        this.note = note;
        this.jobPrices = jobPrices;
        this.creationTime = creationTime;
        this.userAddress = userAddress;
        this.userPoint = userPoint;
        this.isPaid = isPaid;
        this.status = status;
        this.jobInfoName = jobInfoName;
        this.workerName = workerName;
    }

    public Order(String jobInfoName, String creationTime, String workerName, String status){
        this.jobInfoName = jobInfoName;
        this.creationTime = creationTime;
        this.workerName = workerName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getJobPrices() {
        return jobPrices;
    }

    public void setJobPrices(int jobPrices) {
        this.jobPrices = jobPrices;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(String userPoint) {
        this.userPoint = userPoint;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobInfoName() {
        return jobInfoName;
    }

    public void setJobInfoName(String jobInfoName) {
        this.jobInfoName = jobInfoName;
    }
    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
}
