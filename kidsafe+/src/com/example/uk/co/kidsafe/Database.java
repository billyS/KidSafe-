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

import android.database.CursorJoiner.Result;
import android.util.Log;

 
public class Database extends AsyncTask<String, Void, Result>{
	
	InputStream is=null;
	String result=null;
	String line=null;
	int code;
 
    public Database(){ 
    	
    }
    
    
    protected void insert(String longitude, String latitude)
    {
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 
    	nameValuePairs.add(new BasicNameValuePair("longitude",longitude));
    	nameValuePairs.add(new BasicNameValuePair("latitude",latitude));
    	
    	try {
				HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://10.0.2.2:8888/postData.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost); 
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		        Log.i("INFO", "pass 1 connection success ");
    	}catch(Exception e) {
        	Log.e("Fail 1", e.toString());
    	}     
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
            	Log.e("......", line);
                sb.append(line + "\n");
            }
            
            is.close();
            result = sb.toString();
            Log.e("INFO", "pass 2 connection success ");
        }catch(Exception e) {
            Log.e("Fail 2", e.toString());
        }     
       
        try {
            JSONObject json_data = new JSONObject(result);
            code=(json_data.getInt("code"));
        } catch(Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }
    
    protected void select(String id)
    {
    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 
    	nameValuePairs.add(new BasicNameValuePair("id",id));
    	
    	try {
		    HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://10.0.2.2:8888/getData.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost); 
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	        Log.e("INFO", "pass 1 connection success ");
	    }catch(Exception e) {
        	Log.e("Fail 1", e.toString());
	    }     
        
        try {
         		BufferedReader reader = new BufferedReader (new InputStreamReader(is,"iso-8859-1"),8);
            	StringBuilder sb = new StringBuilder();
            	while ((line = reader.readLine()) != null) {
            		sb.append(line + "\n");
            	}
            	
            	is.close();
            	result = sb.toString();
	        Log.e("INFO", "pass 2 connection success ");
        }catch(Exception e) {
        	Log.e("Fail 2", e.toString());
        }     
       
        try {
        	JSONObject json_data = new JSONObject(result);
        	String name=(json_data.getString("name"));
    	}catch(Exception e) {
        	Log.e("Fail 3", e.toString());
        	
    	}
    }


	@Override
	protected Result doInBackground(String... url) {
		Log.i("****************", url.toString());
		Log.i("****************", url[0]);
		Log.i("****************", url[1]);
		Log.i("****************", url[2]);
		
		if(url[0].equals("insert")) { 
			insert(url[1],url[2]);
		}else {
			select(url[1]);
		}
		return null;
	}
}