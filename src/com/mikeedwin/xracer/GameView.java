package com.mikeedwin.xracer;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SensorEventListener {
	
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private Car racecar;
    private SensorManager mSensorManager;
    private Sensor mOrientation;
    private Road road;
    private Sky sky;
    private int viewWidth;
    private int viewHeight;
	
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	    
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    viewWidth = metrics.widthPixels;
	    viewHeight = metrics.heightPixels;
	    
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
        sky = new Sky(this);
        racecar = new Car(carbitmap, viewWidth, viewHeight);
        road = new Road(viewWidth, viewHeight);

        start();
	}
	
	

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		
		racecar.x += 0;
		
		int tilt = (int)event.values[1];

		if(tilt > 10)
			tilt = 10;
			
		else if(tilt < -10)
			tilt = -10;
		
		racecar.turn = -tilt;
		
	}

	private void start() {
		
		racecar.x = viewWidth/2;
		racecar.y = viewHeight - 80;
	}
	
	
	@Override
    protected void onDraw(Canvas canvas) {
		movecar();
		adjustroad();

		canvas.drawColor(Color.rgb(30, 151, 220));
		sky.onDraw(canvas);
		road.onDraw(canvas);
		racecar.onDraw(canvas);
    }
	
	
	private void movecar() {
		racecar.x += racecar.turn/2;
	}
	
	private void adjustroad() {
		
		Calendar c = Calendar.getInstance(); 
		int seconds = c.get(Calendar.SECOND);
		
		road.turn = (seconds%20 - 10) * 5;  //will set it between -50 and 50 depending on the time
	}
	
	
}
