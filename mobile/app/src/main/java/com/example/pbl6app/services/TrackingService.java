package com.example.pbl6app.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.pbl6app.Models.ObjectTracking;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.activities.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackingService extends Service {

    private static final int NOTIFICATION_ID = 111;
    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static LocationCallback locationCallback;
    private static LatLng start, end;
    private static int id = 0;
    private static LocationRequest locationRequest;
    private static String workerId = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String data = intent.getStringExtra("UserPoint");
        workerId = intent.getStringExtra("WorkerId");
        end = new LatLng(Float.parseFloat(data.split("-")[0]), Float.parseFloat(data.split("-")[1]));
        startTracking();
        return START_NOT_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void startTracking() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(1000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    start = new LatLng(location.getLatitude(), location.getLongitude());
                    FirebaseRepository.TrackingChild.child(workerId).child(id++ + "").setValue(
                            new ObjectTracking(
                                    location.getLongitude() + "",
                                    location.getLatitude() + "",
                                    calculateDistance() + " km"
                            ));
                    createNotification();
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void createNotification() {
        check_SDK_Notification();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Constant.CHANNEL_ID);
        Intent intent = new Intent(this, MainActivity.class);
        //set quay lại màn hình chính dùng set Action + add  Category
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // Create the PendingIntent, FLAG_UPDATE_CURRENT: mở được nheièu lần, FLAG_ONE_SHOT: mở được 1 lần
        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_MUTABLE);
        builder.setSmallIcon(R.drawable.ic_app);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food_delivery);
        builder.setLargeIcon(bitmap);
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setOngoing(true);
        builder.setContentIntent(pendingIntent);
        builder.setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE);
        builder.setContentTitle("Khoảng cách của bạn đến khách hàng:");
        builder.setContentText(calculateDistance() + " km");
        builder.setSilent(true);
        startForeground(NOTIFICATION_ID, builder.build());
    }

    private void check_SDK_Notification() {
//        //channel cho phiên bản mới
//        CharSequence name = "Notification";
//        String des = "This is my Personal Notification";
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
//
//        channel.setDescription(des);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.createNotificationChannel(channel);
    }

    private float calculateDistance() {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude,
                end.latitude, end.longitude,
                results);
        return ((float) Math.round(results[0] / 1000 * 100) / 100);
    }

    @Override
    public void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        FirebaseRepository.TrackingChild.child(workerId).setValue("");
        super.onDestroy();
    }
}
