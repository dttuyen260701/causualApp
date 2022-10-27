package com.example.pbl6app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Methods {
    private static Methods Instance;

    private Methods(){

    }

    public static Methods getInstance(){
        if(Instance == null)
            Instance = new Methods();
        return Instance;
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }




//    JSONObject jsonParam = new JSONObject();
//    jsonParam.put("AuditScheduleDetailID", param1);
//    jsonParam.put("AuditAnswerId", param2);
//    jsonParam.put("LocalFindingID", param3);
//    jsonParam.put("LocalMediaID", param4);
//    jsonParam.put("Files", param5);
//    jsonParam.put("ExtFiles", param6);
    public JSONObject make_Request(String Url, String method, JSONObject js_params) throws IOException, JSONException {
        JSONObject data = null;
        URL url = new URL(Url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        if(!method.equals(HTTPMethod.GET)){
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
//            os.writeBytes(URLEncoder.encode(js_params.toString(), "UTF-8"));
            os.writeBytes(js_params.toString());
        }
        try {
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bin = new BufferedReader(new InputStreamReader(in));
                data = new JSONObject(bin.readLine());
            } else {

            }
        } catch (IOException e) {
            data = make_Request(Url, method, js_params);
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }
}
