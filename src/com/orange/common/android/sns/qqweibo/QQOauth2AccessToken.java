/**  
        * @title QQOauth2AccessToken.java  
        * @package com.orange.common.android.sns.qqweibo  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-22 上午9:30:47  
        * @version V1.0  
 */
package com.orange.common.android.sns.qqweibo;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.weibo.oauthv2.OAuthV2;



import android.text.TextUtils;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-2-22 上午9:30:47  
 */

public class QQOauth2AccessToken
{
	private String mAccessToken = "";
	private String mRefreshToken = "";
	private String appKey = "";
	private String appSecret = "";
	private String openId = "";
	private long mExpiresTime = 0;

//	private String mOauth_verifier = "";
//	protected String[] responseStr = null;
//	protected SecretKeySpec mSecretKeySpec;
	
	/*public QQOauth2AccessToken() {
	}
	
	public QQOauth2AccessToken(OAuthV2 oAuthV2)
	{
		if (oAuthV2 != null)
		{
			setToken(oAuthV2.getAccessToken());
			setExpiresIn(oAuthV2.getExpiresIn());
			setRefreshToken(oAuthV2.getRefreshToken());
			setAppKey(oAuthV2.getClientId());
			setAppSecret(oAuthV2.getClientSecret());
			setOpenId(oAuthV2.getOpenid());
		}
	}
	
	public QQOauth2AccessToken(String accessToken, String expires_in,String appKey,String appSecret,String openId) {
		mAccessToken = accessToken;
		mExpiresTime = System.currentTimeMillis() + Long.parseLong(expires_in)*1000;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.openId = openId;
	}
	
	*//**
	 *  AccessToken是否有效,如果accessToken为空或者expiresTime过期，返回false，否则返回true
	 *  @return 如果accessToken为空或者expiresTime过期，返回false，否则返回true
	 *//*
	public boolean isSessionValid() {
		return (!TextUtils.isEmpty(mAccessToken) && (mExpiresTime == 0 || (System
				.currentTimeMillis() < mExpiresTime)));
	}
	*//**
	 * 获取accessToken
	 *//*
	public String getToken() {
		return this.mAccessToken;
	}
	*//**
     * 获取refreshToken
     *//*
	public String getRefreshToken() {
		return mRefreshToken;
	}
	*//**
	 * 设置refreshToken
	 * @param mRefreshToken
	 *//*
	public void setRefreshToken(String mRefreshToken) {
		this.mRefreshToken = mRefreshToken;
	}
	*//**
	 * 获取超时时间，单位: 毫秒，表示从格林威治时间1970年01月01日00时00分00秒起至现在的总 毫秒数
	 *//*
	public long getExpiresTime() {
		return mExpiresTime;
	}

	*//**
	 * 设置过期时间长度值，仅当从服务器获取到数据时使用此方法
	 *            
	 *//*
	public void setExpiresIn(String expiresIn) {
		if (expiresIn != null && !expiresIn.equals("0")) {
			setExpiresTime(System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000);
		}
	}

	*//**
	 * 设置过期时刻点 时间值
	 * @param mExpiresTime 单位：毫秒，表示从格林威治时间1970年01月01日00时00分00秒起至现在的总 毫秒数
	 *            
	 *//*
	public void setExpiresTime(long mExpiresTime) {
		this.mExpiresTime = mExpiresTime;
	}
	*//**
	 * 设置accessToken
	 * @param mToken
	 *//*
	public void setToken(String mToken) {
		this.mAccessToken = mToken;
	}

	public void setAppKey(String appKey)
	{
		this.appKey = appKey;
	}

	public void setAppSecret(String appSecret)
	{
		this.appSecret = appSecret;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public String getAppKey()
	{
		return appKey;
	}

	public String getAppSecret()
	{
		return appSecret;
	}

	public String getOpenId()
	{
		return openId;
	}*/
}
