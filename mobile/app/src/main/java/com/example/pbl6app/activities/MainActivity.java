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
import com.example.pbl6app.fragment.DetailPostOnWorkerRoleFragment;
import com.example.pbl6app.fragment.HistoryFragment;
import com.example.pbl6app.fragment.NewfeedFragment;
import com.example.pbl6app.fragment.OrderDetailFragment;
import com.example.pbl6app.fragment.OrderInQueueFragment;
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
    private static ValueEventListener valueEventListenerPODResponse;
    private static int countAccept = 0, countReject = 0;
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

        valueEventListenerPODResponse = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                countAccept = 0;
                countReject = 0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    try {
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

                    if (isRunning) {
                        Methods.showDialog(
                                (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog,
                                "Thông báo",
                                "Bạn có "
                                        + ((countAccept > 0) ? (countAccept + " đơn đã được khách hàng chọn") : "")
                                        + ((countAccept > 0 && countReject > 0) ? " và " : "")
                                        + ((countReject > 0) ? (+countReject + " đơn đã bị từ chối hoặc hủy tìm kiếm thợ") : ""),
                                (countReject > 0) ? "Đã hiểu" : "",
                                (countAccept > 0) ? "Đơn đã nhận" : "",
                                new ListenerDialog() {
                                    @Override
                                    public void onDismiss() {
                                        FirebaseRepository.ResponsePostWorker.child(Constant.USER.getId()).removeValue();
                                        if(DetailPostOnWorkerRoleFragment.isIsRunning()) {
                                            DetailPostOnWorkerRoleFragment.setIsRunning(false);
                                            binding.bottomNavigation.setSelectedItemId(R.id.menu_newFeed);
                                        }
                                    }

                                    @Override
                                    public void onNoClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onYesClick(Dialog dialog) {
                                        StatusFragment.setForWaiting(false);
                                        binding.bottomNavigation.setSelectedItemId(R.id.menu_status);
                                        dialog.dismiss();
                                    }
                                }
                        );
                    }

                    Methods.sendNotification(
                            "Kết quả đặt đơn",
                            "Bạn có "
                                    + ((countAccept > 0) ? (countAccept + " đơn đã được khách hàng chọn") : "")
                                    + ((countAccept > 0 && countReject > 0) ? " và " : "")
                                    + ((countReject > 0) ? (countReject + " đơn đã bị từ chối hoặc hủy tìm kiếm thợ") : ""),
                            (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog, isRunning ? 0 : 4, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

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
                                "Bạn có " + listData.size() + " yêu cầu công việc mới",
                                ((listData.size() > 1) ? "Bao gồm yêu cầu " : "Yêu cầu ") +
                                        listData.get(0).getJobInfoName() +
                                        " từ " + listData.get(0).getCustomerName() +
                                        ((listData.size() > 1) ? " và những yêu cầu khác" : ""),
                                "Xem sau",
                                "Xem ngay",
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
                            R.drawable.smile_dialog, isRunning ? 0 : 1, 1);
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

                if (order != null) {
                    orderUpdate = order;
                    Log.e("TTT", "onChildAdded: " + order.getStatus());
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
                                                if (OrderDetailFragment.isIsRunning()) {
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
                                    R.drawable.sad_dialog, isRunning ? 0 : 2, 2);
                            break;
                        case Constant.ACCEPT_STATUS:
                            if (isRunning) {
                                Methods.showDialog(
                                        R.drawable.smile_dialog,
                                        "Thông báo",
                                        "Khách hàng " + orderUpdate.getCustomerName()
                                                + " đã chọn bạn thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                                        "Đã hiểu",
                                        "Chi tiết",
                                        new ListenerDialog() {
                                            @Override
                                            public void onDismiss() {
                                                FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                                if (OrderDetailFragment.isIsRunning()) {
                                                    StatusFragment.setOrderId(orderUpdate.getId());
                                                    binding.bottomNavigation.setSelectedItemId(R.id.menu_status);
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
                                    R.drawable.sad_dialog, isRunning ? 0 : 3, 2);
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

        FirebaseRepository.ResponsePostWorker.child(Constant.USER.getId()).addValueEventListener(valueEventListenerPODResponse);
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
            switch (result) {
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
                            "Khách hàng " + orderUpdate.getCustomerName()
                                    + " đã hủy công việc " + orderUpdate.getJobInfoName() + ".",
                            "Đã hiểu",
                            "Chi tiết",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    if (OrderDetailFragment.isIsRunning()) {
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
                case 3:
                    FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Thông báo",
                            "Khách hàng " + orderUpdate.getCustomerName()
                                    + " đã chọn bạn thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                            "Đã hiểu",
                            "Chi tiết",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                    if (OrderDetailFragment.isIsRunning()) {
                                        StatusFragment.setOrderId(orderUpdate.getId());
                                        binding.bottomNavigation.setSelectedItemId(R.id.menu_status);
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
                case 4:
                    FirebaseRepository.ResponsePostWorker.child(Constant.USER.getId()).removeValue();
                    if (countAccept > 0 || countReject > 0) {
                        Methods.showDialog(
                                (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog,
                                "Thông báo",
                                "Bạn có "
                                        + ((countAccept > 0) ? (countAccept + " đơn đã được khách hàng chọn") : "")
                                        + ((countAccept > 0 && countReject > 0) ? " và " : "")
                                        + ((countReject > 0) ? (countReject + " đơn đã bị từ chối hoặc hủy tìm kiếm thợ") : ""),
                                (countReject > 0) ? "Đã hiểu" : "",
                                (countAccept > 0) ? "Đơn đã nhận" : "",
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
                                        StatusFragment.setForWaiting(false);
                                        binding.bottomNavigation.setSelectedItemId(R.id.menu_status);
                                        dialog.dismiss();
                                    }
                                }
                        );
                    }
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
        FirebaseRepository.ResponsePostWorker.child(Constant.USER.getId()).removeEventListener(valueEventListenerPODResponse);

        FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeEventListener(childEventListenerOrderStatus);

        FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeEventListener(valueEventListenerOrderResponse);
        Constant.USER = new User();
        super.onDestroy();
    }
}
