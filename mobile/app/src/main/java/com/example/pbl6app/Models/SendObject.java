package com.example.pbl6app.Models;/*
 * Created by tuyen.dang on 12/7/2022
 */

public class SendObject {
    private String type;
    private String idSend;
    private String idReceiver;
    private String message;

    public SendObject() {
    }

    public SendObject(String type, String idSend, String idReceiver, String message) {
        this.type = type;
        this.idSend = idSend;
        this.idReceiver = idReceiver;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdSend() {
        return idSend;
    }

    public void setIdSend(String idSend) {
        this.idSend = idSend;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
