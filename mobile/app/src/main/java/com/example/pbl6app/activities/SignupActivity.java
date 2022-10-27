package com.example.pbl6app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbl6app.Asynctasks.RequestTaskAsyncTask;
import com.example.pbl6app.Listeners.RequestTaskListener;
import com.example.pbl6app.databinding.ActivityMainBinding;
import com.example.pbl6app.databinding.ActivitySignupBinding;
import com.example.pbl6app.utils.Constant;
import com.example.pbl6app.utils.Methods;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private void initView() {

    }

    private void initListener() {

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!onCheckValid()) {
                    Toast.makeText(SignupActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
                } else {

                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("email", binding.edtSignupEmail.getText().toString());
                        jsonParam.put("userName", binding.edtSignupEmail.getText().toString());
                        jsonParam.put("password", binding.edtSignupPw1.getText().toString());
                        jsonParam.put("name", binding.edtSignupName.getText().toString());
                        jsonParam.put("phone", binding.edtSignupPhone.getText().toString());
                        jsonParam.put("gender", 2);
                        jsonParam.put("address", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onSubmitData(jsonParam);
                }
            }
        });

        binding.edtSignupEmail.addTextChangedListener(new TextWatcher() {
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

        binding.edtSignupPw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern pattern = Pattern.compile(Constant.PASSWORD_PATTERN,Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(s);
                if(matcher.matches()){
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
                if(s.toString().equals(binding.edtSignupPw1.getText().toString())){
                    binding.tvWarningConfirm.setVisibility(View.GONE);
                } else {
                    binding.tvWarningConfirm.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private boolean onCheckValid() {
        if(binding.tvWarningConfirm.getVisibility() == View.VISIBLE)
            return false;
        if(binding.tvWarningEmail.getVisibility() == View.VISIBLE)
            return false;
        if(binding.tvWarningPass.getVisibility() == View.VISIBLE)
            return false;
        return true;
    }

    private void onSubmitData(JSONObject js_param) {
        RequestTaskAsyncTask requestTaskAsyncTask = new RequestTaskAsyncTask(js_param, false, new RequestTaskListener() {
            @Override
            public void onPre() {
                if (!Methods.getInstance().isNetworkConnected(SignupActivity.this)) {
                    Toast.makeText(SignupActivity.this, "Please connect Internet", Toast.LENGTH_SHORT).show();
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.viewBg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean value, boolean done, String message) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (done) {
                    Log.e("DDD", "onEnd: " + value);
                    if (value) {
                        Toast.makeText(SignupActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestTaskAsyncTask.execute(Constant.BASE_URL + "api/app/account/register-as-customer", "POST");
    }

}