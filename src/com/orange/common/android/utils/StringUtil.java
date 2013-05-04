package com.orange.common.android.utils;

public class StringUtil {
	public static boolean isEmpty(String str){
		return (str == null || str.length() == 0||str.trim().equalsIgnoreCase(""));
	}

	public static boolean isNotEmpty(String str){
		return !(str == null || str.length() == 0);
	}
	
	
	
}
