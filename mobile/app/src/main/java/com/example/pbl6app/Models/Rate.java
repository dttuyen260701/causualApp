package com.example.pbl6app.Models;

import com.google.gson.annotations.SerializedName;

public class Rate {
    @SerializedName("attitudeRateAverage")
    private float attitudeRateAverage;
    @SerializedName("skillRateAverage")
    private float skillRateAverage;
    @SerializedName("pleasureRateAverage")
    private float pleasureRateAverage;
    @SerializedName("rateAverage")
    private float rateAverage;
    @SerializedName("comment")
    private String comment;
    @SerializedName("customerImage")
    private String customerImage;
    @SerializedName("customerName")
    private String customerName;


    public Rate(float attitudeRateAverage, float skillRateAverage, float pleasureRateAverage, float rateAverage) {
        this.attitudeRateAverage = attitudeRateAverage;
        this.skillRateAverage = skillRateAverage;
        this.pleasureRateAverage = pleasureRateAverage;
        this.rateAverage = rateAverage;
    }

    public float getAttitudeRateAverage() {
        return attitudeRateAverage;
    }

    public void setAttitudeRateAverage(float attitudeRateAverage) {
        this.attitudeRateAverage = attitudeRateAverage;
    }

    public float getSkillRateAverage() {
        return skillRateAverage;
    }

    public void setSkillRateAverage(float skillRateAverage) {
        this.skillRateAverage = skillRateAverage;
    }

    public float getPleasureRateAverage() {
        return pleasureRateAverage;
    }

    public void setPleasureRateAverage(float pleasureRateAverage) {
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
}
