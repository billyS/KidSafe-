package com.example.uk.co.kidsafe;

import android.provider.BaseColumns;


/**
 * This code in this class is from a tutorial on ContentProviders by Jason Wei at http://thinkandroid.wordpress.com/2010/01/13/writing-your-own-contentprovider/
 * @author Jason Wei
 * 
 */
public class UserLocation {

	public UserLocation() {
	}

	public static final class UserLocations implements BaseColumns {
		private UserLocations() {
		}

		//public static final Uri CONTENT_URI = Uri.parse("content://" + UserLocationContentProvider.AUTHORITY + "/user_locations");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/userlocations";
		public static final String USER_LOCATION_ID = "_id";
		public static final String LONGITUDE = "longitude";
		public static final String LATITUDE = "latitude";
	}

}

