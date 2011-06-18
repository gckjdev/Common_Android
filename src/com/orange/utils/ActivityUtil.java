package com.orange.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtil {
	public static void setNoTitle(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	public static void setFullScreen(Activity activity) {
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
