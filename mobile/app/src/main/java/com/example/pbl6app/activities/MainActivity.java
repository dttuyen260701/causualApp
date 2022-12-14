package com.example.pbl6app.activities;

import static com.example.pbl6app.Utils.Methods.toStringNumber;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.pbl6app.fragment.OrderInQueueFragment;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private static boolean isChecking = false;
    private static ChildEventListener childEventListener;

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

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                assert order != null;
                Methods.showDialog(
                        R.drawable.sad_dialog,
                        "Bạn có đơn hàng mới!!!",
                        "Công việc " +
                                order.getJobInfoName() +
                                " với giá " +
                                toStringNumber(Integer.parseInt(order.getJobPrices())) +
                                " đ được yêu cầu từ " + order.getCustomerName() + " !!!",
                        "Xem sau",
                        "Xem chi tiết",
                        new ListenerDialog() {
                            @Override
                            public void onNoClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onYesClick(Dialog dialog) {
                                addFragment(new OrderInQueueFragment(order), R.id.layoutMain, true);
                                dialog.dismiss();
                            }
                        }
                );

//                FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).child(order.getCustomerId()).removeValue();

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

        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).addChildEventListener(childEventListener);
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

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ctFragmentUser, fragment);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeEventListener(childEventListener);
        Constant.USER = new User();
        super.onStop();
    }
}