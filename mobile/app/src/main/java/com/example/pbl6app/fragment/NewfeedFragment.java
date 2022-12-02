package com.example.pbl6app.fragment;/*
 * Created by tuyen.dang on 10/20/2022
 */

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pbl6app.Adapters.NewsPostAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.activities.BaseActivity;
import com.example.pbl6app.databinding.FragmentNewfeedBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewfeedFragment extends FragmentBase {

    private FragmentNewfeedBinding binding;
    private ArrayList<PostOfDemand> listData;
    private NewsPostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewfeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imageView2, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });

        binding.tvNameFragUserHome.setText(Constant.USER.getName());

        adapter = new NewsPostAdapter(listData, new OnItemCLickListener<PostOfDemand>() {
            @Override
            public void onItemClick(PostOfDemand item) {

            }
        });

        binding.rclViewData.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclViewData.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        binding.refreshLayout.setOnRefreshListener(this::loadData);
    }

    private void loadData() {
        listData = new ArrayList<>();

        binding.refreshLayout.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getListPostOfDemand(Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<ArrayList<PostOfDemand>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<PostOfDemand>>> call, Response<ResponseRetrofit<ArrayList<PostOfDemand>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listData.clear();
                        listData.addAll(response.body().getResultObj());
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
            public void onFailure(Call<ResponseRetrofit<ArrayList<PostOfDemand>>> call, Throwable t) {
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
