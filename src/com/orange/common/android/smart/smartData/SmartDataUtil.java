/**  
        * @title SmartDataUtil.java  
        * @package com.common_android_20  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-18 下午12:51:42  
        * @version V1.0  
 */
package com.orange.common.android.smart.smartData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import android.content.Context;
import android.util.Log;

import com.orange.common.android.smart.model.ConfigManager;
import com.orange.common.android.smart.model.SmartDataType;
import com.orange.common.android.utils.FileUtil;
import com.orange.common.android.utils.MemoryUtil;



public class SmartDataUtil
{
	 public static final String SMART_DATA_TOP_DIR = FileUtil.getSDPath()+"/%s/config_data/";
	 public static final String SMART_DATA_DOWNLOAD_DIR = FileUtil.getSDPath()+"/%s/config_data/download/";
	 public static final String SMART_DATA_DOWNLOAD_TEMP_DIR = FileUtil.getSDPath()+"/%s/config_data/download/temp/";
	 public static final String VERSION_FILE = "%s_version.txt";
	 public static final String ZIP_FILE = ".zip";
	 public static final String PB_FILE = ".pb";
	private static final String TAG = "SmartDataUtil";
	 
	 
	 
	 
	 public static void initPaths(String appName){
		FileUtil.creatDir(getTopPath(appName));
		FileUtil.creatDir(getFileUpdateDownloadPath(appName));
		FileUtil.creatDir(getFileUpdateDownloadTempPath(appName));
	}
	 
	 
	 public static void initFile(Context context,String appName,String dataName,SmartDataType type){
		 String dataPath = getPathByName(appName, dataName, type);
		 String dataFileName = getUpdateFileName(dataName, type);
		 String versionFile = getVersionFileName(dataName);
		 FileUtil.copyAssetsFile(context,dataFileName,dataPath);
		 FileUtil.copyAssetsFile(context, versionFile, dataPath);
	 }
	 
	 public static String  getTopPath(String appName){
		String topPath = String.format(SMART_DATA_TOP_DIR, appName);
		return topPath;
	 }
	 
	
	 
	 public static String getVersionUpdateURL(String dataName){
		 String versionFile = getVersionFileName(dataName);
		 String versionUpdateURL = ConfigManager.SMART_DATA_SERVER_URL+versionFile;
		 return versionUpdateURL;
	 }
	 
	public static String getVersionFileName(String dataName)
	{
		if (dataName != null && !dataName.equalsIgnoreCase(""))
		{
			return String.format(VERSION_FILE, dataName);
		}
		return null;
	}
	 
	 
	 public static float getVersionByName(String appName,String dataName)
	{
		 	String versionFilePath = getFileUpdateDownloadPath(appName)+getVersionFileName(dataName);
		 	if (FileUtil.checkFileIsExits(versionFilePath))
			{
				File versionFile = new File(versionFilePath);
				FileInputStream fileInputStream = null;
				try
				{
					fileInputStream = new FileInputStream(versionFile);
					ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
					fileInputStream.getChannel().read(byteBuffer);
					String dataVersion = new String(byteBuffer.array());	
					return Float.parseFloat(dataVersion);
				} catch (Exception e)
				{
					e.printStackTrace();
					Log.e(TAG, "<getVersionByName> but catch exception "+e.toString());
				}finally{
					if (fileInputStream != null)
					{
						try
						{
							fileInputStream.close();
						} catch (IOException e)
						{
						}
					}
				}
			}
		 	return 0;
	}
	 
	 
	 public static void storeVersionByName(String appName,String dataName,String version){
		 if (version == null||version.equalsIgnoreCase("")||!MemoryUtil.externalMemoryAvailable())
			 return;
		 String versionFilePath = getFileUpdateDownloadPath(appName)+getVersionFileName(dataName);
		 Log.d(TAG, "store version file path = "+versionFilePath);
		 File versionFile = new File(versionFilePath);
		 FileOutputStream fileOutputStream = null;
		 try
		{
			fileOutputStream = new FileOutputStream(versionFile);			
			fileOutputStream.write(version.getBytes());
		} catch (Exception e)
		{
			e.printStackTrace();
			Log.e(TAG, "<storeVersionByName> but catch exception "+e.toString());
		}finally{
			if (fileOutputStream != null){
				try
				{
					fileOutputStream.close();
				} catch (IOException e)
				{
				}
			}
		}
	 }
	 
	 
	 public static String getFileUpdateURL(String dataName,SmartDataType type){
		String fileUpdateURL =  ConfigManager.SMART_DATA_SERVER_URL+getUpdateFileName(dataName, type);
		return fileUpdateURL;
	 }
	 
	 public static String getFileUpdateDownloadPath(String appName){
		 String downloadPath = String.format(SMART_DATA_DOWNLOAD_DIR, appName);
		 return downloadPath;
	 }
	 
	 public static String getFileUpdateDownloadTempPath(String appName){
		 String downloadTempPath = String.format(SMART_DATA_DOWNLOAD_TEMP_DIR, appName);
		 return downloadTempPath;
	 }
	 
	 public static String getUpdateFileUnZipPath(String dataName,String appName){
		 String downloadPath = String.format(SMART_DATA_DOWNLOAD_DIR, appName)+dataName;
		 return downloadPath;
	 }
	 
	 public static String getUpdateFileName(String dataName,SmartDataType type){
		 StringBuffer fileName = new StringBuffer();
		 fileName.append(dataName);
		 switch (type)
		{
		case ZIP:
			fileName.append(ZIP_FILE);
			break;
		case PB:
			fileName.append(PB_FILE);
		default:
			break;
		}
		return fileName.toString(); 
	 }
	 
	 
	 
	 

	
	 public static String getPathByName(String appName,String dataName,SmartDataType type){
		 String dataFilePath = String.format(SMART_DATA_DOWNLOAD_DIR, appName)+getUpdateFileName(dataName, type);
		 return dataFilePath;
	 }
	 
	 
	/*public static String getDataFilePath(String dataName,String appName)
	{
		 String downloadPath = String.format(SMART_DATA_DOWNLOAD_DIR, appName)+dataName;
		 return downloadPath;
	}*/
	 
	
	public static boolean isDataExists(String dataName,String appName,SmartDataType type){
		
		return FileUtil.checkFileIsExits(getPathByName(appName, dataName, type));
		
	}


	

	    
	   
}
