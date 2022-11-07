package com.example.pbl6app.Models;

public class Worker {
    private int id;
    private String name;
    private String linkIMG;
    private int idTypeOfJob;
    private String nameTypeOfJob;
    private float rate;
    private int totalReviews;

    public Worker(int id, String name, String linkIMG, int idTypeOfJob, String nameTypeOfJob, float rate, int totalReviews) {
        this.id = id;
        this.name = name;
        this.linkIMG = linkIMG;
        this.idTypeOfJob = idTypeOfJob;
        this.nameTypeOfJob = nameTypeOfJob;
        this.rate = rate;
        this.totalReviews = totalReviews;
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

    public int getIdTypeOfJob() {
        return idTypeOfJob;
    }

    public void setIdTypeOfJob(int idTypeOfJob) {
        this.idTypeOfJob = idTypeOfJob;
    }

    public String getNameTypeOfJob() {
        return nameTypeOfJob;
    }

    public void setNameTypeOfJob(String nameTypeOfJob) {
        this.nameTypeOfJob = nameTypeOfJob;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }
}
