package com.example.uk.co.kidsafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.location.Geofence;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener{

private static final int THERTY_SECONDS = 1000 * 30;
private LocationManager locationManager;
private Database db = null;
private Criteria criteria = null;
private String result= null;
private String line = null;
private JSONArray locations = null;
private InputStream is = null;

private static final String PROX_ALERT_INTENT = "com.example.uk.co.kidsafe.android.lbs.ProximityAlert";
private static final long PROX_ALERT_EXPIRATION = -1;

	@Override
	public void onCreate() { 
		
		Log.i("INFO", "Location Service onCreate() Called");
		
		db= new Database();
		getLocations();
	    criteria = new Criteria();
	    
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(false);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
		
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
	    
	    locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, false), THERTY_SECONDS, 10, this);
	    
	    
	}

	@Override
	public void onLocationChanged(Location location) {
		
		Log.i("INFO", "Location changed");
		
		  Date dNow = new Date( );
	      SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd hh:mm:ss a zzz");
        
        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());//TODO try do something with post code etc!!!!
        try {
            db.insert(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), ft.format(dNow));
            Log.i("INFO", "Time of fix: " + ft.format(dNow));

        }catch (Exception e) {
            e.printStackTrace();
        }	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	@Override
	public void onProviderEnabled(String provider) {}
	@Override
	public void onProviderDisabled(String provider) {}
	
    @Override
    public IBinder onBind(Intent intent) {
    	
		return null;
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
        Log.i("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(this); 
    }
    
private void addProximityAlert(double latitude, double longitude, int radius) {
		
        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        
        locationManager.addProximityAlert(
    		latitude, 						// the latitude of the central point of the alert region
    		longitude, 						// the longitude of the central point of the alert region
    		radius, 						// the radius of the central point of the alert region, in meters
    		PROX_ALERT_EXPIRATION, 			
    		proximityIntent 				
       );
        
       IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);  
       registerReceiver(new ProximityIntentReceiver(), filter);
	}

public void getLocations(){
	
	 new AsyncTask<String, Integer, String>() {
		 
		 @Override
           protected String doInBackground(String... params) {
               try {
               	
			        //HttpPost httppost = new HttpPost("http://10.0.2.2:8888/postData.php");
			        //this one is for my home router 
			        //HttpPost httppost = new HttpPost("http://89.243.62.195:8888/postData.php");
               			HttpClient httpclient = new DefaultHttpClient();
				        HttpPost httppost = new HttpPost("http://itsuite.it.brighton.ac.uk/ws52/getGeofence.php");
				        HttpResponse response = httpclient.execute(httppost);
				        HttpEntity entity = response.getEntity();
				        is = entity.getContent();
				        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				        
				        Log.i("INFO", "susseccfully requested Geofence locations from the Database");
				        
		            	StringBuilder sb = new StringBuilder();
		            	
		            	while ((line = reader.readLine()) != null) {
		            		//Log.i("INFO", "line read from get locations responce: " +line);
		            		sb.append(line + "\n");
		            	}
		            	
		            	result = sb.toString();
		            	is.close();
		            	Log.i("INFO", "successfully converted get locations responce to JSON" + result);
               } catch (IOException ex) {
                   result = "Error :" + ex.getMessage();

               }
               return result;
           }
		 
		 @Override 
		 protected void onPostExecute(String result) {
			 Log.i("INFO","result passed to postExecute: "+ result);
			 try {
				locations = new JSONArray(result);
				JSONObject 		  jsonObj   = new JSONObject();
				ArrayList<String> longitude = new ArrayList<String>();
				ArrayList<String> latitude  = new ArrayList<String>();
				ArrayList<String> radius    = new ArrayList<String>();
				
				for(int i = 0; i < locations.length(); i++) {
					jsonObj 	 = locations.getJSONObject(i);
					Log.i("INFO","json passed to postExecute: "+ jsonObj);
					longitude.add((String) jsonObj.get("geo_long"));
					latitude.add((String) jsonObj.getString("geo_lat_name"));
					radius.add((String) jsonObj.getString("geo_radius"));
					Log.i("INFO","Location Service got Geofence locations");
					Log.i("INFO", jsonObj.get("geo_long") + " " + jsonObj.getString("geo_lat_name") + " " + jsonObj.getString("geo_radius"));
					addProximityAlert(Double.valueOf(latitude.get(i)), Double.valueOf(longitude.get(i)), Integer.valueOf(radius.get(i)));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 };
		 
       }.execute(null, null, null);
       
       
	
	}
}