package com.example.roamer.events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import graphics.HelloGoogleMaps;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
import android.widget.TextView;
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
	public byte[] pic;
	public String date;
	public EditText blurbText;
	public TextView profileJob;
	public int currentLocation;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		 blurbText = (EditText)findViewById(R.id.editText2);
		 profileJob = (TextView)findViewById(R.id.textProfileJob);
		 
		 SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		 location = preferences.getString("location","");
		 System.out.println("Location is: "+location);
		 profileJob.setText(location);
		 
		 ImageButton postButton = (ImageButton) findViewById(R.id.imageButtonPostEvent);
	        postButton.setOnClickListener(new OnClickListener() {
	            @SuppressLint("NewApi")
				@Override
	            public void onClick(View v) {
	            	
	            	//Add to events
	            	DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
	            	
	            	
	            	
	            	//Save to Calendar
	            	SimpleDateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
	            	String formattedDate = sdf.format(Calendar.getInstance().getTime());
	    	        
	            	Date eventDate = new Date();
	    	        day = datePicker.getDayOfMonth();
	    	        month = datePicker.getMonth();
	    	        Calendar c = Calendar.getInstance();
	    	        
	    	        eventDate.setDate(day);
	    	        eventDate.setMonth(month);
	    	        eventDate.setYear(c.get(Calendar.YEAR));
	    	        
	    	        //Check with the time of posting
	    	        if ((month - c.get(Calendar.MONTH) )< 6){
	    	        	
	    	        	date = Integer.toString(day) +"/"+ Integer.toString(month)+"/"+c.get(Calendar.YEAR);
		            	blurb = blurbText.getText().toString();
		            	addToEvents(host, type, time, date, location, blurb, pic);
		            	
		            	  //Get Date
		    	        Calendar yeartime = Calendar.getInstance();
		    	        Calendar beginTime = Calendar.getInstance();
						int year =yeartime.get(Calendar.YEAR);

						System.out.println("Time is: "+time);
						System.out.println("Year is: "+year);
						System.out.println("Month is: "+month);
						
						
						//Get time
						int startHour = 0;
						int startMinute = 0;
						int finishMinute = 0;
						int finishHour = 0;
						if (time.equals("Mid-day")){
							startHour = 11;
							startMinute = 30;	
							finishHour = 13;
							finishMinute = 30;
						}
						if (time.equals("Evening")){
							startHour = 17;
							startMinute = 30;	
							finishHour = 19;
							finishMinute = 30;
						}
						if (time.equals("Night")){
							startHour = 20;
							startMinute = 30;	
							finishHour = 22;
							finishMinute = 30;
						}
						if (time.equals("Morning")){
							startHour = 6;
							startMinute = 30;	
							finishHour = 11;
							finishMinute = 30;
						}
						if (time.equals("Late Night")){
							startHour = 20;
							startMinute = 30;	
							finishHour = 23;
							finishMinute = 59;
						}
						
						//Add to parse
						try {
							addToParse(time,type,blurb,eventDate,location,host);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						beginTime.set(year, month, day, startHour, startMinute);
    					Calendar endTime = Calendar.getInstance();
    					endTime.set(year, month, day, finishHour, finishMinute);
    					Intent intent = new Intent(Intent.ACTION_INSERT)
    					    .setData(Events.CONTENT_URI)
    					    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
    					    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
    					    .putExtra(Events.TITLE, type)
    					    .putExtra(Events.DESCRIPTION, blurb)
    					    .putExtra(Events.EVENT_LOCATION, location)
    					    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
    					startActivity(intent);
						
						Toast.makeText(getApplicationContext(), "Event added to Calendar!",
								   Toast.LENGTH_LONG).show();
		            	
		            	//set  on screen message
		            	Toast.makeText(getApplicationContext(), "Event Posted!", Toast.LENGTH_LONG).show();
		            	
		            	//finish();
		            	//Intent i=new Intent(CreateEventActivity.this,HomeScreenActivity.class);
		               // startActivity(i);
	    	        }
	    	        if(month - c.get(Calendar.MONTH) > 6){
	    	        	Toast.makeText(getApplicationContext(), "Cannot post more than 6 months out!", Toast.LENGTH_LONG).show();
	    	        }
		  
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
	        final MyData items2[] = new MyData[5];
	        items2[0] = new MyData("Morning", "value1");
	        items2[1] = new MyData("Mid-day", "value2");
	        items2[2] = new MyData("Evening", "value3");
	        items2[3] = new MyData("Night", "value4");
	        items2[4] = new MyData("Late Night", "value5");
	        ArrayAdapter<MyData> adapter2 = new ArrayAdapter<MyData>(this,
	                android.R.layout.simple_spinner_item, items2);
	        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        position1.setAdapter(adapter2);
	        
	        position1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view,
	                    int position, long id) {
	                MyData d = items2[position];

	                //Get selected value of key 
	                String value = d.getValue();
	                time = d.getSpinnerText();
	                System.out.println("Spinner is: "+time);
	                
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
    public void addToEvents(String hosttype, String type, String time1, String date, String location, String desc, byte[] image){
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
     	int index3 = c.getColumnIndex("CurrentLocation");
     	
     	pic = c.getBlob(index2);
     	
     	host = c.getString(index1);
     	System.out.println("Host  is: "+host);
     	currentLocation = c.getInt(index3);
    	 
     	String locationModified = location.replace("'", "*/");
     	
    	String sql =   "INSERT INTO MyEvents(Host,HostPic,Time,Type,Date,Blurb,Location,Attend) VALUES(?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,host);
        insertStmt.bindBlob(2,pic);
        insertStmt.bindString(3, time1);
        insertStmt.bindString(4, type);
        insertStmt.bindString(5, date);
        insertStmt.bindString(6, desc);
        insertStmt.bindString(7, locationModified);
        insertStmt.bindLong(8,1);

        insertStmt.executeInsert();

    	
    	myDB.close();
    }
    
    public void addToParse(String timetemp,String typetemp,String blurbtemp,Date datetemp,String locationtemp,String hosttemp) throws ParseException{
    	
    	ParseObject event = new ParseObject("Event");
    	ParseFile file = new ParseFile(host+".png",pic);
    	file.save();
    	
    	int typeInt = 0;
    	int timeInt = 0;
    	ArrayList<String> attendees = new ArrayList();
    	attendees.add(host);
    	if(typetemp.equals("At Airport")){
    		typeInt = 1;
    	}
    	if(typetemp.equals("Concert/Festival")){
    		typeInt = 2;
    	}
    	if(typetemp.equals("Dinner/Meal")){
    		typeInt = 3;
    	}
    	if(typetemp.equals("Drinks")){
    		typeInt = 4;
    	}
    	if(typetemp.equals("Professional/Seminar")){
    		typeInt = 5;
    	}
    	if(typetemp.equals("Sporting Event")){
    		typeInt = 6;
    	}
    	
    	//Get time int
    	if(timetemp.equals("Morning")){
    		timeInt = 1;
    	}
    	if(timetemp.equals("Mid-day")){
    		timeInt = 2;
    	}
    	if(timetemp.equals("Evening")){
    		timeInt = 3;
    	}
    	if(timetemp.equals("Night")){
    		timeInt = 4;
    	}
    	if(timetemp.equals("Late Night")){
    		timeInt = 5;
    	}
    
    	
    	event.put("Place", locationtemp);
    	event.put("Date", datetemp);
    	event.put("Attend",1);
    	event.put("Desc", blurbtemp);
    	event.put("Type",typeInt );
    	event.put("Attendees", attendees);
    	event.put("Host", hosttemp);
    	event.put("Time", timeInt);
    	event.put("Pic",file);
    	event.put("Location", currentLocation);  	
    	
    	event.saveInBackground();
    	
    }

}
