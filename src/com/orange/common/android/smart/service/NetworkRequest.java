/**  
        * @title HttpReuest.java  
        * @package com.common_android_20.service  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-18 下午5:04:10  
        * @version V1.0  
 */
package com.orange.common.android.smart.service;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orange.common.android.smart.model.ErrorCode;
import com.orange.common.android.smart.smartData.SmartDataCheckVersionCompletetInterface;
import com.orange.common.android.smart.smartData.SmartDataCompleteInterface;








public class NetworkRequest
{
	protected static final String TAG = "NetworkRequest";
	static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void checkVersionHttpRequest(final float dataVersion,final String updataVersionURL,final SmartDataCheckVersionCompletetInterface completeInterface)
	{
		AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(Throwable arg0, String arg1)
			{
				super.onFailure(arg0, arg1);
				completeInterface.onComplete("0");
			}

			@Override
			public void onSuccess(String arg0)
			{
				super.onSuccess(arg0);
				completeInterface.onComplete(arg0);
			}
			
		};
		client.get(updataVersionURL, asyncHttpResponseHandler);
	}
	
	
	public static  void download(
			final String downloadURL, final String savePath,  
			final String tempPath,final String fileName,
			final SmartDataCompleteInterface completeInterface)
	{

		DownloadHandler cityHandler = new DownloadHandler(savePath, tempPath
				+ fileName)
		{
			@Override
			public void onSuccess(byte[] fileData)
			{
				Log.d(TAG, "<download> success ");
				completeInterface.onComplete(ErrorCode.ERROR_SUCCESS);			
			}

			@Override
			public void onFailure(Throwable e, byte[] arg1)
			{
				Log.e(TAG, "<onFailure> downloading failure=" + e.toString());
				completeInterface.onComplete(ErrorCode.ERROR_FAIL);
			}

		};
		client.get(downloadURL, cityHandler);

	}
}
