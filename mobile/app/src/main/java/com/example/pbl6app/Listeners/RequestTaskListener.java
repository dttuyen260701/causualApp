package com.example.pbl6app.Listeners;

public interface RequestTaskListener {
    public void onPre();
    public void onEnd(boolean value, boolean done, String message);
}
