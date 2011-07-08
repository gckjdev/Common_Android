package com.orange.sns.qqweibo;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.sns.common.CommonSNSRequestHandler;

public class QQWeiboGetUserRequestHandler extends CommonSNSRequestHandler {

	public QQWeiboGetUserRequestHandler(QQWeiboSNSRequest qqWeiboSNSRequest) {
		super(qqWeiboSNSRequest);
	}

	@Override
	public boolean addParameters(Map<String, String> params) {
		return true;
	}

	@Override
	public String getBaseURL() {
		return QQWeiboSNSRequest.QQ_USER_INFO_URL;
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
