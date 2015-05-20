package com.example.uk.co.kidsafe;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class AdminReceiver extends DeviceAdminReceiver {
	private static ComponentName devAdminReceiver;
	private static DevicePolicyManager dpm;
	private String secPassword ="";

	@Override
    public CharSequence onDisableRequested(final Context context, Intent intent) {

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		secPassword = pref.getString("password", "");
		if(!secPassword.equalsIgnoreCase("")) {
			 Intent startMain = new Intent(Intent.ACTION_MAIN);
	         startMain.addCategory(Intent.CATEGORY_HOME);
	         startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	         context.startActivity(startMain); 
	         
	         devAdminReceiver = new ComponentName(context, AdminReceiver.class);
	         dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
	         dpm.removeActiveAdmin(devAdminReceiver);
	         
	         lockPhone(context, secPassword);
	         Log.i("INFO", "DEVICE ADMINISTRATION DISABLE REQUESTED & LOCKED PHONE");
		}
            return "locked";
    }
	
    public static boolean lockPhone(Context context, String password){
    	Log.i("INFO", "LOCKING PHONE NOW");
        devAdminReceiver = new ComponentName(context, AdminReceiver.class);
        dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        boolean pwChange = dpm.resetPassword(password, 0);
        dpm.lockNow();
        return pwChange;
    }   
}
