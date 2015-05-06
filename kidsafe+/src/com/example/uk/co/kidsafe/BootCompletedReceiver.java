package com.example.uk.co.kidsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

    final static String TAG = "BootCompletedReceiver";

    //starting service when device boots
    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i(TAG, "Starting Location Service...");
        context.startService(new Intent(context, LocationService.class));
    }
}