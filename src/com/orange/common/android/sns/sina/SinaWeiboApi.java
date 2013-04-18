/**  
        * @title SinaWeiboApi.java  
        * @package com.orange.common.android.sns.sina  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-21 上午11:40:25  
        * @version V1.0  
 */
package com.orange.common.android.sns.sina;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.orange.common.android.utils.JsonUtil;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;



public class SinaWeiboApi 
{
	private static final String TAG = "SinaWeiboApi";
	private Activity activity;
	private Oauth2AccessToken accessToken;
	private String appKey;
	private String redirectUrl;
	private SinaWeiboHandler sinaWeiboHandler;

	public SinaWeiboApi(Activity activity, Oauth2AccessToken accessToken)
	{
		super();
		this.activity = activity;
		this.accessToken = accessToken;
	}

	
	

	
	public SinaWeiboApi(Activity activity, String appKey, String redirectUrl)
	{
		super();
		this.activity = activity;
		this.appKey = appKey;
		this.redirectUrl = redirectUrl;
		sinaWeiboHandler = new SinaWeiboHandler(activity);
		accessToken = sinaWeiboHandler.getAccessToken(appKey, redirectUrl);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		sinaWeiboHandler.authorizeCallBack(requestCode, resultCode, data);
		accessToken = sinaWeiboHandler.getAccessToken(appKey, redirectUrl);
	}
	
	
	
	
	public void  sendTextWeibo(String content,RequestListener requestListener)
	{
		
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			StatusesAPI sinaWeiBoApi = new StatusesAPI(accessToken);
			sinaWeiBoApi.update(content, "", "", requestListener);
		}else {
			Log.e(TAG, "<sendTextWeibo> but accessToken is null");
		}
	}

	
	public void sendImageWeibo(String content,String imageUrl,RequestListener requestListener)
	{
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			StatusesAPI sinaWeiBoApi = new StatusesAPI(accessToken);
			sinaWeiBoApi.upload(content, imageUrl, "", "", requestListener);
		}else {
			Log.e(TAG, "<sendTextWeibo> but accessToken is null");
		}
	}
	
	
	
	
	
	public void getUserUid(String screenName)
	{
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			AccountAPI accountAPI = new AccountAPI(accessToken);
			UsersAPI usersAPI = new UsersAPI(accessToken);
			//usersAPI.
		}else {
			Log.e(TAG, "<sendTextWeibo> but accessToken is null");
		}
		
	}
	
	
	
	public void follow(long uid,final String screenName,final RequestListener listener)
	{
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			final FriendshipsAPI friendshipsAPI = new FriendshipsAPI(accessToken);
			//friendshipsAPI.create(uid, screenName, listener);
			UsersAPI usersAPI = new UsersAPI(accessToken);
			usersAPI.show(screenName, new RequestListener()
			{
				
				@Override
				public void onIOException(IOException arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(WeiboException arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(String arg0)
				{
					JSONObject json;
					try
					{
						json = new JSONObject(arg0);
						long uid = JsonUtil.getLong(json, "id");
						friendshipsAPI.create(uid, screenName, listener);
					} catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			});
		}else {
			Log.e(TAG, "<sendTextWeibo> but accessToken is null");
		}
		
		
	}
	
	
	
	
	
	
}
