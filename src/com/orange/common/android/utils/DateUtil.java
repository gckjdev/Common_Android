/**  
        * @title DateUtil.java  
        * @package com.orange.common.android.utils  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-9 下午4:58:51  
        * @version V1.0  
 */
package com.orange.common.android.utils;

import android.util.Log;



public class DateUtil
{

	private static final String TAG = "DateUtil";

	public static String dateFormatToString(int date){
		String dateString = "";
		long currentTime = System.currentTimeMillis()/1000;
		date = (int)currentTime-date;
		if (date == 0)
		{
			return dateString;
		}
		if(date/60>60){
			dateString = date/3600+"小时前";
		}else {
			dateString = (date/60<1?1:date/60) +"分钟前";
		}
		return dateString;
	}
}
