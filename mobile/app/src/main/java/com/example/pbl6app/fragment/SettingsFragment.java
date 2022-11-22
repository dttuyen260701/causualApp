package com.example.pbl6app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.databinding.FragmentSettingsBinding;

public class SettingsFragment extends FragmentBase{

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
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
        binding.txtAccountFrag.setText(Constant.USER.getName());
    }

    @Override
    protected void initListener() {
        binding.btnProfile.setOnClickListener(view -> {
            addFragment(new ProfileFragment(), R.id.ctFragmentUser);
        });

        binding.btnLogOut.setOnClickListener(view -> {
            Constant.USER = new User();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
    }
}
