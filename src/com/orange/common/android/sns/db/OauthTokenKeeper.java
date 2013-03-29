package com.orange.common.android.sns.db;

import com.orange.common.android.sns.qqweibo.QQOauth2AccessToken;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.weibo.sdk.android.Oauth2AccessToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class OauthTokenKeeper {
	private static final String PREFERENCES_NAME = "weibo_oauth_token";
	public static final String SINA_OAUTH_TOKEN = "sina_token";
	public static final String SINA_OAUTH_EXPIRES_TIME = "sina_expires_time";
	public static final String QQ_OAUTH_TOKEN = "qq_token";
	public static final String QQ_OAUTH_EXPIRES_TIME = "qq_expires_time";
	public static final String QQ_OAUTH_APP_KEY = "qq_app_key";
	public static final String QQ_OAUTH_APP_SECRET = "qq_app_secret";
	public static final String QQ_OAUTH_OPEN_ID = "qq_oauth_open_id";
	public static final String TAG = "OauthTokenKeeper";
	
	public static void saveSinaToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(SINA_OAUTH_TOKEN, token.getToken());
		editor.putLong(SINA_OAUTH_EXPIRES_TIME, token.getExpiresTime());
		editor.commit();
	}
	
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	
	public static Oauth2AccessToken getSinaToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString(SINA_OAUTH_TOKEN, ""));
		token.setExpiresTime(pref.getLong(SINA_OAUTH_EXPIRES_TIME, 0));
		return token;
	}
	
	
	public static  void saveQQToken(Context context,QQOauth2AccessToken token)
	{
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(QQ_OAUTH_TOKEN, token.getToken());
		editor.putString(QQ_OAUTH_APP_KEY, token.getAppKey());
		editor.putString(QQ_OAUTH_APP_SECRET, token.getAppSecret());
		editor.putString(QQ_OAUTH_OPEN_ID, token.getOpenId());
		editor.putLong(QQ_OAUTH_EXPIRES_TIME, token.getExpiresTime());	
		editor.commit();
	}
	
	public static QQOauth2AccessToken getQQToken(Context context){
		QQOauth2AccessToken token = new QQOauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
		token.setToken(pref.getString(QQ_OAUTH_TOKEN, ""));
		token.setExpiresTime(pref.getLong(QQ_OAUTH_EXPIRES_TIME, 0));
		token.setAppKey(pref.getString(QQ_OAUTH_APP_KEY, ""));
		token.setAppSecret(pref.getString(QQ_OAUTH_APP_SECRET,""));
		token.setOpenId(pref.getString(QQ_OAUTH_OPEN_ID, ""));
		return token;
	}
}
