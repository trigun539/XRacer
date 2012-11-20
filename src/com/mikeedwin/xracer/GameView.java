package com.mikeedwin.xracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private Bitmap car;
	private int x = 0;
	
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

               public void surfaceDestroyed(SurfaceHolder holder) {
            	   boolean retry = false;
            	   gameLoopThread.setRunning(false);
            	   
            	   while(retry){
            		   try {
            			   	gameLoopThread.join();
            			   	retry = false;
            		   } catch (InterruptedException e) {
            			   
            		   }
            	   }
               }

               public void surfaceCreated(SurfaceHolder holder) {
            	   gameLoopThread.setRunning(true);
            	   gameLoopThread.start();
               }

               public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {
               }
        });
        
        car = BitmapFactory.decodeResource(getResources(), R.drawable.car);

	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		if (x < getWidth() - car.getWidth()) {
	            x++;
	     }
	     canvas.drawBitmap(car, x, 10, null);
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {

		return true;
    }
	
}
