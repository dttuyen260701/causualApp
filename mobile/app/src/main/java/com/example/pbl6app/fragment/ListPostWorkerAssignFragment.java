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

import com.example.pbl6app.Adapters.NewsPostAdapter;
import com.example.pbl6app.Adapters.OrderItemLinesAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentHistoryBinding;
import com.example.pbl6app.databinding.FragmentListPostWorkerAssignBinding;
import com.google.android.gms.common.api.Api;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPostWorkerAssignFragment extends FragmentBase {

    private FragmentListPostWorkerAssignBinding binding;
    private ArrayList<PostOfDemand> listPost;
    private NewsPostAdapter adapter;

    public ListPostWorkerAssignFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListPostWorkerAssignBinding.inflate(inflater, container, false);
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
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        listPost = new ArrayList<>();

        adapter = new NewsPostAdapter(listPost, item -> {
            addFragment(
                    DetailPostOnWorkerRoleFragment.newInstance(item.getId(), object -> {
                        loadData();
                    }),
                    R.id.ctFragmentUser
            );
        }, new ArrayList<>());

        binding.recyclerPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerPost.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        binding.swipeRefresh.setOnRefreshListener(this::loadData);
        loadData();
    }

    void loadData() {

        binding.swipeRefresh.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getListPostWorkerAssign(Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<ArrayList<PostOfDemand>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<PostOfDemand>>> call, Response<ResponseRetrofit<ArrayList<PostOfDemand>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefresh.setEnabled(true);
                binding.swipeRefresh.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listPost.clear();
                        listPost.addAll(response.body().getResultObj());
                        adapter.notifyDataSetChanged();
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
            public void onFailure(Call<ResponseRetrofit<ArrayList<PostOfDemand>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}