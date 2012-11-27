package com.mikeedwin.xracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SensorEventListener {
	
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private Car racecar;
	private float[] outR = new float[16];
    private float[] orientationValues = new float[3];
    private SensorManager mSensorManager;
    private Sensor mOrientation;
	
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	    
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
        
        //car = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        
        Bitmap carbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        racecar = new Car(carbitmap);
        
        start();
	}
	
	

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		
		racecar.x += 0;
		
		if(event.values[2] > -1.5)
			racecar.x -= 5;
		
		else
			racecar.x += 5;
		
	}






	private void start() {
		
		racecar.x = 100;
		racecar.y = 200;
	}
	
	
	@Override
    protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		racecar.onDraw(canvas);
		
		update();
    }
	
	private void update() {
	
		
		
	}
	
}
