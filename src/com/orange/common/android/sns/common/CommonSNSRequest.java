package com.orange.common.android.sns.common;

public abstract class CommonSNSRequest {

    String   callbackURL;
    String   appKey;
    String   appSecret;
    String   oauthToken;
    String   oauthTokenSecret;
    
    public abstract String getRequestTokenBaseURL();
    public abstract String getAuthorizeBaseURL();
    public abstract String getAccessTokenBaseURL();
    public abstract String getUserInfoBaseURL();
    
	public String getCallbackURL() {
		return callbackURL;
	}
	
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	
	public String getAppKey() {
		return appKey;
	}
	
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	public String getAppSecret() {
		return appSecret;
	}
	
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	public String getOauthToken() {
		return oauthToken;
	}
	
	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}
	
	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}
	
	public void setOauthTokenSecret(String oauthTokenSecret) {
		this.oauthTokenSecret = oauthTokenSecret;
	}
    
    abstract public CommonSNSRequestHandler getUserInfoRequestHandler();
    abstract public CommonSNSRequestHandler getSendWeiboRequestHandler();
	
    public CommonSNSRequestHandler getAccessTokenRequestHandler(){
		return new OAuthGetAccessTokenRequestHandler(this);
	}
    
	public CommonSNSRequestHandler authorizeRequestHandler() {
		return new OAuthGetRequestTokenRequestHandler(this);
	}	
}
