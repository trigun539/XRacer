package com.mikeedwin.xracer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_layout);
		
		super.onCreate(savedInstanceState);
	}
	
	

}
