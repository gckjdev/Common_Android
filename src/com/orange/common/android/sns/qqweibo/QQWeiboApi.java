/**  
        * @title QQWeiboApi.java  
        * @package com.orange.common.android.sns.qqweibo  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-22 上午10:08:24  
        * @version V1.0  
 */
package com.orange.common.android.sns.qqweibo;

import java.io.LineNumberInputStream;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.orange.common.android.sns.db.OauthTokenKeeper;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;



public class QQWeiboApi
{
	private static final String TAG = "QQWeiboApi";
	private OAuthV2 oAuthV2;
	private Activity activity;
	private QQOauth2AccessToken token;
	private QQWeiboHandler qqWeiboHandler;
	private ExecutorService executorService;
	private String QQAppKey;
	private String QQAppSecret;
	private String QQRedirectUrl;
	
	public QQWeiboApi(Activity activity,String QQAppKey,String QQAppSecret,String QQRedirectUrl)
	{
		super();
		this.activity = activity;
		this.oAuthV2 = new OAuthV2();
		qqWeiboHandler = new QQWeiboHandler(activity);
		this.QQAppKey = QQAppKey;
		this.QQAppSecret = QQAppSecret;
		this.QQRedirectUrl = QQRedirectUrl;
		init(QQAppKey, QQAppSecret, QQRedirectUrl);
		executorService = Executors.newSingleThreadExecutor();
		
	}

	
	private void init(String QQAppKey,String QQAppSecret,String QQRedirectUrl){
		token = qqWeiboHandler.getOauthToken(QQAppKey, QQAppSecret, QQRedirectUrl);
		Log.d(TAG, "qq token app key = "+token.getAppKey());
		Log.d(TAG, "qq token app secret = "+token.getAppSecret());
		oAuthV2.setClientId(token.getAppKey());
		oAuthV2.setClientSecret(token.getAppSecret());
		oAuthV2.setOpenid(token.getOpenId());
		oAuthV2.setAccessToken(token.getToken());
	}

	public void  onActivityResult(int requestCode, int resultCode, Intent data)
	{
		qqWeiboHandler.onActivityResult(requestCode, resultCode, data);
		init(QQAppKey, QQAppSecret, QQRedirectUrl);
		
	}
	
	
	/*image url must be local path or tencent url*/
	public void sendWeiBo(final String content,final String imageUrl,final QQRequestListener listener)
	{
		executorService.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				boolean result = false;
				TAPI tapi = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
				try
				{
					Log.d(TAG, "image url = "+imageUrl);
					String response = tapi.addPic(oAuthV2, "json", content, "127.0.0.1", imageUrl);
					result = decodeResponse(response);
					Log.d(TAG, "send qq weibo result = "+result);
					if (result)
						listener.onComplete(response);
					else
						listener.onError(response);
					tapi.shutdownConnection();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
	}

	
	
	
	private boolean decodeResponse(String response){
		boolean result = false;
		if (response == null||response.equalsIgnoreCase(""))
		{
			return result;
		}
		try
		{
			JSONObject jsonObject = new JSONObject(response);
			String errorCode = jsonObject.getString("errcode");
			if (errorCode.equalsIgnoreCase("0"))
			{
				result = true;
			}		
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
