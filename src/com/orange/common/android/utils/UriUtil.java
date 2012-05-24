package com.orange.common.android.utils;

import android.net.Uri.Builder;
import android.util.Log;

public class UriUtil {
	public static Builder appendQueryParameter(Builder builder, String key, String value) {
		if (key == null) {
			Log.e(UtilConstants.LOG_TAG, "Can not add query parameter with null key!");
		}
		if (value == null) {
			return builder.appendQueryParameter(key, "");
		}
		return builder.appendQueryParameter(key, value);
	}
}
