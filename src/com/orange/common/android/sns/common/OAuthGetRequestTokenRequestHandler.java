package com.orange.common.android.sns.common;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class OAuthGetRequestTokenRequestHandler extends CommonSNSRequestHandler {

	public OAuthGetRequestTokenRequestHandler(CommonSNSRequest request) {
		super(request);
	}

	@Override
	public boolean addParameters(Map<String, String> params) {
		
		String oauthCallBack = params.get(SNSConstants.OAUTH_CALLBACK_URL);
		if (oauthCallBack != null){
			addParam(SNSConstants.OAUTH_CALLBACK_URL, oauthCallBack);
		}
		
		return true;
	}

	@Override
	public String getBaseURL() {
		return request.getRequestTokenBaseURL();
	}

	@Override
	public String getHttpMethod() {
		return METHOD_GET;
	}

	@Override
	public JSONObject parseResponse(String response) throws JSONException {
		Map<String, String> values = parseSimpleResponse(response);
		if (values == null)
			return null;
		
		String oauthToken = values.get(SNSConstants.OAUTH_TOKEN);
		String oauthTokenSecret = values.get(SNSConstants.OAUTH_TOKEN_SECRET);

		if (oauthToken == null || oauthTokenSecret == null)
			return null;
		
		JSONObject json = new JSONObject();
		json.put(SNSConstants.OAUTH_TOKEN, oauthToken);
		json.put(SNSConstants.OAUTH_TOKEN_SECRET, oauthTokenSecret);		
		return json;
	}

}
