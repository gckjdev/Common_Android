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
	/*private Activity activity;
	private OAuthV2 oAuth;
	public static final int REQUEST_CODE = 1;
	private QQOauth2AccessToken oauth2AccessToken;
	public QQWeiboHandler(Activity activity)
	{
		super();
		this.activity = activity;
	}
	
	
	public QQOauth2AccessToken getOauthToken(String appKey, String appSecret,String redirectUri)
	{
		oauth2AccessToken = OauthTokenKeeper.getQQToken(activity);
		if (!oauth2AccessToken.isSessionValid())
		{
			oAuth = new OAuthV2(redirectUri);
			oAuth.setClientId(appKey);
			oAuth.setClientSecret(appSecret);
			Intent intent = new Intent(activity,OAuthV2AuthorizeWebView.class);   
			intent.putExtra("oauth", oAuth);  
			activity.startActivityForResult(intent, REQUEST_CODE);
		}
			
		return oauth2AccessToken;
	}
	
	
	public void  onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode==REQUEST_CODE) {  
		     if (resultCode==OAuthV2AuthorizeWebView.RESULT_CODE) {
		         oAuth=(OAuthV2) data.getExtras().getSerializable("oauth");
		         oauth2AccessToken = new QQOauth2AccessToken(oAuth);
		         OauthTokenKeeper.saveQQToken(activity, oauth2AccessToken);
		     }
		 }
	}


	public static int getRequestCode()
	{
		return REQUEST_CODE;
	}
*/

	
}
