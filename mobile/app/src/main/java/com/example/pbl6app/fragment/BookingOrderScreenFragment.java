package com.example.pbl6app.fragment;

import static com.example.pbl6app.Utils.Methods.toStringNumber;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.activities.MainActivityUser;
import com.example.pbl6app.databinding.FragmentBookingOrderScreenBinding;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingOrderScreenFragment extends FragmentBase {
    private FragmentBookingOrderScreenBinding binding;
    private static WorkerDetail workerDetail;
    private static JobInfo idJobInfo = new JobInfo("");
    private ChoiceFragment choiceFragment;
    private String userAddress = "", userPoint = "";

    public static JobInfo getIdJobInfo() {
        return idJobInfo;
    }

    public static void setIdJobInfo(JobInfo idJobInfo) {
        BookingOrderScreenFragment.idJobInfo = idJobInfo;
    }

    public static ArrayList<JobInfo> getListJobInfo() {
        return workerDetail.getListJobList();
    }

    public BookingOrderScreenFragment() {
        // Required empty public constructor
    }

    public BookingOrderScreenFragment(WorkerDetail workerDetail) {
        BookingOrderScreenFragment.workerDetail = workerDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingOrderScreenBinding.inflate(inflater, container, false);
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
        Picasso.get().load(Constant.BASE_URL + workerDetail.getLinkIMG()).into(binding.imgWorkerLineItem);
        binding.tvNameWorker.setText(workerDetail.getName());
        binding.tvTypeOfJobWorker.setText(workerDetail.getListJobInfo());
        binding.tvPhone.setText(workerDetail.getPhone());
        binding.tvAddressWorker.setText(workerDetail.getAddress());
        userAddress = Constant.USER.getAddress();
        userPoint = Constant.USER.getAddressPoint();
        binding.tvAdressPicker.setText(userAddress);
    }

    @Override
    protected void initListener() {
        binding.tvJobInfo.setOnClickListener(view -> {
            onChoiceShow(Constant.JOB_INFO_DATA_DETAIL, "Chọn công việc", new OnItemCLickListener<AddressTemp>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(AddressTemp item) {
                    binding.tvJobInfo.setText(item.getName());
                    for (JobInfo i : workerDetail.getListJobList()) {
                        if (i.getId().equals(item.getId())) {
                            idJobInfo = i;
                            binding.tvMoney.setText(toStringNumber(Integer.parseInt(i.getPrice())) + " đ");
                        }
                    }
                    choiceFragment.dismiss();
                }
            }, "");
        });

        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.btnPickAddress.setOnClickListener(view -> {
            addFragment(new MapFragment_Parent((address, point) -> {
                binding.tvAdressPicker.setText(address);
                userAddress = address;
                userPoint = point;
                backToPreviousFrag();
            }), R.id.ctFragmentUser);
        });

        binding.btnConfirmJob.setOnClickListener(view -> {
            if (idJobInfo.getId().equals("")) {
                Methods.makeToast("Vui lòng chọn công việc!");
            } else {
                submitData();
            }
        });
    }

    private void submitData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);
        Map<String, String> options = new HashMap<>();
        options.put("workerId", workerDetail.getId());
        options.put("jobInfoId", idJobInfo.getId());
        options.put("note", binding.edtNote.getText().toString());
        options.put("addressPoint", userPoint);
        options.put("address", userAddress);

        ApiService.apiService.createOrder(Constant.USER.getId(), options).enqueue(new Callback<ResponseRetrofit<Order>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Order>> call, Response<ResponseRetrofit<Order>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        FirebaseRepository.
                                PickWorkerChild.
                                child(workerDetail.getId()).
                                child(response.body().getResultObj().getId()).
                                setValue(response.body().getResultObj());

                        Methods.showDialog(
                                R.drawable.smile_dialog,
                                "Tuyệt vời !!!",
                                "Yêu cầu của bạn đã được gửi đến thợ !!!",
                                "Trở về",
                                "Đơn đã yêu cầu",
                                new ListenerDialog() {
                                    @Override
                                    public void onDismiss() {

                                    }

                                    @Override
                                    public void onNoClick(Dialog dialog) {
                                        dialog.dismiss();
                                        backToPreviousFrag();
                                    }

                                    @Override
                                    public void onYesClick(Dialog dialog) {
                                        StatusFragment.setOrderId(response.body().getResultObj().getId());
                                        StatusFragment.setForWaiting(true);
                                        MainActivityUser.setIdNavigate(R.id.menu_status);
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Order>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("CHANGE_PASS", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onChoiceShow(
            int keyData,
            String title,
            OnItemCLickListener<AddressTemp> onItemCLickListener,
            String IdLoad) {
        choiceFragment = new ChoiceFragment(keyData, title, onItemCLickListener, IdLoad);
        choiceFragment.show(getActivity().getSupportFragmentManager(), choiceFragment.getTag());
    }

    @Override
    public void onDestroy() {
        idJobInfo = new JobInfo("");
        super.onDestroy();
    }
}
