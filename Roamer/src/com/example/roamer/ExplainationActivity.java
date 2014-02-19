package com.example.roamer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ExplainationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_explaination);
		
		ImageButton introButton = (ImageButton) findViewById(R.id.continueButton);
        introButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(ExplainationActivity.this,CreateAccountActivity.class);
                startActivity(i);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explaination, menu);
		return true;
	}

}
