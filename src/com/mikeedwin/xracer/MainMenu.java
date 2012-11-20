package com.mikeedwin.xracer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainMenu extends Activity {

	private Button startRaceButton, settingsButton;
	private MediaPlayer themeSong;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_menu_layout);
        startRaceButton = (Button)findViewById(R.id.startRaceButton);
        settingsButton = (Button)findViewById(R.id.settingsButton);
        
        startRaceButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Starting Game
				Intent startingGame = new Intent(MainMenu.this, Game.class);
				startActivity(startingGame);
				
			}
		});
        
        settingsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent openSettings = new Intent(MainMenu.this, Settings.class);
				startActivity(openSettings);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
        return true;
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		themeSong.stop();
	}

	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		themeSong = MediaPlayer.create(MainMenu.this, R.raw.speedracer);
        themeSong.start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.quitOption)
		{
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	
    
}
