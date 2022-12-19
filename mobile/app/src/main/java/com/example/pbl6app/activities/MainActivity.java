package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.ActivityMainBinding;
import com.example.pbl6app.fragment.NewfeedFragment;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private static boolean isChecking = false;
    private static ValueEventListener valueEventListener;

    @Override
    public void onBackPressed() {
        if (!isChecking) {
            super.onBackPressed();
        }
    }

    public static void setIsChecking(boolean isChecking) {
        MainActivity.isChecking = isChecking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Methods.setContext(MainActivity.this);

        initView();

        initListener();

        onNewIntent(getIntent());
    }

    private void initView() {
        binding.bottomNavigation.setSelectedItemId(R.id.menu_newFeed);
        Fragment fragment = new NewfeedFragment();
        addFragment(fragment);
//        addFragment(new TrackingFragment());
//        addFragment(new MapTrackingFragmentParent(new Listener_for_PickAddress() {
//            @Override
//            public void onClick_pick(String address, String point) {
//
//            }
//        }));

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> listData = new ArrayList<>();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    try {
                        listData.add(orderSnapshot.getValue(Order.class));
                    } catch (Exception e) {
                        Log.e("PARSE_ORDER_WORKER", "onChildAdded: ", e);
                    }

                }

                if (listData.size() > 0) {
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Bạn có " + listData.size() + " yêu cầu công việc mới!!!",
                            ((listData.size() > 1) ? "Bao gồm yêu cầu " : "Yêu cầu") +
                                    listData.get(0).getJobInfoName() +
                                    " được yêu cầu từ " + listData.get(0).getCustomerName() +
                                    ((listData.size() > 1) ? "và những yêu cầu khác" : ""),
                            "Xem sau",
                            "Xem chi tiết",
                            new ListenerDialog() {

                                @Override
                                public void onDismiss() {
                                    FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeValue();
                                }

                                @Override
                                public void onNoClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onYesClick(Dialog dialog) {
                                    binding.bottomNavigation.setSelectedItemId(R.id.menu_status);
                                    dialog.dismiss();
                                }
                            }
                    );

                    Methods.sendNotification(
                            "Bạn có " + listData.size() + " yêu cầu công việc mới!!!",
                            ((listData.size() > 1) ? "Bao gồm yêu cầu " : "Yêu cầu") +
                                    listData.get(0).getJobInfoName() +
                                    " được yêu cầu từ " + listData.get(0).getCustomerName() +
                                    ((listData.size() > 1) ? " và những yêu cầu khác" : ""),
                            R.drawable.smile_dialog);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).addValueEventListener(valueEventListener);
    }

    @SuppressLint("NonConstantResourceId")
    private void initListener() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            new Thread(() -> {
                clearBackstack();
                Thread.currentThread().interrupt();
            }).start();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_Info:
                    fragment = new SettingsFragment();
                    break;
                case R.id.menu_status:
                    fragment = new StatusFragment();
                    break;
                case R.id.menu_newFeed:
                    fragment = new NewfeedFragment();
                    break;
                default:
                    return true;
            }
            addFragment(fragment);
            return true;
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(intent != null){
            int result = intent.getIntExtra("KEY_SETTING_FRAG", 0);
            if(result == 1) {
                FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeValue();
                binding.bottomNavigation.setSelectedItemId(R.id.menu_status);
            }
        }
        super.onNewIntent(intent);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ctFragmentUser, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeEventListener(valueEventListener);
        Constant.USER = new User();
        super.onDestroy();
    }
}