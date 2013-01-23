/**  
        * @title DateUtil.java  
        * @package com.orange.common.android.utils  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-9 下午4:58:51  
        * @version V1.0  
 */
package com.orange.common.android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.util.Log;



public class DateUtil
{

	private static final String TAG = "DateUtil";

	public static String dateFormatToString(int date){
		String dateString = "";
		long currentTime = System.currentTimeMillis()/1000;
		int dateTime = (int)(currentTime-date)/60;
		if (date == 0)
		{
			return dateString;
		}
		if(dateTime>1440){
			dateString = getDateString(date);
		}else if (dateTime>60&&dateTime<1440) {
			dateString = dateTime/60+"小时前";
		}else {
			dateString = (dateTime/60<1?1:dateTime/60) +"分钟前";
		}
		return dateString;
	}
	
	
	public static String getDateString(long time) {
		Calendar date = Calendar.getInstance();
		time = time*1000;
		//long targetTime = time - TimeZone.getDefault().getRawOffset();
		date.setTimeInMillis(time);
		
		SimpleDateFormat dateformat=new SimpleDateFormat("yy-MM-dd HH:mm");
		String dateString =dateformat.format(date.getTime());
		return dateString;
	}
}
