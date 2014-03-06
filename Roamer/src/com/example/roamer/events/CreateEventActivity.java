package com.example.roamer.events;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import graphics.HelloGoogleMaps;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateEventActivity extends Activity {

	final Context context = this;
	public int day;
	public int month;
	public String type;
	public String location = "anywhere";
	public String time;
	public String blurb;
	public String host;
	public String pic;
	public String date;
	public EditText blurbText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		 blurbText = (EditText)findViewById(R.id.editText1);
		 ImageButton postButton = (ImageButton) findViewById(R.id.imageButtonPostEvent);
	        postButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	//Add to events
	            	DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
	            	
	            	SimpleDateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
	            	String formattedDate = sdf.format(Calendar.getInstance().getTime());
	    	        
	    	        day = datePicker.getDayOfMonth();
	    	        month = datePicker.getMonth();
	            	date = Integer.toString(day) +"/"+ Integer.toString(month)+"/"+formattedDate;
	            	blurb = blurbText.getText().toString();
	            	addToEvents(host, type, time, date, location, blurb, pic);
	            	
	            	//set  on screen message
	            	Toast.makeText(getApplicationContext(), "Event Posted!", Toast.LENGTH_LONG).show();
	            	
	            	finish();
	            	Intent i=new Intent(CreateEventActivity.this,HomeScreenActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	        ImageButton backButton = (ImageButton) findViewById(R.id.imageBackFromCreate);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	finish();
	            	Intent i=new Intent(CreateEventActivity.this,HomeScreenActivity.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	       Button mapButton = (Button) findViewById(R.id.buttonSelectFromMap);
	        mapButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(CreateEventActivity.this,HelloGoogleMaps.class);
	                startActivity(i);
	            		  
	            }
	        });
	        
	        //Type of Event
	        Spinner position = (Spinner) findViewById(R.id.spinnerCreateNewType);
	        //Prepar adapter 
	        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
	        final MyData items1[] = new MyData[6];
	        items1[0] = new MyData("At Airport", "value1");
	        items1[1] = new MyData("Concert/Festival", "value2");
	        items1[2] = new MyData("Dinner/Meal", "value3");
	        items1[3] = new MyData("Drinks", "value4");
	        items1[4] = new MyData("Professional/Seminar", "value5");
	        items1[5] = new MyData("Sporting Event", "value6");
	        ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
	                android.R.layout.simple_spinner_item, items1);
	        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        position.setAdapter(adapter1);
	        
	        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view,
	                    int position, long id) {
	                MyData d = items1[position];

	                //Get selected value of key 
	                String value = d.getValue();
	                String key = d.getSpinnerText();
	                
	                type = key;
	            }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
	        });
	        
	        //Type of Event
	        Spinner position1 = (Spinner) findViewById(R.id.spinnerCreateTime);
	        //Prepare adapter 
	        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
	        final MyData items2[] = new MyData[3];
	        items2[0] = new MyData("Mid-day", "value1");
	        items2[1] = new MyData("Evening", "value2");
	        items2[2] = new MyData("Night", "value3");
	        ArrayAdapter<MyData> adapter2 = new ArrayAdapter<MyData>(this,
	                android.R.layout.simple_spinner_item, items2);
	        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        position1.setAdapter(adapter2);
	        
	        position1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view,
	                    int position, long id) {
	                MyData d = items1[position];

	                //Get selected value of key 
	                String value = d.getValue();
	                String key = d.getSpinnerText();
	                
	                time = key;
	            }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
	        });
	        
	        
	}
	
	class MyData {
        public MyData(String spinnerText, String value) {
            this.spinnerText = spinnerText;
            this.value = value;
        }

        public String getSpinnerText() {
            return spinnerText;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return spinnerText;
        }

        String spinnerText;
        String value;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_event, menu);
		return true;
	}
	
	//Add event to myevents table and allevents table
    public void addToEvents(String host, String type, String time1, String date, String location, String desc, String image){
    	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	 
     	//Update count of events in Credentials
     	ContentValues args = new ContentValues();
     	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
     	c.moveToFirst();
     	int index = c.getColumnIndex("CountM");
     	args.put("CountM",c.getInt(index)+1);
     	myDB.update("MyCred", args, "rowid"+"="+1, null);
     	
     	int index1 = c.getColumnIndex("Username");
     	int index2 = c.getColumnIndex("Pic");
     	
     	pic = c.getString(index2);
     	host = c.getString(index1);
    	 
    	myDB.execSQL("INSERT INTO "
			       + "MyEvents "
			       + "(Type,Location,Time,Date,Host,HostPic,Blurb,Attend) "
			       + "VALUES ('"+host+"','"+image+"','"+time1+"','"+type+"','"+date+"','"+desc+"','"+location+"','"+"1"+"');");
    
    	/*
    	myDB.execSQL("INSERT INTO "
			       + "AllEvents "
			       + "(Type,Location,Time,Date,Host,HostPic,Blurb,Attend) "
			       + "VALUES ('"+type+"','"+location+"','"+time1+"','"+date+"','"+host+"','"+image+"','"+desc+"','"+"1"+"');");
    	*/

    	
    	myDB.close();
    }

}
