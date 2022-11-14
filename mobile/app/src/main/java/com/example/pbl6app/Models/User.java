package com.example.pbl6app.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("userName")
    private String userName;
    @SerializedName("phone")
    private String phoneNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("identityCard")
    private String identityCard;
    @SerializedName("identityCardDate")
    private String identityCardDate;
    @SerializedName("identityCardBy")
    private String identityCardBy;
    @SerializedName("gender")
    private int gender;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("startWorkingTime")
    private String startWorkingTime;
    @SerializedName("endWorkingTime")
    private String endWorkingTime;
    @SerializedName("provinceId")
    private String provinceId;
    @SerializedName("provinceName")
    private String provinceName;
    @SerializedName("districtName")
    private String districtName;
    @SerializedName("districtId")
    private String districtId;
    @SerializedName("wardId")
    private String wardId;
    @SerializedName("wardName")
    private String wardName;
    @SerializedName("address")
    private String address;
    @SerializedName("addressPoint")
    private String addressPoint;
    @SerializedName("role")
    private String role;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("lastModificationTime")
    private String lastModificationTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getIdentityCardDate() {
        return identityCardDate;
    }

    public void setIdentityCardDate(String identityCardDate) {
        this.identityCardDate = identityCardDate;
    }

    public String getIdentityCardBy() {
        return identityCardBy;
    }

    public void setIdentityCardBy(String identityCardBy) {
        this.identityCardBy = identityCardBy;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStartWorkingTime() {
        return startWorkingTime;
    }

    public void setStartWorkingTime(String startWorkingTime) {
        this.startWorkingTime = startWorkingTime;
    }

    public String getEndWorkingTime() {
        return endWorkingTime;
    }

    public void setEndWorkingTime(String endWorkingTime) {
        this.endWorkingTime = endWorkingTime;
    }

    public String getProvinceId() {return provinceId;}

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictId() {return districtId;}

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(String lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }
}
