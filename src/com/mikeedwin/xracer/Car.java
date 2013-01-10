package com.mikeedwin.xracer;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Car {
	public static final int BMP_ROWS = 1;
	public static final int BMP_COLUMNS = 3;
	private int viewWidth;
    private int viewHeight;
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
    public double turn;  //-10 to 10, -10 is full left, 10 is full right, 0 is straight
    
    public Car(Bitmap bmp, int vWidth, int vHeight){
        this.bmp = bmp;
        this.width = 34; //bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        this.currentFrame = 0;
        this.srcX = 0;
        this.srcY = 0;
        this.x = 10;
        this.y = 10;
        this.animationRow = 0;
        this.viewHeight = vHeight;
        this.viewWidth = vWidth;
    }
    
    public void update(){
    	// THIS WHERE YOU 
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
