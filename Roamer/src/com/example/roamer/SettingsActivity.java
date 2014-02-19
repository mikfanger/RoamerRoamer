package com.example.roamer;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettingsActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		String email;
		String user;
		
		// Call user data from shared preferences
		email = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("RoamerEmail","");
		user = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("RoamerUsername","");
		
		// Set data on screen to match user preferences
		TextView emailAddress = (TextView) findViewById(R.id.currentEmail);
		emailAddress.setText(email);
		TextView userName = (TextView) findViewById(R.id.usernameProf);
		userName.setText(user);
		
		 ImageButton backButton = (ImageButton) findViewById(R.id.backFromProfile);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(SettingsActivity.this,HomeScreenActivity.class);
	                startActivity(i);
	            }
	        });
	        
	        ImageButton nextButton = (ImageButton) findViewById(R.id.nextProfile1);
	        nextButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(SettingsActivity.this,SettingsActivity2.class);
	                startActivity(i);
	            }
	        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
