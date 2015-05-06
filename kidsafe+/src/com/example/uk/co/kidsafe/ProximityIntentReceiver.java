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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {

	GoogleCloudMessaging gcm;
	private Context context;
	private static final int NOTIFICATION_ID = 1000;

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//proximity alert detected
		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		this.context = context;
		 Log.i("INFO", "Proximity Alert Broadcast Reciever Registered");
		if (entering) {
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
			Notification noti = new Notification.Builder(context)
	        .setContentTitle("Proximity Alert!: ")
	        .setContentText("You are entering a restricted area")
	        .setWhen(System.currentTimeMillis())
	        .setDefaults(Notification.DEFAULT_VIBRATE)
	        .setDefaults(Notification.DEFAULT_LIGHTS)
	        .setLights(Color.WHITE, 1500, 1500)
	        .setSmallIcon(R.drawable.ic_plusone_standard_off_client)
	        .build();
		
			notificationManager.notify(NOTIFICATION_ID, noti);
			sendNotification(context);
			Log.d(getClass().getSimpleName(), "entering");
		}
		else {
			Log.d(getClass().getSimpleName(), "exiting");
			//TODO send notification that the device has left restricted area
		}
	}
	
	private void sendNotification(Context context){
		//contact my server to tell GCM to notify parents device
		Log.i("INFO", "sending notification from Intent Reciever");
		gcm = GoogleCloudMessaging.getInstance(context);
		
		new AsyncTask<Void, Integer, String>() {
			 @Override
	            protected String doInBackground(Void... params) {
				 String msg = "";
	                try {
	                	//Socket 			 	clientSocket = new Socket("10.0.2.2", 8080);   
	                	Socket 			 	clientSocket = new Socket("88.107.65.111", 8080);   
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
	        }.execute(null, null, null);   				
		}
	
	private Notification createNotification() {
		
		        Notification notification = new Notification();
		        notification.icon = R.drawable.ic_plusone_standard_off_client;
		        notification.when = System.currentTimeMillis();
		        notification.flags |= Notification.FLAG_AUTO_CANCEL;
		        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		        notification.defaults |= Notification.DEFAULT_VIBRATE;
		        notification.defaults |= Notification.DEFAULT_LIGHTS;
		        notification.ledARGB = Color.WHITE;
		        notification.ledOnMS = 1500;
		        notification.ledOffMS = 1500;
		        return notification;
	}
}
