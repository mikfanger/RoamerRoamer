package com.example.roamer.profilelist;

import com.example.roamer.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class RoamerProfileShortActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roamer_profile_short);
		
		 ImageButton backButton = (ImageButton) findViewById(R.id.imageOutOfProfile);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(RoamerProfileShortActivity.this,ProfileListActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.roamer_profile_short, menu);
		return true;
	}

}
