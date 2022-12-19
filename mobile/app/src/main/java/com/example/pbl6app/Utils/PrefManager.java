package com.example.pbl6app.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.activities.MainActivity;
import com.example.pbl6app.activities.MainActivityUser;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrefManager {
    Context context;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", username);
        editor.putString("password", password);
        editor.commit();
    }

    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", "");
        editor.putString("password", "");
        editor.commit();
    }

    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("userName", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("userName", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public String getEmail(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("userName","");
    }

    public String getPassword(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("password","");
    }

    public void rememberLoginDetails(String username, String password, boolean isRemembered) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userNameRemember", username);
        editor.putString("passwordRemember", password);
        editor.putBoolean("isRemembered", isRemembered);

        editor.commit();
    }

    public boolean isUserRemembered() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isRemembered", false);
    }

    public String getRememberedEmail(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("userNameRemember","");
    }

    public String getRememberedPassword(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.DATA_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getString("passwordRemember","");
    }




}
