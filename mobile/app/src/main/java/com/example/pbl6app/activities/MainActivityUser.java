package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityUser extends BaseActivity {

    private static ActivityMainUserBinding binding;
    private static ValueEventListener valueEventListener;
    private static int countAccept = 0, countReject = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Methods.setContext(MainActivityUser.this);

        initView();
        initListener();
    }

    public static void setIdNavigate(int idNavigate) {
        binding.bottomNavigationUser.setSelectedItemId(idNavigate);
    }

    private void initView() {

        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Home);
        UserHomeFragment fragment = new UserHomeFragment();
        addFragment(fragment, R.id.ctFragmentUser, false);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> listData = new ArrayList<>();

                countAccept = 0;
                countReject = 0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    try {
                        listData.add(orderSnapshot.getValue(Order.class));
                        if (orderSnapshot.getValue(Order.class).getStatus() == Constant.REJECT_STATUS) {
                            countReject += 1;
                        } else if (orderSnapshot.getValue(Order.class).getStatus() == Constant.ACCEPT_STATUS) {
                            countAccept += 1;
                        }
                    } catch (Exception e) {
                        Log.e("PARSE_ORDER_WORKER", "onChildAdded: ", e);
                    }

                }
                if (countAccept > 0 || countReject > 0) {

                    Methods.showDialog(
                            (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog,
                            "Kết quả",
                            "Bạn có "
                                    + ((countAccept > 0) ? (countAccept + " đơn đã được nhận") : "")
                                    + ((countReject > 0) ? (" và " + countReject + " đơn đã bị từ chối") : ""),
                            "Đơn từ chối",
                            "Đơn được nhận",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeValue();
                                }

                                @Override
                                public void onNoClick(Dialog dialog) {
                                    SettingsFragment.setForHistory(true);
                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Setting);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onYesClick(Dialog dialog) {
                                    StatusFragment.setForWaiting(false);
                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
                                    dialog.dismiss();
                                }
                            }
                    );

                    Methods.sendNotification(
                            "Kết quả đặt đơn",
                            "Bạn có "
                                    + ((countAccept > 0) ? (countAccept + " đơn đã được nhận") : "")
                                    + ((countReject > 0) ? (" và " + countReject + " đơn đã bị từ chối") : ""),
                            (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog);
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
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            int result = intent.getIntExtra("KEY_SETTING_FRAG", 0);
            if (result == 1) {
                if (countAccept > 0 || countReject > 0) {

                    Methods.showDialog(
                            (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog,
                            "Kết quả",
                            "Bạn có "
                                    + ((countAccept > 0) ? (countAccept + " đơn đã được nhận") : "")
                                    + ((countReject > 0) ? (" và " + countReject + " đơn đã bị từ chối") : ""),
                            "Đơn từ chối",
                            "Đơn được nhận",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeValue();
                                }

                                @Override
                                public void onNoClick(Dialog dialog) {
                                    SettingsFragment.setForHistory(true);
                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Setting);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onYesClick(Dialog dialog) {
                                    StatusFragment.setForWaiting(false);
                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
                                    dialog.dismiss();
                                }
                            }
                    );
                    countAccept = 0;
                    countReject = 0;
                }
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeEventListener(valueEventListener);
        Constant.USER = new User();
        super.onDestroy();
    }
}