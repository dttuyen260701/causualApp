package com.example.pbl6app.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pbl6app.Models.ObjectTracking;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MapTrackingFragment extends SupportMapFragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    private String address_line;
    private LatLng EndP;
    private MapTrackingFragmentParent parent;
    private String idWorker;
    private LatLng value;

    private ChildEventListener childEventListener;

    public MapTrackingFragment(MapTrackingFragmentParent parent, String idWorker, String userPoint) {
        this.idWorker = idWorker;
        this.parent = parent;
        EndP = new LatLng(Float.parseFloat(userPoint.split("-")[0]), Float.parseFloat(userPoint.split("-")[1]));
        address_line = "";
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mGoogleMap == null) {
            getMapAsync(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ObjectTracking objectTracking = null;
                try {
                    objectTracking = snapshot.getValue(ObjectTracking.class);
                } catch (Exception e) {
                    return;
                }

                if (value == null || value.latitude != Float.valueOf(objectTracking.getLatitude())
                        && value.longitude != Float.valueOf(objectTracking.getLongitude())) {
                    value = new LatLng(Float.valueOf(objectTracking.getLatitude()), Float.valueOf(objectTracking.getLongitude()));

                    parent.setTextDistance(objectTracking.getDistance());

                    mGoogleMap.clear();

                    Bitmap workerBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.food_delivery);
                    Bitmap workerMarker = Bitmap.createScaledBitmap(workerBitMap, 50, 50, false);

                    Bitmap userBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.user);
                    Bitmap userMarker = Bitmap.createScaledBitmap(userBitMap, 50, 50, false);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(value);
                    if (EndP != null) {
                        builder.include(EndP);
                    }
                    mGoogleMap.addMarker(new MarkerOptions()
                                    .position(EndP)
                                    .title("You"))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(userMarker));
                    LatLngBounds bounds = builder.build();
                    int padding = 100; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mGoogleMap.addMarker(new MarkerOptions()
                                    .position(value)
                                    .title("Worker"))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(workerMarker));
                    mGoogleMap.moveCamera(cu);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseRepository.TrackingChild.child(idWorker).addChildEventListener(childEventListener);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onDestroy() {
        FirebaseRepository.TrackingChild.child(idWorker).removeEventListener(childEventListener);
        super.onDestroy();
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

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
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
