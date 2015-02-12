package com.example.uk.co.kidsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	GoogleCloudMessaging gcm;
	private Context context;

	AtomicInteger msgId = new AtomicInteger();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button conectToServer = (Button) findViewById(R.id.button1);
		conectToServer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gcm = GoogleCloudMessaging.getInstance(context);
		    	
				new AsyncTask<Void, Integer, String>() {
					 @Override
			            protected String doInBackground(Void... params) {
						 String msg = "";
			                try {
			                	Socket 			 	clientSocket = new Socket("10.0.2.2", 8080);   
			            		ObjectOutputStream  outToServer  = new ObjectOutputStream(clientSocket.getOutputStream()); 
			            		ObjectInputStream   inFromServer = new ObjectInputStream(clientSocket.getInputStream()); 
			            		StringBuilder 		sb 			 = new StringBuilder();
			            		
			            		outToServer.writeObject("Proximity Alert");
			            		
			            		while(true){
			            			if(inFromServer.readObject() == null) {
			            				break;
			            			}
			            			sb.append(inFromServer.readObject());
			            		}
			            		msg = sb.toString();
			            		
			            		Log.i("INFO",msg);
			            		
			            		outToServer.close();//TODO close these in on destroy method
			            		inFromServer.close();
			            		clientSocket.close();
			                } catch (IOException ex) {
			                    msg = "Error :" + ex.getMessage();
			                } catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			                return msg;
			            }
					 @Override
					 protected void onPostExecute(String result) {
						 //Toast.makeText(getApplicationContext(), "From Main Activity " + result, Toast.LENGTH_SHORT).show();
					 };
			        }.execute(null, null, null);   				
			}
		});
		
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.example.uk.co.kidsafe.LocationService");
		startService(serviceIntent);
		//finish();//TODO Test this works properly!!! this just makes the screen blank need to think of something else
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
}
