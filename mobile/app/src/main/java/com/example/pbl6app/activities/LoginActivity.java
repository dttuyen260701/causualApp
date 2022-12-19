package com.example.pbl6app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.PrefManager;
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
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        prefManager = new PrefManager(LoginActivity.this);
        initView();
        initListener();
    }

    private void initView() {
        if(prefManager.isUserRemembered()){
            binding.edtLoginUser.setText(prefManager.getRememberedEmail());
            binding.edtLoginPass.setText(prefManager.getRememberedPassword());
            binding.checkRemember.setChecked(true);
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

        binding.edtLoginUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edtLoginPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkEnable();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean onCheckValid() {
        if (binding.edtLoginUser.getText().toString().isEmpty())
            return false;
        return !binding.edtLoginPass.getText().toString().isEmpty();
    }

    void checkEnable(){
        if(binding.edtLoginUser.getText().toString().isEmpty() || binding.edtLoginPass.getText().toString().isEmpty()
        ){
            binding.btnLogin.setEnabled(false);
        }
        else{
            binding.btnLogin.setEnabled(true);
        }
    }

    private void onSubmitData() {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

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

                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        prefManager.saveLoginDetails(binding.edtLoginUser.getText().toString(),binding.edtLoginPass.getText().toString());
                        if(binding.checkRemember.isChecked()){
                            prefManager.rememberLoginDetails(binding.edtLoginUser.getText().toString(),binding.edtLoginPass.getText().toString(), true);
                        }
                        else prefManager.rememberLoginDetails("","", false);
                        Constant.USER = response.body().getResultObj();
                        if(Constant.USER.getRole() == Constant.ROLE_WORKER) {
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
                Log.e("LOGIN", "onFailure: ", t);
                Toast.makeText(LoginActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }
}