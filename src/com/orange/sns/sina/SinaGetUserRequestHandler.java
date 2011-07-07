package com.orange.sns.sina;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.sns.common.CommonSNSRequest;
import com.orange.sns.common.CommonSNSRequestHandler;

public class SinaGetUserRequestHandler extends CommonSNSRequestHandler {

	public SinaGetUserRequestHandler(CommonSNSRequest sinaSNSRequest) {
		super(sinaSNSRequest);
	}

	@Override
	public boolean addParameters() {
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
		return this.parseJSONResponse(response);
	}

}
