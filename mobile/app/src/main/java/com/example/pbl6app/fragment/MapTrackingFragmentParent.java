package com.example.pbl6app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbl6app.Listeners.Listener_for_PickAddress;
import com.example.pbl6app.R;

public class MapTrackingFragmentParent extends Fragment {
    private ImageView img_Back_Map_Frag;
    private TextView tvDistance;
    private ConstraintLayout layout_MapFragment;
    private static Button btnSave_Location;
    private String workerId = "";
    private String userPoint = "";

    public MapTrackingFragmentParent(String workerId, String userPoint) {
        this.workerId = workerId;
        this.userPoint = userPoint;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking_map, container, false);
        SetUp(view);
        return view;
    }

    private void SetUp(View view) {
        MapTrackingFragment mapFragment = new MapTrackingFragment(this, workerId, userPoint);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);

        img_Back_Map_Frag = (ImageView) view.findViewById(R.id.img_Back_Map_Frag);
        img_Back_Map_Frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        layout_MapFragment = (ConstraintLayout) view.findViewById(R.id.layout_MapFragment);
        btnSave_Location = (Button) view.findViewById(R.id.btnSave_Location);
        btnSave_Location.setVisibility(View.GONE);

        tvDistance.setText("0 km");

        btnSave_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //add để trạng thái trước được lưu
        transaction.add(R.id.layout_MapFragment, mapFragment);
        transaction.commit();
    }

    public void setTextDistance(String distance) {
        tvDistance.setText(distance);
    }
}
