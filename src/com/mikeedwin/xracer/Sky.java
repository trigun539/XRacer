package com.mikeedwin.xracer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Sky {
	private int viewWidth;
	private int viewHeight;
	private Bitmap skyBitmap;
	private int skywidth;
	private int skyheight;
	private double yAdjuster;
	private float skyX = 0;
	private float skyY = 0;
	
	public Sky(Bitmap bmp, int width, int height) {
		viewWidth = width;
		viewHeight = height;
		skyBitmap = bmp;
		
		skywidth = viewWidth * 5;
		skyheight = skywidth/8;
		
		yAdjuster = skyheight - ((double)viewHeight * .49);
		//skyheight = (int)((double)viewHeight * .49);
		//skywidth = skyheight * 8;
	}
	
	public void update(){
		// TODO: Add date to night change and vice versa
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas, float roadhill, float roadcompass) {
		//update();
		
		// Sky
		/*
		Paint myPaint = new Paint();
		myPaint.setColor(Color.rgb(30, 151, 220));
		canvas.drawRect(0, 0, viewWidth, viewHeight, myPaint);
		*/
		
		skyY = (float)(0 - ((roadhill-20) * -.008 * viewHeight) - yAdjuster);  //the sky image moves up 80% the amount the hill moves up
		//skyY = (float)(0 - yAdjuster);
		skyX = (float)(0 - (roadcompass * .005 * skywidth));
		
		RectF dst = new RectF(skyX, skyY, skywidth+skyX, skyheight+skyY);
		canvas.drawBitmap(skyBitmap, null, dst, null);
	}
	
	
}
