/**  
        * @title WechatHandler.java  
        * @package com.orange.common.android.sns.wechat  
        * @description   
        * @author liuxiaokun  
        * @update 2013-2-22 下午2:45:53  
        * @version V1.0  
 */
package com.orange.common.android.sns.wechat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Util;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-2-22 下午2:45:53  
 */

public class WeChatApi
{
	private Activity activity;
	private String appId;
	private IWXAPI iwxapi;
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private static final int THUMB_SIZE = 150;
	
	public WeChatApi(Activity activity,String appId)
	{
		super();
		this.activity = activity;
		this.appId = appId;
		regToWx(this.activity, this.appId);
	}

	
	private void regToWx(Context context,String appId){
		iwxapi = WXAPIFactory.createWXAPI(context, appId, true);
		iwxapi.registerApp(appId);
	}
	
	public boolean sendTextToWeChat(String content)
	{
		SendMessageToWX.Req req = setTextReq(content);
		return iwxapi.sendReq(req);
	}
	
	public boolean sendImageToWeChat(String imageUrl)
	{
		SendMessageToWX.Req req = setImageReq(imageUrl);
		return iwxapi.sendReq(req);
	}
	
	
	public boolean sendTextToWeChatFri(String content){
		SendMessageToWX.Req req = setTextReq(content);
		req.scene = SendMessageToWX.Req.WXSceneTimeline ;
		return iwxapi.sendReq(req);
	}
	
	public boolean sendImageToWeChatFriend(String imageUrl)
	{
		SendMessageToWX.Req req = setImageReq(imageUrl);
		req.scene = SendMessageToWX.Req.WXSceneTimeline ;
		return iwxapi.sendReq(req);
	}
	
	
	private SendMessageToWX.Req setTextReq(String content){
		WXTextObject wxTextObject = new WXTextObject();
		wxTextObject.text = content;
		
		WXMediaMessage wxMediaMessage = new WXMediaMessage();
		wxMediaMessage.mediaObject = wxTextObject;
		wxMediaMessage.description = content;
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("text");
		req.message = wxMediaMessage;
		return req;
	}
	
	
	private SendMessageToWX.Req setImageReq(String imageUrl){
		WXImageObject imgObj = new WXImageObject();
		imgObj.imageUrl = imageUrl;
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = imgObj;

		Bitmap bmp = null;
		try
		{
			bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
		bmp.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("img");
		req.message = msg;
		
		return req;
	}	
	
	
	public boolean isSupportTimeline()
	{
		boolean isSupport = false;
		int wxSdkVersion = iwxapi.getWXAppSupportAPI();
		if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION)
			isSupport = true;
		return isSupport;
	}
	
	public void close()
	{
		iwxapi.unregisterApp();
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
