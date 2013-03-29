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

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;



public class SinaWeiboApi 
{
	private Activity activity;
	private Oauth2AccessToken accessToken;


	public SinaWeiboApi(Activity activity, Oauth2AccessToken accessToken)
	{
		super();
		this.activity = activity;
		this.accessToken = accessToken;
	}


	public void  sendTextWeibo(String content,RequestListener requestListener)
	{
		
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			StatusesAPI sinaWeiBoApi = new StatusesAPI(accessToken);
			sinaWeiBoApi.update(content, "", "", requestListener);
			activity.finish();
		}
	}

	
	public void sendImageWeibo(String content,String imageUrl,RequestListener requestListener)
	{
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			StatusesAPI sinaWeiBoApi = new StatusesAPI(accessToken);
			sinaWeiBoApi.upload(content, imageUrl, "", "", requestListener);
			activity.finish();
		}
	}
	
	
	public void getUserInfo()
	{
		if (!TextUtils.isEmpty(accessToken.getToken()))
		{
			AccountAPI accountAPI = new AccountAPI(accessToken);
			UsersAPI usersAPI = new UsersAPI(accessToken);
		}
	}
	
	
	private void sendWeiboSuccess(){
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(activity, "send weibo success", Toast.LENGTH_LONG)
						.show();
			}
		});
	}
	
	
	
	
}
