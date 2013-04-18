package com.orange.common.android.gallery;


import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



public class CoverFlowView extends GLSurfaceView 
{
	
	
    private SceneRenderer mRenderer;
    
    private int TexturesCount = 0;
    private int[] textureIds;
    private HandlerThread ht = new HandlerThread("MyGLSurfaceView");
	private Handler handler;
	private float ox;
	private int next = 1;
	private int front = 2;
	private int slideIndex = 0;
	private boolean leftToRightDirection = true;
	private CoverFlowAdapter adapter;
	private int curruntPosition = 0 ;
	//private CoverFlowOnItemClickListener onItemClickListener;
	private ImageLoader imageLoader;
	
	public CoverFlowView(Context context) {
        super(context);
        
        
        ht.start();

        this.setEGLContextClientVersion(2); 
        mRenderer = new SceneRenderer();	
        setRenderer(mRenderer);					        
        
        setRenderMode(RENDERMODE_WHEN_DIRTY);
		handler = new Handler(ht.getLooper()){
			public void handleMessage(Message msg){
				if(msg.what == next){
					mRenderer.next(false);
				}else{
					mRenderer.next(true);
				}
			}
		};
      
        Constant.pic_ratio = (float)186/275;
        imageLoader = ImageLoader.getInstance();
    }   
	
	
    @Override 
    public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
    	switch(e.getAction()){
    	case MotionEvent.ACTION_DOWN:
    		ox = x;
    		break;
    	case MotionEvent.ACTION_UP:
    		float length = x - ox;
    		if(length < -Constant.slide_length){
    			Message msg = handler.obtainMessage(next);
    			handler.sendMessage(msg);
    		}else if(length > Constant.slide_length){
    			Message msg = handler.obtainMessage(front);
    			handler.sendMessage(msg);
    		}
    	}
    	return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {   
		private static final String TAG = "MySurfaceView";
		ImageRect imgRect[] = new ImageRect[Constant.MAX_IMG_COUNT];
		private Object lock = new Object();
    	
		
		
		
        public void onDrawFrame(GL10 gl) 
        { 
        	
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            MatrixState.setLightLocation(0f, 5f, 5f);
            
            DrawPics();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            
        	GLES20.glViewport(0, 0, width, height); 
        	
            Constant.ratio = (float) width / height;
            
            
			MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 4f, 100);
			
			MatrixState.setCamera(0f,0f,10.0f,0f,0f,0f,0f,1.0f,0.0f);
            
            
            MatrixState.setInitStack();
           
            for (int i = 0; i < TexturesCount; i++)
            	textureIds[i] = initTexture(i);
            
            
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            
            GLES20.glClearColor(0.0f,0.0f,0.0f, 1.0f);  
            
            for (int i=0; i<Constant.MAX_IMG_COUNT; i++)
            	imgRect[i] =new ImageRect(CoverFlowView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            
            GLES20.glEnable(GLES20.GL_CULL_FACE);  
        }      
        
        
        public void originTransformPic(int picIndex)
        {
        	
        	int factor = picIndex -(Constant.MAX_IMG_COUNT-1)/2;
        	float xOffset, zOffset;
        	xOffset = factor*Constant.xDistance;
        	zOffset = -Math.abs(factor*Constant.zDistance);
        	MatrixState.translate(xOffset, 0, zOffset);   
        	Log.d(TAG, "pic index = "+picIndex);
        	Log.d(TAG, "xOffset = "+xOffset);
        	
        }
        
        public void AnimationTransform(int picIndex, 
        								int rotateIndex, 
        								boolean leftToRightDirection)
        {
        	
        	originTransformPic(picIndex);
        	if (rotateIndex == 0)
        		return;
        	
        	int factor = picIndex -(Constant.MAX_IMG_COUNT-1)/2;
        	float xOffset, zOffset;
        	xOffset = Constant.xFrameStep*rotateIndex;
        	zOffset = -Constant.zFrameStep*rotateIndex;
        	if (!leftToRightDirection)
        		xOffset = -xOffset;
        	
        	if ((leftToRightDirection && (factor < 0)) || (!leftToRightDirection && (factor > 0)))
        		zOffset = -zOffset;
        	
        	MatrixState.translate(xOffset, 0, zOffset);  
        	Log.d(TAG, "picIndex = "+picIndex);
        	
        }
        
        public void DrawPics()
        {
        	float iAlphoaValue = 1.0f;

        	
            int mediaIndex = (Constant.MAX_IMG_COUNT-1)/2;
            
            for (int i=0; i<Constant.MAX_IMG_COUNT; i++)
            {
            	iAlphoaValue = 1.0f;
            	if (i != mediaIndex)
            	{
            		
            		if (((i == 0) && (!leftToRightDirection))
            			|| ((i == Constant.MAX_IMG_COUNT -1) && (leftToRightDirection)))
            		{
            			iAlphoaValue = 1.0f-(float)slideIndex/(float)Constant.slideFrames;
            		}
            		
            		MatrixState.pushMatrix();
            		AnimationTransform(i, slideIndex, leftToRightDirection);
            		int textureid = i;
            		if (textureid > (TexturesCount -1))
            			textureid = textureid % TexturesCount;
            		imgRect[i].drawSelf(textureIds[textureid],iAlphoaValue);
            		MatrixState.popMatrix();
            	}
            }
            
            if (Constant.MAX_IMG_COUNT > 1)
            {
	            iAlphoaValue = (float)slideIndex/(float)Constant.slideFrames;
	            
	            if (leftToRightDirection)
	            {
	            	MatrixState.pushMatrix();
	            	AnimationTransform(0, 0, leftToRightDirection);
	            	imgRect[0].drawSelf(textureIds[TexturesCount -1],iAlphoaValue);
	            	MatrixState.popMatrix();
	            }
	            else
	            {
	            	MatrixState.pushMatrix();
	            	AnimationTransform(Constant.MAX_IMG_COUNT -1, 0, leftToRightDirection);
	            	int textureid = Constant.MAX_IMG_COUNT;
	            	if (textureid > TexturesCount -1)
	            		textureid = textureid % TexturesCount;
	            	imgRect[Constant.MAX_IMG_COUNT -1].drawSelf(textureIds[textureid],iAlphoaValue);
	            	MatrixState.popMatrix();
	            }	
            }
            
            iAlphoaValue = 1.0f;
            MatrixState.pushMatrix();  
            int textureid = mediaIndex;
    		if (textureid > TexturesCount -1)
    			textureid = textureid % TexturesCount;
            AnimationTransform(mediaIndex, slideIndex, leftToRightDirection);
            
    		imgRect[mediaIndex].drawSelf(textureIds[textureid], iAlphoaValue);
    		MatrixState.popMatrix();
    		
    		synchronized (lock) 
            {
				lock.notifyAll();
			}
        }
        
       
    	public void next(boolean leftToRight){
    		int count = Constant.slideFrames;
    		int j;
			for(j=1; j<count;j++)
			{
				synchronized (lock) 
				{
					slideIndex = j;
					leftToRightDirection = leftToRight;
					CoverFlowView.this.requestRender();
				}
				
    			synchronized (lock) {
    				try {
    					lock.wait();
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
			}
			
			int tmpTextureid;
			if (!leftToRight){
				tmpTextureid = textureIds[0];
				if (curruntPosition == TexturesCount-1)
				{
					curruntPosition =0;
				}else {
					curruntPosition++;
				}
				
			}
			else{
				tmpTextureid = textureIds[TexturesCount-1];
				if (curruntPosition == 0)
				{
					curruntPosition = TexturesCount-1;
				}else {
					curruntPosition--;
				}
				
			}
			
			Log.d(TAG, "current postion = "+curruntPosition);
			
			if (!leftToRight)
			{
				for (int i=0;i<TexturesCount-1; i++)
	    			textureIds[i] = textureIds[i+1];
				textureIds[TexturesCount-1] = tmpTextureid;
			}
			else
			{
				for (int i=TexturesCount-1;i>0; i--)
	    			textureIds[i] = textureIds[i-1];
				textureIds[0] = tmpTextureid;
			}

			
			slideIndex = 0;
			leftToRightDirection = true;
			CoverFlowView.this.requestRender();
    	}

    }
	public int initTexture(int position)
	{
		
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          
				textures,   
				0           
		);    		

		int textureId=textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        
        
      //  Bitmap bitmapTmp = adapter.getBitmap(position);
		Bitmap bitmapTmp = null;
		/*String imageUrl = adapter.getImageURL(position);
		imageLoader.loadImage(imageUrl, new ImageLoadingListener()
		{
			
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{
				// TODO Auto-generated method stub
				
			}
		});*/
        
		 GLUtils.texImage2D
	        (
	        		GLES20.GL_TEXTURE_2D,   
	        		0, 					  
	        		bitmapTmp, 			  
	        		0					  
	        );
	        
	        
	        bitmapTmp.recycle();
        return textureId;
	}

	public CoverFlowAdapter getAdapter()
	{
		return adapter;
	}

	public void setAdapter(CoverFlowAdapter adapter)
	{
		this.adapter = adapter;
		TexturesCount = adapter.getCount();
		textureIds = new int[adapter.getCount()];
		
	}


	/*public void setOnItemClickListener(
			CoverFlowOnItemClickListener onItemClickListener)
	{
		this.onItemClickListener = onItemClickListener;
	}*/
	
}
