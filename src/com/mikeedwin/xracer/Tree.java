package com.mikeedwin.xracer;

import android.R.bool;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tree {
	
	public int x = 100;
	public int y = 100;
	public int offset = 0;   //number of pixels from the horizon
	public int distance;  //number of pixels from the bottom edge
	public Boolean isLeft;  //is on the left side if true, otherwise right side
	public Boolean distanceSet = false;  //if the distance has been set by the road yet or not
	public float currentSize = 100;   //the current size in the view, set by road.java
	private int viewWidth;
	private int viewHeight;
	private Bitmap treeBitmap;
	public Rect src;
    public Rect dst;
	
	
	public Tree(Bitmap bmp, int width, int height, Boolean _isLeft) {
		
		viewWidth = width;
		viewHeight = height;
		treeBitmap = bmp;
		isLeft = _isLeft;
		
        
	}
	
	public void update(){
		
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		update();
		
		//int sizer = treeSize();
		
		
		//dst = new Rect((int)(x-sizer*.35), (int)(y-sizer*.65), (int)(x+sizer-sizer*.35), (int)(y+sizer-sizer*.65));
		dst = new Rect((int)(x-currentSize*.5), (int)(y-currentSize*1), (int)(x+currentSize-currentSize*.5), (int)(y+currentSize-currentSize*1));
		canvas.drawBitmap(treeBitmap, null, dst, null);

	}
	
	public int treeSize() {  //calculates the tree size appearance based on distance and viewWidth
		
		int returner = (400 * viewWidth) / (7 * (distance+180));
		
		return returner;
	}

}
