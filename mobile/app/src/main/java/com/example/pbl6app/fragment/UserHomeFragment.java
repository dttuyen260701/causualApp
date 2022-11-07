package com.example.pbl6app.fragment;

/*
 * Created by tuyen.dang on 11/6/2022
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pbl6app.Adapters.SlideShowAdapter;
import com.example.pbl6app.Adapters.UredServiceAdapter;
import com.example.pbl6app.Adapters.WorkerLinesAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.SlideItem;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.R;
import com.example.pbl6app.databinding.FragmentUserHomeBinding;

import java.util.ArrayList;

public class UserHomeFragment extends FragmentBase {
    private FragmentUserHomeBinding binding;
    private SlideShowAdapter slideShowAdapter;
    private ArrayList<SlideItem> listSlideItem;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.slideShowUserHome.getCurrentItem();
            if (currentPosition == listSlideItem.size() - 1) {
                binding.slideShowUserHome.setCurrentItem(0);
            } else {
                binding.slideShowUserHome.setCurrentItem(currentPosition + 1);
            }
        }
    };
    private ArrayList<TypeOfJob> listTypeOfJob;
    private UredServiceAdapter uredServiceAdapter;
    private ArrayList<Worker> listWorker;
    private WorkerLinesAdapter workerLinesAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    protected void initView() {

        binding.slideShowUserHome.setOffscreenPageLimit(3);//3 item
        binding.slideShowUserHome.setClipToPadding(false);
        binding.slideShowUserHome.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        listSlideItem = new ArrayList<>();
        listSlideItem.add(new SlideItem("https://demoda.vn/wp-content/uploads/2021/12/hinh-nen-phong-canh-dep-nhat-the-gioi-800x491.jpg"));
        listSlideItem.add(new SlideItem("https://toigingiuvedep.vn/wp-content/uploads/2021/04/hinh-nen-slide-dep-chu-de-lich-su.jpg"));
        listSlideItem.add(new SlideItem("https://data.webnhiepanh.com/wp-content/uploads/2020/11/21105453/phong-canh-1.jpg"));
        listSlideItem.add(new SlideItem("https://phunugioi.com/wp-content/uploads/2020/02/hinh-anh-dep-thien-nhien.jpg"));

        binding.slideShowUserHome.setPageTransformer(compositePageTransformer);
        slideShowAdapter = new SlideShowAdapter(listSlideItem);
        binding.slideShowUserHome.setAdapter(slideShowAdapter);
        binding.indicatorUserHome.setViewPager(binding.slideShowUserHome);
        binding.slideShowUserHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SlideShowAdapter.setSelected_index(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2500);
            }
        });

        listTypeOfJob = new ArrayList<>();
        listTypeOfJob.add(new TypeOfJob(1, "Fixing", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
        listTypeOfJob.add(new TypeOfJob(2, "Cleaning", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
        listTypeOfJob.add(new TypeOfJob(3, "Fixing 1", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
        listTypeOfJob.add(new TypeOfJob(4, "Cleaning 1", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
        listTypeOfJob.add(new TypeOfJob(5, "Fixing 2", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
        listTypeOfJob.add(new TypeOfJob(6, "Cleaning 2", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
        listTypeOfJob.add(new TypeOfJob(7, "Fixing 3", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
        listTypeOfJob.add(new TypeOfJob(8, "Cleaning 3", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
        listTypeOfJob.add(new TypeOfJob(9, "Fixing 4", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
        listTypeOfJob.add(new TypeOfJob(10, "Cleaning 4", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
        listTypeOfJob.add(new TypeOfJob(11, "Fixing 5", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
        listTypeOfJob.add(new TypeOfJob(12, "Cleaning 5", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));

        uredServiceAdapter = new UredServiceAdapter(listTypeOfJob, new OnItemCLickListener<TypeOfJob>() {
            @Override
            public void onItemClick(TypeOfJob item) {
                ListWorkerFragment fragment = new ListWorkerFragment(item);
                addFragment(fragment, R.id.ctFragmentUser);
            }
        });

//        binding.rclMostUredService.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        binding.rclMostUredService.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
        binding.rclMostUredService.setAdapter(uredServiceAdapter);

        listWorker = new ArrayList<>();
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
        workerLinesAdapter = new WorkerLinesAdapter(listWorker, new OnItemCLickListener<Worker>() {
            @Override
            public void onItemClick(Worker item) {
                Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rclMostWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclMostWorker.setAdapter(workerLinesAdapter);

    }

    protected void initListener() {

    }
}
