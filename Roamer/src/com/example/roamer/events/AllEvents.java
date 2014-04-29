package com.example.roamer.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

import graphics.FlyOutContainer;

import com.example.roamer.ConvertCode;
import com.example.roamer.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
@SuppressLint("NewApi")
public class AllEvents extends Activity {
	
	ListView listView;
	
	 ImageView imageView;
	 
     TextView textViewHost;
	 TextView textViewEventType;
     TextView textViewDate;
     TextView textViewAttend;
     TextView textViewLocation;
     TextView textViewDesc;
     TextView textViewTime;
     ArrayList<Item> eventsArray;
     ArrayList<String> usernameArray;
     
     String newHost;
     String newType;
     String newDate;
     String newAttend;
     String newLocation;
     String newDesc;
     byte[] newImage, hostImage;
     String newEventId;
     String newTime;
     
     private int day;
     private int month;
     private int year;
     private String parseEventId;
     
     FlyOutContainer root;
     
     //Look at what we have done!
     
	final Context context = this;
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.events_list, null);
		
		this.setContentView(root);
		
		loadArray();
        
        Model.LoadModel(eventsArray);
        
        listView = (ListView) findViewById(R.id.listViewEvents);
        final String[] ids = new String[Model.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

       final  ItemAdapter adapter = new ItemAdapter(this,R.layout.row, ids);
        listView.setAdapter(adapter);
        
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        ImageButton button = (ImageButton) findViewById(R.id.sortButton);
        button.bringToFront();
           
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	
              // When clicked, show a dialog with event information
            	final Dialog dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.activity_total_event);
    			dialog.setTitle("Event");
    			
    			ImageButton imageUnattend = (ImageButton) dialog.findViewById(R.id.imageButtonUnattendEvent);
    			imageUnattend.setVisibility(View.INVISIBLE);
    			
            	//Populate dialog with allevent information
            	 imageView = (ImageView) dialog.findViewById(R.id.imageChildImage);
                 textViewHost = (TextView) dialog.findViewById(R.id.textChildHost);
                 textViewTime = (TextView) dialog.findViewById(R.id.textChildTime);
                 
                 textViewEventType = (TextView) dialog.findViewById(R.id.textChildType);
                 textViewDate = (TextView) dialog.findViewById(R.id.textChildDate);
                 textViewAttend = (TextView) dialog.findViewById(R.id.textChildAttend);
                 textViewLocation = (TextView) dialog.findViewById(R.id.textChildLocation);
                 textViewDesc = (TextView) dialog.findViewById(R.id.textChildDesc);        
                 
                 System.out.println("Location is: "+Model.GetbyId(position+1).Location);
                 System.out.println("Desc is: "+Model.GetbyId(position+1).Description);
                 
                 textViewHost.setText(Model.GetbyId(position+1).Host);
                 textViewDesc.setText(Model.GetbyId(position+1).Description);
                 textViewAttend.setText(Model.GetbyId(position+1).Attend);
                 textViewDate.setText(Model.GetbyId(position+1).Date);
                 hostImage = Model.GetbyId(position+1).IconFile;
                 textViewLocation.setText(Model.GetbyId(position+1).Location);
                 textViewEventType.setText(Model.GetbyId(position+1).EventType);
                 textViewTime.setText(Model.GetbyId(position+1).Time);
                 parseEventId = Model.GetbyId(position+1).ObjectId;
            	
                 Bitmap bmp = BitmapFactory.decodeByteArray(hostImage, 0, hostImage.length);
         	     imageView.setBackgroundResource(0);
         	     imageView.setImageBitmap(bmp);
           

    			dialog.show();
    			
    			ImageButton backButton = (ImageButton) dialog.findViewById(R.id.imageBackFromMyEvent);
    			// if button is clicked, close the custom dialog
    			backButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					dialog.dismiss();
    				}
    			});
    			
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageButtonJoin);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@SuppressLint("InlinedApi")
					@Override
    				public void onClick(View v) {
    					
    					//Get current data to be loaded into MyEvents
    					
    					newHost = (String) textViewHost.getText();
    					newType = (String) textViewEventType.getText();
    					newDate = (String) textViewDate.getText();
    					newAttend = (String) textViewAttend.getText();
    					newLocation = (String) textViewLocation.getText();
    					newDesc = (String) textViewDesc.getText();
    					newTime = (String) textViewTime.getText();
			
    					addToMyEvents(newHost, newType, newDate, newAttend, newLocation, newDesc, hostImage, newEventId, newTime);
    					
    					updateEventInParse();
    					
    					//Add this event to Calendar
    					
    					//Get Date
    					Calendar beginTime = Calendar.getInstance();
    					List<String> items = Arrays.asList(newDate.split("\\s*/\\s*"));
    					int day = Integer.parseInt(items.get(0));
    					int month = Integer.parseInt(items.get(1));
    					int year = Integer.parseInt(items.get(2));
    					
    					//Get time
    					int startHour = 0;
    					int startMinute = 0;
    					int finishMinute = 0;
    					int finishHour = 0;
    					if (newTime.equals("Mid-day")){
							startHour = 11;
							startMinute = 30;	
							finishHour = 13;
							finishMinute = 30;
						}
						if (newTime.equals("Evening")){
							startHour = 17;
							startMinute = 30;	
							finishHour = 19;
							finishMinute = 30;
						}
						if (newTime.equals("Night")){
							startHour = 20;
							startMinute = 30;	
							finishHour = 22;
							finishMinute = 30;
						}
						if (newTime.equals("Morning")){
							startHour = 6;
							startMinute = 30;	
							finishHour = 11;
							finishMinute = 30;
						}
						if (newTime.equals("Late Night")){
							startHour = 20;
							startMinute = 30;	
							finishHour = 23;
							finishMinute = 59;
						}
    					
    				
    					beginTime.set(year, month, day, startHour, startMinute);
    					Calendar endTime = Calendar.getInstance();
    					endTime.set(year, month, day, finishHour, finishMinute);
    					Intent intent = new Intent(Intent.ACTION_INSERT)
    					    .setData(Events.CONTENT_URI)
    					    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
    					    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
    					    .putExtra(Events.TITLE, newType)
    					    .putExtra(Events.DESCRIPTION, newDesc)
    					    .putExtra(Events.EVENT_LOCATION, newLocation)
    					    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
    					startActivity(intent);
    					
    					Toast.makeText(getApplicationContext(), "Event added to Calendar!",
 							   Toast.LENGTH_LONG).show();
    					dialog.dismiss();
    						
    				}
    			});

            }

            public void onNothingSelected(AdapterView<?> parent){
			}
          });
        
       
    }
    
    public void toggleMenu(View v){
		this.root.toggleMenu();
	}
    
    //Add event to my events table
    public void addToMyEvents(String host, String type, String date, String attend, String location, String desc, byte[] image, String eventId, String time){
    	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	 
    	 String my_new_str = desc.replaceAll("'", "&amp&");
    	 String my_new_str_place = location.replaceAll("'", "&amp&");
    	 
    	String sql =   "INSERT INTO MyEvents(Type,Location,Date,Host,HostPic,Blurb,Attend,EventId,Time) VALUES(?,?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,type);
        insertStmt.bindString(2,my_new_str_place);
        insertStmt.bindString(3, date);
        insertStmt.bindString(4, host);
        insertStmt.bindBlob(5, image);
        insertStmt.bindString(6, my_new_str);
        insertStmt.bindString(7, attend);
        insertStmt.bindString(8, parseEventId);
        insertStmt.bindString(9, time);
        insertStmt.executeInsert();
    	
    	//Update count of events in Credentials
    	ContentValues args = new ContentValues();
    	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
    	c.moveToFirst();
    	int index = c.getColumnIndex("CountM");
    	args.put("CountM",c.getInt(index)+1);
    	myDB.update("MyCred", args, "rowid"+"="+1, null);
    	
    	myDB.close();
    }
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid "+"= "+1, null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	int indexName = cur.getColumnIndex("Username");
    	usernameArray = new ArrayList<String>();
    	
    	usernameArray.add(cur.getString(indexName));
    	int locationInt = cur.getInt(index);
    	eventsArray = new ArrayList<Item>();
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
    	query.whereEqualTo("Location", locationInt);
    	try {
    		
			List<ParseObject> eventList = query.find();
			int i = 0;
        	int type;
        	int location;
        	int time;
        	int attend;
        	String place;
        	String desc;
        	String host;
        	Date date;
        	byte[] pic = null;
        	String eventId;
        	
			if(eventList.size()!=0){
				
	        	
	        	type = eventList.get(i).getInt("Type");
	        	host = eventList.get(i).getString("Host");
	        	location = eventList.get(i).getInt("Location");
	        	time = eventList.get(i).getInt("Time");
	        	desc = eventList.get(i).getString("Desc");
	        	attend = eventList.get(i).getInt("Attend");
	        	place = eventList.get(i).getString("Place");
	        	date = eventList.get(i).getDate("Date");
	        	eventId = eventList.get(i).getObjectId();
	        	
	        	try {
					pic = eventList.get(i).getParseFile("Pic").getData();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
	        	String timeString = "";
	        	if (time == 1){
	        		timeString = "Mid-Day";
	        	}
	        	if (time == 2){
	        		timeString = "Evening";
	        	}
	        	if (time == 3){
	        		timeString = "Night";
	        	}
	        	
	        	day = date.getDay();
	        	month = date.getMonth();
	        	year = date.getYear();
	        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
	        	System.out.println("Date is: "+fullDate);
	        	
	        	
	    		eventsArray.add(new Item(i+1, 
	    				pic, 
	    				host, 
	    				fullDate, 
	    				ConvertCode.convertType(type), 
	    				Integer.toString(attend),
	    				place,
	    				desc,
	    				eventId,
	    				timeString));
	    		i++;
			}
            
        	
    		while (i < (eventList.size())){
    			
    			type = eventList.get(i).getInt("Type");
	        	host = eventList.get(i).getString("Host");
	        	location = eventList.get(i).getInt("Location");
	        	date = eventList.get(i).getDate("Date");
	        	time = eventList.get(i).getInt("Time");
	        	desc = eventList.get(i).getString("Desc");
	        	attend = eventList.get(i).getInt("Attend");
	        	place = eventList.get(i).getString("Place");
	        	eventId = eventList.get(i).getObjectId();
	        	
	        	try {
					pic = eventList.get(i).getParseFile("Pic").getData();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
	        	String timeString = ConvertCode.convertTime(time);
	        	
	        	day = date.getDay();
	        	month = date.getMonth();
	        	year = date.getYear();
	        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
	        	
	        	eventsArray.add(new Item(i+1, 
	    				pic, 
	    				host, 
	    				fullDate, 
	    				ConvertCode.convertType(type), 
	    				Integer.toString(attend),
	    				place,
	    				desc,
	    				eventId,
	    				timeString));
	    		i++;
    		}
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	
    }
    
    public void updateEventInParse(){
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
    	query.whereEqualTo("objectId", parseEventId);
    	
    	query.getFirstInBackground(new GetCallback<ParseObject>() {
    	  public void done(ParseObject event, ParseException e) {
    	    if (event == null) {
    	    	 

    	    } else {
    	    	 int i = event.getInt("Attend");
    	    	 JSONArray newJSON = event.getJSONArray("Attendees");
    	    	 
    	    	 event.put("LoginCount",i+1);
    	    	 event.addAllUnique("Attendees", usernameArray);
    		     event.saveInBackground();
    	    }
    	  }
    	});
    	
    	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Roamer");
    	query1.whereEqualTo("Username", usernameArray.get(1));
    	final ArrayList eventList = new ArrayList<String>();
    	eventList.add(parseEventId);
    	
    	query1.getFirstInBackground(new GetCallback<ParseObject>() {
    	  public void done(ParseObject event, ParseException e) {
    	    if (event == null) {
    	    	 

    	    } else {
    	    	 event.addAllUnique("MyEvents", eventList);
    		     event.saveInBackground();
    	    }
    	  }
    	});
    }
}