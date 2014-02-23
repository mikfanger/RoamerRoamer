package com.example.roamer.profilelist;

import java.io.IOException;
import java.io.InputStream;

import com.example.roamer.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RoamerProfileShortActivity extends Activity {

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roamer_profile_short);
		
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		Cursor c = myDB.rawQuery("SELECT * FROM " + "TempRoamer" , null);
	   	c.moveToFirst();
	   	
	   	int picDex= c.getColumnIndex("Pic");
	   	int nameDex= c.getColumnIndex("Name");
	   	int locDex= c.getColumnIndex("Loc");
	   	
	   	final String picString = c.getString(picDex);
	   	final String nameString = c.getString(nameDex);
	   	final String locString = c.getString(locDex);
	   	
	   	TextView nameView = (TextView) findViewById(R.id.textProfileName);
	   	TextView locView = (TextView) findViewById(R.id.textView3);
	   	ImageView picView = (ImageView) findViewById(R.id.imageProfilePicture);
	   	
	   	System.out.println("Picture location is: "+picString);
	   	nameView.setText(nameString);
	   	locView.setText(locString);
	    InputStream ims = null;
        try {
            ims = this.getAssets().open(locString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(ims, null);
        // set image to ImageView
        picView.setImageDrawable(d);
	   	
		
		 ImageButton backButton = (ImageButton) findViewById(R.id.imageButtonOut);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(RoamerProfileShortActivity.this,ProfileListActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	        ImageButton addButton = (ImageButton) findViewById(R.id.imageAddRoamer);
	        addButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	//Add Roamer to myroamers
	            	addRoamer(picString,nameString,locString);
	            	
	            	//Move back to global roamers list
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
	
	public void addRoamer(String icon, String name, String location){
	   	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
	   	 
	   	myDB.execSQL("INSERT INTO "
				       + "MyRoamers "
				       + "(Pic,Name,Loc) "
				       + "VALUES ('"+icon+"','"+name+"','"+location+"');");
	   	
	   	
	   	//Update count of events in Credentials
	   	ContentValues args = new ContentValues();
	   	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
	   	c.moveToFirst();
	   	int index = c.getColumnIndex("CountR");
	   	args.put("CountR",c.getInt(index)+1);
	   	myDB.update("MyCred", args, "rowid"+"="+1, null);
	   	
	   	myDB.delete("TempRoamer", null, null);
	   	
	   	myDB.close();
	   }

}
