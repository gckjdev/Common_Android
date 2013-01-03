package com.orange.common.android.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static String getString(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return null;
			}

			return json.getString(key);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static int getInt(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return 0;
			}

			return json.getInt(key);
		} catch (JSONException e) {
			return 0;
		}
	}
	
	public static Object get(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return null;
			}

			return json.get(key);
		} catch (JSONException e) {
			return null;
		}
	}
}