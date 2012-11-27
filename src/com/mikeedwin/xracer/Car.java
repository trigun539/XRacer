package com.mikeedwin.xracer;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Car {
	public static final int BMP_ROWS = 1;
	public static final int BMP_COLUMNS = 3;
    public Bitmap bmp;
    public int width;
    public int height;
    public int srcX;
    public int srcY;
    public Rect src;
    public Rect dst;
    public int currentFrame;
    public int animationRow;
    public int x;
    public int y;
    
    public Car(Bitmap bmp){
        this.bmp = bmp;
        this.width = 34; //bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.currentFrame = 0;
        this.srcX = 0;
        this.srcY = 0;
        this.x = 10;
        this.y = 10;
        this.animationRow = 0;
    }
    
    public void update(){
    	
    }
    
    @SuppressLint({ "DrawAllocation", "DrawAllocation", "DrawAllocation" })
    public void onDraw(Canvas canvas){
    	srcX = currentFrame * width;
        srcY = height * animationRow;
        src = new Rect(srcX, srcY, srcX + width, srcY + height);
        dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }
    
}
