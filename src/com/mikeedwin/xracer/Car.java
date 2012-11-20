package com.mikeedwin.xracer;

import java.util.Random;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Car {
	private static final int BMP_ROWS = 4;
	private static final int BMP_COLUMNS = 3;
	private int x;
	private int y;
    private int xSpeed;
    private int ySpeed;
    private GameView gameView;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width;
    private int height;
    
    public Car(GameView gameView, Bitmap bmp){
    	this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);
        xSpeed = rnd.nextInt(10)-5;
        ySpeed = rnd.nextInt(10)-5;
    }
    
    private void update() {
		if (x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0) {
		    xSpeed = -xSpeed;
		}
		x = x + xSpeed;
		if (y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0) {
			ySpeed = -ySpeed;
		}
		y = y + ySpeed;
		
        currentFrame = ++currentFrame % BMP_COLUMNS;
    }
   
    @SuppressLint({ "DrawAllocation", "DrawAllocation" })
	public void onDraw(Canvas canvas) {
          update();
          int srcX = currentFrame * width;
          int srcY = getAnimationRow() * height;
          Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
          Rect dst = new Rect(x, y, x + width, y + height);
          canvas.drawBitmap(bmp, src, dst, null);
    }
    
    private int getAnimationRow() {
        // get sprite position here
    	return 0;
    }
    
    public boolean isCollition(float x2, float y2) {
    	// collision stuff here
    	return false;
    }
}
