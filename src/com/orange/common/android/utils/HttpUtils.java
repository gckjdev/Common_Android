package com.orange.common.android.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class HttpUtils {

	public static JSONObject httpGet(Uri uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri.toString());
		ResponseHandler<String> strRespHandler = new BasicResponseHandler();

		String response = null;
		JSONObject jsonResponse = null;

		try {
			response = httpclient.execute(httpGet, strRespHandler);
			Log.d(UtilConstants.LOG_TAG, "Get http response: " + response);
			
			jsonResponse = new JSONObject(response);
			Log.d(UtilConstants.LOG_TAG, "Get Json response: " + jsonResponse);
		} catch (Exception e) {
			logException(e);
		}

		return jsonResponse;
	}

	private static void logException(Exception e) {
		Log.e(UtilConstants.LOG_TAG, "Get exception when handling http request/response!", e);
	}
}
