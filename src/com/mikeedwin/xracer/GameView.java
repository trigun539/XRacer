package com.mikeedwin.xracer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
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
    private Hud hud;
    private int viewWidth;
    private int viewHeight;
    private Random rand;
    private List<Cloud> clouds = new ArrayList<Cloud>();
    private int speed = 0;  // Current speed in MPH
    private double distance = 0;  // Total distance traveled
    private int score = 0;  //total score = distance * (5 * floor(speed/250)) can be changed
    private Timer T;
    private Bitmap speedometerBitmap;
    private int framecount = 0;  //the number of total frames processed by the game, iterates every time ondraw is called
    private Track track;
    private Bitmap bmpBike;
    private Bike bike;
    private double turn;
    private int time = 0; // In seconds
    
    // TEST TIMER
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	    
	    setKeepScreenOn(true);
	    
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
        
        // LOADING BITMAPS
        Bitmap carbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
        speedometerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.speedometer);
        Bitmap treeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
        Bitmap bmpBike = BitmapFactory.decodeResource(getResources(), R.drawable.bike);
        
        // INITIALIZING OBJECTS
        
        // SKY
        sky = new Sky(viewWidth, viewHeight);
        
        // CLOUDS
        for(int i = 0; i < 20; i++){
        	Cloud cloud = new Cloud(viewWidth, viewHeight);
        	clouds.add(cloud);
        }
        
        
        // LET ME KNOW IF THIS IS OK TO REMOVE 
        // WE DON"T NEET IT
        
        // *******************
        T = new Timer();
        TimerTask task = new TimerTask(){
        	@Override
            public void run() {
                //speed++;
        }};
        
        T.scheduleAtFixedRate(task, 1000, 1000);  
        // **************************
        
        // CAR
        racecar = new Car(carbitmap, viewWidth, viewHeight);
        // ROAD
        road = new Road(treeBitmap, viewWidth, viewHeight);
        // TRACK
        track = new Track();
        // Bike
        bike = new Bike(bmpBike, viewWidth, viewHeight);
        // HUD
        hud = new Hud(viewWidth, viewHeight, speedometerBitmap);

        rand = new Random();
        
        start();
        updateDistSpeedScore();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		racecar.x += 0;

		int tilt = (int)event.values[1];
		turn = (double) event.values[1];
		
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
		
		// GAME BACKGROUND
		canvas.drawColor(Color.rgb(30, 151, 220));
		
		sky.onDraw(canvas);

		for (Cloud cloud : clouds) {
			cloud.onDraw(canvas);
    	}
		
		if(framecount%50 == 21)   //creates a tree on the road every 50 frames
			road.makeTree(true);
		
		road.onDraw(canvas);
		racecar.onDraw(canvas);
		bike.onDraw(canvas, (int)turn);
		hud.onDraw(canvas, speed, score, time, distance);
		
		// Distance in feet
	    distance = speed * 0.00146667 * time;
		
		framecount++;
    }
	
	
	private void movecar() {
		//move car based on how the road is turned
		//road.road_leftright -= road.turn/20;
		
		//move car based on how it is turned
		road.road_leftright += (float)racecar.turn*.3;
		
		road.moveCarForward(speed);
	}
	
	private void adjustroad() {
		
		
		if(road.hill > 13)
			road.hill = -13;
		
		float distanceForTrack = (float) distance;
		track.setValues(distanceForTrack);
		
		road.turn = track.turnVal;
		road.hill = track.hillVal;
		road.nextHill = track.nextHillVal;
		road.nextTurn = track.nextTurnVal;
		road.turnChangeSpeed = track.turnChangeSpeed;
	}
	
	// TIMER 
	
  @SuppressLint("HandlerLeak")
  class RefreshHandler extends Handler {
	  @Override
	  public void handleMessage(Message msg) {
		  GameView.this.updateDistSpeedScore();
	  }

	  public void sleep(long delayMillis) {
		  this.removeMessages(0);
		  sendMessageDelayed(obtainMessage(0), delayMillis);
	  }
  };

  private void updateDistSpeedScore(){
	  mRedrawHandler.sleep(100);
  		
	  time++;
	  
	  if(speed >= 0 && speed < 50){
		  speed += 5;
	  }else if(speed >= 50 && speed < 100){
		  int divisibleByTwo = time % 2;
		  if(divisibleByTwo == 0){
			  speed++;
		  }
	  }else if(speed >= 100 && speed < 160){
		  int divisibleByFive = time % 5;
		  if(divisibleByFive == 0){
			  speed++;
		  }
	  }else{
		  speed = 160;
	  }
	  
	  score = (int) Math.floor(distance * 100); // 100 points per 1mile
  }
	
}
