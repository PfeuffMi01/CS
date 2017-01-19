package com.example.michael.cs.Handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Patrick PC on 23.12.2016.
 */

public class InternetConnectionHandler {

    public static boolean isInternetAvailable(Context c) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static void noInternetAvailable(Context c) {
        Toast.makeText(c, "Es besteht keine Internetverbindung", Toast.LENGTH_LONG).show();
    }
}
