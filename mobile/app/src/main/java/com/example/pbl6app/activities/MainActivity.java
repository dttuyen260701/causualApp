package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.example.pbl6app.fragment.HistoryFragment;
import com.example.pbl6app.fragment.NewfeedFragment;
import com.example.pbl6app.fragment.OrderDetailFragment;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private static ValueEventListener valueEventListenerOrderResponse;
    private static ChildEventListener childEventListenerOrderStatus;
    private static Boolean isRunning = true;
    private static Order orderUpdate = new Order();
    private static final ArrayList<Order> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Methods.setContext(MainActivity.this);

        onNewIntent(getIntent());

        initView();

        initListener();
    }

    private void initView() {
        binding.bottomNavigation.setSelectedItemId(R.id.menu_newFeed);
        Fragment fragment = new NewfeedFragment();
        addFragment(fragment);

        valueEventListenerOrderResponse = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    try {
                        listData.add(orderSnapshot.getValue(Order.class));
                    } catch (Exception e) {
                        Log.e("PARSE_ORDER_WORKER", "onChildAdded: ", e);
                    }

                }

                if (listData.size() > 0) {
                    if (isRunning) {
                        Methods.showDialog(
                                R.drawable.smile_dialog,
                                "Bạn có " + listData.size() + " yêu cầu công việc mới!!!",
                                ((listData.size() > 1) ? "Bao gồm yêu cầu " : "Yêu cầu") +
                                        listData.get(0).getJobInfoName() +
                                        " được yêu cầu từ " + listData.get(0).getCustomerName() +
                                        ((listData.size() > 1) ? " và những yêu cầu khác" : ""),
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
                    }

                    Methods.sendNotification(
                            "Bạn có " + listData.size() + " yêu cầu công việc mới!!!",
                            ((listData.size() > 1) ? "Bao gồm yêu cầu " : "Yêu cầu") +
                                    listData.get(0).getJobInfoName() +
                                    " được yêu cầu từ " + listData.get(0).getCustomerName() +
                                    ((listData.size() > 1) ? " và những yêu cầu khác" : ""),
                            R.drawable.smile_dialog, isRunning ? 0 : 1, false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        childEventListenerOrderStatus = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Order order = null;
                try {
                    order = snapshot.getValue(Order.class);
                } catch (Exception e) {
                    Log.e("PARSE_ORDER_WORKER", "onChildAdded: ", e);
                }

                if(order != null) {
                    orderUpdate = order;
                    Log.e("TTT", "onChildAdded: " + order.getStatus() );
                    switch (order.getStatus()) {
                        case Constant.CANCEL_STATUS:
                            if (isRunning) {
                                Methods.showDialog(
                                        R.drawable.sad_dialog,
                                        "Thông báo",
                                        "Khách hàng " + orderUpdate.getCustomerName()
                                                + " đã hủy công việc " + orderUpdate.getJobInfoName() + ".",
                                        "Đã hiểu",
                                        "Chi tiết",
                                        new ListenerDialog() {
                                            @Override
                                            public void onDismiss() {
                                                FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                                if(OrderDetailFragment.isIsRunning()) {
                                                    OrderDetailFragment.setIsRunning(false);
                                                    SettingsFragment.setForHistory(true);
                                                    HistoryFragment.setOrderId(orderUpdate.getId());
                                                    binding.bottomNavigation.setSelectedItemId(R.id.menu_Setting);
                                                }
                                            }

                                            @Override
                                            public void onNoClick(Dialog dialog) {
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onYesClick(Dialog dialog) {
                                                OrderDetailFragment.setIsRunning(true);
                                                dialog.dismiss();
                                            }
                                        }
                                );
                            }

                            Methods.sendNotification(
                                    "Thông báo",
                                    "Khách hàng " + orderUpdate.getCustomerName()
                                            + " đã hủy công việc " + order.getJobInfoName() + ".",
                                    R.drawable.sad_dialog, isRunning ? 0 : 2, true);
                            break;
                        default:
                            break;
                    }
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

        FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).addChildEventListener(childEventListenerOrderStatus);

        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).addValueEventListener(valueEventListenerOrderResponse);
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
        if (intent != null) {
            int result = intent.getIntExtra("KEY_SETTING_FRAG", 0);
            switch (result){
                case 1:
                    FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeValue();
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Bạn có " + listData.size() + " yêu cầu công việc mới!!!",
                            ((listData.size() > 1) ? "Bao gồm yêu cầu " : "Yêu cầu") +
                                    listData.get(0).getJobInfoName() +
                                    " được yêu cầu từ " + listData.get(0).getCustomerName() +
                                    ((listData.size() > 1) ? " và những yêu cầu khác" : ""),
                            "Xem sau",
                            "Xem chi tiết",
                            new ListenerDialog() {

                                @Override
                                public void onDismiss() {

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
                    break;
                case 2:
                    FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                    Methods.showDialog(
                            R.drawable.sad_dialog,
                            "Thông báo",
                            "Thợ " + orderUpdate.getWorkerName()
                                    + " đã hủy công việc " + orderUpdate.getJobInfoName() + ".",
                            "Đã hiểu",
                            "Chi tiết",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    if(OrderDetailFragment.isIsRunning()) {
                                        OrderDetailFragment.setIsRunning(false);
                                        SettingsFragment.setForHistory(true);
                                        HistoryFragment.setOrderId(orderUpdate.getId());
                                        binding.bottomNavigation.setSelectedItemId(R.id.menu_Setting);
                                    }
                                }

                                @Override
                                public void onNoClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onYesClick(Dialog dialog) {
                                    OrderDetailFragment.setIsRunning(true);
                                    dialog.dismiss();
                                }
                            }
                    );
                    break;
                default:
                    break;
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
    protected void onResume() {
        isRunning = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isRunning = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeEventListener(childEventListenerOrderStatus);

        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeEventListener(valueEventListenerOrderResponse);
        Constant.USER = new User();
        super.onDestroy();
    }
}
