package com.example.pbl6app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class MyApplication extends Application {
    private static final String CHANNEL_ID = "CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChanel();

    }

    private void createNotificationChanel() {
        CharSequence name = "Notification";
        String des = "This is my Personal Notification";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);

        channel.setDescription(des);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }
}
