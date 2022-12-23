package com.example.pbl6app.Models;/*
 * Created by tuyen.dang on 12/2/2022
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostOfDemand {
    @SerializedName("id")
    private String id;
    @SerializedName("customerId")
    private String customerId;
    @SerializedName("imageUser")
    private String customerImage;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("endDateTime")
    private String endDateTime;
    @SerializedName("endDateTimeString")
    private String endDateTimeString;
    @SerializedName("creationTime")
    private String creationTime;
    @SerializedName("description")
    private String description;
    @SerializedName("note")
    private String note;
    @SerializedName("jobInfoId")
    private String jobInfoId;
    @SerializedName("jobInfoName")
    private String jobInfoName;
    @SerializedName("address")
    private String address;
    @SerializedName("addressPoint")
    private String addressPoint;
    @SerializedName("listWorkerRequestInPostOfDemandResponse")
    private ArrayList<Worker> listWorkerRequestInPostOfDemandResponse;

    public ArrayList<Worker> getListWorkerRequestInPostOfDemandResponse() {
        return listWorkerRequestInPostOfDemandResponse;
    }

    public void setListWorkerRequestInPostOfDemandResponse(ArrayList<Worker> listWorkerRequestInPostOfDemandResponse) {
        this.listWorkerRequestInPostOfDemandResponse = listWorkerRequestInPostOfDemandResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getEndDateTimeString() {
        return endDateTimeString;
    }

    public void setEndDateTimeString(String endDateTimeString) {
        this.endDateTimeString = endDateTimeString;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getJobInfoId() {
        return jobInfoId;
    }

    public void setJobInfoId(String jobInfoId) {
        this.jobInfoId = jobInfoId;
    }

    public String getJobInfoName() {
        return jobInfoName;
    }

    public void setJobInfoName(String jobInfoName) {
        this.jobInfoName = jobInfoName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressPoint() {
        return addressPoint;
    }

    public void setAddressPoint(String addressPoint) {
        this.addressPoint = addressPoint;
    }
}
