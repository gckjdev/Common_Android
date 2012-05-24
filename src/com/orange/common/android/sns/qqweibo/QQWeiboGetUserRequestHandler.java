package com.orange.common.android.sns.qqweibo;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.common.android.sns.common.CommonSNSRequestHandler;
import com.orange.common.android.sns.common.SNSConstants;

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
		JSONObject dataJSON = parseJSONResponse(response);
		if (dataJSON == null)
			return null;
		
		JSONObject resultJSON = dataJSON.getJSONObject("data");
		if (resultJSON == null)
			return null;
		
		JSONObject userJSON = new JSONObject();		
		userJSON.put(SNSConstants.SNS_NETWORK, SNSConstants.SNS_QQ_WEIBO);
		
	    safeSetKeyValue(resultJSON, userJSON, "name", SNSConstants.SNS_USER_ID);    
	    safeSetKeyValue(resultJSON, userJSON, "nick", SNSConstants.SNS_NICK_NAME);
	    safeSetKeyValue(resultJSON, userJSON, "name", SNSConstants.SNS_DOMAIN);
	    safeSetKeyValue(resultJSON, userJSON, "province_code", SNSConstants.SNS_PROVINCE);
	    safeSetKeyValue(resultJSON, userJSON, "city_code", SNSConstants.SNS_CITY);
	    safeSetKeyValue(resultJSON, userJSON, "location", SNSConstants.SNS_LOCATION);

	    if (resultJSON.has("sex")){
	    	int gender = resultJSON.getInt("sex");
	    	String value = (gender == 1) ? "m" : "f";
	    	userJSON.put(SNSConstants.SNS_GENDER, value);
	    }
	    
	    if (resultJSON.has("birth_day") && resultJSON.has("birth_month") && resultJSON.has("birth_year")){
	    	int day = resultJSON.getInt("birth_day");
	    	int month = resultJSON.getInt("birth_month");
	    	int year = resultJSON.getInt("birth_year");
	    	String value = String.format("%d-%02d-%02d", year, month, day);
	    	userJSON.put(SNSConstants.SNS_GENDER, value);
	    }
	    
	    if (resultJSON.has("head") && resultJSON.getString("head").length() > 0){
	    	String avatar = resultJSON.getString("head").concat("/100");
	    	userJSON.put(SNSConstants.SNS_USER_IMAGE_URL, avatar);
	    }
	    
		return userJSON;		
	}

}
