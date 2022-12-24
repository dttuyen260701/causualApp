package com.example.pbl6app.Utils;/*
 * Created by tuyen.dang on 12/11/2022
 */

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {
    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final static DatabaseReference myRef = database.getReference("data");

    public final static DatabaseReference TrackingChild = myRef.child("Tracking");

    public final static DatabaseReference PickWorkerChild = myRef.child("Working");

    public final static DatabaseReference UpdateOrderChild = myRef.child("Order");

    public final static DatabaseReference PickPostChild = myRef.child("Post");

    public final static DatabaseReference ResponsePost = myRef.child("ResponsePost");
}
