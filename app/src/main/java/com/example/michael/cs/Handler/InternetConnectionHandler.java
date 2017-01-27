package com.example.michael.cs.Handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.michael.cs.R;

/**
 * Kontrolliert die Internetverbindung
 */

public class InternetConnectionHandler {

    public static boolean isInternetAvailable(Context c) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static void noInternetAvailable(Context c) {
        Toast.makeText(c, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
    }
}
