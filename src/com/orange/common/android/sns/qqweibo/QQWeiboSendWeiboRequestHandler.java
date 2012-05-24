package com.orange.common.android.sns.qqweibo;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.common.android.sns.common.CommonSNSRequestHandler;
import com.orange.common.android.sns.common.SNSConstants;

public class QQWeiboSendWeiboRequestHandler extends CommonSNSRequestHandler {

	public QQWeiboSendWeiboRequestHandler(QQWeiboSNSRequest qqWeiboSNSRequest) {
		super(qqWeiboSNSRequest);
	}

	@Override
	public boolean addParameters(Map<String, String> params) {
		String text = params.get(SNSConstants.SNS_WEIBO_TEXT);
		if (text == null)
			return false;
		
		return 	addParam("content", text) &&
				addParam("format", "json");		
	}

	@Override
	public String getBaseURL() {
		return QQWeiboSNSRequest.QQ_CREATE_WEIBO_URL;
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
