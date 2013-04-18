/**  
        * @title CoverFlowAdapter.java  
        * @package com.alex.Sample_CoverFlow  
        * @description   
        * @author liuxiaokun  
        * @update 2013-4-9 下午5:05:47  
        * @version V1.0  
 */
package com.orange.common.android.gallery;

import android.graphics.Bitmap;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-4-9 下午5:05:47  
 */

public abstract class CoverFlowAdapter
{
	abstract public int getCount();
	abstract public String getImageURL(int position);
}
