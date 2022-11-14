package com.example.pbl6app.fragment;
/*
 * Created by tuyen.dang on 11/9/2022
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.databinding.FragmentWorkerDetailBinding;
import com.squareup.picasso.Picasso;

public class WorkerDetailFragment extends FragmentBase {

    private FragmentWorkerDetailBinding binding;
    private WorkerDetail worker;

    public WorkerDetailFragment(int ID) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkerDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    @Override
    protected void initView() {
        Picasso.get().load(worker.getLinkIMG()).into(binding.imvAva);
        binding.tvUsername.setText(worker.getName());
        binding.tvAdress.setText(worker.getAddress());
        binding.tvPhone.setText(worker.getPhone());
        binding.tvWorkingTime.setText(worker.getWorkingTime());
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });
    }
}
