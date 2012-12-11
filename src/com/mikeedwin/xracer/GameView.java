package com.mikeedwin.xracer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    private Random rand;
    private List<Cloud> clouds = new ArrayList<Cloud>();
    private int speed = 0;  //current speed in mph
    private int distance = 0;  //total distance travelled
    private Timer T;
	
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
        
        Bitmap carbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);

        sky = new Sky(viewWidth, viewHeight);
        
        // Clouds
        for(int i = 0; i < 20; i++){
        	Cloud cloud = new Cloud(viewWidth, viewHeight);
        	clouds.add(cloud);
        }
        
        T = new Timer();
        TimerTask task = new TimerTask(){
        	@Override
            public void run() {
                speed++;
        }};
        
        T.scheduleAtFixedRate(task, 1000, 1000);  
        speed = 20; 
        
        racecar = new Car(carbitmap, viewWidth, viewHeight);
        road = new Road(viewWidth, viewHeight);
        rand = new Random();

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
		racecar.y = (viewHeight * 85)/100 - racecar.height/2;
	}
	
	
	@Override
    protected void onDraw(Canvas canvas) {
		movecar();
		adjustroad();

		canvas.drawColor(Color.rgb(30, 151, 220));
		sky.onDraw(canvas);

		for (Cloud cloud : clouds) {
			cloud.onDraw(canvas);
    	}
		
		road.onDraw(canvas);
		racecar.onDraw(canvas);
    }
	
	
	private void movecar() {
		//move car based on how the road is turned
		//road.road_leftright -= road.turn/20;
		
		//move car based on how it is turned
		road.road_leftright += racecar.turn/4;
		
		road.moveCarForward(speed);
	}
	
	private void adjustroad() {
		
		if(road.turn > 50)
			road.turn = -50;
		
		if(road.hill > 20)
			road.hill = -20;
		
		road.turn += 1;
		
		road.hill += 1;
		
		
		
	}
	
	
}
