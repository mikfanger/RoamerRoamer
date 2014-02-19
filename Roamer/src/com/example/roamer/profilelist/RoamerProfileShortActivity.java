package com.example.roamer.profilelist;

import com.example.roamer.R;
import com.example.roamer.R.layout;
import com.example.roamer.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class RoamerProfileShortActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roamer_profile_short);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.roamer_profile_short, menu);
		return true;
	}

}
