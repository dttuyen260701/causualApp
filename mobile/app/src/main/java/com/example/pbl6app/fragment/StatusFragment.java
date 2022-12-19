package com.example.pbl6app.fragment;/*
 * Created by tuyen.dang on 10/20/2022
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pbl6app.Adapters.StatusFragmentAdapter;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.FragmentStatusBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatusFragment extends FragmentBase {

    private FragmentStatusBinding binding;
    private static String orderId = "";
    private static boolean forWaiting = false;
    private static StatusFragmentAdapter adapter;

    public static void setOrderId(String orderId) {
        StatusFragment.orderId = orderId;
    }

    public static void setForWaiting(boolean forWaiting) {
        StatusFragment.forWaiting = forWaiting;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        initRoute();
    }

    @Override
    protected void initView() {
        adapter = new StatusFragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? "Đang chờ" : "Đang thực hiện");
            } else {
                tab.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? "Đang thực hiện" : "Đang chờ");
            }
        }).attach();


    }

    @Override
    protected void initListener() {
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                WaitingOrderFragment.setListener(item -> {
                    forWaiting = false;
                    orderId = "";
                    if (item.getStatus() == Constant.ACCEPT_STATUS) {
                        addFragment(new OrderDetailFragment(item.getId()), R.id.ctFragmentUser);
                        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1));
                        binding.viewPager.setCurrentItem(1);
                    } else if (item.getStatus() == Constant.REJECT_STATUS) {
                        Methods.makeToast("Bạn đã từ chối đơn này.");
                    }
                });
            }
        });

//        binding.tabLayout.selectTab(binding.tabLayout.getTabAt((forWaiting) ? 1 : 0));
    }

    private void initRoute() {
        if (forWaiting) {

        } else if (!orderId.equals("")) {
            addFragment(new OrderDetailFragment(orderId), R.id.ctFragmentUser);
        }
    }

    @Override
    public void onDestroy() {
        orderId = "";
        forWaiting = false;
        super.onDestroy();
    }
}
