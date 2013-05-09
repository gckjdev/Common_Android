package com.orange.common.android.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;







import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class FileUtil {
	private static final String TAG = null;
	private static int len = 0;
	private static byte[] buffer = new byte[1024];

	
	public static String getSDCardCachePath(Context context,String appName){
		return FileUtil.getSDPath()+"/"+appName+"/cache/";
	}
	
	
	public static void saveFileAsString(Context context, String filename, String content) {
		saveFileAsByte(context, filename, content.getBytes());
	}
	
	public static void saveFileAsByte(Context context, String filename, byte[] content) {
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(content);
		} catch (Exception e) {
			logException(filename, e);
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				logException(filename, e);
			}
		}
	}

	public static String readFileAsString(Context context, String filename) {
		byte[] data = readFileAsByte(context, filename);
		return new String(data);
	}

	public static byte[] readFileAsByte(Context context, String filename) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = null;
		try {
			fis = context.openFileInput(filename);
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			logException(filename, e);
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				logException(filename, e);
			}
		}

		return baos.toByteArray();
	}

	public static void logException(String filename, Exception e) {
		Log.e(UtilConstants.LOG_TAG, "File operation failed, file name: " + filename, e);
	}
	
	/**
	 * @param path
	 * @description
	 * @version 1.0
	 * @author liuxiaokun
	 */
	public static long getFileSize(String path)
	{
		// TODO Auto-generated method stub
		File file = new File(path);
		if (file.exists())
		{
			return file.length();
		} else
		{
			return 0;
		}
	}
	
	 public static boolean moveFile(String srcFile, String destPath) {
	        // File (or directory) to be moved
	        File file = new File(srcFile);
	        // Destination directory
	        File dir = new File(destPath);
	        // Move file to new directory
	       boolean moveFlag = file.renameTo(new File(dir, file.getName()));
	        //boolean moveFlag = file.renameTo(new File(destPath));
	        return moveFlag;
	}
	 
	
	public static void saveBitmapInFile(String filePath,String fileName,Bitmap bitmap)
	{
		File cacheFolder = new File(filePath);
		if (!cacheFolder.exists())
		{
			cacheFolder.mkdirs();
			creatNoMediaFile(filePath);
		}
		FileOutputStream fouts = null;
		try
		{
			File cacheFile = new File(filePath, fileName);
			cacheFile.createNewFile();
			fouts = new FileOutputStream(cacheFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fouts);
			fouts.flush();
			fouts.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	} 
	
	public static boolean deleteFile(String filePath)
	{
		boolean flag = false;
		File file = new File(filePath);
		if (file.isFile() && file.exists())
		{
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	
	public static String getSDPath(){
        boolean sdCardExist = MemoryUtil.externalMemoryAvailable();
        if   (sdCardExist)                              
        return	android.os.Environment.getExternalStorageDirectory().getAbsolutePath(); 
        return null;
    } 
	
	
	public static void creatNoMediaFile(String path){
		String fileName = path+".nomedia";
		File file = new File(fileName);
		try
		{
			file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	

 
public static boolean checkFileIsExits(String filePath)
{
	File file = new File(filePath);
	if (file.exists())
	{
		return true;
	}
	return false;
}

    
   /* public static String getSDPath(){
        boolean sdCardExist = MemoryUtil.externalMemoryAvailable();
        if   (sdCardExist)                              
        return	android.os.Environment.getExternalStorageDirectory().getAbsolutePath(); 
        return null;
    } */
    
    
    public static void creatDir(String filePath){
    	File fileSaveDir = new File(filePath);
		if (!fileSaveDir.exists())
			fileSaveDir.mkdirs();
    }

	
	public static boolean copyAssetsFile(Context context, String fileName,
			String dataPath)
	{

		InputStream inputStream = null;
		OutputStream myOutput = null;
		BufferedInputStream myInput = null;
		boolean result = false;
		String targetFile = dataPath+fileName;
		try
		{
			inputStream = context.getAssets().open(fileName);
			myOutput = new FileOutputStream(targetFile);
			myInput = new BufferedInputStream(inputStream);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) != -1)
			{
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			result = true;	
		} catch (Exception e)
		{
			Log.e(TAG, "<copyAssetsFile> assets file = " +fileName + ", to dest file "
					+ targetFile + ", but catch exception " + e.toString(), e);
			result = false;
	} finally
	{
		try
		{
			if (myOutput != null)
			{
				myOutput.close();
			}
			if (myInput != null)
			{
				myInput.close();
			}
			if (inputStream != null)
			{
				inputStream.close();
			}
		} catch (IOException e)
		{
		}
	}
		return result;
	}
}
