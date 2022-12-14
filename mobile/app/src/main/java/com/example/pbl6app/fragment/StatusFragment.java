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
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pbl6app.Adapters.StatusFragmentAdapter;
import com.example.pbl6app.databinding.FragmentStatusBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatusFragment extends Fragment {

    private FragmentStatusBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusFragmentAdapter adapter = new StatusFragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if(position == 0){
                tab.setText("Đang thực hiện");
            } else {
                tab.setText("Đang chờ");
            }
        }).attach();
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        });
    }
}
