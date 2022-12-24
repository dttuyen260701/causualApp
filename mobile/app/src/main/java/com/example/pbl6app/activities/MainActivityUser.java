package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Models.ObjectTracking;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.ActivityMainUserBinding;
import com.example.pbl6app.fragment.HistoryFragment;
import com.example.pbl6app.fragment.NewfeedFragment;
import com.example.pbl6app.fragment.OrderDetailFragment;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;
import com.example.pbl6app.fragment.UserHomeFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityUser extends BaseActivity {

    private static ActivityMainUserBinding binding;
    private static ValueEventListener valueEventListenerOrderResponse;
    private static ChildEventListener childEventListenerOrderStatus, childEventListenerTracking;
    private static int countAccept = 0, countReject = 0;
    private static Boolean isRunning = true;
    private static Order orderUpdate = new Order();
    private static final ArrayList<Order> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Methods.setContext(MainActivityUser.this);

        onNewIntent(getIntent());
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

        valueEventListenerOrderResponse = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listData.clear();

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

                    if (isRunning) {
                        Methods.showDialog(
                                (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog,
                                "Thông báo",
                                "Bạn có "
                                        + ((countAccept > 0) ? (countAccept + " đơn đã được nhận") : "")
                                        + ((countReject > 0) ? (" và " + countReject + " đơn đã bị từ chối") : ""),
                                (countReject > 0) ? "Đơn từ chối" : "",
                                (countAccept > 0) ? "Đơn đã nhận" : "",
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
                                        StatusFragment.setForWaiting(false);
                                        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
                                        dialog.dismiss();
                                    }
                                }
                        );
                    }

                    Methods.sendNotification(
                            "Kết quả đặt đơn",
                            "Bạn có "
                                    + ((countAccept > 0) ? (countAccept + " đơn đã được nhận") : "")
                                    + ((countReject > 0) ? (" và " + countReject + " đơn đã bị từ chối") : ""),
                            (countAccept >= countReject) ? R.drawable.smile_dialog : R.drawable.sad_dialog, isRunning ? 0 : 1, false);
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
                        case Constant.TRACKING_STATUS:
                            childEventListenerTracking = new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    ObjectTracking objectTracking = null;
                                    try {
                                        objectTracking = snapshot.getValue(ObjectTracking.class);
                                    } catch (Exception e) {
                                        return;
                                    }
                                    if (Float.parseFloat(objectTracking.getDistance().split(" ")[0]) < 0.1f) {
                                        Methods.sendNotification(
                                                "Thông báo",
                                                "Có vẻ thợ của bạn đã gần đến nơi, bạn hãy ra đón thợ nhé !!!",
                                                R.drawable.gif_coming,
                                                0,
                                                true
                                        );
                                        FirebaseRepository.TrackingChild.child(orderUpdate.getWorkerId()).removeEventListener(childEventListenerTracking);
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

                            FirebaseRepository.TrackingChild.child(orderUpdate.getWorkerId()).addChildEventListener(childEventListenerTracking);

                            if (isRunning) {
                                Methods.showDialog(
                                        R.drawable.smile_dialog,
                                        "Thông báo",
                                        "Thợ " + orderUpdate.getWorkerName()
                                                + " đang trên đường đến để thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                                        "Đã hiểu",
                                        "Xem vị trí",
                                        new ListenerDialog() {
                                            @Override
                                            public void onDismiss() {
                                                FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                                if (OrderDetailFragment.isIsRunning()) {
                                                    StatusFragment.setForWaiting(false);
                                                    StatusFragment.setOrderId(orderUpdate.getId());
                                                    OrderDetailFragment.setIsComing(true);
                                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
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
                                    "Thợ " + orderUpdate.getWorkerName()
                                            + " đang trên đường đến để thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                                    R.drawable.smile_dialog, isRunning ? 0 : 2, true);
                            break;
                        case Constant.CANCEL_STATUS:
                            if (isRunning) {
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
                                                FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                                if (OrderDetailFragment.isIsRunning()) {
                                                    OrderDetailFragment.setIsRunning(false);
                                                    SettingsFragment.setForHistory(true);
                                                    HistoryFragment.setOrderId(orderUpdate.getId());
                                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Setting);
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
                                    "Thợ " + order.getWorkerName()
                                            + " đã hủy công việc " + order.getJobInfoName() + ".",
                                    R.drawable.sad_dialog, isRunning ? 0 : 3, true);
                            break;
                        case Constant.PROCESS_STATUS:
                            if (isRunning) {
                                Methods.showDialog(
                                        R.drawable.smile_dialog,
                                        "Thông báo",
                                        "Thợ " + orderUpdate.getWorkerName()
                                                + " đã đến và đang thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                                        "Đã hiểu",
                                        "Chi tiết",
                                        new ListenerDialog() {
                                            @Override
                                            public void onDismiss() {
                                                FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                                if (OrderDetailFragment.isIsRunning()) {
                                                    StatusFragment.setForWaiting(false);
                                                    StatusFragment.setOrderId(orderUpdate.getId());
                                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
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
                                    "Thợ " + orderUpdate.getWorkerName()
                                            + " đã đến và đang thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                                    R.drawable.sad_dialog, isRunning ? 0 : 4, true);
                            break;
                        case Constant.COMPLETED_STATUS:
                            if (isRunning) {
                                Methods.showDialog(
                                        R.drawable.smile_dialog,
                                        "Thông báo",
                                        "Thợ " + orderUpdate.getWorkerName()
                                                + " đã hoàn thành công việc " + orderUpdate.getJobInfoName() + ".",
                                        "Đã hiểu",
                                        "Chi tiết",
                                        new ListenerDialog() {
                                            @Override
                                            public void onDismiss() {
                                                FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                                                if (OrderDetailFragment.isIsRunning()) {
                                                    SettingsFragment.setForHistory(true);
                                                    HistoryFragment.setForCompletedOrder(true);
                                                    HistoryFragment.setOrderId(orderUpdate.getId());
                                                    binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Setting);
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
                                    "Thợ " + orderUpdate.getWorkerName()
                                            + " đã hoàn thành công việc " + orderUpdate.getJobInfoName() + ".",
                                    R.drawable.sad_dialog, isRunning ? 0 : 5, true);
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
            switch (result) {
                case 1:
                    FirebaseRepository.PickWorkerChild.child(Constant.USER.getId()).removeValue();
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
                        countReject = 0;
                        countAccept = 0;
                    }
                    break;
                case 2:
                    FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Thông báo",
                            "Thợ " + orderUpdate.getWorkerName()
                                    + " đang trên đường đến để thực hiện công việc" + orderUpdate.getJobInfoName() + ".",
                            "Đã hiểu",
                            "Xem vị trí",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    if (OrderDetailFragment.isIsRunning()) {
                                        StatusFragment.setForWaiting(false);
                                        StatusFragment.setOrderId(orderUpdate.getId());
                                        OrderDetailFragment.setIsComing(true);
                                        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
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
                            R.drawable.sad_dialog,
                            "Thông báo",
                            "Thợ " + orderUpdate.getWorkerName()
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
                                        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Setting);
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
                    FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Thông báo",
                            "Thợ " + orderUpdate.getWorkerName()
                                    + " đã đến và đang thực hiện công việc " + orderUpdate.getJobInfoName() + ".",
                            "Đã hiểu",
                            "Chi tiết",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    if(OrderDetailFragment.isIsRunning()) {
                                        StatusFragment.setForWaiting(false);
                                        StatusFragment.setOrderId(orderUpdate.getId());
                                        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_status);
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
                case 5:
                    FirebaseRepository.UpdateOrderChild.child(Constant.USER.getId()).removeValue();
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Thông báo",
                            "Thợ " + orderUpdate.getWorkerName()
                                    + " đã hoàn thành công việc " + orderUpdate.getJobInfoName() + ".",
                            "Đã hiểu",
                            "Chi tiết",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {
                                    if(OrderDetailFragment.isIsRunning()) {
                                        SettingsFragment.setForHistory(true);
                                        HistoryFragment.setForCompletedOrder(true);
                                        HistoryFragment.setOrderId(orderUpdate.getId());
                                        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Setting);
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
