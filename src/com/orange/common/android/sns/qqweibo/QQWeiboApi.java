/**  
        * @title QQWeiboApi.java  
        * @package com.orange.common.android.sns.qqweibo  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-22 上午10:08:24  
        * @version V1.0  
 */
package com.orange.common.android.sns.qqweibo;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;

import android.R.string;
import android.app.Activity;
import android.util.Log;



public class QQWeiboApi
{
	private static final String TAG = "QQWeiboApi";
	private OAuthV2 oAuthV2;
	private Activity activity;
	private QQOauth2AccessToken token;
	
	
	public QQWeiboApi(Activity activity,QQOauth2AccessToken token)
	{
		super();
		this.activity = activity;
		this.oAuthV2 = new OAuthV2();
		oAuthV2.setClientId(token.getAppKey());
		oAuthV2.setClientSecret(token.getAppSecret());
		oAuthV2.setOpenid(token.getOpenId());
		oAuthV2.setAccessToken(token.getToken());
	}


	/*image url must be local path or tencent url*/
	public boolean sendWeiBo(String content,String imageUrl)
	{
		boolean result = false;
		TAPI tapi = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
		try
		{
			Log.d(TAG, "image url = "+imageUrl);
			String response = tapi.addPic(oAuthV2, "json", content, "127.0.0.1", imageUrl);
			result = decodeResponse(response);
			Log.d(TAG, "send qq weibo result = "+result);
			tapi.shutdownConnection();
			activity.finish();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
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
