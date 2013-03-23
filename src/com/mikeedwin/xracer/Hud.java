package com.mikeedwin.xracer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class Hud {
	private int score;
	private int speed;
	private int viewWidth;
	private int viewHeight;
	private Bitmap speedometerBackground;
	private static DisplayMetrics metrics = null;
	private static float density = 0;
	
	public Hud(int width, int height, Bitmap speedometerBitmap){
		this.viewWidth = width;
		this.viewHeight = height;
		this.speedometerBackground = speedometerBitmap;
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas, int speed, int score, int time, double distance, double turn) {
		this.score = score;
		this.speed = speed;
		this.speedometerBackground = speedometerBackground;
		
		// SCORE TEXT
		Paint textPaint = new Paint();
		textPaint.setColor(Color.rgb(24, 122, 150)); 
		textPaint.setTextSize((int) Math.floor(viewHeight * 0.11111111));
		textPaint.setTypeface(Typeface.SANS_SERIF);
		// radius=10, x-offset=-2 y-offset=-2, color=black
		textPaint.setShadowLayer(5.0f, -1.0f, -1.0f, Color.rgb(0, 0, 0));
		canvas.drawText("Score", 10, (int) Math.floor(viewHeight * 0.09722222), textPaint);
		
		// ACTUAL SCORE
		Paint textPaint2 = new Paint();
		textPaint2.setColor(Color.rgb(255, 255, 255)); 
		textPaint2.setTextSize((int) Math.floor(viewHeight * 0.04166667));
		textPaint2.setTypeface(Typeface.SANS_SERIF);
		// radius=10, x-offset=-2 y-offset=-2, color=black
		textPaint2.setShadowLayer(5.0f, -1.0f, -1.0f, Color.rgb(0, 0, 0));
		canvas.drawText(Integer.toString(score), 10, (int) Math.floor(viewHeight * 0.15277778), textPaint2);
		
		// SPEEDOMETER
		// Background Paint
		//Paint spdomtrBckgrndPt = new Paint();
		// Background shape
		/*
		spdomtrBckgrndPt.setAntiAlias(true);
		spdomtrBckgrndPt.setColor(Color.BLACK);
		spdomtrBckgrndPt.setStyle(Paint.Style.FILL); 
		spdomtrBckgrndPt.setAlpha(100);
		canvas.drawCircle(viewWidth - (float)(viewWidth * 0.1015625), viewHeight - (float)(viewWidth * 0.125), (int)(viewWidth * 0.0546875), spdomtrBckgrndPt);
		canvas.drawRoundRect(new RectF(viewWidth - (float)(viewWidth * 0.1015625), 
				viewHeight - (float)(viewWidth * 0.125), viewWidth - (int)(viewWidth * 0.046875), viewHeight - (int)(viewWidth * 0.0703125)), 5, 5, spdomtrBckgrndPt);
		*/
		
		// Spedometer
		// Background
		//RectF src = new Rect(0, 0, speedometerBackground.getWidth(), speedometerBackground.getHeight());
		RectF dst = new RectF((int)(viewWidth * 0.875), (int)(viewWidth * 0.3984375), (int)(viewWidth * 0.9921875), (int)(viewWidth * 0.515625));
		canvas.drawBitmap(speedometerBackground, null, dst, null);
		// SPEED
		canvas.drawText(Integer.toString(speed), 1180, 600, textPaint2);
		
		
		
		// DEBUG INFO
		
		// TIME
		canvas.drawText(Integer.toString(time), 200, 200, textPaint2);
		// SPEED
		canvas.drawText(Integer.toString(speed), 300, 200, textPaint2);
		// DISTANCE
		canvas.drawText(Double.toString(distance), 400, 200, textPaint2);
		// DEBUG
		canvas.drawText(Integer.toString(viewHeight), 200, 250, textPaint2);
		// TURN
		canvas.drawText(Double.toString(turn), 300, 250, textPaint2);
		
	}	
}
