/**  
        * @title PopupWindowUtil.java  
        * @package com.orange.common.android.utils  
        * @description   
        * @author liuxiaokun  
        * @update 2013-1-30 上午10:59:35  
        * @version V1.0  
 */
package com.orange.common.android.utils;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-1-30 上午10:59:35  
 */

public class PopupWindowUtil
{

	public static PopupWindow getMatcParentPopupWindow(View view,Drawable backgroundDrawable,Context context)
	{
		PopupWindow popupWindow = new PopupWindow(context);	
		popupWindow.setContentView(view);		
		popupWindow.setBackgroundDrawable(backgroundDrawable);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();
		return popupWindow;
	}
	
	
	public static PopupWindow getPopupWindow(View view, Drawable backgroundDrawable,Context context,int width,int height)
	{
		PopupWindow popupWindow = new PopupWindow(context);	
		popupWindow.setContentView(view);		
		popupWindow.setBackgroundDrawable(backgroundDrawable);
		popupWindow.setWidth(width);
		popupWindow.setHeight(height);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();
		return popupWindow;
	}
	
	
}
