package com.example.pbl6app.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentRateWorkerBinding;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateWorkerFragment extends FragmentBase {
    private FragmentRateWorkerBinding binding;
    private String comment = "";
    private final Order order;
    private Rate rate = null;

    public RateWorkerFragment(Order order) {
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRateWorkerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        onLoadData();
    }

    @Override
    protected void initView() {
        Picasso.get().load(Constant.BASE_URL + order.getWorkerImage()).into(binding.avaWorker);
        binding.tvWorkerName.setText(order.getWorkerName());
        binding.tvWork.setText(order.getJobInfoName());
        binding.tvPhone.setText(order.getWorkerPhone());
        initRate();
    }

    private void initRate() {
        if (rate != null) {
            binding.ratingBarAttitude.setRating(rate.getAttitudeRateAverage());
            binding.ratingBarPleasure.setRating(rate.getPleasureRateAverage());
            binding.ratingBarSkill.setRating(rate.getSkillRateAverage());
            if(rate.getComment()!=null){
                binding.edtComment.setText(rate.getComment() + " ");
            }
            else{
                binding.edtComment.setText("Không có bình luận");
            }
            binding.edtComment.setEnabled(false);
            binding.ratingBarAttitude.setIsIndicator(true);
            binding.ratingBarPleasure.setIsIndicator(true);
            binding.ratingBarSkill.setIsIndicator(true);
            binding.btnRate.setVisibility(View.GONE);
        } else {
            binding.ratingBarAttitude.setRating(5f);
            binding.ratingBarPleasure.setRating(5f);
            binding.ratingBarSkill.setRating(5f);
            binding.edtComment.setText("");
            binding.edtComment.setEnabled(true);
            binding.ratingBarAttitude.setIsIndicator(false);
            binding.ratingBarPleasure.setIsIndicator(false);
            binding.ratingBarSkill.setIsIndicator(false);
            binding.btnRate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });
        binding.btnRate.setOnClickListener(view -> {
            onSubmitData();
        });

        binding.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                comment = binding.edtComment.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLoadData();
            }
        });
    }

    private void onLoadData() {
        binding.refreshLayout.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getRateByOrderId(order.getId()).enqueue(new Callback<ResponseRetrofit<Rate>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Rate>> call, Response<ResponseRetrofit<Rate>> response) {
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        rate = response.body().getResultObj();
                        initRate();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Rate>> call, Throwable t) {
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("NEW_RATE", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSubmitData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        Map<String, String> options = new HashMap<>();
        options.put("workerId", order.getWorkerId());
        options.put("attitudeRate", String.valueOf((int) binding.ratingBarAttitude.getRating()));
        options.put("skillRate", String.valueOf((int) binding.ratingBarSkill.getRating()));
        options.put("pleasureRate", String.valueOf((int) binding.ratingBarPleasure.getRating()));
        options.put("comment", comment);
        options.put("orderId", order.getId());

        ApiService.apiService.createNewRate(options, Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<Rate>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Rate>> call, Response<ResponseRetrofit<Rate>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        rate = response.body().getResultObj();
                        initRate();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Rate>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("NEW_RATE", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });

    }
}