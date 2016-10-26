package com.example.michael.cs.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Patrick PC on 26.10.2016.
 */

public class HueExampleAsyncTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = "HueExampleAsyncTask";
    private final Context context;
    private HueExampleAsyncTaskListener listener;

    public HueExampleAsyncTask(HueExampleAsyncTaskListener listener, Context context) {

        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "doInBackground: HTTP_OK");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));


                br.close();
                is.close();
                conn.disconnect();
            } else {
                Log.i(TAG, "doInBackground: HTTP NOT OK");
                throw new IllegalStateException("HTTP response: " + responseCode);
            }

            return "fine";
        } catch (IOException e) {
            e.printStackTrace();
            String error = e.toString();
            return "error " + error;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result.equals("fine")) {
            listener.finished(true);
        } else {
            listener.finished(false);
        }
    }
}
