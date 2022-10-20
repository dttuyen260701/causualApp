package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pbl6app.databinding.ActivityLoginBinding;
import com.example.pbl6app.databinding.ActivitySignupEmailBinding;

public class SignupEmailActivity extends AppCompatActivity {
    private ActivitySignupEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        backToLogin();
        openSignup();
    }

    void backToLogin(){
        binding.imvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void openSignup(){
        binding.btnSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupEmailActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}