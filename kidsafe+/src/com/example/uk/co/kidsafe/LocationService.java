package com.example.uk.co.kidsafe;

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
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener{

private static final int THERTY_SECONDS = 1000 * 30;
private LocationManager locationManager;
private Database db = null;
private Criteria criteria = null;
int counter = 0;

	
	public void onCreate() { 
		
		db= new Database();
		
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
		
		Log.i("**************************************", "Location changed");
	           
        db.insert(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
        //TODO add time stamp and other location data like post code etc
        
        final Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());         
        String text = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); 
            
            text = "My current location is: "+ addresses.get(0).getAddressLine(0); 
            Log.i("**********************", text);

        }catch (Exception e) {
            e.printStackTrace();
            text = "My current location is: " +"Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude();  
        }
        
        Toast.makeText( getApplicationContext(), "Location sent to server", Toast.LENGTH_SHORT).show();
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public class LocalBinder extends Binder 
    {
    	LocationService getService() 
        {
            return LocationService.this;
        }
    }
	
    private final IBinder mBinder = new LocalBinder();
    
    @Override
    public IBinder onBind(Intent intent) {
    	
		return mBinder;
	}
}