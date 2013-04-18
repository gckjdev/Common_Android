package com.orange.common.android.gallery;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;


public class ImageRect
{	
	int mProgram;
    int muMVPMatrixHandle;
    int muMMatrixHandle;
    int maPositionHandle;     
    int maTexCoorHandle;
    int maAlphaHandle;
    
    int maCameraHandle; 
    int maNormalHandle;
    int maLightLocationHandle;
    
    String mVertexShader;
    String mFragmentShader;
	
	FloatBuffer   mVertexBuffer;
	FloatBuffer   mNormalBuffer;
	FloatBuffer   mTexCoorBuffer;
    int vCount=0;  
    
    public ImageRect(CoverFlowView mv)
    {    	
    	initVertexData();   
    	initShader(mv);
    }
    
    public void initVertexData()
    {
        vCount=12;       
        
        float vertices[]=new float[]
        {
        	
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	Constant.UNIT_SIZE,Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	-Constant.UNIT_SIZE,Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	-Constant.UNIT_SIZE,-(3*Constant.UNIT_SIZE)/Constant.pic_ratio,0,
        	Constant.UNIT_SIZE,-(3*Constant.UNIT_SIZE)/Constant.pic_ratio,0,
        	Constant.UNIT_SIZE,-(3*Constant.UNIT_SIZE)/Constant.pic_ratio,0,
        	Constant.UNIT_SIZE,-Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	-Constant.UNIT_SIZE,-Constant.UNIT_SIZE/Constant.pic_ratio,0,
        	
        	
        	
        };
	
        float normals[]=new float[]
        {
        	
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	0,0,1,
        	0,0,1,       	
        	
        };
     
        
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
       
        //================end============================
        
        float[] textures=new float[vCount*2];
        textures[0]=0.0f;
		textures[1]=0.0f;
		textures[2]=0.0f;
		textures[3]=1.0f;
		textures[4]=1.0f;
		textures[5]=1.0f;
		textures[6]=1.0f;
		textures[7]=1.0f;
		textures[8]=1.0f;
		textures[9]=0.0f;
		textures[10]=0.0f;
		textures[11]=0.0f;
		textures[12]=0.0f;
		textures[13]=1.0f;
		textures[14]=0.0f;
		textures[15]=0.0f;
		textures[16]=1.0f;
		textures[17]=0.0f;
		textures[18]=1.0f;
		textures[19]=0.0f;
		textures[20]=1.0f;
		textures[21]=1.0f;
		textures[22]=0.0f;
		textures[23]=1.0f;
        ByteBuffer cbb = ByteBuffer.allocateDirect(textures.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mTexCoorBuffer = cbb.asFloatBuffer();
        mTexCoorBuffer.put(textures);
        mTexCoorBuffer.position(0);
        
      
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
    }

    
    public void initShader(CoverFlowView mv)
    {
    	
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
       
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
       
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        
        maAlphaHandle= GLES20.glGetUniformLocation(mProgram, "aAlphaValue");
       
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
       
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
       
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation"); 
       
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");  
       
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");  
    }
    
    public void drawSelf(int texId, float Alpha)
    {    
    	 float[] AlphaValues = new float[1];
    	 AlphaValues[0] = Alpha;
    	
    	 GLES20.glUseProgram(mProgram);
        
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);  
        
         GLES20.glUniform1fv(maAlphaHandle, 1, AlphaValues, 0);
        
         GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        
         GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
	     
         GLES20.glVertexAttribPointer
         (
        		 maNormalHandle, 
        		 3, 
        		 GLES20.GL_FLOAT, 
        		 false,
        		 3 * 4, 
        		 mNormalBuffer
          );
        
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         ); 
         
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         
         GLES20.glEnableVertexAttribArray(maNormalHandle);
         
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
         
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount); 
    }
}
