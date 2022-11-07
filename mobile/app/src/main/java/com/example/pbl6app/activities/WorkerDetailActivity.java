package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.pbl6app.databinding.ActivitySignupBinding;
import com.example.pbl6app.databinding.ActivityWorkerDetailBinding;

public class WorkerDetailActivity extends AppCompatActivity {
    private ActivityWorkerDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWorkerDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}