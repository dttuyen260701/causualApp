package com.example.pbl6app.Models;
/*
 * Created by tuyen.dang on 11/6/2022
 */

public class TypeOfJob {
    private int id;
    private String name;
    private String linkIMG;

    public TypeOfJob(int id, String name, String linkIMG) {
        this.id = id;
        this.name = name;
        this.linkIMG = linkIMG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
