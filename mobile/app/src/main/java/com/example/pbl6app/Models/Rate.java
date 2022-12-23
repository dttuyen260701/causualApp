package com.example.pbl6app.Models;

import com.google.gson.annotations.SerializedName;

public class Rate {
    @SerializedName("attitudeRate")
    private int attitudeRateAverage;
    @SerializedName("skillRate")
    private int skillRateAverage;
    @SerializedName("pleasureRate")
    private int pleasureRateAverage;
    @SerializedName("rateAverage")
    private float rateAverage;
    @SerializedName("comment")
    private String comment;
    @SerializedName("customerImage")
    private String customerImage;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("creationTime")
    private String creationTime;

    public Rate(int attitudeRateAverage, int skillRateAverage, int pleasureRateAverage, float rateAverage, String comment, String customerImage, String customerName, String orderId) {
        this.attitudeRateAverage = attitudeRateAverage;
        this.skillRateAverage = skillRateAverage;
        this.pleasureRateAverage = pleasureRateAverage;
        this.rateAverage = rateAverage;
        this.comment = comment;
        this.customerImage = customerImage;
        this.customerName = customerName;
        this.orderId = orderId;
    }

    public float getAttitudeRateAverage() {
        return attitudeRateAverage;
    }

    public void setAttitudeRateAverage(int attitudeRateAverage) {
        this.attitudeRateAverage = attitudeRateAverage;
    }

    public int getSkillRateAverage() {
        return skillRateAverage;
    }

    public void setSkillRateAverage(int skillRateAverage) {
        this.skillRateAverage = skillRateAverage;
    }

    public int getPleasureRateAverage() {
        return pleasureRateAverage;
    }

    public void setPleasureRateAverage(int pleasureRateAverage) {
        this.pleasureRateAverage = pleasureRateAverage;
    }

    public float getRateAverage() {
        return rateAverage;
    }

    public void setRateAverage(float rateAverage) {
        this.rateAverage = rateAverage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
}
