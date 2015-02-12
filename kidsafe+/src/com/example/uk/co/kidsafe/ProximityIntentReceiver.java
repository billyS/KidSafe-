package com.example.uk.co.kidsafe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {

	GoogleCloudMessaging gcm;
	private Context context;
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		this.context = context;
		 Log.i("INFO", "Proximity Alert Broadcast Reciever Registered");
		if (entering) {
			sendNotification(context);
			Log.d(getClass().getSimpleName(), "entering");
		}
		else {
			Log.d(getClass().getSimpleName(), "exiting");
		}

	}
	
	private void sendNotification(Context context){
		
		Log.i("INFO", "sending notification from Intent Reciever");
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
				 //Toast.makeText(getApplicationContext(), "From Intent Receiver " + result, Toast.LENGTH_SHORT).show();
			 };
	        }.execute(null, null, null);   				
		}
}
