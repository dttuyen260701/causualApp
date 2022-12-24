package com.example.pbl6app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.FragmentOrderDetailBinding;
import com.example.pbl6app.services.TrackingService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private static boolean isComing = false;
    private static boolean isRunning = false;

    public static boolean isIsRunning() {
        return isRunning;
    }

    public static void setIsRunning(boolean isRunning) {
        OrderDetailFragment.isRunning = isRunning;
    }

    public static void setIsComing(boolean isComing) {
        OrderDetailFragment.isComing = isComing;
    }

    public OrderDetailFragment(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRunning = true;
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

        binding.btnCancel.setVisibility(View.VISIBLE);
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
                    binding.btnSuBmit.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? "Bắt đầu di chuyển" : "Tôi đã chờ quá lâu");
                    binding.gifStatus.setImageResource(R.drawable.gif_coming);
                    break;
                case Constant.TRACKING_STATUS:
                    status = "Đang di chuyển";
                    binding.ic1.setSelected(true);
                    binding.tv1.setSelected(true);
                    binding.btnSuBmit.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? "Đã đến nơi" : "Hành trình của thợ");
                    binding.gifStatus.setImageResource(R.drawable.gif_coming);
                    break;
                case Constant.PROCESS_STATUS:
                    status = "Đang thực hiện";
                    binding.ic1.setSelected(true);
                    binding.tv1.setSelected(true);
                    binding.ic2.setSelected(true);
                    binding.tv2.setSelected(true);
                    binding.btnSuBmit.setText("Đã hoàn thành");
                    binding.btnSuBmit.setVisibility((Constant.USER.getRole() == Constant.ROLE_WORKER) ? View.VISIBLE : View.GONE);
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
                    binding.btnCancel.setVisibility(View.GONE);
                    binding.btnSuBmit.setText("Đánh giá thợ");
                    binding.btnSuBmit.setVisibility((Constant.USER.getRole() == Constant.ROLE_WORKER) ? View.GONE : View.VISIBLE);
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

            binding.tvIsPaid.setText((order.isPaid()) ? "Đã thanh toán" : "Chưa thanh toán");

            binding.imvStatus.setImageResource((order.isPaid()) ? R.drawable.paid : R.drawable.unpaid);
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

        binding.btnCancel.setOnClickListener(view -> {
            Methods.showDialog(
                    R.drawable.sad_dialog,
                    "Cảnh báo",
                    "Bạn sẽ hủy đơn này, bạn có chắc muốn hủy?",
                    "Không",
                    "Hủy đơn",
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
                            dialog.dismiss();
                            submitData(orderId, Constant.CANCEL_STATUS, item -> {
                                Intent intent = new Intent(getActivity(), TrackingService.class);
                                getActivity().stopService(intent);
                                FirebaseRepository.
                                        UpdateOrderChild.
                                        child((Constant.USER.getRole() == Constant.ROLE_WORKER) ? item.getCustomerId() : item.getWorkerId()).
                                        child(item.getId()).removeValue();
                                FirebaseRepository.
                                        UpdateOrderChild.
                                        child((Constant.USER.getRole() == Constant.ROLE_WORKER) ? item.getCustomerId() : item.getWorkerId()).
                                        child(item.getId()).
                                        setValue(item);
                                backToPreviousFrag();
                                Methods.makeToast("Bạn đã hủy đơn này");
                            });
                        }
                    }
            );
        });

        binding.btnSuBmit.setOnClickListener(view -> {
            if (order != null) {
                switch (order.getStatus()) {
                    case Constant.REJECT_STATUS:
                        break;
                    case Constant.WAITING_STATUS:
                        break;
                    case Constant.CANCEL_STATUS:
                        break;
                    case Constant.ACCEPT_STATUS:
                        if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                            Methods.showDialog(
                                    R.drawable.smile_dialog,
                                    "Bắt đầu di chuyển",
                                    "Bạn sẽ được chuyển hướng sang Google Map và chia sẻ vị trí cho người dùng.",
                                    "Tôi chưa bắt đầu",
                                    "Bắt đầu",
                                    new ListenerDialog() {

                                        @Override
                                        public void onDismiss() {

                                        }

                                        @Override
                                        public void onNoClick(Dialog dialog) {
                                            Methods.makeToast("Bạn vui lòng thực hiện đơn đã nhận sớm nhất có thể");
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onYesClick(Dialog dialog) {
                                            dialog.dismiss();
                                            submitData(orderId, Constant.TRACKING_STATUS, item -> {
                                                Intent intent = new Intent(getActivity(), TrackingService.class);
                                                intent.putExtra("UserPoint", item.getUserPoint());
                                                intent.putExtra("WorkerId", item.getWorkerId());
                                                getActivity().startService(intent);
                                                FirebaseRepository.
                                                        UpdateOrderChild.
                                                        child(item.getCustomerId()).
                                                        child(item.getId()).removeValue();
                                                FirebaseRepository.
                                                        UpdateOrderChild.
                                                        child(item.getCustomerId()).
                                                        child(item.getId()).
                                                        setValue(item);
                                                openMap(item.getUserPoint());
                                            });
                                        }
                                    }
                            );
                        } else {
                            Methods.showDialog(
                                    R.drawable.sad_dialog,
                                    "Bạn đã chờ quá lâu",
                                    "Nếu bạn cảm thấy quá lâu vui lòng liên hệ thợ hoặc liên hệ chúng tôi hoặc hủy đơn",
                                    "Tôi sẽ chờ",
                                    "Liên hệ",
                                    new ListenerDialog() {

                                        @Override
                                        public void onDismiss() {

                                        }

                                        @Override
                                        public void onNoClick(Dialog dialog) {
                                            Methods.makeToast("Hãy chờ thêm một chút nhé");
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onYesClick(Dialog dialog) {
                                            dialog.dismiss();
                                            askPermissionAndCall("0123456789");
                                        }
                                    }
                            );
                        }
                        break;
                    case Constant.TRACKING_STATUS:
                        if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                            Methods.showDialog(
                                    R.drawable.smile_dialog,
                                    "Đã bắt đầu công việc",
                                    "Bạn đã đến nơi thực hiện công việc,hãy xác nhận để bắt đầu làm việc",
                                    "Chưa đến",
                                    "Xác nhận",
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
                                            dialog.dismiss();
                                            Intent intent = new Intent(getActivity(), TrackingService.class);
                                            getActivity().stopService(intent);
                                            submitData(orderId, Constant.PROCESS_STATUS, item -> {
                                                FirebaseRepository.
                                                        UpdateOrderChild.
                                                        child(item.getCustomerId()).
                                                        child(item.getId()).removeValue();
                                                FirebaseRepository.
                                                        UpdateOrderChild.
                                                        child(item.getCustomerId()).
                                                        child(item.getId()).
                                                        setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Methods.makeToast("Đã thông báo đến khách hàng bạn đang thực hiện công việc");
                                                            }
                                                        });
                                            });
                                        }
                                    }
                            );
                        } else {
                            addFragment(new MapTrackingFragmentParent(order.getWorkerId(), order.getUserPoint()), R.id.ctFragmentUser);
                        }
                        break;
                    case Constant.PROCESS_STATUS:
                        if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                            Methods.showDialog(
                                    R.drawable.smile_dialog,
                                    "Hoàn thành công việc",
                                    "Bạn đã hoàn thành công việc, vui lòng xác nhận với hệ thống và thông báo cho khách hàng.",
                                    "Chưa hoàn thành",
                                    "Đã hoàn thành",
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
                                            dialog.dismiss();
                                            submitData(orderId, Constant.COMPLETED_STATUS, item -> {
                                                FirebaseRepository.
                                                        UpdateOrderChild.
                                                        child(item.getCustomerId()).
                                                        child(item.getId()).removeValue();
                                                FirebaseRepository.
                                                        UpdateOrderChild.
                                                        child(item.getCustomerId()).
                                                        child(item.getId()).
                                                        setValue(item).addOnCompleteListener(task ->
                                                                Methods.makeToast("Đã thông báo đến khách hàng bạn đã hoàn thành công việc")
                                                        );
                                            });
                                        }
                                    }
                            );
                        }
                        break;
                    case Constant.COMPLETED_STATUS:
                        addFragment(new RateWorkerFragment(order), R.id.ctFragmentUser);
                        break;
                    default:
                        break;
                }
            }
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
                            order = response.body().getResultObj();
                            initView();

                            if (isComing) {
                                isComing = false;
                                addFragment(new MapTrackingFragmentParent(order.getWorkerId(), order.getUserPoint()), R.id.ctFragmentUser);
                            }
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

    private void submitData(String orderId, int status, OnItemCLickListener<Order> onEnd) {
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
                            onEnd.onItemClick(order);
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

    @SuppressLint("ObsoleteSdkInt")
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

    private void openMap(String endPointStr) {
        //String uri = "http://maps.google.com/maps/dir/?api=1&destination=" + latitude + "%2C-" + longitude + " (" + "Your partner is here" + ")";
        //String uri = "http://maps.google.com/maps/dir/?api=1&query=" + latitude + "%2C-" + longitude + " (" + "Your partner is here" + ")";
        //String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Your partner is here" + ")";
        //String uri = "https://maps.googleapis.com/maps/api/directions/json?origin="+latLng1.latitude+","+latLng1.longitude+"&destination="+latitude+","+longitude+"  &key=AIzaSyCaIgerehFWzBZuERI0lkVpp3y-fZIz94s";
        LatLng endPoint = new LatLng(Float.parseFloat(endPointStr.split("-")[0]), Float.parseFloat(endPointStr.split("-")[1]));
        String uri = "http://maps.google.com/maps?daddr=" + endPoint.latitude + "," + endPoint.longitude + "";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Methods.makeToast("Vui lòng cài đặt Google Map");
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void callNow(String phoneNB) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNB));
        startActivity(callIntent);
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }
}