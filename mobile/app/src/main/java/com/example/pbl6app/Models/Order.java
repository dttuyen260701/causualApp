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
    private String jobPrices;
    @SerializedName("creationTime")
    private String creationTime;
    @SerializedName("userAddress")
    private String userAddress;
    @SerializedName("userPoint")
    private String userPoint;
    @SerializedName("isPaid")
    private boolean isPaid;
    @SerializedName("status")
    private int status;
    @SerializedName("jobInfoName")
    private String jobInfoName;
    @SerializedName("jobInfoImage")
    private String jobInfoImage;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("customerImage")
    private String customerImage;
    @SerializedName("customerPhone")
    private String customerPhone;
    @SerializedName("workerName")
    private String workerName;
    @SerializedName("workerImage")
    private String workerImage;
    @SerializedName("workerPhone")
    private String workerPhone;

    public Order() {
    }

    public Order(String id, String jobId, String customerId, String workerId, String note, String jobPrices, String creationTime, String userAddress, String userPoint, boolean isPaid, int status, String jobInfoName, String jobInfoImage, String customerName, String customerImage, String customerPhone, String workerName, String workerImage, String workerPhone) {
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
        this.jobInfoImage = jobInfoImage;
        this.customerName = customerName;
        this.customerImage = customerImage;
        this.customerPhone = customerPhone;
        this.workerName = workerName;
        this.workerImage = workerImage;
        this.workerPhone = workerPhone;
    }

    public Order(String jobInfoName, String creationTime, String workerName, String status){
        this.id = "";
        this.jobInfoName = jobInfoName;
        this.creationTime = creationTime;
        this.workerName = workerName;
        this.status = Integer.parseInt(status);
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

    public String getJobPrices() {
        return jobPrices;
    }

    public void setJobPrices(String jobPrices) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJobInfoName() {
        return jobInfoName;
    }

    public void setJobInfoName(String jobInfoName) {
        this.jobInfoName = jobInfoName;
    }

    public String getJobInfoImage() {
        return jobInfoImage;
    }

    public void setJobInfoImage(String jobInfoImage) {
        this.jobInfoImage = jobInfoImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerImage() {
        return workerImage;
    }

    public void setWorkerImage(String workerImage) {
        this.workerImage = workerImage;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }
}
