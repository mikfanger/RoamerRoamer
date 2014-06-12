package com.example.roamer;

import com.example.roamer.checkinbox.ChatsAndRequestsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
		
		// Call user data from database
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred ", null);
  	  	c.moveToFirst();
  	  	int Column1 = c.getColumnIndex("Email");
  	    int Column2 = c.getColumnIndex("Username");
  	  	
		email = c.getString(Column1);
		user = c.getString(Column2);
		// Set data on screen to match user preferences
		TextView emailAddress = (TextView) findViewById(R.id.currentEmail);
		emailAddress.setText(email);
		TextView userName = (TextView) findViewById(R.id.usernameProf);
		userName.setText(user);
		
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
	
	@Override
	public void onBackPressed() 
	{
		 Intent i=new Intent(SettingsActivity.this,HomeScreenActivity.class);
	    startActivity(i);
	}

}
