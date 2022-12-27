package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ActivityForgotPasswordBinding;
import com.example.pbl6app.fragment.DatePickerFragment;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private boolean isSetDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initListener();
    }

    private void initView() {

    }

    private void initListener() {
        binding.edtSignupDob.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(result -> {
                binding.edtSignupDob.setText(result);
                isSetDate = true;
            });
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });

        binding.btnNext.setOnClickListener(v -> {
            String email = binding.edtSignupEmail.getText().toString();
            String username = binding.edtSignupUsername.getText().toString();
            String phone = binding.edtSignupNumber.getText().toString();
            String dob = binding.edtSignupDob.getText().toString();

            if(email.isEmpty() || username.isEmpty() || phone.isEmpty() || !isSetDate){
                Toast.makeText(this, "Vui lòng điền đầy đủ các thông tin!", Toast.LENGTH_SHORT).show();
            }else{
                verifyInformation(email, username, phone, dob);
            }
        });
    }

    private void verifyInformation(String email, String username, String phone, String dob) {

        binding.progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> body = new HashMap<>();
        body.put("userName", username);
        body.put("dateOfBirth", dob);
        body.put("email", email);
        body.put("phoneNumber", phone);

        ApiService.apiService.verifyUserInformation(body).enqueue(new Callback<ResponseRetrofit<Object>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Object>> call, Response<ResponseRetrofit<Object>> response) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, EnterNewPasswordActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Thông tin không hợp lệ!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }

                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Object>> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }


}