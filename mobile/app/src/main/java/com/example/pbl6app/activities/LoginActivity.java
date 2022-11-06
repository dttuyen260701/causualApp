package com.example.pbl6app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbl6app.Asynctasks.RequestTaskAsyncTask;
import com.example.pbl6app.Listeners.RequestTaskListener;
import com.example.pbl6app.R;
import com.example.pbl6app.databinding.ActivityLoginBinding;
import com.example.pbl6app.fragment.UserHomeFragment;
import com.example.pbl6app.utils.Constant;
import com.example.pbl6app.utils.Methods;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        initView();
//        initListener();

        UserHomeFragment fragment = new UserHomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.commit();
    }

//    private void initView() {
//        if(!Constant.email.isEmpty()) {
//            binding.edtLoginUser.setText(Constant.email);
//        }
//
//        if(!Constant.pass.isEmpty()) {
//            binding.edtLoginPass.setText(Constant.pass);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initView();
//    }
//
//    private void initListener() {
//        binding.btnLogin.setOnClickListener(view -> {
//            if (!onCheckValid()) {
//                Toast.makeText(LoginActivity.this, "Data invalid", Toast.LENGTH_SHORT).show();
//            } else {
//
//                JSONObject jsonParam = new JSONObject();
//                try {
//                    jsonParam.put("email", binding.edtLoginUser.getText().toString());
//                    jsonParam.put("password", binding.edtLoginPass.getText().toString());
//                    jsonParam.put("rememberme", false);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                onSubmitData(jsonParam);
//            }
//        });
//
//
//        binding.layoutSignup.setOnClickListener(view -> {
//            Intent intent = new Intent(LoginActivity.this, SignupEmailActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    private boolean onCheckValid() {
//        if (binding.edtLoginUser.getText().toString().isEmpty())
//            return false;
//        return !binding.edtLoginPass.getText().toString().isEmpty();
//    }
//
//    private void onSubmitData(JSONObject js_param) {
//        RequestTaskAsyncTask requestTaskAsyncTask = new RequestTaskAsyncTask(js_param, false, new RequestTaskListener() {
//            @Override
//            public void onPre() {
//                if (!Methods.getInstance().isNetworkConnectedCheck(LoginActivity.this)) {
//                    Toast.makeText(LoginActivity.this, "Please connect Internet", Toast.LENGTH_SHORT).show();
//                }
//                binding.progressBar.setVisibility(View.VISIBLE);
//                binding.viewBg.setVisibility(View.VISIBLE);
//                binding.layoutPass.setVisibility(View.GONE);
//                binding.layoutUser.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onEnd(boolean value, boolean done, String message) {
//                binding.progressBar.setVisibility(View.GONE);
//                binding.viewBg.setVisibility(View.GONE);
//                binding.layoutPass.setVisibility(View.VISIBLE);
//                binding.layoutUser.setVisibility(View.VISIBLE);
//                if (done) {
//                    Log.e("DDD", "onEnd: " + value);
//                    if (value) {
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        requestTaskAsyncTask.execute(Constant.BASE_URL + "api/app/login/login", "POST");
//    }
}