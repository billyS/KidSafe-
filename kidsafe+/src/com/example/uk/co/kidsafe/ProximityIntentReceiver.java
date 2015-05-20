package com.example.uk.co.kidsafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
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
		    .setDefaults(Notification.DEFAULT_VIBRATE)
		    .setDefaults(Notification.DEFAULT_LIGHTS)
		    .setLights(Color.WHITE, 1500, 1500)
		    .setSmallIcon(R.drawable.kidsafe)
		    .setAutoCancel(true)
		    .build();
		
			notificationManager.notify(NOTIFICATION_ID, noti);
			sendNotification(context, getCurrentLocation());
			Log.d(getClass().getSimpleName(), "entering");
		}else {
			Log.d(getClass().getSimpleName(), "exiting");
			//TODO send notification that the device has left restricted area
		}
		
		
	}
	
	private JSONObject getCurrentLocation() {
		/* new AsyncTask<JSONArray, Integer, JSONArray>() {
			 @Override
	           protected JSONArray doInBackground(JSONArray... params) {
				 
				    JSONArray locations = null;
					String msg = "";
			        HttpClient httpclient =null;
			    	HttpPost httppost = null;
			    	HttpResponse response = null;
			    	HttpEntity entity = null;
			    	BufferedReader reader =null;
			    	StringBuilder sb =null;
			        InputStream is = null;
			        String line = null;
			        
					httpclient = new DefaultHttpClient();
			        httppost = new HttpPost("http://itsuite.it.brighton.ac.uk/ws52/getCurrentLocation.php");
			        
			        try {
						response = httpclient.execute(httppost);
						entity = response.getEntity();
				        is = entity.getContent();
				        
				        Log.i("INFO", "susseccfully requested current location from the Database ProximityReciever");
				        reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				    	sb = new StringBuilder();
				    	
				    	while ((line = reader.readLine()) != null) {
				    		//Log.i("INFO", "line read from get locations responce: " +line);
				    		sb.append(line + "\n");
				    	}
				    	
				    	msg = sb.toString();
				    	locations = new JSONArray(msg);
				    	is.close();
						
					} catch (ClientProtocolException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	                return locations;
	           }
			 
			 @Override 
			 protected void onPostExecute(JSONArray result) {
				 
				 try {
					locationResult = result.getJSONObject(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 };
			 
	       }.execute(null, null, null);*/

		return locationResult;
	}
	 
	private void sendNotification(Context context, JSONObject location){
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
	            		outToServer.writeObject("Proximity Alert:latitude:");
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