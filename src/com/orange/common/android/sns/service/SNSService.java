package com.orange.common.android.sns.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orange.common.android.sns.common.CommonSNSRequest;
import com.orange.common.android.sns.common.CommonSNSRequestHandler;
import com.orange.common.android.sns.common.SNSConstants;


public class SNSService {
	
	public static final String LOG_TAG = "SNSService";
	
	public boolean startAuthorization(CommonSNSRequest snsRequest) {
		
		try {
			CommonSNSRequestHandler handler = snsRequest.authorizeRequestHandler();
			Map<String, String> params = new HashMap<String, String>();
			if (snsRequest.getCallbackURL() != null){
				params.put(SNSConstants.OAUTH_CALLBACK_URL, snsRequest.getCallbackURL());
			}
			JSONObject result = handler.execute(params);			
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
		CommonSNSRequestHandler handler = snsRequest.getSendWeiboRequestHandler();
		Map<String, String> params = new HashMap<String, String>();
		params.put(SNSConstants.SNS_WEIBO_TEXT, text);
		JSONObject result = handler.execute(params);
		if (result != null){
			Log.d(LOG_TAG, "<sendWeibo> success");
		}
		return (result != null);
		
	}	
	
	public JSONObject getUserInfo(CommonSNSRequest snsRequest) {
		
		CommonSNSRequestHandler handler = snsRequest.getUserInfoRequestHandler();
		JSONObject result = handler.execute(null);		
		return result;		
	}

	public String getAuthorizeURL(CommonSNSRequest snsRequest) {		
		return snsRequest.getAuthorizeBaseURL().concat("?oauth_token=").concat(snsRequest.getOauthToken());
	}

}
