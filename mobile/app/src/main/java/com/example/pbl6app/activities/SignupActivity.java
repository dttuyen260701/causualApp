package com.example.pbl6app.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pbl6app.R;
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
            checkEnable();
             if (!onCheckValid()) {
                 Toast.makeText(SignupActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
             } else {
                 onSubmitData();
             }
        });

        binding.edtSignupName.addTextChangedListener(new TextWatcher() {
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

        binding.edtSignupUsername.addTextChangedListener(new TextWatcher() {
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

        binding.edtSignupPhone.addTextChangedListener(new TextWatcher() {
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
                checkEnable();
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
                checkEnable();
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
        if (binding.tvWarningPass.getVisibility() == View.VISIBLE)
            return false;
        return binding.tvWarningPass.getVisibility() != View.VISIBLE;
    }

    void checkEnable(){
        if(binding.edtSignupUsername.getText().toString().isEmpty()
                || binding.edtSignupPhone.getText().toString().isEmpty()
                || binding.edtSignupPw1.getText().toString().isEmpty()
                || binding.edtSignupPw2.getText().toString().isEmpty()
                || binding.edtSignupName.getText().toString().isEmpty()
        ){
            binding.btnSignup.setEnabled(false);
        }
        else{
            binding.btnSignup.setEnabled(true);
        }
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
                        showSuccessDialog();
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

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(SignupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog_arrive);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);

        TextView txtTitleDialog = dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText("Đăng ký tài khoản thành công");

        Button btnOK = dialog.findViewById(R.id.btnOk);
        btnOK.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }



}