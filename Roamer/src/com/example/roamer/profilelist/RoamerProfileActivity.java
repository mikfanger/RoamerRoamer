package com.example.roamer.profilelist;

import java.io.IOException;
import java.io.InputStream;

import com.example.roamer.ConvertCode;
import com.example.roamer.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roamer_profile);
		
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		Cursor c = myDB.rawQuery("SELECT * FROM " + "TempRoamer" , null);
	   	c.moveToFirst();
	   	
	   	 int C1 = c.getColumnIndex("Pic");
		 int C2 = c.getColumnIndex("Username");
		 int C3 = c.getColumnIndex("Loc");
		 int C4 = c.getColumnIndex("Sex");
		 int C5 = c.getColumnIndex("Travel");
		 int C6 = c.getColumnIndex("Industry");
		 int C7 = c.getColumnIndex("Hotel");
		 int C8 = c.getColumnIndex("Air");
		 int C10 = c.getColumnIndex("Job");
		 int C11 = c.getColumnIndex("Start");
		 
		 byte[] tempPic = c.getBlob(C1);
		 String tempName = c.getString(C2);
		 String tempSex = ConvertCode.converSex(c.getInt(C4));
		 String tempTravel = ConvertCode.convertTravel(c.getInt(C5));
		 String tempIndustry = ConvertCode.convertIndustry(c.getInt(C6));
		 String tempHotel = ConvertCode.convertHotel(c.getInt(C7));
		 String tempAir = ConvertCode.convertAirline(c.getInt(C8));
		 String tempJob = ConvertCode.convertJob(c.getInt(C10));
		 String tempStart = c.getString(C11);
		 String tempLoc = ConvertCode.convertLocation(C3);
	   	
	   	TextView nameView = (TextView) findViewById(R.id.textProfileName);
	   	TextView sexView = (TextView) findViewById(R.id.textProfileSex);
	   	ImageView picView = (ImageView) findViewById(R.id.imageProfilePicture);
	   	TextView dateView = (TextView) findViewById(R.id.textRoamerDate);
	   	//TextView industryView = (TextView) findViewById(R.id.textProfileIndustry);
	   	TextView travelView = (TextView) findViewById(R.id.textProfileTravel);
	   	TextView hotelView = (TextView) findViewById(R.id.textProfileHotel);
	   	TextView jobView = (TextView) findViewById(R.id.textProfileJob);
	   	TextView airView = (TextView) findViewById(R.id.textProfileAir);
	   	TextView locView = (TextView) findViewById(R.id.textProfileJob);
	   	
	   	
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
        }
		
		 ImageButton backButton = (ImageButton) findViewById(R.id.imageButtonOut);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(RoamerProfileActivity.this,MyRoamersListActivity.class);
	                startActivity(i);
	            		  
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
