package com.example.pbl6app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.FragmentOrderInQueueBinding;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderInQueueFragment extends FragmentBase {

    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    private FragmentOrderInQueueBinding binding;
    private Order order;
    private String orderId;
    private OnItemCLickListener<Order> listener;

    public OrderInQueueFragment(String orderId, OnItemCLickListener<Order> listener) {
        this.orderId = orderId;
        this.listener = listener;
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
        if (!orderId.equals("")) {
            loadData();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        if (order != null) {
            String status = "";
            switch (order.getStatus()) {
                case Constant.REJECT_STATUS:
                    status = "Đã từ chối";
                    binding.tvTitle.setText("Đơn đã bị từ chối");
                    break;
                case Constant.WAITING_STATUS:
                    status = "Đang chờ phản hồi";
                    binding.tvTitle.setText("Đơn đang chờ phản hồi");
                    break;
                case Constant.CANCEL_STATUS:
                    status = "Đã hủy";
                    binding.tvTitle.setText("Đơn đã bị hủy");
                    break;
                case Constant.COMPLETED_STATUS:
                    status = "Đã hoàn thành";
                    binding.tvTitle.setText("Đơn đã hoàn thành");
                    break;
                case Constant.ACCEPT_STATUS:
                    status = "Đã nhận";
                    binding.tvTitle.setText("Đơn đã hoàn thành");
                    break;
                default:
                    status = "Đang thực hiện";
                    binding.tvTitle.setText("Đơn đang thực hiện");
                    break;
            }

            binding.btnAccept.setVisibility(View.GONE);
            binding.btnReject.setVisibility(View.GONE);

            if (order.getStatus() == Constant.WAITING_STATUS && Constant.USER.getRole() == Constant.ROLE_WORKER) {
                binding.btnAccept.setVisibility(View.VISIBLE);
                binding.btnReject.setVisibility(View.VISIBLE);
            }

            binding.tvStatus.setText(status);

            Picasso.get().load(Constant.BASE_URL + ((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerImage() : order.getWorkerImage())).into(binding.imvAva);

            binding.tvWorkerName.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerName() : order.getWorkerName());

            binding.tvWorkerPhone.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerPhone() : order.getWorkerPhone());

            binding.tvAddressCus.setText(order.getUserAddress());

            binding.tvNameJobInfo.setText(order.getJobInfoName());

            Picasso.get().load(Constant.BASE_URL + order.getJobInfoImage()).into(binding.imgJobInfo);

            binding.edtNoteInQueue.setText(order.getNote());

            binding.edtNoteInQueue.setEnabled(false);

            binding.tvTime.setText(order.getCreationTime());

            binding.tvTotal.setText(Methods.toStringNumber(Integer.parseInt(order.getJobPrices())) + "đ");

        }
    }

    @Override
    protected void initListener() {
        binding.layoutWorkerPhone.setOnClickListener(view -> {
            if (order.getStatus() != Constant.CANCEL_STATUS && order.getStatus() != Constant.REJECT_STATUS) {
                askPermissionAndCall((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerPhone() : order.getWorkerPhone());
            }
        });

        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.btnReject.setOnClickListener(view -> {
            Methods.showDialog(
                    R.drawable.sad_dialog,
                    "Từ chối",
                    "Bạn có chắc muốn từ chối đơn hàng này",
                    "Hủy",
                    "Từ chối",
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
                            submitData(order.getId(), Constant.REJECT_STATUS);
                            dialog.dismiss();
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
                        public void onDismiss() {

                        }

                        @Override
                        public void onNoClick(Dialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onYesClick(Dialog dialog) {
                            submitData(order.getId(), Constant.ACCEPT_STATUS);
                            dialog.dismiss();
                        }
                    }
            );


        });

    }

    private void loadData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getOrderByID(orderId, Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<Order>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Order>> call, Response<ResponseRetrofit<Order>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        if (response.body().getResultObj() != null) {
                            OrderInQueueFragment.this.order = response.body().getResultObj();
                            initView();
                        }
                    } else {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Order>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitData(String orderId, int status) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.updateStatusOrder(orderId, status).enqueue(new Callback<ResponseRetrofit<Order>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Order>> call, Response<ResponseRetrofit<Order>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        if (response.body().getResultObj() != null) {
                            OrderInQueueFragment.this.order = response.body().getResultObj();
                            FirebaseRepository.
                                    PickWorkerChild.
                                    child(order.getCustomerId()).
                                    child(orderId).
                                    setValue(order);
                            backToPreviousFrag();
                            listener.onItemClick(order);
                        }
                    } else {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Order>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void askPermissionAndCall(String phoneNB) {

        // With Android Level >= 23, you have to ask the user
        // for permission to Call.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // Check if we have Call permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSION_REQUEST_CODE_CALL_PHONE
                );
                return;
            }
        }
        callNow(phoneNB);
    }

    @SuppressLint("MissingPermission")
    private void callNow(String phoneNB) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNB));
        startActivity(callIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
