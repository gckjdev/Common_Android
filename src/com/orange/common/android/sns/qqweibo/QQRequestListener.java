/**  
        * @title QQRequest.java  
        * @package com.orange.common.android.sns.qqweibo  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-2 上午10:18:44  
        * @version V1.0  
 */
package com.orange.common.android.sns.qqweibo;

import com.weibo.sdk.android.WeiboException;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-2 上午10:18:44  
 */

public interface QQRequestListener
{
	public void onComplete(String response);
	public void onError(String response);
}
