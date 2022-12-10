package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbl6app.Listeners.Listener_for_PickAddress;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ActivityMainBinding;
import com.example.pbl6app.fragment.MapFragment_Parent;
import com.example.pbl6app.fragment.NewfeedFragment;
import com.example.pbl6app.fragment.SettingsFragment;
import com.example.pbl6app.fragment.StatusFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        initListener();
    }

    private void initView() {
//        Toast.makeText(MainActivity.this, "Chào mừng " + Constant.USER.getName() + " !!!", Toast.LENGTH_SHORT).show();
        binding.bottomNavigation.setSelectedItemId(R.id.menu_newFeed);
        Fragment fragment = new NewfeedFragment();
        addFragment(fragment);
        addFragment(new MapFragment_Parent(new Listener_for_PickAddress() {
            @Override
            public void onClick_pick(String address, float distance) {

            }
        }));
    }

    @SuppressLint("NonConstantResourceId")
    private void initListener() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    clearBackstack();
                    Thread.currentThread().interrupt();
                }
            }).start();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.menu_Info:
                    fragment = new SettingsFragment();
                    break;
                case R.id.menu_status:
                    fragment = new StatusFragment();
                    break;
                case R.id.menu_newFeed:
                    fragment = new NewfeedFragment();
                    break;
                default:
                    return true;
            }
            addFragment(fragment);
            return true;
        });

    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ctFragmentUser, fragment);
        transaction.commit();
    }

}