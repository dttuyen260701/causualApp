package com.example.pbl6app.Retrofit;
/*
 * Created by tuyen.dang on 11/9/2022
 */

public class ResponseRetrofit <T>  {
    private boolean isSuccessed;
    private String message;
    private T resultObj;

    public ResponseRetrofit(boolean isSuccessed, String message, T resultObj) {
        this.isSuccessed = isSuccessed;
        this.message = message;
        this.resultObj = resultObj;
    }

    public boolean isSuccessed() {
        return isSuccessed;
    }

    public void setSuccessed(boolean successed) {
        isSuccessed = successed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResultObj() {
        return resultObj;
    }

    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }
}
