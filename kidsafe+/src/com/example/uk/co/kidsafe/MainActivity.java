package com.example.uk.co.kidsafe;

import java.util.concurrent.atomic.AtomicInteger;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ComponentName devAdminReceiver;
	private DevicePolicyManager dpm;
	private SharedPreferences pref;

	AtomicInteger msgId = new AtomicInteger();
	private EditText password = null;
	private TextView userInfo =null;
	private TextView userInfo2 =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent serviceIntent = new Intent(MainActivity.this, LocationService.class);
		// serviceIntent.setPackage("com.example.uk.co.kidsafe");
		startService(serviceIntent);
		//android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(1);
		
		password = (EditText) findViewById(R.id.pass3);
		userInfo = (TextView) findViewById(R.id.user_info);
		userInfo.setText(getString(R.string.user_info));
		userInfo2 = (TextView) findViewById(R.id.user_info2);
		userInfo2.setText(getString(R.string.user_info2));
		Button conectToServer = (Button) findViewById(R.id.button1);
		conectToServer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tempPass = password.getText().toString();
				password.setText("");
				if(tempPass.equals("")) {
					Toast.makeText(MainActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
				} else {
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("passwordReset",tempPass);
					editor.apply();
					enableDiviceAdmin(MainActivity.this);
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void enableDiviceAdmin(Context aContext) {
		
		Context context = aContext;
		dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		devAdminReceiver = new ComponentName(context, AdminReceiver.class);
        if (!dpm.isAdminActive(devAdminReceiver)) {//FIXME add offline preference check capabilities here 
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            	    pref = PreferenceManager.getDefaultSharedPreferences(context);
            	    SharedPreferences.Editor editor = pref.edit();
					editor.putString("deviceadmin","active");
					editor.apply();
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, devAdminReceiver);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, context.getString(R.string.security_text));
                    context.startActivity(intent);
                    
            }
        } 
        finish();
	}
	
	private void disableDiviceAdmin(Context aContext) {
		
		Context context = aContext;
		dpm = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		devAdminReceiver = new ComponentName(context, AdminReceiver.class);
        if (dpm.isAdminActive(devAdminReceiver)) {
        	pref = PreferenceManager.getDefaultSharedPreferences(context);
    	    SharedPreferences.Editor editor = pref.edit();
			editor.putString("deviceadmin","deactivated");
			editor.apply();
        	dpm.removeActiveAdmin(devAdminReceiver);
        	Toast.makeText(context, "Device Administrator is disabled.", Toast.LENGTH_SHORT).show();
        } 
	}

}
