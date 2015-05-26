package com.example.uk.co.kidsafe;

import java.io.IOException;

import java.io.ObjectOutputStream;
import java.net.Socket;

import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {

	GoogleCloudMessaging gcm;
	private Context context;
	private static final int NOTIFICATION_ID = 1000;
	JSONObject location = null;
	JSONObject locationResult = null;

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//proximity alert detected
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		this.context = context;
		 Log.i("INFO", "Proximity Alert Broadcast Reciever Registered");
		if (entering) {
			    //TODO can't decide if i want to alert the child they are entering a restricted area
			 NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
			 Notification noti = new NotificationCompat.Builder(context)
		    .setContentTitle("Proximity Alert!: ")
		    .setContentText("You are entering a restricted area")
		    .setWhen(System.currentTimeMillis())
		    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
		    .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
		    .setLights(Color.WHITE, 1500, 1500)
		    .setSmallIcon(R.drawable.kidsafe)
		    .setAutoCancel(true)
		    .build();
		
			notificationManager.notify(NOTIFICATION_ID, noti);
			sendNotification(context);
			Log.d(getClass().getSimpleName(), "entering");
			
		}//else {
			//Log.d(getClass().getSimpleName(), "exiting");
			//TODO send notification that the device has left restricted area
		//}
		
		
	}
	 
	private void sendNotification(Context context){
		//contact my server to tell GCM to notify parents device
		Log.i("INFO", "sending notification from Intent Reciever");
		gcm = GoogleCloudMessaging.getInstance(context);
		new AsyncTask<JSONObject, Integer, String>() {
			 @Override
	            protected String doInBackground(JSONObject... params) {
				 String msg = "";
	                try {
	                	Socket 			 	clientSocket = new Socket("10.0.2.2", 8080);   
	                	//Socket 			 	clientSocket = new Socket("88.107.65.111", 8080);   
	            		ObjectOutputStream  outToServer  = new ObjectOutputStream(clientSocket.getOutputStream());  
	            		//String latitude 				 = params[0].getString("latitude");
	            		//String longitude 				 = params[0].getString("longitude");
	            		outToServer.writeObject("Proximity Alert:");
	            		//sending the current location to the server so can use it with android wear.   		
	            		outToServer.close();
	            		clientSocket.close();
	                } catch (IOException ex) {
	                    msg = "Error :" + ex.getMessage();
	                }
	                return msg;
	            }
	        }.execute(null, null, location);   				
		}
}