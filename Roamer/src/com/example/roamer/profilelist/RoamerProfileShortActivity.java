package com.example.roamer.profilelist;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.roamer.ConvertCode;
import com.example.roamer.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RoamerProfileShortActivity extends Activity {
	
	private String nameString = "";
	private String myName = "";
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
	   	c.moveToFirst();
	   	int nameInt = c.getColumnIndex("Username");
	   	myName = c.getString(nameInt);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roamer_profile_short);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		c = myDB.rawQuery("SELECT * FROM " + "TempRoamer" , null);
	   	c.moveToFirst();
	   	
	   	int picDex= c.getColumnIndex("Pic");
	   	int nameDex= c.getColumnIndex("Username");
	   	int sexDex= c.getColumnIndex("Sex");
	   	int locDex= c.getColumnIndex("Loc");
	   	int dateDex = c.getColumnIndex("Start");
	   	int hotelDex = c.getColumnIndex("Hotel");
	   	int industryDex = c.getColumnIndex("Industry");
	   	int airDex = c.getColumnIndex("Air");
	   	int travelDex = c.getColumnIndex("Travel");
	   	int jobDex = c.getColumnIndex("Job");
	   	
	   	
	   	final byte[] picByte = c.getBlob(picDex);
	   	nameString = c.getString(nameDex);
	   	final int sexInt = c.getInt(sexDex);
	   	final String locString = c.getString(locDex);
	   	final String dateString = c.getString(dateDex);
	   	final int hotelString = c.getInt(hotelDex);
	   	final int industryString = c.getInt(industryDex);
	   	final int airString = c.getInt(airDex);
	   	final int travelString = c.getInt(travelDex);
	   	final int jobString = c.getInt(jobDex);
	   	final String sex = ConvertCode.converSex(sexInt);

	   	
	   	TextView nameView = (TextView) findViewById(R.id.textProfileName);
	   	TextView sexView = (TextView) findViewById(R.id.textProfileSex);
	   	ImageView picView = (ImageView) findViewById(R.id.imageProfilePicture);
	   	TextView dateView = (TextView) findViewById(R.id.textRoamerDate);
	   	
	   	nameView.setText(nameString);
	   	sexView.setText(sex);
	   	dateView.setText(dateString);
	   	

	   	// get image
	   	if(picByte != null){
        	Bitmap bmp = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
    	    picView.setBackgroundResource(0);
    	    picView.setImageBitmap(bmp);
        }
        if(picByte == null){
        	InputStream ims = null;
            try {
                ims = this.getAssets().open("default_userpic.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            picView.setImageDrawable(d);
        }
	   	
		
		 ImageButton backButton = (ImageButton) findViewById(R.id.imageButtonOut);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	finish();
	            	Intent i=new Intent(RoamerProfileShortActivity.this,ProfileListActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	        ImageButton addButton = (ImageButton) findViewById(R.id.imageAddRoamer);
	        addButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	if (checkIfAlreadyFriends(nameString)){
	            		//Add Roamer to myroamers
		            	addRoamer(picByte,nameString,sexInt,locString,dateString,
		            			hotelString,travelString,jobString,industryString,airString);
		            	
		            	System.out.println("Roamer to be added is: "+nameString);
		            	//Send request to roamer
		            	try {
							sendRequest(nameString);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            	
		            	//Move back to global roamers list
		            	Toast.makeText(getApplicationContext(), "Request sent to Roamer!",
		            			   Toast.LENGTH_LONG).show();
		            	finish();
		            	Intent i=new Intent(RoamerProfileShortActivity.this,ProfileListActivity.class);
		                startActivity(i);
	            	}
	            	else{
	            		Toast.makeText(getApplicationContext(), "Already connected to Roamer!",
		            			   Toast.LENGTH_LONG).show();
	            	}
	            	
	            		  
	            }
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.roamer_profile_short, menu);
		return true;
	}
	
	public void addRoamer(byte[] icon, String name, int sex, String loc, String date,
			int hotel, int travel, int job, int industry, int air){
	   	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
	   	 
	   	String sql                      =   "INSERT INTO MyRoamers (Pic,Username,Sex,Loc,Start,Hotel,Travel,Job,Industry,Air) VALUES(?,?,?,?,?,?,?,?,?,?)";
	    SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
	    insertStmt.clearBindings();
	    insertStmt.bindBlob(1,icon);
	    insertStmt.bindString(2,name);
	    insertStmt.bindLong(3, sex);
	    insertStmt.bindString(4, loc);
	    insertStmt.bindString(5, date);
	    insertStmt.bindLong(6, hotel);
	    insertStmt.bindLong(7, travel);
	    insertStmt.bindLong(8, job);
	    insertStmt.bindLong(9, industry);
	    insertStmt.bindLong(10, air);
	    
	    insertStmt.executeInsert();
	   	
	   	//Update count of events in Credentials
	   	ContentValues args = new ContentValues();
	   	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
	   	c.moveToFirst();
	   	int index = c.getColumnIndex("CountR");
	   	System.out.println("Current Count of my Roamers is: "+c.getInt(index));
	   	args.put("CountR",c.getInt(index)+1);
	   	myDB.update("MyCred", args, "rowid"+"="+1, null);
	   	
	   	myDB.delete("TempRoamer", null, null);
	   	
	   	myDB.close();
	   }
	
	public void sendRequest(String name) throws ParseException, JSONException{
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
       	query.whereEqualTo("Username", name);
       	
       	final ParseObject Roamer = query.getFirst();
       	
       	JSONArray roamerList = Roamer.getJSONArray("Requests");
       	ArrayList<String> newList = new ArrayList();
       	
       	System.out.println("Array before is: " + roamerList);
       	if (roamerList != null){
       		
       		int i = 0;
       		while (i < roamerList.length()){
       			newList.add(roamerList.getString(i));
       		}
       		//roamerList.put(name);
       		newList.add(myName);
       	}
       	else{
       		newList.add(myName);
       	}
       	System.out.println("Array after is: " + newList);
       	Roamer.put("Requests", newList);
       	Roamer.save();
				
	}
	
	public boolean checkIfAlreadyFriends(String name){
		boolean notexists = true;
		
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		Cursor c = myDB.rawQuery("SELECT * FROM " + "MyRoamers" , null);
	   	
		if (c != null && c.getCount() > 0){
			
			c.moveToFirst();
		   	
		   	int nameDex= c.getColumnIndex("Username");
		   	
		   	
		   	if(name.equals(c.getString(nameDex))){
		   		notexists = false;
		   		
		   		while(c.moveToNext()){
		   			nameDex= c.getColumnIndex("Username");
		   		   	
		   		   	if(name.equals(c.getString(nameDex))){
		   		   		notexists = false;
		   		   	}
		   		}
		   	}
		}
		
		
		
		return notexists;
		
	}

}
