package com.mikeedwin.xracer;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;



public class Game extends Activity {

	private GameView gameView;
	private NotificationManager notificationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		gameView = new GameView(this);
		setContentView(gameView);
		
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		
	}

	
	@Override
	public void onBackPressed() {
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.ingame_menu, menu);
        return true;
    }
    
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.quitOption)
		{
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onStop();
		
		/*
		int icon = R.drawable.bike;
		Notification notification = new Notification(icon, "XRacer Paused" , (System.currentTimeMillis()));    
		Context context = getApplicationContext();     
		CharSequence contentTitle = "X Racer DESTROYED";  
		CharSequence contentText = "Click to resume game";      
		Intent notificationIntent = new Intent(this, Game.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		notificationManager.notify(1, notification);
		
		*/
	}


	@Override
	protected void onPause() {
		super.onPause();
		
		gameView.pauseGame();
		
		
		
		
		
		//-------- \/ Creating a notification so game can be resumed 
		//String ns = Context.NOTIFICATION_SERVICE;
		//mNotificationManager = (NotificationManager) getSystemService(ns);
      
		int icon = R.drawable.bike;
		Notification notification = new Notification(icon, "XRacer Paused" , (System.currentTimeMillis()));    
		Context context = getApplicationContext();     
		CharSequence contentTitle = "X Racer";  
		CharSequence contentText = "Click to resume game";      
		Intent notificationIntent = new Intent(this, Game.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		notificationManager.notify(0, notification);
		//-------- /\ Creating a notification so game can be resumed 
		
	
		
		TimerTask killMe = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				notificationManager.cancel(0);
				finish();
				
			}
		};
		
		
		Timer killtimer = new Timer();
		killtimer.schedule(killMe, 5000);
		
	}

	
	
}
