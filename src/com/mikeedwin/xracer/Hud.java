package com.mikeedwin.xracer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Hud {
	private int score;
	private int speed;
	private int viewWidth;
	private int viewHeight;
	private Bitmap speedometer;
	
	public Hud(int width, int height, Bitmap speedometerBitmap){
		this.viewWidth = width;
		this.viewHeight = height;
		speedometer = speedometerBitmap;
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas, int speed, int score) {
		this.score = score;
		this.speed = speed;
		
		// SCORE TEXT
		Paint textPaint = new Paint();
		textPaint.setColor(Color.rgb(24, 122, 150)); 
		textPaint.setTextSize(80);
		textPaint.setTypeface(Typeface.SANS_SERIF);
		// radius=10, x-offset=-2 y-offset=-2, color=black
		textPaint.setShadowLayer(5.0f, -1.0f, -1.0f, Color.rgb(0, 0, 0));
		canvas.drawText("Score", 10, 70, textPaint);
		
		// ACTUAL SCORE
		Paint textPaint2 = new Paint();
		textPaint2.setColor(Color.rgb(255, 255, 255)); 
		textPaint2.setTextSize(30);
		textPaint2.setTypeface(Typeface.SANS_SERIF);
		// radius=10, x-offset=-2 y-offset=-2, color=black
		textPaint2.setShadowLayer(5.0f, -1.0f, -1.0f, Color.rgb(0, 0, 0));
		canvas.drawText(Integer.toString(score), 10, 110, textPaint2);
		
		// SPEDOMETER
		canvas.drawBitmap(speedometer, viewWidth - 180, viewHeight - 230, null);
		//canvas.drawBitmap(speedometer, new Rect(0,0,100,100), new Rect(0,0,130,130), null);
		
		// TODO: Draw Speedometer needle
		
		
		
	}

}
