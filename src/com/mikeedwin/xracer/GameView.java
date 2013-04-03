package com.mikeedwin.xracer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SensorEventListener {
	
	private SharedPreferences mySettings;
	private Boolean tiltMode = true;
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
    private double distChanged = 0;  //distance changed since last frame
    private int score = 0;  //total score = distance * (5 * floor(speed/250)) can be changed
    private Timer T;
    
    private int framecount = 0;  //the number of total frames processed by the game, iterates every time ondraw is called
    private Track track;
    private Bitmap bmpBike;
    private Bike bike;
    private double turn;
    private int time = 0; // In seconds
    private long StartTime;  //clock time when the game was first started
    private long TimePaused; //milliseconds in which the game has been paused 
    private Boolean isPaused = false;
    
    // TEST TIMER
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    
	public GameView(Context context) {
		super(context);
		gameLoopThread = new GameLoopThread(this);
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_NORMAL);
	    
	    mySettings = PreferenceManager.getDefaultSharedPreferences(getContext());
	    if(mySettings.getString("control_scheme", "").contains("On Screen"))
	    	tiltMode = false;
	    
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
        Bitmap treeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
        Bitmap bmpBike = BitmapFactory.decodeResource(getResources(), R.drawable.bike);
        Bitmap skyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky360png);
        Bitmap speedometersBGBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.speedometer_background);
        
        // INITIALIZING OBJECTS
        
        // SKY
        sky = new Sky(skyBitmap, viewWidth, viewHeight);
        
        /*
        // CLOUDS
        for(int i = 0; i < 20; i++){
        	Cloud cloud = new Cloud(viewWidth, viewHeight);
        	clouds.add(cloud);
        }
        */
        
         
        StartTime = System.currentTimeMillis();
        
        
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
        hud = new Hud(viewWidth, viewHeight, speedometersBGBitmap);

        rand = new Random();
        
        start();
        updateDistSpeedScore();
        
	}
	
	
	public void pauseGame()
	{
		isPaused = true;
		
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
		
		sky.onDraw(canvas, road.hill, road.compass);

		/*
		for (Cloud cloud : clouds) {
			cloud.onDraw(canvas);
    	}
    	*/
		
		if(framecount%26 == 7)   //creates a tree on the left every 26 frames
			road.makeTree(true);
		
		if(framecount%26 == 20)   //creates a tree on the right every 26 frames
			road.makeTree(false);
		
		road.onDraw(canvas);
		
		
		
		racecar.onDraw(canvas);
		bike.onDraw(canvas, turn);
		hud.onDraw(canvas, speed, score, time, distance, turn);
		
		// Distance in feet
		
		
		framecount++;
    }
	
	
	private void movecar() {
		//move car based on how the road is turned
		//road.road_leftright -= road.turn/20;
		
		//move car based on how it is turned
		//road.road_leftright += (float)racecar.turn* Math.pow(speed, .8) * .014;
		road.road_leftright -= (float)turn * Math.pow(speed, 0.8 ) * 0.005;
		
		road.moveCarForward(distChanged);
	}
	
	private void adjustroad() {
		
		
		if(road.hill > 13)
			road.hill = -13;
		
		track.setValues((float) distance);
		
		road.turn = track.turnVal;
		road.hill = track.hillVal;
		road.nextHill = track.nextHillVal;
		road.nextTurn = track.nextTurnVal;
		road.turnChangeSpeed = track.turnChangeSpeed;
		road.distToNextTrackPt = track.distToNextTrackPt;
	}
	
	// TIMER 
	
  @SuppressLint("HandlerLeak")
  class RefreshHandler extends Handler {
	  @Override
	  public void handleMessage(Message msg) {
		  
		  if(!isPaused)
			  GameView.this.updateDistSpeedScore();
	  }

	  public void sleep(long delayMillis) {
		  this.removeMessages(0);
		  sendMessageDelayed(obtainMessage(0), delayMillis);
	  }
  };

  private void updateDistSpeedScore(){
	  mRedrawHandler.sleep(10);
  		 
	  long prevtime = time;
	  
	  
	  
	  time = (int)( System.currentTimeMillis() - StartTime - TimePaused);
	  long timepassed = time-prevtime;  //the time passed since the last draw frame
	  
	  speed = (int) Math.floor((Math.pow((float)time/1000, .4))*17);
	  
	  distChanged = (timepassed)*(speed)*.0015;
	  //Log.v('asd','test');
	  distance += distChanged;
	  
	  if(speed < 0){
		  speed = 0;
	  }else if(speed > 120){
		  speed = 120;
	  }
	  
	  double newCompass = road.compass + (distChanged * road.turn*.002);
	  
	  if(newCompass > 100)
		  newCompass -= 100;
	  
	  if(newCompass < 0)
		  newCompass += 100;
	  
	  road.compass = (float)newCompass;
	 
		  
	  score = (int) Math.floor(distance * 100); // 100 points per 1mile
  }
	
}
