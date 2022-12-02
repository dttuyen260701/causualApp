package com.example.pbl6app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pbl6app.Adapters.OrderItemLinesAdapter;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.databinding.FragmentInProgressOrderBinding;
import com.example.pbl6app.databinding.FragmentWaitingOrderBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WaitingOrderFragment extends FragmentBase {

    private FragmentWaitingOrderBinding binding;
    private ArrayList<Order> listOrder;
    private OrderItemLinesAdapter adapter;

    public WaitingOrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitingOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected void initView() {
        listOrder = new ArrayList<>();
        loadData();
        adapter = new OrderItemLinesAdapter(listOrder, item -> addFragment(new OrderDetailFragment(), R.id.ctFragmentUser));

        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerOrder.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    void loadData(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        listOrder.add(new Order("job 1", currentDate, "Nguyen Van A", "Waiting"));
        listOrder.add(new Order("job 2", currentDate, "Nguyen Van A","Waiting"));
        listOrder.add(new Order("job 3", currentDate, "Nguyen Van A","Waiting"));
        listOrder.add(new Order("job 4", currentDate, "Nguyen Van A","Waiting"));
        listOrder.add(new Order("job 5", currentDate,"Nguyen Van A","Waiting"));
    }
}