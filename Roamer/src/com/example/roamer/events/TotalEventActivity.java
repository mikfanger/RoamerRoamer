package com.example.roamer.events;

import com.example.roamer.R;
import com.example.roamer.R.layout;
import com.example.roamer.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TotalEventActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_total_event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.total_event, menu);
		return true;
	}

}
