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

public class LoginActivity extends Activity {
	private EditText password 		= null;
	private EditText username 		= null;
	private Button createAccount    = null;
	private Button login 			= null;
	private String aPassword 		= "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		aPassword = pref.getString("passwordReset", "");
		
		createAccount = (Button) findViewById(R.id.button4);
		createAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent startCreateAccount = new Intent(LoginActivity.this,CreateAccountActivity.class);
				 startActivity(startCreateAccount);
				
			}
		});
		
		password = (EditText) findViewById(R.id.pass);
		username = (EditText) findViewById(R.id.user_name);
		
		login = (Button) findViewById(R.id.button2);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tempPass = password.getText().toString();
				String tempUsername = username.getText().toString();
				password.setText("");
				username.setText("");
				
				if(tempPass.equals("") || tempUsername.equals("")) {
					Toast.makeText(LoginActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
				} else {
					if(validateUserLogin(tempUsername, tempPass)) {
						 Intent startMain = new Intent(LoginActivity.this,MainActivity.class);
						 LoginActivity.this.startActivity(startMain); 
					}else {
						Toast.makeText(LoginActivity.this, "Incorrect User Name or Password", Toast.LENGTH_SHORT).show();
					}
					
				}
				
			}
		});
		
		validatePassword();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	
	private boolean validateUserLogin(String username, String password) {
		boolean valid = false;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		String aPassword = pref.getString("password", "");
		String aUsername = pref.getString("username", "");
		
		if(username.equals(aUsername) && password.equals(aPassword)){
			valid = true;
		}
		return valid;
	}
	
	private void validatePassword() {
		if(!aPassword.equals("")) {
			createAccount.setVisibility(View.INVISIBLE);
			login.setX(240);	
		}
	}
}
