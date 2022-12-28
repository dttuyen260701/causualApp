package com.example.pbl6app.fragment;

/*
 * Created by tuyen.dang on 11/6/2022
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pbl6app.Adapters.SlideShowAdapter;
import com.example.pbl6app.Adapters.UsedServiceAdapter;
import com.example.pbl6app.Adapters.WorkerLinesAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.SlideItem;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.databinding.FragmentUserHomeBinding;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import com.example.pbl6app.Utils.Constant;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private UsedServiceAdapter usedServiceAdapter;
    private static ArrayList<WorkerDetail> listWorker;
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
        if(listTypeOfJob == null || listWorker == null || listSlideItem == null) {
            initData();
        }
        initView();
        initListener();
        if(listTypeOfJob.isEmpty() || listWorker.isEmpty() || listSlideItem.isEmpty()) {
            loadData();
        }
    }

    private void initData() {
        listTypeOfJob = new ArrayList<>();
        listWorker = new ArrayList<>();
        listSlideItem = new ArrayList<>();

//        listTypeOfJob.add(new TypeOfJob("1", "Fixing", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
//        listTypeOfJob.add(new TypeOfJob("2", "Cleaning", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
//        listTypeOfJob.add(new TypeOfJob("3", "Fixing 1", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
//        listTypeOfJob.add(new TypeOfJob("4", "Cleaning 1", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
//        listTypeOfJob.add(new TypeOfJob("5", "Fixing 2", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
//        listTypeOfJob.add(new TypeOfJob("6", "Cleaning 2", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
//        listTypeOfJob.add(new TypeOfJob("7", "Fixing 3", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
//        listTypeOfJob.add(new TypeOfJob("8", "Cleaning 3", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));
//        listTypeOfJob.add(new TypeOfJob("9", "Fixing 4", "https://cdn-icons-png.flaticon.com/512/2907/2907930.png"));
//        listTypeOfJob.add(new TypeOfJob("10", "Cleaning 4", "https://cdn-icons-png.flaticon.com/512/2946/2946701.png"));

//        listWorker.add(new Worker(
//                "1",
//                "James Cameron",
//                "https://thegioidienanh.vn/stores/news_dataimages/huonggiang/102017/01/14/3306_140719-ramano-james-cameron-tease_nn0wvp.jpg",
//                new Rate(4.5f , 5.0f, 4f, 4f),
//                123,
//                new ArrayList<>()
//        ));
//        listWorker.add(new Worker(
//                "2",
//                "James Cameron 4",
//                "https://image.thanhnien.vn/w1024/Uploaded/2022/abfluao/2022_09_16/2-2440.jpg",
//                new Rate(4.5f , 5.0f, 4f, 4f),
//                123,
//                new ArrayList<>()
//        ));
//        listWorker.add(new Worker(
//                "3",
//                "James Cameron 3 ",
//                "https://i.imgur.com/dYyNiNQ.jpg",
//                new Rate(4.5f , 5.0f, 4f, 4f),
//                123,
//                new ArrayList<>()
//        ));
//        listWorker.add(new Worker(
//                "4",
//                "James Cameron 2 ",
//                "https://static.tuoitre.vn/tto/i/s626/2011/12/20/GRcMwUpE.jpg",
//                new Rate(4.5f , 5.0f, 4f, 4f),
//                123,
//                new ArrayList<>()
//        ));
//        listWorker.add(new Worker(
//                "5",
//                "James Cameron 1 ",
//                "https://znews-photo.zingcdn.me/w660/Uploaded/aobovhp/2020_09_27/james_cameron_in_atlantis_1.jpg",
//                new Rate(4.5f , 5.0f, 4f, 4f),
//                123,
//                new ArrayList<>()
//        ));
    }

    protected void initView() {

        Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imageView2, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        binding.imageView2.setImageResource(R.drawable.default_avatar);
                    }
                });

        binding.tvNameFragUserHome.setText(Constant.USER.getName());

        binding.slideShowUserHome.setOffscreenPageLimit(3);//3 item
        binding.slideShowUserHome.setClipToPadding(false);
        binding.slideShowUserHome.setClipChildren(false);

        binding.tvNameFragUserHome.setText("Xin chào");
        binding.tvDescripFragUserHome.setText(Constant.USER.getName());

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
        listSlideItem.add(new SlideItem("https://freelancervietnam.vn/wp-content/uploads/2020/05/post-thumb-dich-vu-boc-xep-hang-hoa.jpg"));
        listSlideItem.add(new SlideItem("https://empire-s3-production.bobvila.com/articles/wp-content/uploads/2021/02/What-Is-the-Going-Rate-for-House-Cleaning-1.jpg"));
        listSlideItem.add(new SlideItem("https://images.unsplash.com/photo-1556911220-e15b29be8c8f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80"));
        listSlideItem.add(new SlideItem("https://www.gardeningknowhow.com/wp-content/uploads/2019/08/landscaper.jpg"));

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

        usedServiceAdapter = new UsedServiceAdapter(listTypeOfJob, new OnItemCLickListener<TypeOfJob>() {
            @Override
            public void onItemClick(TypeOfJob item) {
                ListJobInfoFragment fragment = new ListJobInfoFragment(item);
                addFragment(fragment, R.id.ctFragmentUser);
            }
        });

//        binding.rclMostUredService.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        binding.rclMostUredService.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
        binding.rclMostUredService.setAdapter(usedServiceAdapter);


        workerLinesAdapter = new WorkerLinesAdapter(listWorker, item -> addFragment(new WorkerDetailFragment(item), R.id.ctFragmentUser));

        binding.rclMostWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclMostWorker.setAdapter(workerLinesAdapter);

    }

    protected void initListener() {
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData() {
        binding.refreshLayout.setEnabled(false);

        ApiService.apiService.getTypeOfJob().enqueue(new Callback<ResponseRetrofit<ArrayList<TypeOfJob>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<TypeOfJob>>> call, Response<ResponseRetrofit<ArrayList<TypeOfJob>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listTypeOfJob.clear();
                        listTypeOfJob.addAll(response.body().getResultObj());
                        usedServiceAdapter.notifyDataSetChanged();
                    } else {
                        if(getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if(getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ArrayList<TypeOfJob>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ",t );
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ApiService.apiService.getListWorkerByIDUser(Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<ArrayList<WorkerDetail>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<WorkerDetail>>> call, Response<ResponseRetrofit<ArrayList<WorkerDetail>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listWorker.clear();
                        listWorker.addAll(response.body().getResultObj());
                        workerLinesAdapter.notifyDataSetChanged();
                    } else {
                        if(getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if(getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ArrayList<WorkerDetail>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                Log.e("TTT", "onFailure: ",t );
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
