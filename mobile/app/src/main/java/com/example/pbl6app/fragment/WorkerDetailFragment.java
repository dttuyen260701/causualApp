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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.JobInfoAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentWorkerDetailBinding;
import com.squareup.picasso.Picasso;

public class WorkerDetailFragment extends FragmentBase {

    private FragmentWorkerDetailBinding binding;
    private WorkerDetail worker;
    private JobInfoAdapter adapter;

    public WorkerDetailFragment(WorkerDetail worker) {
        this.worker = worker;
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
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        Picasso.get().load(Constant.BASE_URL + worker.getLinkIMG()).into(binding.imvAva);
        binding.tvUsername.setText(worker.getName());
        binding.tvAdress.setText(worker.getAddress());
        binding.tvPhone.setText(worker.getPhone());
        binding.tvWorkingTime.setText(worker.getWorkingTime());
        binding.ratingBarReviewFirstRow.setRating(worker.getRate().getRateAverage());
        binding.totalReviews.setText("(" + worker.getTotalReviews() + " đánh giá)");

        adapter = new JobInfoAdapter(worker.getListJobList(), new OnItemCLickListener<JobInfo>() {
            @Override
            public void onItemClick(JobInfo item) {

            }
        });

        binding.rclJobInfo.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        binding.rclJobInfo.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });
    }
}
