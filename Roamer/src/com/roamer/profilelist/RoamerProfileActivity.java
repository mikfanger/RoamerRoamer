package com.roamer.profilelist;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.roamer.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.ConvertCode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RoamerProfileActivity extends Activity {
	
	private ImageView largeImage;
	private String tempName = "";
	private String myName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roamer_profile);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
		Cursor cName = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
    	cName.moveToFirst();
    	int index = cName.getColumnIndex("Username");
    	myName = cName.getString(index);
    	
		Cursor c = myDB.rawQuery("SELECT * FROM " + "TempRoamer" , null);
	   	c.moveToFirst();
	   	
	   	 int C1 = c.getColumnIndex("Pic");
		 int C2 = c.getColumnIndex("Username");
		 int C3 = c.getColumnIndex("origin");
		 int C4 = c.getColumnIndex("Sex");
		 int C5 = c.getColumnIndex("Travel");
		 int C6 = c.getColumnIndex("Industry");
		 int C7 = c.getColumnIndex("Hotel");
		 int C8 = c.getColumnIndex("Air");
		 int C10 = c.getColumnIndex("Job");
		 int C11 = c.getColumnIndex("Start");
		 
		 byte[] tempPic = c.getBlob(C1);
		 tempName = c.getString(C2);
		 String tempSex = ConvertCode.converSex(c.getInt(C4));
		 String tempTravel = ConvertCode.convertTravel(c.getInt(C5));
		 String tempIndustry = ConvertCode.convertIndustry(c.getInt(C6));
		 String tempHotel = ConvertCode.convertHotel(c.getInt(C7));
		 String tempAir = ConvertCode.convertAirline(c.getInt(C8));
		 String tempJob = ConvertCode.convertJob(c.getInt(C10));
		 String tempStart = c.getString(C11);
		 String tempLoc = c.getString(C3);
	   	
	   	TextView nameView = (TextView) findViewById(R.id.textProfileName);
	   	TextView sexView = (TextView) findViewById(R.id.textProfileSex);
	   	ImageView picView = (ImageView) findViewById(R.id.imageProfilePicture);
	   	largeImage = (ImageView) findViewById(R.id.imageButtonLargePicture3);
	   	TextView dateView = (TextView) findViewById(R.id.textRoamerDate);
	   	//TextView industryView = (TextView) findViewById(R.id.textProfileIndustry);
	   	TextView travelView = (TextView) findViewById(R.id.textProfileTravel);
	   	TextView hotelView = (TextView) findViewById(R.id.textProfileHotel);
	   	TextView jobView = (TextView) findViewById(R.id.textProfileJob);
	   	TextView airView = (TextView) findViewById(R.id.textProfileAir);
	   	TextView locView = (TextView) findViewById(R.id.textProfileLocation);
	   	
	   	
	   	nameView.setText(tempName);
	   	sexView.setText(tempSex);
	   	dateView.setText(tempStart);
	   	travelView.setText(tempTravel);
	   	hotelView.setText(tempHotel);
	   	airView.setText(tempAir);
	   	locView.setText(tempLoc);
	   	jobView.setText(tempJob);
	   	
	   	

	   	// get image
	   	if(tempPic != null){
        	Bitmap bmp = BitmapFactory.decodeByteArray(tempPic, 0, tempPic.length);
    	    picView.setBackgroundResource(0);
    	    picView.setImageBitmap(bmp);
    	    
    	    largeImage.setBackgroundResource(0);
    	    largeImage.setImageBitmap(bmp);
        }
        if(tempPic == null){
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
            largeImage.setImageDrawable(d);
        }
		
		 ImageButton backButton = (ImageButton) findViewById(R.id.imageButtonOut);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	finish();
	            	Intent i=new Intent(RoamerProfileActivity.this,MyRoamersListActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	        ImageButton removeButton = (ImageButton) findViewById(R.id.imageRemoveRoamer);
	        removeButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	//Remove myself from their list
	            	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
	            	
	               	query.whereEqualTo("Username", tempName);
	               	
	               	try {
						final ParseObject Roamer = query.getFirst();
						
						ArrayList<String> newArray = new ArrayList();
						newArray.add(myName);
						
						Roamer.removeAll("MyRoamers", newArray);
						Roamer.save();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	               	
	               	//Remove them from my list
	               	query.whereEqualTo("Username", myName);
	               	
	               	try {
						final ParseObject Roamer2 = query.getFirst();
						
						ArrayList<String> values = new ArrayList();
						values.add(tempName);
						
						Roamer2.removeAll("MyRoamers", values);
						Roamer2.save();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	            	Intent i=new Intent(RoamerProfileActivity.this,MyRoamersListActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	        picView.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	               largeImage.setVisibility(View.VISIBLE);
	               largeImage.bringToFront();
	               
	            }
	        });
	        
	        largeImage.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	
	            	if(largeImage.getVisibility() == View.VISIBLE){
	            		largeImage.setVisibility(View.INVISIBLE);
	            	}
	               
	            }
	        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.roamer_profile, menu);
		return true;
	}

}
