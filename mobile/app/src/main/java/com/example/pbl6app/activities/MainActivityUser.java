package com.example.pbl6app.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ActivityMainUserBinding;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;
import com.example.pbl6app.fragment.UserHomeFragment;

public class MainActivityUser extends BaseActivity {

    private ActivityMainUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initListener();
    }

    private void initView() {
        Toast.makeText(MainActivityUser.this, "Chào mừng " + Constant.USER.getName() + " !!!", Toast.LENGTH_SHORT).show();
        binding.bottomNavigationUser.setSelectedItemId(R.id.menu_Home);
        UserHomeFragment fragment = new UserHomeFragment();
        addFragment(fragment, R.id.ctFragmentUser, false);
    }

    private void initListener() {
        binding.bottomNavigationUser.setOnItemSelectedListener(item -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    clearBackstack();
                    Thread.currentThread().interrupt();
                }
            }).start();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_status:
                    fragment = new StatusFragment();
                    break;
                case R.id.menu_Home:
                    fragment = new UserHomeFragment();
                    break;
                case R.id.menu_Setting:
                    fragment = new SettingsFragment();
                    break;
                default:
                    return true;
            }
            replaceFragment(fragment, R.id.ctFragmentUser, false);
            return true;
        });
    }
}