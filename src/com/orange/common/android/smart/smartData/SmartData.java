/**  
        * @title SmartData.java  
        * @package com.common_android_20  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-18 下午12:19:36  
        * @version V1.0  
 */
package com.orange.common.android.smart.smartData;

import android.content.Context;
import android.util.Log;

import com.orange.common.android.smart.model.ErrorCode;
import com.orange.common.android.smart.model.SmartDataType;
import com.orange.common.android.smart.service.NetworkRequest;
import com.orange.common.android.utils.ZipUtil;



public class SmartData
{
	public static final String TAG = "SmartData";
	private Context context;
	//private String dataFilePath;
	//private String localDataFilePath;
	//private SmartDataType type;
	private float dataVersion = 0f;
	//private String dataName;
	private String appName;
	
	
	
	
	

	
	/**  
	* Constructor Method   
	* @param context  
	*/
	public SmartData(Context context)
	{
		super();
		this.context = context;
		this.appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
		SmartDataUtil.initPaths(appName);
	}



	public void initDataWithName(String dataName,SmartDataType smartDataType){
		/*this.type = smartDataType;
		this.dataName = dataName;*/
		if (!SmartDataUtil.isDataExists(dataName,appName, smartDataType))
		{				
			SmartDataUtil.initFile(context,dataName, dataName, smartDataType);
		}
		this.dataVersion = SmartDataUtil.getVersionByName(appName,dataName);
		
	}
	
	

	public String getDataFilePath(String dataName,SmartDataType type){
		return SmartDataUtil.getPathByName(appName, dataName, type);
	}


	public void downloadData(final String dataName,final SmartDataType type,final SmartDataCompleteInterface completeInterface){
		String downloadURL = SmartDataUtil.getFileUpdateURL(dataName,type );
		Log.d(TAG, "<downloadData> download url = "+downloadURL);
		final String savePath = SmartDataUtil.getFileUpdateDownloadPath(appName);
		final String tempPath = SmartDataUtil.getFileUpdateDownloadTempPath( appName);
		final String updateFileName = SmartDataUtil.getUpdateFileName(dataName, type);
		
		NetworkRequest.download(downloadURL, savePath, tempPath, updateFileName, new SmartDataCompleteInterface()
		{
			
			@Override
			public void onComplete(int errorCode)
			{
				if (ErrorCode.ERROR_SUCCESS == errorCode)
				{
					Log.d(TAG, "<downloadData> smart data download data = "+dataName+" success");
					if (type == SmartDataType.ZIP)
					{
						final String unZipFolderPath = SmartDataUtil.getUpdateFileUnZipPath(dataName,appName);
						String zipFilePath = savePath+updateFileName;
						unZipFile(zipFilePath, unZipFolderPath, completeInterface);
					}else {
						completeInterface.onComplete(ErrorCode.ERROR_SUCCESS);
					}
				}else {
					Log.d(TAG, "<downloadData> smart data download data = "+dataName+" fail");
					completeInterface.onComplete(ErrorCode.ERROR_FAIL);
				}
				
			}
		});
	}
	
	
	public void checkHasUpdate( String dataName,SmartDataCheckVersionCompletetInterface completeInterface){
		String versionUpdateURL = SmartDataUtil.getVersionUpdateURL(dataName);
		NetworkRequest.checkVersionHttpRequest(dataVersion,versionUpdateURL,completeInterface);
	}
	

	public void checkUpdateAndDownload(final String dataName,final SmartDataType type,final SmartDataCompleteInterface completeInterface){
		checkHasUpdate(dataName,new SmartDataCheckVersionCompletetInterface()
		{
			
			@Override
			public void onComplete(String version)
			{
				float latestVersion = 0f;
				if (version != null&!version.equalsIgnoreCase(""))
				{
					latestVersion = Float.parseFloat(version);
				}		
				Log.d(TAG, "<checkUpdateAndDownload> data = "+dataName+" latestVersion = "+latestVersion);
				if (latestVersion>dataVersion)
				{
					SmartDataUtil.storeVersionByName(appName, dataName, version);
					downloadData(dataName,type,completeInterface);
				}else{
					Log.d(TAG, "<checkUpdateAndDownload> there is no need to update");
					completeInterface.onComplete(ErrorCode.ERROR_FAIL);
				}			
			}
		});
	}
	
	public void unZipFile(String zipFilePath,String unZipFolderPath,SmartDataCompleteInterface  completeInterface){
		boolean result = ZipUtil.unZipFile(zipFilePath, unZipFolderPath);
		if (result)
		{
			completeInterface.onComplete(ErrorCode.ERROR_SUCCESS);
		}else {
			completeInterface.onComplete(ErrorCode.ERROR_FAIL);
		}
	}
	
	
	
	
}
