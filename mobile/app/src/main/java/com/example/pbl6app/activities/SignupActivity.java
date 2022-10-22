package com.example.pbl6app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.databinding.ActivitySignupBinding;
import com.example.pbl6app.databinding.ActivitySignupEmailBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        openLogin();
        formatDate();
    }

    void openLogin(){
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    String formatDate(){
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        binding.edtSignupAge.setHint(timeStamp);
        try{
            String sDate1=binding.edtSignupAge.getText().toString();
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            return sDate1;
        }
        catch (Exception e){
            Toast.makeText(SignupActivity.this, "Please input date as dd/MM/yyyy", Toast.LENGTH_LONG);
        }
        return timeStamp;
    }

}