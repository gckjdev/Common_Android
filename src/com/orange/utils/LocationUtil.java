package com.orange.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtil {
	public static Location getCurrentLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Log.d(UtilConstants.LOG_TAG, "Location providers: " + locationManager.getAllProviders());

		// sometimes can not get location from emulator, check why!
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		
		Log.d(UtilConstants.LOG_TAG, "Get location: " + location);
		
		return location;
	}
}
