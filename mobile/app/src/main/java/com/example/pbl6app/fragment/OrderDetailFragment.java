package com.example.pbl6app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.example.pbl6app.Listeners.Listener_for_PickAddress;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.FragmentOrderDetailBinding;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends FragmentBase {
    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    private FragmentOrderDetailBinding binding;
    private Order order;
    private String orderId = "";
    private boolean isComing = false;

    public OrderDetailFragment(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        loadData();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {

        binding.btnSuBmit.setVisibility(View.VISIBLE);
        if (order != null) {
            String status = "";

            switch (order.getStatus()) {
                case Constant.REJECT_STATUS:
                    status = "Đã bị từ chối";
                    break;
                case Constant.WAITING_STATUS:
                    status = "Đang chờ phản hồi";
                    break;
                case Constant.CANCEL_STATUS:
                    status = "Đã bị hủy";
                    break;
                case Constant.ACCEPT_STATUS:
                    status = "Đã đặt";
                    binding.ic1.setSelected(true);
                    binding.tv1.setSelected(true);
                    binding.btnSuBmit.setText("Hành trình thợ");
                    binding.gifStatus.setImageResource(R.drawable.gif_coming);
                    break;
                case Constant.PROCESS_STATUS:
                    status = "Đang thực hiện";
                    binding.ic1.setSelected(true);
                    binding.tv1.setSelected(true);
                    binding.ic2.setSelected(true);
                    binding.tv2.setSelected(true);
                    binding.btnSuBmit.setVisibility(View.GONE);
                    binding.gifStatus.setImageResource(R.drawable.gif_in_progress);
                    break;
                default:
                    status = "Đã hoàn thành";
                    binding.ic1.setSelected(true);
                    binding.tv1.setSelected(true);
                    binding.ic2.setSelected(true);
                    binding.tv2.setSelected(true);
                    binding.ic3.setSelected(true);
                    binding.tv3.setSelected(true);
                    binding.btnSuBmit.setText("Đánh giá thợ");
                    binding.gifStatus.setImageResource(R.drawable.gif_done);
                    break;
            }

            binding.tvStatus.setText(status);

            binding.tvTime.setText(order.getCreationTime());

            Picasso.get().load(Constant.BASE_URL + ((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerImage() : order.getWorkerImage())).into(binding.imvAva);

            binding.tvWorkerName.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerName() : order.getWorkerName());

            binding.tvPhone.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerPhone() : order.getWorkerPhone());

            binding.tvAddressCus.setText(order.getUserAddress());

            binding.tvJobInfoName.setText(order.getJobInfoName());

            binding.tvJobInfoPrice.setText(Methods.toStringNumber(Integer.parseInt(order.getJobPrices())) + "đ");

            binding.tvTotal.setText(Methods.toStringNumber(Integer.parseInt(order.getJobPrices())) + "đ");

            binding.tvIsPaid.setText((order.getStatus() == Constant.COMPLETED_STATUS) ? "Đã thanh toán" : "Chưa thanh toán");
        }

    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.tvPhone.setOnClickListener(view -> {
            askPermissionAndCall((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerPhone() : order.getWorkerPhone());
        });

        binding.btnSuBmit.setOnClickListener(view -> {
            if(order != null) {
                switch (order.getStatus()) {
                    case Constant.REJECT_STATUS:
                        break;
                    case Constant.WAITING_STATUS:
                        break;
                    case Constant.CANCEL_STATUS:
                        break;
                    case Constant.ACCEPT_STATUS:
                        if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                            addFragment(new TrackingFragment(order), R.id.ctFragmentUser);
                        } else {
                            addFragment(new MapTrackingFragmentParent(order.getWorkerId(), order.getUserPoint()), R.id.ctFragmentUser);
                        }
                        break;
                    case Constant.PROCESS_STATUS:
                        binding.ic2.setSelected(true);
                        binding.tv2.setSelected(true);
                        binding.btnSuBmit.setVisibility(View.GONE);
                        binding.gifStatus.setImageResource(R.drawable.gif_in_progress);
                        break;
                    default:
                        binding.ic3.setSelected(true);
                        binding.tv3.setSelected(true);
                        binding.btnSuBmit.setText("Đánh giá thợ");
                        binding.gifStatus.setImageResource(R.drawable.gif_done);
                        break;
                }
            }
        });
    }

    private void loadData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getOrderByID(orderId).enqueue(new Callback<ResponseRetrofit<Order>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Order>> call, Response<ResponseRetrofit<Order>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        if (response.body().getResultObj() != null) {
                            order = response.body().getResultObj();
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
                            order = response.body().getResultObj();
                            FirebaseRepository.
                                    PickWorkerChild.
                                    child(order.getCustomerId()).
                                    child(orderId).
                                    setValue(order);
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