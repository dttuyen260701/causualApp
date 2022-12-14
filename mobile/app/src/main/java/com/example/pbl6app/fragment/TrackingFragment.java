package com.example.pbl6app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.pbl6app.Models.ObjectTracking;
import com.example.pbl6app.databinding.FragmentTrackingBinding;
import com.example.pbl6app.services.TrackingService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TrackingFragment extends Fragment {
    private FragmentTrackingBinding binding;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private int id = 0;

    DatabaseReference usersRef;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrackingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        //set all properties of Location

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 30000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(1000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("data");
                DatabaseReference usersRef = myRef.child("Tracking").child("Id1");
                for (Location location : locationResult.getLocations()) {
                    updateUIValue(location);
                    Geocoder geocoder = new Geocoder(getActivity());
                    ArrayList<Address> list_address = new ArrayList<>();
                    try {
                        list_address = (ArrayList<Address>) geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        binding.tvAddress.setText(list_address.get(0).getAddressLine(0));
                    } catch (Exception e) {
                        binding.tvAddress.setText("Unable to get street address");
                    }
                    usersRef.child(id++ + "").setValue(
                            new ObjectTracking(
                                    location.getLongitude() + "",
                                    location.getLatitude() + "",
                                    list_address.isEmpty() ? "Empty" : list_address.get(0).getAddressLine(0)
                            ));
                }
            }
        };


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data");

    }

    private void initListener() {
        binding.swGps.setOnClickListener(view -> {
            if (binding.swGps.isChecked()) {
                binding.tvSensor.setText("Using GPS sensor");
                locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                        .setWaitForAccurateLocation(false)
                        .setMinUpdateIntervalMillis(1000)
                        .setMaxUpdateDelayMillis(1000)
                        .build();
            } else {
                binding.tvSensor.setText("Using Tower + wifi");
                locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000)
                        .setWaitForAccurateLocation(false)
                        .setMinUpdateIntervalMillis(1000)
                        .setMaxUpdateDelayMillis(1000)
                        .build();
            }

            updateGPS();
        });

        binding.swLocationsupdates.setOnClickListener(view -> {
            if (binding.swLocationsupdates.isChecked()) {
                startLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        });

        binding.btnNewWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnShowMap.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TrackingService.class);
            intent.putExtra("UserPoint", "15.9935033-108.2066692");
            getActivity().startService(intent);
        });

        binding.btnStop.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), TrackingService.class);
            getActivity().stopService(intent);
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        updateGPS();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateGPS() {
        //get permission
        //get the current location from the fused client
        //update the UI - i.e. set all properties in their associated tv items
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation
            // put value to UI
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), this::updateUIValue);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    private void updateUIValue(Location location) {
        binding.tvLat.setText(String.valueOf(location.getLatitude()));
        binding.tvLon.setText(String.valueOf(location.getLongitude()));
        binding.tvAccuracy.setText(String.valueOf(location.getAccuracy()));

        binding.tvAltitude.setText((location.hasAltitude()) ? String.valueOf(location.getAltitude()) : "Not available");

        binding.tvSpeed.setText((location.hasSpeed()) ? String.valueOf(location.getSpeed()) : "Not available");

        if (getActivity() != null) {
            Geocoder geocoder = new Geocoder(getActivity());
            ArrayList<Address> list_address;
            try {
                list_address = (ArrayList<Address>) geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                binding.tvAddress.setText(list_address.get(0).getAddressLine(0));
            } catch (Exception e) {
                binding.tvAddress.setText("Unable to get street address");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        updateGPS();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
