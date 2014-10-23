package com.roamer.events;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.roamer.ConvertCode;
import com.roamer.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.HomeScreenActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
 
public class MyEvents extends Activity {
	
	 ImageView imageView;
	 
     TextView textViewHost;
	 TextView textViewEventType;
     TextView textViewDate;
     TextView textViewAttend;
     TextView textViewLocation;
     TextView textViewDesc;
     TextView textViewTime;
     int count;
     ArrayList<ItemMyEvents> eventsArray;
     
     private int day;
     private int month;
     private int year;
     private String parseEventId;
     private String startingEventId = "";
     
	final Context context = this;
	
	ListView listView;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
 
        TextView textview = new TextView(this);
        textview.setText("This is the list of your events");
        setContentView(R.layout.my_events_list);

        listView = (ListView) findViewById(R.id.listView);
        
        try {
 
			loadArray();
		} catch (ParseException | JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	
                // When clicked, show a dialog with event information
            	final Dialog dialog = new Dialog(context);
            	
            	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            	
    			dialog.setContentView(R.layout.activity_total_event);
    			dialog.setTitle("My Event");
    			
    			ImageButton imageAttend = (ImageButton) dialog.findViewById(R.id.imageButtonJoin);
    			imageAttend.setVisibility(View.INVISIBLE);
    			
            	//Populate dialog with allevent information
            	 imageView = (ImageView) dialog.findViewById(R.id.imageChildImage);
                 textViewHost = (TextView) dialog.findViewById(R.id.textChildHost);
                 
                 textViewEventType = (TextView) dialog.findViewById(R.id.textChildType);
                 textViewDate = (TextView) dialog.findViewById(R.id.textChildDate);
                 textViewAttend = (TextView) dialog.findViewById(R.id.textChildAttend);
                 textViewLocation = (TextView) dialog.findViewById(R.id.textChildLocation);
                 textViewDesc = (TextView) dialog.findViewById(R.id.textChildDesc);    
                 textViewTime = (TextView) dialog.findViewById(R.id.textChildTime);
                 
                 String correctString = ModelMyEvents.GetbyId(position+1).Description.replaceAll("&amp&", "'");
                 String correctStringPlace = ModelMyEvents.GetbyId(position+1).Location.replaceAll("&amp&", "'");
                 textViewHost.setText(ModelMyEvents.GetbyId(position+1).Host);
                 textViewTime.setText(ModelMyEvents.GetbyId(position+1).Time);
                 textViewDesc.setText(correctString);
                 textViewAttend.setText(ModelMyEvents.GetbyId(position+1).Attend);
                 textViewDate.setText(ModelMyEvents.GetbyId(position+1).Date);
                 //imageView.setBackgroundResource(Model.GetbyId(position+1).IconFile);
                 textViewLocation.setText(correctStringPlace);
                 textViewEventType.setText(ModelMyEvents.GetbyId(position+1).EventType);
            	 final String myId = ModelMyEvents.GetbyId(position+1).EventId;
            	 byte[] picByte = ModelMyEvents.GetbyId(position+1).IconFile;
            	 
            	 if(picByte != null){
                 	Bitmap bmp = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
             	    imageView.setBackgroundResource(0);
             	    imageView.setImageBitmap(bmp);
                 }
                 
                 if(picByte == null){
                 	InputStream ims = null;
                     try {
                         ims = context.getAssets().open("default_userpic.png");
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     // load image as Drawable
                     Drawable d = Drawable.createFromStream(ims, null);
                     // set image to ImageView
                     imageView.setImageDrawable(d);
                 }
                 
           
    			dialog.show();
    			
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageBackFromMyEvent);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					dialog.dismiss();
    				}
    			});
    			
    			ImageButton unButton = (ImageButton) dialog.findViewById(R.id.imageButtonUnattendEvent);
    			// if button is clicked, close the custom dialog
    			unButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					
    					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
    					    @Override
    					    public void onClick(DialogInterface dialog, int which) {
    					        switch (which){
    					        case DialogInterface.BUTTON_POSITIVE:
    					            
    					        	try {
    									removeEvent(myId);
    									dialog.dismiss();
    								} catch (ParseException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								} catch (JSONException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								}
    		    					
    					        	
    					            break;

    					        case DialogInterface.BUTTON_NEGATIVE:
    					            //No button clicked
    					            break;
    					        }
    					    }
    					};

    					AlertDialog.Builder builder = new AlertDialog.Builder(context);
    					builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
    					    .setNegativeButton("No", dialogClickListener).show();
    				}
    			});

            }

            public void onNothingSelected(AdapterView<?> parent){
			}
          });
    }
    
    public void removeEvent(String eventId) throws ParseException, JSONException{
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   
    	//Get current username
    	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
    	c.moveToFirst();
    	int indexUser = c.getColumnIndex("Username");
    	String userName = c.getString(indexUser);
    	
    	myDB.close();
    	
    	//Remove from Parse Event
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
       	query.whereEqualTo("objectId", eventId);
       	ParseObject Event = query.getFirst();
       	
       	JSONArray roamerList = Event.getJSONArray("Attendees");
       	int newIndex = 0;
       	
       	ArrayList newAttend = new ArrayList<String>();
       	
       	while(newIndex < roamerList.length()){
       		if(roamerList.get(newIndex).toString().equals(userName)){
       			//nothing
       		}
       		else{
       			newAttend.add(roamerList.get(newIndex).toString());
       		}
       		newIndex++;
       	}
       	
       	Event.put("Attendees", newAttend);
       	Event.save();
       	
        //Remove from Parse MyEvents
    	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Roamer");
       	query1.whereEqualTo("Username", userName);
       	ParseObject Roamer = query1.getFirst();
       	
       	JSONArray eventList = Roamer.getJSONArray("MyEvents");
       	int eventIndex = 0;
       	
       	ArrayList myEvents = new ArrayList<String>();
       	
       	while(eventIndex < eventList.length()){
       		if(eventList.get(eventIndex).toString().equals(eventId)){
       			//nothing
       		}
       		else{
       			myEvents.add(eventList.get(eventIndex).toString());
       		}
       		eventIndex++;
       	}
       	
       	//Add back to Parse
       	Roamer.put("MyEvents", myEvents);
       	Roamer.save();
       	
    	
    	
    	
    	 loadArray();
         
         ModelMyEvents.LoadModel(eventsArray);
         
         listView = (ListView) findViewById(R.id.listView);
         String[] ids = new String[ModelMyEvents.ItemsMyEvents.size()];
         for (int i= 0; i < ids.length; i++){

             ids[i] = Integer.toString(i+1);
         }

         ItemAdapterMyEvents adapter = new ItemAdapterMyEvents(this,R.layout.row, ids);
         listView.setAdapter(adapter);
    	
    }
    
    public void loadArray() throws ParseException, JSONException{
    	final SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid "+"= "+1, null);
    	cur.moveToFirst();
    	int index = cur.getColumnIndex("Username"); 
    	String myName = cur.getString(index);
    	
    	myDB.close();
    	
    	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Roamer");
      	query1.whereEqualTo("Username", myName);
      	
      	ParseObject object = query1.getFirst();
      	JSONArray roamerList = object.getJSONArray("MyEvents");
      	
      	ArrayList<String> eventsList = new ArrayList();
		
		int i = 0;
		System.out.println("My Event List = "+roamerList);
		
		
		while (roamerList != null && i < roamerList.length()){
			eventsList.add(roamerList.get(i).toString());
			i++;
		}
		
		Date dateToday = new Date(System.currentTimeMillis());
		
		System.out.println("About to search for My Events!  Events List new = "+eventsList);
      	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
    	query.whereContainedIn("objectId", eventsList);
    	query.whereGreaterThan("Date", dateToday);
    	query.findInBackground(new FindCallback<ParseObject>() {
    	    public void done(List<ParseObject> eventList, ParseException e) {
    	        if (e == null) {
    	        	
    	        	eventsArray = new ArrayList<ItemMyEvents>();
    	        	System.out.println("Found some in MyEvents!"+eventsArray);
    	        	
    	        	int typeNow;
		        	int location;
		        	int timeNow;
		        	int attend;
		        	String place;
		        	String desc;
		        	String host;
		        	Date date;
		        	byte[] pic = null;
		        	String eventId;
		        	String timeString = "";

    	    			int i = 0;	
    	    			
    	    			if (eventList != null && eventList.size() != 0 ){
    	    				typeNow = eventList.get(i).getInt("Type");
    			        	host = eventList.get(i).getString("Host");
    			        	location = eventList.get(i).getInt("Location");
    			        	timeNow = eventList.get(i).getInt("Time");
    			        	desc = eventList.get(i).getString("Desc").replace("*/", "'");
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
    			        	catch (NullPointerException e1) {
    			        		InputStream ims = null;
    			                try {
    			                    ims = context.getAssets().open("default_userpic.png");
    			                } catch (IOException e2) {
    			                    e1.printStackTrace();
    			                }
    			                // load image as Drawable
    			                Drawable d = Drawable.createFromStream(ims, null);
    			                // set image to ImageView
    			                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

    			                ByteArrayOutputStream out = new ByteArrayOutputStream();
    			                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    			                pic= out.toByteArray(); 
    			        	}
    			        	timeString = ConvertCode.convertTime(timeNow);
    			        	
    			        	day = date.getDate();
    			        	month = date.getMonth();
    			        	year = date.getYear();
    			        	String fullDate = Integer.toString(month+1)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
    			        	
    			        	//Check that the date of the event is after or equal to today.
    			        	Date currentDate = new Date(System.currentTimeMillis());
    			        	
    			        	int currentDay = currentDate.getDate();
    			        	currentDate.setDate(currentDay-1);
    			        	int dateCompare = date.compareTo(currentDate);

    			        	if (dateCompare > 0 || dateCompare == 0){
    			        		
    			        		System.out.println("Event is: "+eventList.get(i).getString("Place"));
    			        		System.out.println("Event date is: "+eventList.get(i).getDate("Date"));

    			        	 		eventsArray.add(new ItemMyEvents(i+1, 
    					    				pic, 
    					    				fullDate, 
    					    				ConvertCode.convertType(typeNow), 
    					    				host, 
    					    				Integer.toString(attend),
    					    				desc,
    					    				place,
    					    				eventId,
    					    				timeString));   		
    			        	}
    			        	i++;
    			        	while(i < eventList.size()){
        	    				typeNow = eventList.get(i).getInt("Type");
        			        	host = eventList.get(i).getString("Host");
        			        	location = eventList.get(i).getInt("Location");
        			        	timeNow = eventList.get(i).getInt("Time");
        			        	desc = eventList.get(i).getString("Desc").replace("*/", "'");
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
        			        	catch (NullPointerException e1) {
        			        		InputStream ims = null;
        			                try {
        			                    ims = context.getAssets().open("default_userpic.png");
        			                } catch (IOException e2) {
        			                    e1.printStackTrace();
        			                }
        			                // load image as Drawable
        			                Drawable d = Drawable.createFromStream(ims, null);
        			                // set image to ImageView
        			                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

        			                ByteArrayOutputStream out = new ByteArrayOutputStream();
        			                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        			                pic= out.toByteArray(); 
        			        	}
        			        	timeString = "";
        			        	timeString = ConvertCode.convertTime(timeNow);
        			        	
        			        	day = date.getDate();
        			        	month = date.getMonth();
        			        	year = date.getYear();
        			        	fullDate = Integer.toString(month+1)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
        			        	
        			        	//Check that the date of the event is after or equal to today.
        			        	currentDate = new Date(System.currentTimeMillis());
        			        	
        			        	currentDay = currentDate.getDate();
        			        	currentDate.setDate(currentDay-1);
        			        	dateCompare = date.compareTo(currentDate);

        			        	if (dateCompare > 0 || dateCompare == 0){
        			        		System.out.println("Event is: "+eventList.get(i).getString("Place"));
        			        		System.out.println("Event date is: "+eventList.get(i).getDate("Date"));
        			        		eventsArray.add(new ItemMyEvents(i+1, 
    					    				pic, 
    					    				fullDate, 
    					    				ConvertCode.convertType(typeNow), 
    					    				host, 
    					    				Integer.toString(attend),
    					    				desc,
    					    				place,
    					    				eventId,
    					    				timeString));   		
        			        	}
        			        	i++;
       
        	    			}
    	    			}
    	    			
    	    		
    	            //Check if there are any events, if so load them
    	    		System.out.println("Finished with loading events");
    	            ModelMyEvents.LoadModel(eventsArray);
    	            
    	            System.out.println(eventsArray);
    	            
    	            String[] ids = new String[ModelMyEvents.ItemsMyEvents.size()];
    	            for (i= 0; i < ids.length; i++){

    	                ids[i] = Integer.toString(i+1);
    	            }

    	            ItemAdapterMyEvents adapter = new ItemAdapterMyEvents(context,R.layout.row, ids);
    	            listView.setAdapter(adapter);
    	        	
    	        } else {
    	            System.out.println("No events in My Events!");
    	        }
    	    }
    	});
    		

    }
    
    @Override
    public void onBackPressed() 
    {
    	 Intent i=new Intent(MyEvents.this,HomeScreenActivity.class);
        startActivity(i);
    }
}