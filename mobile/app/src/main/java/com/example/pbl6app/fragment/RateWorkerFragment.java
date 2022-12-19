package com.example.pbl6app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentCreateNewPostBinding;
import com.example.pbl6app.databinding.FragmentRateWorkerBinding;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateWorkerFragment extends FragmentBase {
    private FragmentRateWorkerBinding binding;
    private float pleasureStar=0, attitudeStar=0, skillStar=0;
    private WorkerDetail worker;
    private String comment = "";


    public RateWorkerFragment(WorkerDetail worker) {
        this.worker = worker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view->{
            backToPreviousFrag();
        });
        binding.btnRate.setOnClickListener(view->{
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
    }

    private void onSubmitData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        Map<String , String> options = new HashMap<>();
        options.put("workerId", worker.getId());
        options.put("attitudeRate", String.valueOf((int)binding.ratingBarAttitude.getRating()));
        options.put("skillRate", String.valueOf((int)binding.ratingBarSkill.getRating()));
        options.put("pleasureRate", String.valueOf((int)binding.ratingBarPleasure.getRating()));
        options.put("comment", comment);

        ApiService.apiService.createNewRate(options, Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<PostOfDemand>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<PostOfDemand>> call, Response<ResponseRetrofit<PostOfDemand>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        addFragment(new ListRateWorkerFragment(worker), R.id.ctFragmentUser);
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<PostOfDemand>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("NEW_RATE", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });

    }
}