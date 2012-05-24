package com.orange.common.android.sns.qqweibo;

import com.orange.common.android.sns.common.CommonSNSRequest;
import com.orange.common.android.sns.common.CommonSNSRequestHandler;

public class QQWeiboSNSRequest extends CommonSNSRequest {

	public static final String QQ_REQUEST_TOKEN_URL = "https://open.t.qq.com/cgi-bin/request_token";
	public static final String QQ_AUTHORIZE_URL     = "https://open.t.qq.com/cgi-bin/authorize";
	public static final String QQ_ACCESS_TOKEN_URL  = "https://open.t.qq.com/cgi-bin/access_token";
	public static final String QQ_USER_INFO_URL     = "http://open.t.qq.com/api/user/info";
	public static final String QQ_CREATE_WEIBO_URL  = "http://open.t.qq.com/api/t/add";
	
	@Override
	public String getAccessTokenBaseURL() {
		return QQ_ACCESS_TOKEN_URL;
	}

	@Override
	public String getAuthorizeBaseURL() {
		return QQ_AUTHORIZE_URL;
	}

	@Override
	public String getRequestTokenBaseURL() {
		return QQ_REQUEST_TOKEN_URL;
	}

	@Override
	public CommonSNSRequestHandler getSendWeiboRequestHandler() {
		return new QQWeiboSendWeiboRequestHandler(this);
	}

	@Override
	public String getUserInfoBaseURL() {
		return QQ_USER_INFO_URL;
	}

	@Override
	public CommonSNSRequestHandler getUserInfoRequestHandler() {
		return new QQWeiboGetUserRequestHandler(this);
	}

}
