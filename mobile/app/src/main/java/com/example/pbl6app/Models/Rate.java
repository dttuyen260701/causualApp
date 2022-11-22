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
}
