package com.example.uk.co.kidsafe;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {

    final static String TAG = "BootCompletedReceiver";
    private ComponentName devAdminReceiver;
	private DevicePolicyManager dpm;
	private SharedPreferences pref;
	
    //starting service when device boots
    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i(TAG, "Starting Location Service...");
        
        context.startService(new Intent(context, LocationService.class));
    }
}