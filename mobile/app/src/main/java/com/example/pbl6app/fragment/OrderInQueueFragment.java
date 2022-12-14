package com.example.pbl6app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.SendObject;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.activities.MainActivity;
import com.example.pbl6app.databinding.FragmentOrderInQueueBinding;
import com.squareup.picasso.Picasso;

public class OrderInQueueFragment extends FragmentBase {
    private FragmentOrderInQueueBinding binding;
    private Order order;

    public OrderInQueueFragment(Order order) {
        this.order = order;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.setIsChecking(true);
    }

    @Override
    public void onStop() {
        MainActivity.setIsChecking(false);
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderInQueueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        Picasso.get().load(Constant.BASE_URL + ((Constant.USER.getRole().equals("Thợ")) ? order.getCustomerImage() : order.getWorkerImage())).into(binding.imvAva);
        binding.tvWorkerName.setText((Constant.USER.getRole().equals("Thợ")) ? order.getCustomerName() : order.getWorkerName());
        binding.tvAddressCus.setText(order.getUserAddress());
        binding.tvNameJobInfo.setText(order.getJobInfoName());
        Picasso.get().load(Constant.BASE_URL +  order.getJobInfoImage()).into(binding.imgJobInfo);
        binding.edtNoteInQueue.setText(order.getNote());
        binding.edtNoteInQueue.setEnabled(false);
        binding.tvTotal.setText(order.getJobPrices());
    }

    @Override
    protected void initListener() {
        binding.btnReject.setOnClickListener(view -> {
            Methods.showDialog(
                    R.drawable.sad_dialog,
                    "Từ chối",
                    "Bạn có chắc muốn từ chối đơn hàng này",
                    "Hủy",
                    "Từ chối",
                    new ListenerDialog() {
                        @Override
                        public void onNoClick(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onYesClick(Dialog dialog) {
                            order.setStatus(Constant.REJECT_STATUS);
                            FirebaseRepository.
                                    PickWorkerChild.
                                    child(order.getCustomerId()).
                                    child(Constant.USER.getId()).
                                    setValue(order);
                            backToPreviousFrag();
                        }
                    }
            );
        });

        binding.btnAccept.setOnClickListener(view -> {
            Methods.showDialog(
                    R.drawable.smile_dialog,
                    "Chấp nhận",
                    "Bạn có chắc muốn chấp nhận và thực hiện đơn hàng này",
                    "Hủy",
                    "Thực hiện",
                    new ListenerDialog() {
                        @Override
                        public void onNoClick(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onYesClick(Dialog dialog) {
                            order.setStatus(Constant.ACCEPT_STATUS);
                            FirebaseRepository.
                                    PickWorkerChild.
                                    child(order.getCustomerId()).
                                    child(Constant.USER.getId()).
                                    setValue(order);
                            backToPreviousFrag();
                        }
                    }
            );
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
