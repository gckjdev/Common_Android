/**  
        * @title ZipUtil.java  
        * @package com.common_android_20  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-18 下午4:30:01  
        * @version V1.0  
 */
package com.orange.common.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-3-18 下午4:30:01  
 */

public class ZipUtil
{
	 private static final String TAG = "ZipUtil";
	 private static final int BUFF_SIZE = 1024 * 4; // 1M Byte
	 
	 public static boolean unZipFile(String zipFilePath, String folderPath)
		{
			long startTime = System.currentTimeMillis();
			//Log.d(TAG, "start unzip time"+startTime);
			boolean zipSuccess = false;
			String strEntry;
			byte data[] = new byte[BUFF_SIZE];
			BufferedOutputStream dest = null;
			BufferedInputStream bis = null;
			ZipEntry entry = null;
			File entryFile = null;
			ZipInputStream zis = null;
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try
			{
				File zipFile = new File(zipFilePath);
				if(zipFile.exists())
				{
					
					fis = new FileInputStream(zipFilePath);
					if (fis != null)
					{
						bis = new BufferedInputStream(fis);
						zis = new ZipInputStream(bis);					
					}
					
					String str = "";			
					while ((entry = zis.getNextEntry()) != null)
					{
						strEntry = entry.getName();
						str = folderPath + File.separator + strEntry;
						entryFile = new File(new String(str.getBytes("8859_1"),"GB2312"));
						if (entry.isDirectory())
						{
							if (!entryFile.exists())
								entryFile.mkdirs();
						} else
						{
							if (!entryFile.getParentFile().exists())
							{
								entryFile.getParentFile().mkdirs();
							}
							int count ;
							//FileOutputStream fos = new FileOutputStream(new File(folderPath + File.separator + strEntry));
							fos = new FileOutputStream(new File(folderPath + File.separator + strEntry));
							dest = new BufferedOutputStream(fos);
							while ((count = zis.read(data)) != -1)
							{
								dest.write(data, 0, count);
							}
							zipSuccess = true;
							dest.close();
							fos.close();
						}
					}
					fis.close();
					zis.close();
					bis.close();
					data = null;
					long endTime = System.currentTimeMillis();
					double unzipTime= (endTime-startTime)/1000;
					Log.d(TAG, "unzip unzipTime = "+unzipTime);
				}
			} catch (Exception e)
			{
				Log.e(TAG, "<upZipFile> but catch exception :" + e.toString(), e);
				//FileUtil.deleteFolder(folderPath);
				return false;
			}finally{
				data = null;
				entry = null;
				entryFile = null;
				try
				{
					if(dest!=null)
					{
						dest.close();
						dest = null;
					}
					if(bis!=null)
					{
						bis.close();
						bis = null;
					}
					if(zis!=null)
					{
						zis.close();
						zis = null;
					}
					if(fis != null){
						fis.close();
						fis = null;
					}
					if(fos != null){
						fos.close();
						fos = null;
					}
				} catch (Exception e2)
				{
				}
				
					
			}
			return zipSuccess;
		}
}
