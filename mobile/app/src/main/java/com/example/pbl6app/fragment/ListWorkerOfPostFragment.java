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
import com.example.pbl6app.Adapters.WorkerItemOfPostAdapter;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.databinding.FragmentHistoryBinding;
import com.example.pbl6app.databinding.FragmentListWorkerOfPostBinding;

import java.util.ArrayList;

public class ListWorkerOfPostFragment extends FragmentBase {
    private FragmentListWorkerOfPostBinding binding;
    private ArrayList<Worker> listWorkers;
    private WorkerItemOfPostAdapter adapter;

    public ListWorkerOfPostFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListWorkerOfPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected void initView() {
        listWorkers = new ArrayList<>();
        loadData();
        adapter = new WorkerItemOfPostAdapter(listWorkers,
                item -> addFragment(new WorkerDetailFragment(new WorkerDetail(
                        item.getId(),
                        item.getName(),
                        item.getLinkIMG(),
                        item.getRate(),
                        item.getTotalReviews(),
                        item.getListJobList(),
                        item.getName(),
                        item.getName(),
                        item.getName(),
                        1
                )), R.id.ctFragmentUser));

        binding.recyclerWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerWorker.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    void loadData(){
    }
}