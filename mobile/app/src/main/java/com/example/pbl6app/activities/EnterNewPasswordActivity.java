package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ActivityEnterNewPasswordBinding;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterNewPasswordActivity extends AppCompatActivity {

    private ActivityEnterNewPasswordBinding binding;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEnterNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = getIntent().getStringExtra("username");

        Toast.makeText(this, "Xác thực thông tin thành công!", Toast.LENGTH_SHORT).show();

        initListener();
    }

    private void initListener() {
        binding.btnUpdate.setOnClickListener(v -> {
            String pw = binding.edtPassword.getText().toString();
            String cfPw = binding.edtConfirmPassword.getText().toString();

            if(pw.isEmpty() || cfPw.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đủ các thông tin!", Toast.LENGTH_SHORT).show();
            }else{
                Pattern pattern = Pattern.compile(Constant.PASSWORD_PATTERN, Pattern.CASE_INSENSITIVE);
                if( pattern.matcher(pw).matches()){
                    if(pw.equals(cfPw)){
                        handleUpdateNewPassword(pw);
                    }else {
                        Toast.makeText(this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Mật khẩu phải có chữ hoa, chữ thường và tý tự đặc biệt!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleUpdateNewPassword(String pw) {

        binding.progressBar.setVisibility(View.VISIBLE);


        HashMap<String, String> body = new HashMap<>();
        body.put("userName", username);
        body.put("newPassword", pw);

        ApiService.apiService.changePasswordWithoutOldPassword(body).enqueue(new Callback<ResponseRetrofit<Object>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Object>> call, Response<ResponseRetrofit<Object>> response) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        Toast.makeText(EnterNewPasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EnterNewPasswordActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EnterNewPasswordActivity.this, "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }

                binding.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Object>> call, Throwable t) {
                Toast.makeText(EnterNewPasswordActivity.this, "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }
}