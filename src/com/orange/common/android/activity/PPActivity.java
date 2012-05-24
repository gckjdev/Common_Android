package com.orange.common.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;


public class PPActivity extends Activity {

	ProgressDialog progressDialog;
	
	private void showProgressDialog(String title, String message){
		progressDialog = ProgressDialog.show(this, title, message);
	}
	
	public void showProgressDialog(int titleId, int messageId){
		Resources res = getResources();
		String title = res.getString(titleId);
		String message = res.getString(messageId);
		showProgressDialog(title, message);
	}
	
	public void hideDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	private static final int ACTION_TAKE_PHOTO = 1000; 
	Bitmap takePhotoBitmap = null;
	
	public void takePhoto(){
		takePhoto(ACTION_TAKE_PHOTO);
	}
	
	private void takePhoto(int requestCode) {
		Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(i, requestCode);		
	}
	
	private void savePhotoTaken(Intent intent){
		Bundle extras = intent.getExtras();
		if (extras == null) {
			return;
		}

		takePhotoBitmap = (Bitmap) extras.get("data");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTION_TAKE_PHOTO) {
			savePhotoTaken(data);
		}
	}
}
