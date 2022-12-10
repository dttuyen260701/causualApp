package com.example.pbl6app.Models;

public class ObjectTracking {
    private String longitude;
    private String latitude;
    private String distance;

    public ObjectTracking(String longitude, String latitude, String distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }

    public ObjectTracking() {}

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "ObjectTracking{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
