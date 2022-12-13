package com.example.pbl6app.Models;
/*
 * Created by tuyen.dang on 11/6/2022
 */

import com.google.gson.annotations.SerializedName;

public class TypeOfJob {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String linkIMG;
    @SerializedName("description")
    private String description;

    public TypeOfJob(String id, String name, String linkIMG) {
        this.id = id;
        this.name = name;
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

    public String getLinkIMG() {
        return linkIMG;
    }

    public void setLinkIMG(String linkIMG) {
        this.linkIMG = linkIMG;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
