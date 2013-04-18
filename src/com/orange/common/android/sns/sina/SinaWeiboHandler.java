/**  
        * @title SinaWeiboHandler.java  
        * @package com.orange.common.android.sns.sina  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-21 上午10:07:17  
        * @version V1.0  
 */
package com.orange.common.android.sns.sina;

import java.text.SimpleDateFormat;

import com.orange.common.android.sns.db.OauthTokenKeeper;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class SinaWeiboHandler
{
	public static final String TAG = "SinaWeiboHandler";
	private Activity activity;
	private Oauth2AccessToken accessToken;
	private SsoHandler ssoHandler;
	
	
	public SinaWeiboHandler(Activity activity)
	{
		super();
		this.activity = activity;
	}

	public SinaWeiboHandler(Activity activity, Intent intent)
	{
		super();
		this.activity = activity;
	}
	
	
	public Oauth2AccessToken getAccessToken(String appKey,String redirectUrl)
	{
		accessToken=OauthTokenKeeper.getSinaToken(activity);
		if (!accessToken.isSessionValid()){
			Weibo sinaWeibo = Weibo.getInstance(appKey, redirectUrl);
			try {
	            Class sso=Class.forName("com.weibo.sdk.android.sso.SsoHandler");
	            ssoHandler = new SsoHandler(activity, sinaWeibo);
	            ssoHandler.authorize( new AuthDialogListener());
	        } catch (ClassNotFoundException e) {
	            Log.i(TAG, "<com.weibo.sdk.android.sso.SsoHandler> not found and use oauth2.0");
	            sinaWeibo.authorize(activity, new AuthDialogListener());
	        }
		}
		return accessToken;
	}


	public void  authorizeCallBack(int requestCode, int resultCode, Intent data)
	{
		if (ssoHandler != null)
		{
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		
	}
	
	

	class AuthDialogListener implements WeiboAuthListener {
			@Override
	        public void onComplete(Bundle values) {
	            String token = values.getString("access_token");
	            String expires_in = values.getString("expires_in");
	            accessToken = new Oauth2AccessToken(token, expires_in);
	            if (accessToken.isSessionValid()) {
	                OauthTokenKeeper.saveSinaToken(activity,accessToken);
	            }
	        }

	        @Override
	        public void onError(WeiboDialogError e) {
	            Toast.makeText(activity,
	                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
	        }

	        @Override
	        public void onCancel() {
	            Toast.makeText(activity, "Auth cancel",
	                    Toast.LENGTH_LONG).show();
	        }

	        @Override
	        public void onWeiboException(WeiboException e) {
	            Toast.makeText(activity,
	                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
	                    .show();
	        }

	    }
	
	
}
