package com.example.pbl6app.Models;

public class AddressTemp {
    private String id;
    private String name;
    private boolean isCheck;

    public AddressTemp(String id, String name) {
        this.id = id;
        this.name = name;
        this.isCheck = false;
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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
