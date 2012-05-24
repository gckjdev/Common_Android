package com.orange.common.android.sns.common;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class OAuthGetAccessTokenRequestHandler extends CommonSNSRequestHandler {

	public OAuthGetAccessTokenRequestHandler(CommonSNSRequest request) {
		super(request);
	}

	@Override
	public boolean addParameters(Map<String, String> params) {
		if (params == null)
			return false;
		
		String oauthVerifier = params.get(SNSConstants.OAUTH_VERIFIER);
		if (oauthVerifier == null)
			return false;
		
		return addParam(SNSConstants.OAUTH_VERIFIER, oauthVerifier);
	}

	@Override
	public String getBaseURL() {
		return request.getAccessTokenBaseURL();
	}

	@Override
	public String getHttpMethod() {
		return METHOD_POST;
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
