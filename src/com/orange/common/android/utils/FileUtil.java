package com.orange.common.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import android.content.Context;
import android.util.Log;

public class FileUtil {
	private static int len = 0;
	private static byte[] buffer = new byte[1024];

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
}
