package com.example.michael.cs.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Wird aufgerufen, wenn das Gerät vollständig hochgefahren wurde
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        //TODO
//        context.startService(new Intent(context, MQTTService.class));
    }
}
