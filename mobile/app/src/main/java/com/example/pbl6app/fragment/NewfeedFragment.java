package com.example.pbl6app.fragment;/*
 * Created by tuyen.dang on 10/20/2022
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.NewsPostAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.activities.BaseActivity;
import com.example.pbl6app.databinding.FragmentNewfeedBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewfeedFragment extends FragmentBase {

    private FragmentNewfeedBinding binding;
    private ArrayList<String> listData;
    private NewsPostAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewfeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imageView2, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });

        binding.tvNameFragUserHome.setText(Constant.USER.getName());

        adapter = new NewsPostAdapter(listData, new OnItemCLickListener<String>() {
            @Override
            public void onItemClick(String item) {

            }
        });

        binding.rclViewData.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclViewData.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

    }

    private void loadData() {
        listData = new ArrayList<>();
        listData.add("https://img6.thuthuatphanmem.vn/uploads/2022/04/16/anh-nen-manchester-united-full-hd-cho-may-tinh_052457852.jpg");
        listData.add("https://i.pinimg.com/736x/8d/bd/7f/8dbd7f78ffc2c1bb799501c6abd64dda.jpg");
        listData.add("https://mega.com.vn/media/news/0206_hinh-nen-MU-may-tinh4.jpg");
        listData.add("https://img6.thuthuatphanmem.vn/uploads/2022/04/16/anh-nen-manchester-united-full-hd-cho-may-tinh_052457852.jpg");
        listData.add("https://i.pinimg.com/736x/8d/bd/7f/8dbd7f78ffc2c1bb799501c6abd64dda.jpg");
        listData.add("https://mega.com.vn/media/news/0206_hinh-nen-MU-may-tinh4.jpg");
        listData.add("https://img6.thuthuatphanmem.vn/uploads/2022/04/16/anh-nen-manchester-united-full-hd-cho-may-tinh_052457852.jpg");
        listData.add("https://i.pinimg.com/736x/8d/bd/7f/8dbd7f78ffc2c1bb799501c6abd64dda.jpg");
        listData.add("https://mega.com.vn/media/news/0206_hinh-nen-MU-may-tinh4.jpg");
    }
}
