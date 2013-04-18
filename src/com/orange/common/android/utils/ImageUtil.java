/**  
        * @title ImageUtil.java  
        * @package com.orange.common.android.utils  
        * @description   
        * @author liuxiaokun  
        * @update 2012-12-27 下午7:23:51  
        * @version V1.0  
 */
package com.orange.common.android.utils;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;



public class ImageUtil
{
	
    
    public static Bitmap getRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();            
            Bitmap output = Bitmap.createBitmap(width,height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;   
            final Paint paint = new Paint();   
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());          
            paint.setAntiAlias(true);   
            canvas.drawARGB(0, 0, 0, 0);   
            paint.setColor(color);   
            if (width<=height)
			{
            	canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(),(rect.width()/2)-3, paint);
			}else {
				canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(),(rect.height()/2)-3, paint);
			}
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));   
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
    }
    
    
    public static boolean saveImageToGallery(Bitmap bitmap,Context context)
	{
    	boolean flag = false;
    	String result = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "", "");
		if (result!=null)
		{
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, 
					Uri.parse("file://"+ Environment.getExternalStorageDirectory()))); 
			flag = true;
		}else {
			flag = false;
		}		
		return flag;
	}
}
