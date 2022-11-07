package com.example.pbl6app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.WorkerLinesAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.R;
import com.example.pbl6app.databinding.FragmentListWorkerBinding;

import java.util.ArrayList;

public class ListWorkerFragment extends FragmentBase{

    private FragmentListWorkerBinding binding;
    private TypeOfJob typeOfJob;
    private ArrayList<Worker> listWorker;
    private WorkerLinesAdapter workerLinesAdapter;

    public ListWorkerFragment(TypeOfJob typeOfJob) {
        this.typeOfJob = typeOfJob;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListWorkerBinding.inflate(inflater, container, false);
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
        binding.txtTittleListWorkerFragment.setText(getResources().getString(R.string.Workers, typeOfJob.getName()));

        listWorker = new ArrayList<>();
        initData();
        workerLinesAdapter = new WorkerLinesAdapter(listWorker, new OnItemCLickListener<Worker>() {
            @Override
            public void onItemClick(Worker item) {

            }
        });

        binding.recyclerWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerWorker.setAdapter(workerLinesAdapter);
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });
    }

    private void initData() {

        listWorker.add(new Worker(
                1,
                "James Cameron",
                "https://thegioidienanh.vn/stores/news_dataimages/huonggiang/102017/01/14/3306_140719-ramano-james-cameron-tease_nn0wvp.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1202
        ));
        listWorker.add(new Worker(
                2,
                "James Cameron 4",
                "https://image.thanhnien.vn/w1024/Uploaded/2022/abfluao/2022_09_16/2-2440.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1203
        ));
        listWorker.add(new Worker(
                3,
                "James Cameron 3 ",
                "https://i.imgur.com/dYyNiNQ.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1210
        ));
        listWorker.add(new Worker(
                4,
                "James Cameron 2 ",
                "https://static.tuoitre.vn/tto/i/s626/2011/12/20/GRcMwUpE.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1120
        ));
        listWorker.add(new Worker(
                5,
                "James Cameron 1 ",
                "https://znews-photo.zingcdn.me/w660/Uploaded/aobovhp/2020_09_27/james_cameron_in_atlantis_1.jpg",
                1,
                "Electrical Worker",
                4.9f,
                2120
        ));
        listWorker.add(new Worker(
                1,
                "James Cameron",
                "https://thegioidienanh.vn/stores/news_dataimages/huonggiang/102017/01/14/3306_140719-ramano-james-cameron-tease_nn0wvp.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1202
        ));
        listWorker.add(new Worker(
                2,
                "James Cameron 4",
                "https://image.thanhnien.vn/w1024/Uploaded/2022/abfluao/2022_09_16/2-2440.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1203
        ));
        listWorker.add(new Worker(
                3,
                "James Cameron 3 ",
                "https://i.imgur.com/dYyNiNQ.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1210
        ));
        listWorker.add(new Worker(
                4,
                "James Cameron 2 ",
                "https://static.tuoitre.vn/tto/i/s626/2011/12/20/GRcMwUpE.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1120
        ));
        listWorker.add(new Worker(
                5,
                "James Cameron 1 ",
                "https://znews-photo.zingcdn.me/w660/Uploaded/aobovhp/2020_09_27/james_cameron_in_atlantis_1.jpg",
                1,
                "Electrical Worker",
                4.9f,
                2120
        ));
        listWorker.add(new Worker(
                1,
                "James Cameron",
                "https://thegioidienanh.vn/stores/news_dataimages/huonggiang/102017/01/14/3306_140719-ramano-james-cameron-tease_nn0wvp.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1202
        ));
        listWorker.add(new Worker(
                2,
                "James Cameron 4",
                "https://image.thanhnien.vn/w1024/Uploaded/2022/abfluao/2022_09_16/2-2440.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1203
        ));
        listWorker.add(new Worker(
                3,
                "James Cameron 3 ",
                "https://i.imgur.com/dYyNiNQ.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1210
        ));
        listWorker.add(new Worker(
                4,
                "James Cameron 2 ",
                "https://static.tuoitre.vn/tto/i/s626/2011/12/20/GRcMwUpE.jpg",
                1,
                "Electrical Worker",
                4.9f,
                1120
        ));
        listWorker.add(new Worker(
                5,
                "James Cameron 1 ",
                "https://znews-photo.zingcdn.me/w660/Uploaded/aobovhp/2020_09_27/james_cameron_in_atlantis_1.jpg",
                1,
                "Electrical Worker",
                4.9f,
                2120
        ));
    }
}
