package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ActivityLoginBinding;
import com.example.pbl6app.databinding.ActivitySignupEmailBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupEmailActivity extends AppCompatActivity {
    private ActivitySignupEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initView();
        initListener();
    }

    void initView(){
    }

    void initListener(){

        binding.imvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.tvWarningEmail.getVisibility() == View.GONE) {
                    Constant.email = binding.edtForgotEmail.getText().toString();
                    Intent intent = new Intent(SignupEmailActivity.this, SignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        binding.edtForgotEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern r = Pattern.compile(Constant.EMAIL_PATTERN);
                Matcher m = r.matcher(s.toString());
                if (m.find()) {
                    binding.tvWarningEmail.setVisibility(View.GONE);
                } else {
                    binding.tvWarningEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}