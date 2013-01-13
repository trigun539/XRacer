package com.mikeedwin.xracer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Bike {
	public static final int BMP_ROWS = 11;
	public static final int BMP_NORMAL_ROW = 5;
	private Bitmap bitmap;
	private int viewWidth;
	private int viewHeight;
	private float turn;
	private int bmpWidth;
	private int bmpHeight;
	private int x;
	private int y;
	private int turnLevel;
	private Rect source;
	
	public Bike(Bitmap bitmap, int viewWidth, int viewHeight){
		this.bitmap = bitmap;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
		this.bmpWidth = bitmap.getWidth() / BMP_ROWS;
		this.bmpHeight = bitmap.getHeight();
		this.y = viewHeight - bmpHeight - (viewHeight/10);
		this.x = (viewWidth/2) - 70;
		this.turnLevel = 0;
	}
	
	public void update(double turn){
		
		// TRANFORMING NUMBER TO SPRITE POSITION
		turnLevel = (int) Math.floor(-(turn - 50)*0.1);
		
		if(turnLevel > 10){
			turnLevel = 10;
		}
		
		if(turnLevel < 0){
			turnLevel = 0;
		}

		updateSprite(turnLevel);
	}
	
	@SuppressLint({ "DrawAllocation", "DrawAllocation", "DrawAllocation" })
    public void onDraw(Canvas canvas, double turn){
		update(turn);
		canvas.drawBitmap(bitmap, source, new Rect(x, y, x + bmpWidth, y + bmpHeight), null);
    }
	
	private void updateSprite(int turnLevel){
		source = new Rect(bmpWidth * turnLevel, 0, (bmpWidth * (turnLevel + 1)), bmpHeight);
	}

}