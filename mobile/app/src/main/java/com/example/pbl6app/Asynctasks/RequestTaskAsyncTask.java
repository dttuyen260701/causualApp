package com.example.pbl6app.Asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pbl6app.Listeners.RequestTaskListener;
import com.example.pbl6app.utils.Methods;

import org.json.JSONObject;

public class RequestTaskAsyncTask extends AsyncTask<String, String, Boolean> {
    private boolean value;
    private RequestTaskListener listener;
    private JSONObject jsonParams;
    private boolean for_add_matrix;
    private String message = "";

    public RequestTaskAsyncTask(JSONObject jsonParams, boolean for_add_matrix, RequestTaskListener listener) {
        this.listener = listener;
        this.jsonParams = jsonParams;
        this.for_add_matrix = for_add_matrix;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPre();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            JSONObject data = Methods.getInstance().make_Request(
                    strings[0],
                    strings[1],
                    jsonParams);
            value = data.getBoolean("isSuccessed");
            message = data.getString("message");
        } catch (Exception e) {
            Log.e("ERR", "doInBackground: " + e);
            value = false;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onEnd(value, aBoolean, message);
    }
}
