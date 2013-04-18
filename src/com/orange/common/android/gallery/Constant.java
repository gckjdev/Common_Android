package com.orange.common.android.gallery;

public class Constant 
{

	public static final float UNIT_SIZE=1f;

    public static float ratio;

    public static float pic_ratio;
    
    public static final int MAX_IMG_COUNT = 3;
   // public static final int MAX_TEXTURES_COUNT = 6;
    
    public static final int slideFrames = 15;
    public static final float frameRotateAngel = 160/slideFrames;
    public static final float xDistance = 2.5f*UNIT_SIZE;
    public static final float zDistance = 3.0f*UNIT_SIZE;
    public static final float xFrameStep = xDistance/slideFrames;
    public static final float zFrameStep = zDistance/slideFrames;

    public static final int slide_length = 100;
}
