package com.mikeedwin.xracer;

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
	protected void onPause() {
		super.onPause();
		
		gameView.pauseGame();
		

		
		//-------- \/ Creating a notification so game can be resumed 
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
      
		int icon = R.drawable.bike;
		Notification notification = new Notification(icon, "XRacer Paused" , (System.currentTimeMillis()));    
		Context context = getApplicationContext();     
		CharSequence contentTitle = "X Racer";  
		CharSequence contentText = "Click to resume game";      
		Intent notificationIntent = new Intent(this, Game.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		mNotificationManager.notify(0, notification);
		//-------- /\ Creating a notification so game can be resumed 
		
	}

	
	
	
}
