package com.orange.common.android.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static String getStringOrNull(JSONObject json, String key) {
		try {
			if (json == null || json.isNull(key)) {
				return null;
			}

			return json.getString(key);
		} catch (JSONException e) {
			return null;
		}
	}
}
