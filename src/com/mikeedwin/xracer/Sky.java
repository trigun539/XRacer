package com.mikeedwin.xracer;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Sky {
	private GameView view;
	
	public Sky(GameView gameview) {
		this.view = gameview;
	
	}
	
	public void update(){
		
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		//update();
		//canvas.drawRect(background, backgroundPaint);
		
		// Sky
		Paint myPaint = new Paint();
		myPaint.setColor(Color.rgb(255, 255, 255));
		canvas.drawRect(0, 0, view.getWidth(), view.getHeight()/2, myPaint);
	}
	
	
}
