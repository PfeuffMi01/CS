package com.example.michael.cs.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.michael.cs.Services.MQTTService;


/**
 * Created by Patrick PC on 23.11.2016.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        context.startService(new Intent(context, MQTTService.class));
    }
}
