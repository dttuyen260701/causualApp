package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.ActivityMainUserBinding;
import com.example.pbl6app.fragment.NewfeedFragment;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;
import com.example.pbl6app.fragment.UserHomeFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivityUser extends BaseActivity {

    private ActivityMainUserBinding binding;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Methods.setContext(MainActivityUser.this);

        initView();
        initListener();
    }

    private void initView() {
//        Toast.makeText(MainActivityUser.this, "Chào mừng " + Constant.USER.getName() + " !!!", Toast.LENGTH_SHORT).show();
        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Home);
        UserHomeFragment fragment = new UserHomeFragment();
        addFragment(fragment, R.id.ctFragmentUser, false);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = snapshot.getValue(Order.class);
                assert order != null;
                int drawble;
                String result = "";
                ListenerDialog listenerDialog;
                switch (order.getStatus()) {
                    case Constant.REJECT_STATUS:
                        drawble = R.drawable.sad_dialog;
                        result = " đã từ chối đơn của bạn !!!";
                        listenerDialog = new ListenerDialog() {
                            @Override
                            public void onNoClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onYesClick(Dialog dialog) {

                            }
                        };
                        break;
                    case Constant.ACCEPT_STATUS:
                        drawble = R.drawable.smile_dialog;
                        result = " đã nhận đơn của bạn !!!";
                        listenerDialog = new ListenerDialog() {
                            @Override
                            public void onNoClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onYesClick(Dialog dialog) {

                            }
                        };
                        break;
                    default:
                        drawble = R.drawable.sad_dialog;
                        listenerDialog = new ListenerDialog() {
                            @Override
                            public void onNoClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onYesClick(Dialog dialog) {

                            }
                        };
                        break;
                }

                Methods.showDialog(
                        drawble,
                        "Thợ " + order.getWorkerName() + result,
                        "Công việc " + order.getJobInfoName() + " với giá " + order.getJobPrices(),
                        "Đã hiểu",
                        "Xem chi tiết",
                        listenerDialog
                );
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

        //FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).addChildEventListener(childEventListener);
    }

    @SuppressLint("NonConstantResourceId")
    private void initListener() {
        binding.bottomNavigationUser.setOnItemSelectedListener(item -> {
            new Thread(() -> {
                clearBackstack();
                Thread.currentThread().interrupt();
            }).start();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_status:
                    fragment = new StatusFragment();
                    break;
                case R.id.menu_Home:
                    fragment = new UserHomeFragment();
                    break;
                case R.id.menu_Setting:
                    fragment = new SettingsFragment();
                    break;
                case R.id.menu_newFeed:
                    fragment = new NewfeedFragment();
                    break;
                default:
                    return true;
            }
            replaceFragment(fragment, R.id.ctFragmentUser, false);
            return true;
        });
    }

    @Override
    protected void onStop() {
        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeEventListener(childEventListener);
        Constant.USER = new User();
        super.onStop();
    }
}