/**  
        * @title QQWeiboHandler.java  
        * @package com.orange.common.android.sns.qqweibo  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-21 下午4:13:53  
        * @version V1.0  
 */
package com.orange.common.android.sns.qqweibo;

import android.app.Activity;
import android.content.Intent;

import com.orange.common.android.sns.db.OauthTokenKeeper;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv1.OAuthV1Client;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.utils.QHttpClient;
import com.tencent.weibo.webview.OAuthV1AuthorizeWebView;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;



public class QQWeiboHandler
{
	private Activity activity;
	private Intent intent;
	private OAuthV2 oAuth;
	public static final int REQUEST_CODE = 1;
	public QQWeiboHandler(Activity activity,Intent intent)
	{
		super();
		this.activity = activity;
		this.intent = intent;
	}
	
	
	public void getOauthToken(String appKey, String appSecret,String redirectUri)
	{
		QQOauth2AccessToken token = OauthTokenKeeper.getQQToken(activity);
		if (token.isSessionValid())
		{
			activity.startActivity(intent);
		}else{
			oAuth = new OAuthV2(redirectUri);
			oAuth.setClientId(appKey);
			oAuth.setClientSecret(appSecret);
			Intent intent = new Intent(activity,OAuthV2AuthorizeWebView.class);   
			intent.putExtra("oauth", oAuth);  
			activity.startActivityForResult(intent, REQUEST_CODE);
		}
			
		
	}
	
	
	public void  onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode==REQUEST_CODE) {  
		     if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE) {
		         oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
		         QQOauth2AccessToken token = new QQOauth2AccessToken(oAuth);
		         OauthTokenKeeper.saveQQToken(activity, token);
		         activity.startActivity(intent);
		     }
		 }
	}
}
