/**  
        * @title SmartDataType.java  
        * @package com.common_android_20.model  
        * @description   
        * @author liuxiaokun  
        * @update 2013-3-20 上午11:38:19  
        * @version V1.0  
 */
package com.orange.common.android.smart.model;


public enum SmartDataType
{
	ZIP(0),
	PB(1),
	TXT(2);
	
	final int value;

	SmartDataType(int value) {
		this.value = value;
	}

	public int intValue() {
		return value;
	}
}
