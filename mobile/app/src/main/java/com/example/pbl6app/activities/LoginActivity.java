package com.example.pbl6app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.databinding.ActivityLoginBinding;
import com.example.pbl6app.Utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initView();
        initListener();
    }

    private void initView() {
        if(!Constant.username.isEmpty()) {
            binding.edtLoginUser.setText(Constant.username);
        }

        if(!Constant.pass.isEmpty()) {
            binding.edtLoginPass.setText(Constant.pass);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initListener() {
        binding.btnLogin.setOnClickListener(view -> {


            if (!onCheckValid()) {
                Toast.makeText(LoginActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
            } else {
                onSubmitData();
            }
        });


        binding.layoutSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupEmailActivity.class);
            startActivity(intent);
        });
    }

    private boolean onCheckValid() {
        if (binding.edtLoginUser.getText().toString().isEmpty())
            return false;
        return !binding.edtLoginPass.getText().toString().isEmpty();
    }

    private void onSubmitData() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);
        binding.layoutPass.setVisibility(View.GONE);
        binding.layoutUser.setVisibility(View.GONE);

        Map<String , String> options = new HashMap<>();
        options.put("userName", binding.edtLoginUser.getText().toString());
        options.put("password", binding.edtLoginPass.getText().toString());
        options.put("rememberMe", "false");
        options.put("withRole", "0");
        ApiService.apiService.login(options).enqueue(new Callback<ResponseRetrofit<User>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<User>> call, Response<ResponseRetrofit<User>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.layoutPass.setVisibility(View.VISIBLE);
                binding.layoutUser.setVisibility(View.VISIBLE);
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        Constant.USER = response.body().getResultObj();
                        if(Constant.USER.getRole().equalsIgnoreCase("Thợ")) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivityUser.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<User>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.layoutPass.setVisibility(View.VISIBLE);
                binding.layoutUser.setVisibility(View.VISIBLE);
                Log.e("TTT", "onFailure: ", t);
            }
        });
    }
}