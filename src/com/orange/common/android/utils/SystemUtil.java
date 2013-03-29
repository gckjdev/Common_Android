package com.orange.common.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SystemUtil {
	private static String deviceId = null;

	public static String getDeviceId(Context context) {
		if (deviceId != null) {
			return deviceId;
		}

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	public static boolean isPhone(Context context){
		TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE){
            return false;
        }else{
            return true;
        }
	}
}
