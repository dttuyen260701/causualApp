package com.example.pbl6app.Models;
/*
 * Created by tuyen.dang on 11/19/2022
 */

import com.google.gson.annotations.SerializedName;

public class JobInfo {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("description")
    private String description;
    @SerializedName("typeOfJobId")
    private String typeOfJobId;
    @SerializedName("typeOfJobName")
    private String typeOfJobName;
    @SerializedName("image")
    private String linkIMG;

    public JobInfo() {
    }

    public JobInfo(String id) {
        this.id = id;
    }

    public JobInfo(String id, String name, String price, String description, String typeOfJobId, String typeOfJobName, String linkIMG) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.typeOfJobId = typeOfJobId;
        this.typeOfJobName = typeOfJobName;
        this.linkIMG = linkIMG;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfJobId() {
        return typeOfJobId;
    }

    public void setTypeOfJobId(String typeOfJobId) {
        this.typeOfJobId = typeOfJobId;
    }

    public String getTypeOfJobName() {
        return typeOfJobName;
    }

    public void setTypeOfJobName(String typeOfJobName) {
        this.typeOfJobName = typeOfJobName;
    }

    public String getLinkIMG() {
        return linkIMG;
    }

    public void setLinkIMG(String linkIMG) {
        this.linkIMG = linkIMG;
    }
}
