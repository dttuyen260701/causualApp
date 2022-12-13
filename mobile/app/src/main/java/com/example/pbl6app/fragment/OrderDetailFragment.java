package com.example.pbl6app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pbl6app.Adapters.JobInfoOrderDetailAdapter;
import com.example.pbl6app.Adapters.OrderItemLinesAdapter;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.databinding.FragmentHistoryBinding;
import com.example.pbl6app.databinding.FragmentOrderDetailBinding;

import java.util.ArrayList;

public class OrderDetailFragment extends FragmentBase {
    private FragmentOrderDetailBinding binding;
    private Order order;
    private JobInfo jobInfo;

    public OrderDetailFragment() {}

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
    }


    @Override
    protected void initView() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });
        loadData();
    }

    @Override
    protected void initListener() {

    }

    void loadData(){
    }
}