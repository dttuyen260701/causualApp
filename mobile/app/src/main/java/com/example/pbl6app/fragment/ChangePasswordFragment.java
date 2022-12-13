package com.example.pbl6app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.databinding.FragmentChangePasswordBinding;
import com.example.pbl6app.databinding.FragmentHistoryBinding;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends FragmentBase {
    private FragmentChangePasswordBinding binding;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.edtOldPass.addTextChangedListener(new TextWatcher() {
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

        binding.edtNewPassword.addTextChangedListener(new TextWatcher() {
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

        binding.edtConfirmPass.addTextChangedListener(new TextWatcher() {
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

    @Override
    protected void initListener() {
        binding.btnSubmit.setOnClickListener(view -> {
            if(checkEnable()){
                onSubmitData();
            }
        });
    }



    boolean checkEnable(){
        if(binding.edtOldPass.getText().toString().isEmpty()){
            binding.tvWarningOld.setVisibility(View.VISIBLE);
            binding.btnSubmit.setEnabled(false);
            return false;
        }
        if(binding.edtNewPassword.getText().toString().isEmpty()){
            binding.tvWarningPass.setVisibility(View.VISIBLE);
            binding.btnSubmit.setEnabled(false);
            return false;
        }
        if(binding.edtConfirmPass.getText().toString().isEmpty()){
            binding.tvWarningConfirm.setVisibility(View.VISIBLE);
            binding.btnSubmit.setEnabled(false);
            return false;
        }

        binding.btnSubmit.setEnabled(true);
        return true;

    }

    void onSubmitData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        Map<String , String> options = new HashMap<>();
        options.put("oldPassword", binding.edtOldPass.getText().toString());
        options.put("newPassword", binding.edtNewPassword.getText().toString());

        ApiService.apiService.changePassword(options, Constant.USER.getId()).enqueue(new Callback<ResponseRetrofit<User>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<User>> call, Response<ResponseRetrofit<User>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        showSuccessDialog();
                    }
                    else{
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<User>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("CHANGE_PASS", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(getContext());
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
        txtTitleDialog.setText("Đổi mật khẩu thành công");

        Button btnOK = dialog.findViewById(R.id.btnOk);
        btnOK.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

}