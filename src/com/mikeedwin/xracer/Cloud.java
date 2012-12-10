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
	private int xSpeed; // Speed of cloud
	private int scale; 
	
	public Cloud(int width, int height) {
		
		viewWidth = width;
		viewHeight = height;
		
		Random rnd = new Random();
        x = rnd.nextInt(viewWidth);
        y = rnd.nextInt((viewHeight / 2));
        xSpeed = rnd.nextInt(5) - 2;
        scale = rnd.nextInt(15);
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
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setStyle(Paint.Style.FILL);
		
		Path path = new Path();
		path.moveTo(-20, 0);
		
		// Top
		path.quadTo(0, -20, 20, 0);
		
		// Right
		path.quadTo(50, 5, 30, 10);

		// Bottom
		path.quadTo(0, 20, -10, 10);

		// left
		path.quadTo(-40, 20, -20, 0);
		
		path.close();
		path.offset(x, y);
		
		canvas.drawPath(path, paint);

	}

}
