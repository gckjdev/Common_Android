package com.orange.common.android.sns.sina;

import com.orange.common.android.sns.common.CommonSNSRequest;
import com.orange.common.android.sns.common.CommonSNSRequestHandler;

public class SinaSNSRequest extends CommonSNSRequest {

	public static final String SINA_REQUEST_TOKEN_URL = "http://api.t.sina.com.cn/oauth/request_token";
	public static final String SINA_AUTHORIZE_URL = "http://api.t.sina.com.cn/oauth/authorize";
	public static final String SINA_ACCESS_TOKEN_URL = "http://api.t.sina.com.cn/oauth/access_token";
	public static final String SINA_USER_INFO_URL = "http://api.t.sina.com.cn/account/verify_credentials.json";
	public static final String SINA_CREATE_WEIBO_URL = "http://api.t.sina.com.cn/statuses/update.json";
	
	
    public String getRequestTokenBaseURL(){
    	return SINA_REQUEST_TOKEN_URL;
    }

    public String getAuthorizeBaseURL(){
    	return SINA_AUTHORIZE_URL;
    }
    
    public String getAccessTokenBaseURL(){
    	return SINA_ACCESS_TOKEN_URL;
    }
    
    public String getUserInfoBaseURL(){
    	return SINA_USER_INFO_URL;
    }
    
    public String getCreateWeiboBaseURL(){
    	return SINA_CREATE_WEIBO_URL;
    }
    
	@Override
    public CommonSNSRequestHandler getUserInfoRequestHandler(){
    	return new SinaGetUserRequestHandler(this);
    }

	@Override
	public CommonSNSRequestHandler getSendWeiboRequestHandler(){
		return new SinaSendWeiboRequestHandler(this);
	}
    
}
