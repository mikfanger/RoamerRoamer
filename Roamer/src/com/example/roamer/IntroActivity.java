package com.example.roamer;

import com.example.roamer.appengine.GCMIntentService;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;

public class IntroActivity extends Activity { 

	enum State {
	    REGISTERED, REGISTERING, UNREGISTERED, UNREGISTERING
	  }

	  private State curState = State.UNREGISTERED;
	  
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
    	
    	updateState(State.REGISTERING);
    	
    	final String chatTable = "ChatTable";
    	final String myRoamersTable = "MyRoamers";
    	final String myCredTable = "MyCred";
    	final String myLocationTable = "MyLocation";
    	final String myEventsTable = "MyEvents";
    	
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.intro_screen);
        
        SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
        
        /* Create a chat Table in the Database. */
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
          + chatTable
          + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , Field1 VARCHAR,Field2 INT(1));");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myRoamersTable
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , Pic VARCHAR, Name VARCHAR, Loc VARCHAR);");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myLocationTable
                + " (Field1 VARCHAR, Field2 VARCHAR, Field3 VARCHAR);");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myCredTable
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Field1 VARCHAR, Field2 VARCHAR, Field3 VARCHAR, Field4 INT(1), CountM INT(1), CountR INT(1));");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myEventsTable
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , Type VARCHAR, Location VARCHAR, Time VARCHAR, Host VARCHAR, HostPic VARCHAR, Blurb VARCHAR, Attend VARCHAR);");
        
        myDB.delete(myCredTable, null, null);
        
        
       myDB.close();
        
        
       //registerWithEngine();
        
        ImageButton introButton = (ImageButton) findViewById(R.id.StartRoamerButton);
        introButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	           	
            	
            	Intent i=new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void registerWithEngine(){
    	GCMIntentService.register(getApplicationContext());
    	updateState(State.REGISTERED);
    	
    }
    
    private void updateState(State newState) {      
        curState = newState;
      }

    
}
