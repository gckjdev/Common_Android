/**  
        * @title TiltTextView.java  
        * @package com.orange.common.android.utils.view  
        * @description   
        * @author liuxiaokun  
        * @update 2012-12-28 上午11:53:08  
        * @version V1.0  
 */
package com.orange.common.android.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2012-12-28 上午11:53:08  
 */

public class TiltTextView extends TextView
{
	public TiltTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(10, getMeasuredWidth()/2, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
}
