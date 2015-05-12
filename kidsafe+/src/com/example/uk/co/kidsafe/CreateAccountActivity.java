package com.example.uk.co.kidsafe;

import android.app.Activity;
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
import android.widget.Toast;

public class CreateAccountActivity extends Activity {
	private EditText password = null;
	private EditText username = null;
	private EditText name1 = null;
	private EditText name2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		
		password = (EditText) findViewById(R.id.pass2);
		username = (EditText) findViewById(R.id.name);
		name1 = (EditText) findViewById(R.id.user_name2);
		name2 = (EditText) findViewById(R.id.name3);
		
		Button submit = (Button) findViewById(R.id.button3);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tempPass     = password.getText().toString();
				String tempUsername = username.getText().toString();
				String tempName     = name1.getText().toString();
				String tempName2    = name2.getText().toString();
				password.setText("");
				username.setText("");
				name1.setText("");
				name2.setText("");
				
				if(tempPass.equals("") || tempUsername.equals("")) {
					Toast.makeText(CreateAccountActivity.this, "Please Enter a Value for Each Field", Toast.LENGTH_SHORT).show();
				} else {
					SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CreateAccountActivity.this);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString("username",tempUsername);
					editor.putString("password",tempPass);
					editor.putString("firstname",tempName);
					editor.putString("lastname",tempName2);
					editor.apply();
					
					 Intent startCreateAccount = new Intent(CreateAccountActivity.this, MainActivity.class);
					 startActivity(startCreateAccount);
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_account, menu);
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
}
