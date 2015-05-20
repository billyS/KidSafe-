package com.example.uk.co.kidsafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUs extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);

		TextView tv = (TextView) findViewById(R.id.textView1);

		tv.setText(getString(R.string.about_us));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.mymenu, menu);
			return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			switch (id) {
			case R.id.aboutUs:
				Intent intent = new Intent(AboutUs.this, AboutUs.class);
				startActivity(intent);
				break;
			case R.id.preferences:
				
				break;
			case R.id.exit:
				finish();
				break;
			}
		
			return super.onOptionsItemSelected(item);
	}
}
