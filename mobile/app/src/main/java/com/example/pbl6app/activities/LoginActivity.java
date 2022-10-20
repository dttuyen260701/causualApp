package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pbl6app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        openSignUp();
        Login();
    }

    void Login(){
        binding.btnLogin.setOnClickListener(view -> {
//                if(binding.edtLoginPass.getText().length()==0 && binding.edtLoginUser.getText().length()==0 ){
//                    return;
//                }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    void openSignUp(){
        binding.layoutSignup.setOnClickListener(view -> {
//                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
//                startActivity(intent);
        });
    }
}