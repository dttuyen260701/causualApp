package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.PrefManager;
import com.example.pbl6app.databinding.ActivitySignupBinding;
import com.example.pbl6app.databinding.ActivitySignupEmailBinding;
import com.example.pbl6app.databinding.ActivitySplashBinding;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        prefManager = new PrefManager(SplashActivity.this);
        initRoute();
    }

    private void initRoute(){
        if(prefManager.isUserLogedOut()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);

        }
        else{
            login(prefManager.getEmail(), prefManager.getPassword());
        }
    }

    private void login(String userName, String password) {
        Map<String , String> options = new HashMap<>();
        options.put("userName", userName);
        options.put("password", password);
        options.put("rememberMe", "false");
        options.put("withRole", "0");
        ApiService.apiService.login(options).enqueue(new Callback<ResponseRetrofit<User>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<User>> call, Response<ResponseRetrofit<User>> response) {

                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        new PrefManager(SplashActivity.this).saveLoginDetails(userName, password);
                        Constant.USER = response.body().getResultObj();
                        if(Constant.USER.getRole() == Constant.ROLE_WORKER) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainActivityUser.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<User>> call, Throwable t) {
                Log.e("LOGIN", "onFailure: ", t);
                Toast.makeText(SplashActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}