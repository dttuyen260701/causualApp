package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.databinding.ActivitySignupBinding;
import com.example.pbl6app.Utils.Constant;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initListener();
    }

    private void initListener() {

        binding.btnSignup.setOnClickListener(view -> {

            if (!onCheckValid()) {
                Toast.makeText(SignupActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
            } else {
                onSubmitData();
            }
        });

        binding.edtSignupPw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern pattern = Pattern.compile(Constant.PASSWORD_PATTERN, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(s);
                if (matcher.matches() && checkPass(s.toString())) {
                    binding.tvWarningPass.setVisibility(View.GONE);
                } else {
                    binding.tvWarningPass.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtSignupPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(binding.edtSignupPw1.getText().toString())) {
                    binding.tvWarningConfirm.setVisibility(View.GONE);
                } else {
                    binding.tvWarningConfirm.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tvLoginOpen.setOnClickListener(v -> finish());
    }

    private boolean checkPass(String s) {
        if (s.equals(s.toLowerCase()))
            return false;
        return !s.equals(s.toUpperCase());
    }

    private boolean onCheckValid() {
        if (binding.tvWarningConfirm.getVisibility() == View.VISIBLE)
            return false;
//        if (binding.tvWarningEmail.getVisibility() == View.VISIBLE)
//            return false;
        return binding.tvWarningPass.getVisibility() != View.VISIBLE;
    }

    @SuppressLint("SimpleDateFormat")
    private void onSubmitData() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        Map<String , String> options = new HashMap<>();
        options.put("email", Constant.email);
        options.put("password", binding.edtSignupPw1.getText().toString());
        options.put("userName", binding.edtSignupUsername.getText().toString());
        options.put("name", binding.edtSignupName.getText().toString());
        options.put("phone", binding.edtSignupPhone.getText().toString());
        ApiService.apiService.register(options).enqueue(new Callback<ResponseRetrofit<Object>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Object>> call, Response<ResponseRetrofit<Object>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        Constant.username = binding.edtSignupUsername.getText().toString();
                        Constant.pass = binding.edtSignupPw1.getText().toString();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignupActivity.this, "Đăng kí thành công!!!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Object>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                Toast.makeText(SignupActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }

}