package com.mikeedwin.xracer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Sky {
	private int viewWidth;
	private int viewHeight;
	
	public Sky(int width, int height) {
		viewWidth = width;
		viewHeight = height;
	
	}
	
	public void update(){
		
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		//update();
		
		// Sky
		Paint myPaint = new Paint();
		myPaint.setColor(Color.rgb(30, 151, 220));
		canvas.drawRect(0, 0, viewWidth, viewHeight/2, myPaint);
		
	}
	
	
}
