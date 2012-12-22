package com.mikeedwin.xracer;

import android.R.bool;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tree {
	
	public int x;
	public int y;
	public int offset = 0;   //number of pixels from the horizon
	public int distance;  //number of pixels from the bottom edge
	public Boolean distanceSet = false;  //if the distance has been set by the road yet or not
	private int viewWidth;
	private int viewHeight;
	private Bitmap treeBitmap;
	public Rect src;
    public Rect dst;
	
	
	public Tree(Bitmap bmp, int width, int height) {
		
		viewWidth = width;
		viewHeight = height;
		treeBitmap = bmp;
		
		x = 100;
		y = 100;
        
	}
	
	public void update(){
		
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		update();
		
		int sizer = treeSize();
		
		src = new Rect(0, 0, 256, 256);
		//dst = new Rect((int)(x-sizer*.35), (int)(y-sizer*.65), (int)(x+sizer-sizer*.35), (int)(y+sizer-sizer*.65));
		dst = new Rect((int)(x-sizer*.5), (int)(y-sizer*1), (int)(x+sizer-sizer*.5), (int)(y+sizer-sizer*1));
		canvas.drawBitmap(treeBitmap, src, dst, null);

	}
	
	public int treeSize() {  //calculates the tree size appearance based on distance and viewWidth
		
		int returner = (425 * viewWidth) / (6 * (distance+220));
		
		return returner;
	}

}
