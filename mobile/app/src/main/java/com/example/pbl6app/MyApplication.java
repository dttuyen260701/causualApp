package com.example.pbl6app;

import static com.example.pbl6app.Utils.Constant.CHANNEL_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class MyApplication extends Application {

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
