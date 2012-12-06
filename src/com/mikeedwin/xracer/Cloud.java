package com.mikeedwin.xracer;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class Cloud {
	
	private int x;
	private int y;
	private int viewWidth;
	private int viewHeight;
	private int xSpeed;
	
	public Cloud(int width, int height) {
		
		viewWidth = width;
		viewHeight = height;
		
		Random rnd = new Random();
        x = rnd.nextInt(viewWidth);
        y = rnd.nextInt((viewHeight / 2));
        xSpeed = rnd.nextInt(10) - 5;
	}
	
	public void update(){
		if (x > viewWidth - 20 - xSpeed || x + xSpeed < 0) {
		    xSpeed = -xSpeed;
		}
		x += xSpeed;
	}
	
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		update();

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		Path path = new Path();
		path.moveTo(10, 5);
		path.quadTo(10, 10, -5, 5);
		path.quadTo(-20, 5, -15, -5);
		path.quadTo(-5, -10, 5, -10);
		path.quadTo(10, -5, 10, 5);
		path.close();
		path.offset(x, y);

		canvas.drawPath(path, paint);
		
	}

}
