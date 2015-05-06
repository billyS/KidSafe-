package com.example.uk.co.kidsafe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.util.Log;

 
public class Database{
	
	InputStream is=null;
	String result=null;
	String line=null;
	int code;
    public Database(){ 
    	
    }
    
    
    public void insert(String longitude, String latitude, String timeStamp)
    {	
    	final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 
    	nameValuePairs.add(new BasicNameValuePair("longitude",longitude));
    	nameValuePairs.add(new BasicNameValuePair("latitude",latitude));
    	nameValuePairs.add(new BasicNameValuePair("timeStamp",timeStamp));
    	
    	Runnable runnable = new Runnable() {
			public void run() {
		    	try {
						HttpClient httpclient = new DefaultHttpClient();
				        HttpPost httppost = new HttpPost("http://itsuite.it.brighton.ac.uk/ws52/postData.php");
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
				        HttpEntity entity = response.getEntity();
				        is = entity.getContent();
				        
				        Log.i("INFO", "pass 1 connection success ");
				        
				        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		            	StringBuilder sb = new StringBuilder();
		            	while ((line = reader.readLine()) != null) {
		            		Log.e("......", line);
		            		sb.append(line + "\n");
		            	}
		            	
		            	JSONObject json_data = new JSONObject(result);
		                is.close();
		            	result = sb.toString();
		            	Log.e("INFO", "pass 2 connection success ");
		            
		    	}catch(Exception e) {
		        	Log.e("Fail 1", e.toString());
		    	}     
			}
		};
		new Thread(runnable).start();	
    }
}