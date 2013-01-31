package com.orange.common.android.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Gallery;
import android.widget.Toast;

public class ToastUtil {
	public static void makeNotImplToast(Context context) {
		Toast.makeText(context, "Not implemented yet!", Toast.LENGTH_LONG).show();
	}
	
	
    static Handler handler = new Handler(Looper.getMainLooper());  
    static Object synObj = new Object();  
    static Toast toast;
    static Toast viewToast;
    public  static void showToastMessage(Context context, String msg) {  
       showToastMessage(context, msg, Toast.LENGTH_SHORT);  
    }  
	  
	 
	  
   public  static void showToastMessage(final Context context, final String msg,  final int len) {  
       handler.post(new Runnable() {  
           @Override  
           public void run() {  
               synchronized (synObj) {  
                   if (toast != null) {  
                       //toast.cancel();  
                       toast.setText(msg);  
                       toast.setDuration(len);  
                   } else {  
                       toast = Toast.makeText(context, msg, len);  
                   }  
                   toast.show();  
               }  
           }  
       });   
   } 
   
   
   public static void makeToast(final Context context,final View view){
	   handler.post(new Runnable() {  
           @Override  
           public void run() {  
               synchronized (synObj) {  
                   if (viewToast != null) {  
                       //toast.cancel();   
                	   viewToast.setView(view); 
                   } else {  
                	   viewToast = new Toast(context);
                	   viewToast.setDuration(Toast.LENGTH_SHORT);
                	   viewToast.setView(view);
                	   viewToast.setGravity(Gravity.CENTER, 0,0);
                   }  
                   viewToast.show();  
               }  
           }  
       });   
   }
}
