package com.orange.sns.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.orange.utils.codec.Base64;
import com.orange.utils.codec.HMAC_SHA1;


import com.orange.sns.common.CommonSNSRequest;
import com.orange.sns.common.CommonSNSRequestHandler;
import com.orange.sns.common.SNSConstants;
import com.orange.sns.sina.SinaSNSRequest;

public class SNSService {
	
	public static final String LOG_TAG = "SNSService";
	
	public boolean startAuthorization(CommonSNSRequest snsRequest) {
		
		try {
			CommonSNSRequestHandler handler = snsRequest.authorizeRequestHandler();
			JSONObject result = handler.execute(null);			
			if (result != null){
				snsRequest.setOauthToken(result.getString(SNSConstants.OAUTH_TOKEN));
				snsRequest.setOauthTokenSecret(result.getString(SNSConstants.OAUTH_TOKEN_SECRET));
				Log.d(LOG_TAG, "<authorization> success, save oauth_token="+snsRequest.getOauthToken()+
						", oauth_token_secret="+snsRequest.getOauthTokenSecret());
			}
			return (result != null);

		} catch (JSONException e) {
			Log.e(LOG_TAG, "<authorization> catch exception="+e.toString());
			return false;
		}
	}
	
	// not use yet
	public boolean parseAuthorizationResponse(String responseURL, CommonSNSRequest snsRequest){
		return true;
	}

	public boolean getAccessToken(CommonSNSRequest snsRequest, String oauthVerifier){
		Map<String, String> params = new HashMap<String, String>();
		params.put(SNSConstants.OAUTH_VERIFIER, oauthVerifier);
		return getAccessToken(snsRequest, params);
	}
	
	public boolean getAccessToken(CommonSNSRequest snsRequest, Map<String, String> params) {
		try {
			CommonSNSRequestHandler handler = snsRequest.getAccessTokenRequestHandler();
			JSONObject result = handler.execute(params);
			if (result != null){
				snsRequest.setOauthToken(result.getString(SNSConstants.OAUTH_TOKEN));
				snsRequest.setOauthTokenSecret(result.getString(SNSConstants.OAUTH_TOKEN_SECRET));
				Log.d(LOG_TAG, "<getAccessToken> success, save oauth_token="+snsRequest.getOauthToken()+
						", oauth_token_secret="+snsRequest.getOauthTokenSecret());
			}
			return (result != null);

		} catch (JSONException e) {
			Log.e(LOG_TAG, "<getAccessToken> catch exception="+e.toString());
			return false;
		}

	}

	public boolean sendWeibo(CommonSNSRequest snsRequest, String text) {
		
		return false;
		
	}	
	
	public JSONObject getUserInfo(CommonSNSRequest snsRequest) {
		
		CommonSNSRequestHandler handler = snsRequest.getUserInfoRequestHandler();
		JSONObject result = handler.execute(null);		
		return result;		
	}

}
