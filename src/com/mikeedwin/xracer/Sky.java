package com.mikeedwin.xracer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sky {
	private String color;
	private GameView gameview;
	private int width;
	private int height;
	private Rect background;
	private Paint backgroundPaint;
	private Paint cloudPaint;
	
	public Sky(GameView gameview) {
		this.gameview = gameview;
		this.color = "#123456";
		this.width = gameview.getWidth() * 2;
		this.height = gameview.getHeight() / 2;
		this.backgroundPaint = new Paint();
		//backgroundPaint.setColor();
		
		
	}
	
	public update(){
		
	}
	
	public onDraw(Canvas canvas) {
		update();
	}
	
	
}
