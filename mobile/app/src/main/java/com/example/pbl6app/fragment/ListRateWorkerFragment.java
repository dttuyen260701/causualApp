package com.example.pbl6app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pbl6app.Adapters.ItemRateWorkerAdapter;
import com.example.pbl6app.Adapters.OrderItemLinesAdapter;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentCreateNewPostBinding;
import com.example.pbl6app.databinding.FragmentListRateWorkerBinding;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRateWorkerFragment extends FragmentBase {
    private FragmentListRateWorkerBinding binding;
    private ArrayList<Rate> listRate;
    private ItemRateWorkerAdapter adapter;
    private WorkerDetail workerDetail;
    private Rate overallRate;


    public ListRateWorkerFragment(WorkerDetail workerDetail) {
        this.workerDetail = workerDetail;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListRateWorkerBinding.inflate(inflater, container, false);
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
        binding.averageRate.setText(String.valueOf(workerDetail.getRate().getRateAverage()));
        binding.averageRateBar.setRating(workerDetail.getRate().getRateAverage());
        binding.countRate.setText(workerDetail.getTotalReviews()+" đánh giá");
        binding.countRate2.setText(workerDetail.getTotalReviews()+" đánh giá");
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        listRate = new ArrayList<>();
        loadData();
        adapter = new ItemRateWorkerAdapter(listRate);

        binding.rclListRate.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclListRate.setAdapter(adapter);

    }

    void loadData(){
        binding.refreshLayout.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getListRateOfWorker(workerDetail.getId()).enqueue(new Callback<ResponseRetrofit<ArrayList<Rate>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<Rate>>> call, Response<ResponseRetrofit<ArrayList<Rate>>> response) {
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listRate.clear();
                        listRate.addAll(response.body().getResultObj());
                        adapter.notifyDataSetChanged();
                    } else {
                        if(getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if(getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ArrayList<Rate>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ",t );
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}