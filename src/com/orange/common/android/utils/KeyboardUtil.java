package com.orange.common.android.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
	
	public static void showKeyboardForOnCreate(final View view, final Context context) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				showKeyboard(view, context);
			}

		}, 500); // open in 500 seconds
	}

	public static void showKeyboard(View view, Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static void hideKeyboard(View view, Context context) {
		// close keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
