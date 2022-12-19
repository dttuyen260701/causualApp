package com.example.pbl6app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pbl6app.Adapters.OrderItemLinesAdapter;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentInProgressOrderBinding;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InProgressOrderFragment extends FragmentBase {

    private FragmentInProgressOrderBinding binding;
    private ArrayList<Order> listOrder;
    private OrderItemLinesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInProgressOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        loadData();
    }

    @Override
    protected void initView() {
        listOrder = new ArrayList<>();
        adapter = new OrderItemLinesAdapter(listOrder, item -> addFragment(new OrderDetailFragment(item.getId()), R.id.ctFragmentUser));

        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerOrder.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        binding.swipeRefresh.setOnRefreshListener(this::loadData);
    }

    void loadData() {
        binding.swipeRefresh.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getOrderByStatus(Constant.USER.getId(), 1).enqueue(new Callback<ResponseRetrofit<ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<Order>>> call, Response<ResponseRetrofit<ArrayList<Order>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefresh.setEnabled(true);
                binding.swipeRefresh.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listOrder.clear();
                        listOrder.addAll(response.body().getResultObj());
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
            public void onFailure(Call<ResponseRetrofit<ArrayList<Order>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}