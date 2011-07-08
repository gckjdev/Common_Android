package com.orange.sns.sina;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.sns.common.CommonSNSRequest;
import com.orange.sns.common.CommonSNSRequestHandler;
import com.orange.sns.common.SNSConstants;

public class SinaSendWeiboRequestHandler extends CommonSNSRequestHandler {

	public SinaSendWeiboRequestHandler(CommonSNSRequest sinaSNSRequest) {
		super(sinaSNSRequest);
	}

	@Override
	public boolean addParameters(Map<String, String> params) {
		String text = params.get(SNSConstants.PP_WEIBO_TEXT);
		if (text == null){
			return false;
		}
		
		return addParam("status", text);
	}

	@Override
	public String getBaseURL() {
		return SinaSNSRequest.SINA_CREATE_WEIBO_URL;
	}

	@Override
	public String getHttpMethod() {
		return METHOD_POST;
	}

	@Override
	public JSONObject parseResponse(String response) throws JSONException {
		return parseJSONResponse(response);
	}

}