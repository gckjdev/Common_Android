package com.orange.common.android.utils;

import org.json.JSONArray;
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
	
	public static String getString(JSONObject json, String key, String defaultValue) {
		try {
			if (json == null || json.isNull(key)) {
				return defaultValue;
			}

			return json.getString(key);
		} catch (JSONException e) {
			return defaultValue;
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

	public static long getLong(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return 0;
			}

			return json.getLong(key);
		} catch (JSONException e) {
			return 0;
		}
	}	
	
	public static JSONObject get(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return null;
			}

			return json.getJSONObject(key);
		} catch (JSONException e) {
			return null;
		}
	}

	public static JSONArray getJSONArray(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return null;
			}

			return json.getJSONArray(key);
		} catch (JSONException e) {
			return null;
		}
	}
}
