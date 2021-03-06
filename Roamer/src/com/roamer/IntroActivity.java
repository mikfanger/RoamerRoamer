package com.roamer;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import com.roamer.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

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

	  
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
    	

    	ParseAnalytics.trackAppOpened(getIntent());
    	
    	final String chatTable = "ChatTable";
    	final String myRoamersTable = "MyRoamers";
    	final String myCredTable = "MyCred";
    	final String myLocationTable = "MyLocation";
    	final String myEventsTable = "MyEvents";
    	final String tempRoamer = "TempRoamer";
    	
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.intro_screen);
        
        SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
        
        /* Create a chat Table in the Database. */
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
        		+ chatTable
        		+ " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , Field1 VARCHAR, Field2 BLOB, Field3 VARCHAR, Field4 INT(1));");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + tempRoamer
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Email VARCHAR, Password VARCHAR, Username VARCHAR, Pic BLOB, Sex INT(1), Travel INT(2), Industry INT(2), Job INT(2), Hotel INT(2), Air INT(2), Loc VARCHAR, Start VARCHAR, origin VARCHAR);");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myRoamersTable
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Email VARCHAR, Password VARCHAR, Username VARCHAR, Pic BLOB, Sex INT(1), Travel INT(2), Industry INT(2), Job INT(2), Hotel INT(2), Air INT(2), Loc VARCHAR, Start VARCHAR, StartDate VARCHAR);");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myLocationTable
                + " (Field1 VARCHAR, Field2 VARCHAR, Field3 VARCHAR);");
        
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myCredTable
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Sex INT(1), Email VARCHAR, Password VARCHAR, Username VARCHAR, Pic BLOB, Travel INT(2), Industry INT(2), Job INT(2), Hotel INT(2), Air INT(2), Loc INT(2), Start VARCHAR, CurrentLocation INT(2), Save INT(1), CountM INT(3), CountR INT(3), ChatCount INT(2), SentRequests VARCHAR);");
        
        //Add rows if MyCred is a new table
        
        Cursor c = myDB.rawQuery("SELECT  *  FROM " + "MyCred", null);
        
        
        if (c.getCount() < 2){
        	
        	myDB.execSQL("INSERT INTO "
 			       + "MyCred "
 			       + "(Save) "
 			       + "VALUES ("+0+");");
        	
        	myDB.execSQL("INSERT INTO "
  			       + "MyCred "
  			       + "(Save) "
  			       + "VALUES ("+0+");");
        	
        }
        else{
        	//do nothing
        }
        
        Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
    	cur.moveToFirst();

    	int index;
    	index = cur.getColumnIndex("Username");
    	String userName = cur.getString(index);
                
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + myEventsTable
                + " (rowid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , Type VARCHAR, Location VARCHAR, Time VARCHAR, Date VARCHAR, Host VARCHAR, HostPic BLOB, Blurb VARCHAR, Attend VARCHAR, EventId VARCHAR);");
        
        //clear tables
        myDB.delete(tempRoamer, null, null);
        myDB.delete(myRoamersTable, null, null);
        myDB.delete(myEventsTable, null, null);
       
        new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				finish();
            	Intent i=new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(i);
			}
        	
        },5000);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
}
