package com.example.pbl6app.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Worker {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("linkIMG")
    private String linkIMG;
    @SerializedName("rateDetail")
    private Rate rateDetail;
    @SerializedName("totalReviews")
    private int totalReviews;
    @SerializedName("listJobInfo")
    private ArrayList<JobInfo> listJobInfo;

    public Worker(String id, String name, String linkIMG, Rate rate, int totalReviews, ArrayList<JobInfo> listJobInfo) {
        this.id = id;
        this.name = name;
        this.linkIMG = linkIMG;
        this.rateDetail = rate;
        this.totalReviews = totalReviews;
        this.listJobInfo = listJobInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkIMG() {
        return linkIMG;
    }

    public void setLinkIMG(String linkIMG) {
        this.linkIMG = linkIMG;
    }

    public Rate getRate() {
        return rateDetail;
    }

    public void setRate(Rate rate) {
        this.rateDetail = rate;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public ArrayList<JobInfo> getListJobList(){
        return this.listJobInfo;
    }

    public String getListJobInfo() {
        String temp = "";
        for (int i = 0; i < this.listJobInfo.size(); ++i) {
            temp += ((i == 0) ? "" : " ") + listJobInfo.get(i).getName() + ((i == this.listJobInfo.size() - 1) ? "." : ",");
        }
        return temp;
    }

    public void setListJobInfo(ArrayList<JobInfo> listJobInfo) {
        this.listJobInfo = listJobInfo;
    }
}
