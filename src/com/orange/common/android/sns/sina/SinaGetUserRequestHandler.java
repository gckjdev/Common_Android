package com.orange.common.android.sns.sina;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.common.android.sns.common.CommonSNSRequest;
import com.orange.common.android.sns.common.CommonSNSRequestHandler;
import com.orange.common.android.sns.common.SNSConstants;

public class SinaGetUserRequestHandler extends CommonSNSRequestHandler {

	public SinaGetUserRequestHandler(CommonSNSRequest sinaSNSRequest) {
		super(sinaSNSRequest);
	}

	@Override
	public boolean addParameters(Map<String, String> params) {
		return true;
	}

	@Override
	public String getBaseURL() {
		return SinaSNSRequest.SINA_USER_INFO_URL;
	}

	@Override
	public String getHttpMethod() {
		return CommonSNSRequestHandler.METHOD_POST;
	}

	@Override
	public JSONObject parseResponse(String response) throws JSONException {
		JSONObject resultJSON = parseJSONResponse(response);
		if (resultJSON == null)
			return null;
		
		JSONObject userJSON = new JSONObject();		
		userJSON.put(SNSConstants.SNS_NETWORK, SNSConstants.SNS_SINA_WEIBO);
		
	    safeSetKeyValue(resultJSON, userJSON, "id", SNSConstants.SNS_USER_ID);    
	    safeSetKeyValue(resultJSON, userJSON, "screen_name", SNSConstants.SNS_NICK_NAME);
	    safeSetKeyValue(resultJSON, userJSON, "domain", SNSConstants.SNS_DOMAIN);
	    safeSetKeyValue(resultJSON, userJSON, "gender", SNSConstants.SNS_GENDER);
	    safeSetKeyValue(resultJSON, userJSON, "province", SNSConstants.SNS_PROVINCE);
	    safeSetKeyValue(resultJSON, userJSON, "city", SNSConstants.SNS_CITY);
	    safeSetKeyValue(resultJSON, userJSON, "location", SNSConstants.SNS_LOCATION);
	    safeSetKeyValue(resultJSON, userJSON, "profile_image_url", SNSConstants.SNS_USER_IMAGE_URL);
				
		return userJSON;
	}

}
