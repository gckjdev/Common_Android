package com.orange.common.android.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationUtil {
	public static Location getCurrentLocation(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Log.d(UtilConstants.LOG_TAG, "Location providers: " + locationManager.getAllProviders());

		// firstly try GPS, then try network provider
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		// sometimes can not get location from emulator
		if (location == null) {
			location = new Location("dummy");
			location.setLatitude(0);
			location.setLongitude(0);
		}
		Log.d(UtilConstants.LOG_TAG, "Returning location: " + location);
		
		return location;
	}
}
