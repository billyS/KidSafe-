package com.example.uk.co.kidsafe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service implements LocationListener{

private static final int THERTY_SECONDS = 1000 * 30;
private LocationManager locationManager;
private Database db = null;
private Criteria criteria = null;
int counter = 0;

	@Override
	public void onCreate() { 
		Log.i("INFO", "Location Service onCreate() Called");
		db= new Database();
		
	    criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(false);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
		
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
	    
	    locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, false), 1000, 1, this);   
	}

	@Override
	public void onLocationChanged(Location location) {
		
		Log.i("INFO", "Location changed");
		
		Locale locale = new Locale("UK");
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", locale);
        
        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());         
        String text = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); 
            
            text = "My current location is: "+ addresses.get(0).getAddressLine(0);
            String test = addresses.get(0).getPostalCode();
            
            db.insert(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()), sdf.format(cal.getTime()), test );

            Log.i("INFO", "Addess: " + text + "\n Post Code: "+ test + "\n Time of fix: " + sdf.format(cal.getTime()));

        }catch (Exception e) {
            e.printStackTrace();
            text = "My current location is: " +"Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude();  
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
}