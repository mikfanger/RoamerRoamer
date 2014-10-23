package com.roamer.events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import graphics.HelloGoogleMaps;

import com.roamer.R;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.ConvertCode;
import com.roamer.HomeScreenActivity;

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
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEventActivity extends Activity {

	final Context context = this;
	public int day;
	public int month;
	public int type;
	public String location = "anywhere";
	public int time;
	public String blurb;
	public String host;
	public byte[] pic;
	public String date;
	public EditText blurbText;
	public TextView eventLocationPost;
	public int currentLocation;
	private SharedPreferences preferences;
	
	private long defaultType;
	private long defaultTime;
	private long defaultDay;
	private long defaultMonth;
	private long defaultYear;
	private String defaultLocation;
	private String defaultComment;
	private DatePicker datePicker;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		 blurbText = (EditText)findViewById(R.id.editText2);
		 eventLocationPost = (TextView)findViewById(R.id.textEventLocationPost);
     	 datePicker = (DatePicker) findViewById(R.id.datePicker1);

		 preferences = PreferenceManager.getDefaultSharedPreferences(this);
		 location = preferences.getString("location","");
		 
		 
	     defaultComment = preferences.getString("eventComment", ""); 
	     defaultDay = preferences.getLong("eventDay", 1); 
	     defaultMonth = preferences.getLong("eventMonth", 1); 
	     defaultYear = preferences.getLong("eventYear", 2014); 
	     defaultType = preferences.getLong("eventType", 0); 
	     defaultTime = preferences.getLong("eventTime", 0); 
	     
	     
	     blurbText.setText(defaultComment);
	     eventLocationPost.setText(location);
     	 //datePicker.updateDate((int)defaultYear, (int)defaultDay, (int)defaultMonth);
     	 
     	 
     	 //initialize datepicker
	     OnDateChangedListener onDateChangedListener = null;
	     datePicker.init((int)defaultYear, (int)defaultMonth, (int)defaultDay, onDateChangedListener );
		 
		 
		 ImageButton postButton = (ImageButton) findViewById(R.id.imageButtonPostEvent);
	        postButton.setOnClickListener(new OnClickListener() {
	            @SuppressLint("NewApi")
				@Override
	            public void onClick(View v) {
	            	
	            	//Add to events
	            	finish();

	            	//Save to Calendar
	            	SimpleDateFormat sdf = new SimpleDateFormat("yy"); // Just the year, with 2 digits
	            	String formattedDate = sdf.format(Calendar.getInstance().getTime());
	    	        
	            	Date eventDate = new Date();
	    	        day = datePicker.getDayOfMonth();
	    	        month = datePicker.getMonth();

	    	        Calendar c = Calendar.getInstance();
	    	        
	    	        eventDate.setDate(day);
	    	        eventDate.setMonth(month+1);
	    	        eventDate.setYear(c.get(Calendar.YEAR)-1900);
	    	        
	    	        
	    	        //Check with the time of posting
	    	        if (((month - c.get(Calendar.MONTH) )< 6) && (location != "anywhere")){
	    	        	
	    	        	date = Integer.toString(month) +"/"+ Integer.toString(day)+"/"+c.get(Calendar.YEAR);
	    	        	
	    	        	System.out.println("Date of event is: "+date);
		            	blurb = blurbText.getText().toString();
		            	addToEvents(host, type, time, date, location, blurb, pic);
		            	
		            	//Get Date
		            	int year = 0;
		    	        Calendar yeartime = Calendar.getInstance();
		    	        Calendar beginTime = Calendar.getInstance();
		    	        
		    	        if(month - c.get(Calendar.MONTH) < 0){
		    	        	 year = yeartime.get(Calendar.YEAR)+1;
		    	        }
		    	        if(month - c.get(Calendar.MONTH) >= 0){
		    	        	 year = yeartime.get(Calendar.YEAR);
		    	        }
						

						//Get time
						int startHour = 0;
						int startMinute = 0;
						int finishMinute = 0;
						int finishHour = 0;
						if (time == 2){
							startHour = 11;
							startMinute = 30;	
							finishHour = 13;
							finishMinute = 30;
						}
						if (time == 3){
							startHour = 17;
							startMinute = 30;	
							finishHour = 19;
							finishMinute = 30;
						}
						if (time == 4){
							startHour = 20;
							startMinute = 30;	
							finishHour = 22;
							finishMinute = 30;
						}
						if (time == 1){
							startHour = 6;
							startMinute = 30;	
							finishHour = 11;
							finishMinute = 30;
						}
						if (time == 5){
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
    					    .putExtra(Events.TITLE, ConvertCode.convertType(type))
    					    .putExtra(Events.DESCRIPTION, blurb)
    					    .putExtra(Events.EVENT_LOCATION, location)
    					    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
    					
    					startActivity(intent);

		            	//set  on screen message and quit to home screen
		            	
    					Toast.makeText(getApplicationContext(), "Event Posted!", Toast.LENGTH_LONG).show();
		            	eventLocationPost.setText("");
		            	SharedPreferences.Editor editor2 = preferences.edit();
		                editor2.putString("location","");
		                editor2.commit();
		                
	    	        }
	    	        if(month - c.get(Calendar.MONTH) > 6){
	    	        	Toast.makeText(getApplicationContext(), "Cannot post more than 6 months out!", Toast.LENGTH_LONG).show();
	    	        }
	    	        if(location == "anywhere"){
	    	        	Toast.makeText(getApplicationContext(), "Must pick a location!!", Toast.LENGTH_LONG).show();
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
	            	
	    	        //Save to preferences
	    	        SharedPreferences.Editor editor = preferences.edit();
	    	        
	    	        editor.putLong("eventDay", datePicker.getDayOfMonth());
	    	        editor.putLong("eventMonth", datePicker.getMonth());
	    	        editor.commit();
	    	        
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
	                R.layout.spinner_item, items1);
	        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        position.setAdapter(adapter1);
	        
	        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view,
	                    int position, long id) {
	                MyData d = items1[position];

	                //Get selected value of key 
	                String value = d.getValue();
	                String key = d.getSpinnerText();
	                
	                //Save to preferences
	    	        SharedPreferences.Editor editor = preferences.edit();
	    	        editor.putLong("eventType", position);
	    	        editor.commit();
	                
	                type = position+1;
	            }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
	        });
	        position.setSelection((int)defaultType);
	        
	        //Time of Event
	        Spinner position1 = (Spinner) findViewById(R.id.spinnerCreateTime);
	        //Prepare adapter 
	        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
	        final MyData items2[] = new MyData[5];
	        items2[0] = new MyData("Morning:    (6AM - 11:30AM)", "value1");
	        items2[1] = new MyData("Mid-day:    (11:30AM - 1:30PM)", "value2");
	        items2[2] = new MyData("Evening:    (5:30PM - 7:30PM)", "value3");
	        items2[3] = new MyData("Night:      (8:30PM - 10:30PM)", "value4");
	        items2[4] = new MyData("Late Night: (11:30PM - 2:00AM)", "value5");
	        ArrayAdapter<MyData> adapter2 = new ArrayAdapter<MyData>(this,
	        		R.layout.spinner_item, items2);
	        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        position1.setAdapter(adapter2);
	        
	        position1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view,
	                    int position, long id) {
	                MyData d = items2[position];

	                //Get selected value of key 
	                String value = d.getValue();
	                String[] splitter = d.getSpinnerText().toString().split(":");
	                
	                //Save to preferences
	    	        SharedPreferences.Editor editor = preferences.edit();
	    	        editor.putLong("eventTime", position);
	    	        editor.commit();
	    	        
	                time = position+1;
	                
	            }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
	        });
	        position1.setSelection((int)defaultTime);	        
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
    public void addToEvents(String hosttemp, int type, int time1, String date, String location, String desc, byte[] image){
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
     	currentLocation = c.getInt(index3);
    	 
     	String locationModified = location.replace("'", "*/");
     	
    	String sql =   "INSERT INTO MyEvents(Host,HostPic,Time,Type,Date,Blurb,Location,Attend) VALUES(?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,host);
        insertStmt.bindBlob(2,pic);
        insertStmt.bindString(3, ConvertCode.convertTime(time1));
        insertStmt.bindString(4, ConvertCode.convertType(type));
        insertStmt.bindString(5, date);
        insertStmt.bindString(6, desc);
        insertStmt.bindString(7, locationModified);
        insertStmt.bindLong(8,1);

        insertStmt.executeInsert();

    	
    	myDB.close();
    }
    
    public void addToParse(int timetemp,int typetemp,String blurbtemp,Date datetemp,String locationtemp,String hosttemp) throws ParseException{
    	
    	ParseObject event = new ParseObject("Event");
    	ParseFile file = new ParseFile(host+".png",pic);
    	file.save();
    	
    	int typeInt = 0;
    	int timeInt = 0;
    	ArrayList<String> attendees = new ArrayList();
    	attendees.add(host);
    	
    	//Get type int
    	typeInt = typetemp;
    	
    	//Get time int
    	timeInt = timetemp;
    
    	
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
    	
    	ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access while disabling public write access.
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		event.setACL(defaultACL);
    	
    	event.save();
    	
    	String objectId = event.getObjectId();

    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
    	query.whereEqualTo("Username", hosttemp);
    	
    	ParseObject roamer = query.getFirst();
    	
    	roamer.addUnique("MyEvents", objectId);
	    roamer.saveInBackground();
    	
    }
    
    @Override
	public void onBackPressed() 
	{
    	SharedPreferences.Editor editor = preferences.edit();
    	editor.putString("location", "");
		editor.putLong("eventType", 0);
		editor.putLong("eventTime", 0);
		editor.putLong("eventDay", 1);
		editor.putLong("eventMonth", 1);
		editor.putLong("eventYear", 2014);
		editor.putString("eventComment", "");
	    editor.commit();
	    
		Intent i=new Intent(CreateEventActivity.this,HomeScreenActivity.class);
	    startActivity(i);
	}

}
