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
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Adapters.OrderItemLinesAdapter;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ItemPaging;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentInProgressOrderBinding;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InProgressOrderFragment extends FragmentBase {

    private FragmentInProgressOrderBinding binding;
    private ArrayList<Order> listOrder;
    private OrderItemLinesAdapter adapter;
    private static int pageIndex = 1;
    private int pageSize = Constant.DEFAULT_PAGE_SIZE;
    private boolean isLoading = false;

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
        pageIndex = 1;
        listOrder = new ArrayList<>();
        adapter = new OrderItemLinesAdapter(listOrder, item -> addFragment(new OrderDetailFragment(item.getId(), object -> {
            pageIndex = 1;
            loadData();
        }), R.id.ctFragmentUser));
        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerOrder.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            pageIndex = 1;
            loadData();
        });
        binding.recyclerOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!binding.scrollView.canScrollVertically(1)) {
                        if (!isLoading) {
                            handlePagingAction(false);
                            ++pageIndex;
                            loadData();
                        }
                    }
                }
            }
        });
    }

    private void handlePagingAction(boolean isDoneLoading) {

        if (isDoneLoading) {
            isLoading = false;
            binding.progressRV.setVisibility(View.GONE);
        } else {
            isLoading = true;
            binding.progressRV.setVisibility(View.VISIBLE);
        }

    }

    void loadData() {
        if (pageIndex == 1) {
            binding.viewBg.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            binding.progressRV.setVisibility(View.GONE);
        }
        binding.swipeRefresh.setEnabled(false);
        ApiService.apiService.getOrderByStatus(Constant.USER.getId(), 1, pageIndex, pageSize).enqueue(new Callback<ResponseRetrofit<ItemPaging<ArrayList<Order>>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ItemPaging<ArrayList<Order>>>> call, Response<ResponseRetrofit<ItemPaging<ArrayList<Order>>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefresh.setEnabled(true);
                binding.swipeRefresh.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        ItemPaging<ArrayList<Order>> resultObj = response.body().getResultObj();
                        if (pageIndex == 1) {
                            listOrder.clear();
                        }
                        if (listOrder.size() < resultObj.getTotalRecords()) {
                            listOrder.addAll(resultObj.getItems());
                            adapter.notifyDataSetChanged();
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
                if (pageIndex == 1) {
                    isLoading = false;
                } else {
                    handlePagingAction(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ItemPaging<ArrayList<Order>>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
                if (pageIndex == 1) {
                    isLoading = false;
                } else {
                    handlePagingAction(true);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}