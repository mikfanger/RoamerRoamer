package com.example.roamer.events;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import graphics.FlyOutContainer;

import com.example.roamer.ConvertCode;
import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
     private View mAllEventsView;
     
     CheckBox checkTime;
     CheckBox checkDate;
     CheckBox checkType;
     
     private Spinner spinnerSortTime;
     private Spinner spinnerSortType;
     
     String newHost;
     String newType;
     String newDate;
     String newAttend;
     String newLocation;
     String newDesc;
     byte[] newImage, hostImage;
     String newEventId;
     String newTime;
    
     
     private Date dateStart;
     private Date dateEnd;
     private int time = 0;
     private int type = 0;
     
     private int day;
     private int month;
     private int year;
     private String parseEventId;

     private SharedPreferences preferences;
     
     
    FlyOutContainer root;
     
    //Look at what we have done!
     
	final Context context = this;
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.events_list, null);
		
		this.setContentView(root);
		
		mAllEventsView = findViewById(R.id.progressBarAllEvents);
		
		dateStart = new Date();
		dateEnd = new Date();
		
		checkDate = (CheckBox) findViewById(R.id.checkBoxDate);
		checkTime = (CheckBox) findViewById(R.id.checkBoxType);
		checkType = (CheckBox) findViewById(R.id.checkBoxTime);
		
		type = preferences.getInt("type",1);;
		time = preferences.getInt("time",1);;
		
		if (preferences.getInt("timeCheck",0) > 0){
			checkTime.setChecked(true);
		}
		if (preferences.getInt("typeCheck",0) > 0){
			checkType.setChecked(true);
		}

		//Bring sort dialog for type if checked
		checkType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					
					final Dialog dialog = new Dialog(context);
	            	
	    			dialog.setContentView(R.layout.sort_type);
	    			dialog.setTitle("Sort by Type");
	    			dialog.show();
	    			
	    			populateSpinnerType(dialog);
	    			
	    			ImageButton backButton = (ImageButton) dialog.findViewById(R.id.imageBackFromSortType);
	    			// if button is clicked, close the custom dialog
	    			backButton.setOnClickListener(new OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					dialog.dismiss();
	    				}
	    			});
				}
				
			}
		});
		
		//Bring dialog for time if checked
		checkTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					
					final Dialog dialog = new Dialog(context);
	            	
	    			dialog.setContentView(R.layout.sort_time);
	    			dialog.setTitle("Sort by Time");
	    			dialog.show();
	    			
	    			populateSpinnerTime(dialog);
	    			ImageButton backButton = (ImageButton) dialog.findViewById(R.id.imageBackFromSortTime);
	    			// if button is clicked, close the custom dialog
	    			backButton.setOnClickListener(new OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					dialog.dismiss();
	    				}
	    			});
				}
				
			}
		});
		
		//Show sort date dialog if date is checked
		checkDate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					
					final Dialog dialog = new Dialog(context);
	            	
	    			dialog.setContentView(R.layout.sort_date);
	    			dialog.setTitle("Select Date");
	    			dialog.show();
	    			
					final DatePicker datePickerStart = (DatePicker) dialog.findViewById(R.id.datePickerStart1);
					final DatePicker datePickerEnd = (DatePicker) dialog.findViewById(R.id.datePickerEnd2);
					
	    			ImageButton backButton = (ImageButton) dialog.findViewById(R.id.imageBackFromSort);
	    			// if button is clicked, close the custom dialog
	    			backButton.setOnClickListener(new OnClickListener() {
	    				@Override
	    				public void onClick(View v) {
	    					
	    					dateStart.setYear(datePickerStart.getYear());
	    					dateStart.setMonth(datePickerStart.getMonth());
	    					dateStart.setDate(datePickerStart.getDayOfMonth());
	    					
	    					dateEnd.setYear(datePickerEnd.getYear());
	    					dateEnd.setMonth(datePickerEnd.getMonth());
	    					dateEnd.setDate(datePickerEnd.getDayOfMonth());
	    					
	    					
	    					dialog.dismiss();
	    				}
	    			});
				}
				
			}
		});
		//show progress spinner
		showProgress(true);
		
		loadArray(type,dateStart,dateEnd,time);
		listView = (ListView) findViewById(R.id.listViewEvents);
		System.out.println("Amount of events is: "+eventsArray.size());
		if (eventsArray.size() > 0){
			
			
			Model.LoadModel(eventsArray);
	        
	        final String[] ids = new String[Model.Items.size()];
	        for (int i= 0; i < ids.length; i++){

	            ids[i] = Integer.toString(i+1);
	        }
	        
	        //hide progress spinner
	        
	        
	        ItemAdapter adapter = new ItemAdapter(this,R.layout.row, ids);
	        //final ItemAdapter adapter1 = new ItemAdapter(this,R.layout.row, ids);
	        listView.setAdapter(adapter);
	        
	        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		showProgress(false);
        
        
        ImageButton sortButton = (ImageButton) findViewById(R.id.sortButton);
        sortButton.bringToFront();
        
        ImageButton sortNowButton = (ImageButton) findViewById(R.id.sortNow);
        sortNowButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
				startActivity(getIntent());
				
				if(checkTime.isChecked()){
					SharedPreferences.Editor editor = preferences.edit();
  			        editor.putInt("timeCheck",1);
  			        editor.commit();
				}
				if(checkType.isChecked()){
					SharedPreferences.Editor editor = preferences.edit();
  			        editor.putInt("typeCheck",1);
  			        editor.commit();
				}

			}
		});
        
           
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
    	 
    	 String my_new_str = desc.replaceAll("'", "*/");
    	 String my_new_str_place = location.replaceAll("'", "*/");
    	 
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
    
    public void loadArray(int sortType, Date sortDateStart, Date sortDateEnd, int sortTime){
    	
    	
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
			int nextEvent = 1;
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
        	
			if(eventList.size()!=0){
				
	        	
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
	        	catch (NullPointerException e) {
	        		InputStream ims = null;
	                try {
	                    ims = context.getAssets().open("default_userpic.png");
	                } catch (IOException e2) {
	                    e.printStackTrace();
	                }
	                // load image as Drawable
	                Drawable d = Drawable.createFromStream(ims, null);
	                // set image to ImageView
	                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
	                pic= out.toByteArray(); 
	        	}
	        	
	        	String timeString = "";
	        	if (timeNow == 1){
	        		timeString = "Mid-Day";
	        	}
	        	if (timeNow == 2){
	        		timeString = "Evening";
	        	}
	        	if (timeNow == 3){
	        		timeString = "Night";
	        	}
	        	
	        	day = date.getDate();
	        	month = date.getMonth();
	        	year = date.getYear();
	        	String fullDate = Integer.toString(month+1)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);

	        	//Check that the date of the event is after or equal to today.
	        	Date currentDate = new Date(System.currentTimeMillis());
	        	int dateCompare = date.compareTo(currentDate);

	        	if (dateCompare > 0 || dateCompare == 0){
	        		if (checkEventBySort(date,typeNow,timeNow) > 0){
	        	 		eventsArray.add(new Item(nextEvent, 
			    				pic, 
			    				host, 
			    				fullDate, 
			    				ConvertCode.convertType(typeNow), 
			    				Integer.toString(attend),
			    				place,
			    				desc,
			    				eventId,
			    				timeString));
	        	 		nextEvent++;
	        		}		    		
	        	}
	        	i++;
	    		
			}
            
        	
    		while (i < (eventList.size())){
    			
    			typeNow = eventList.get(i).getInt("Type");
	        	host = eventList.get(i).getString("Host");
	        	location = eventList.get(i).getInt("Location");
	        	date = eventList.get(i).getDate("Date");
	        	timeNow = eventList.get(i).getInt("Time");
	        	desc = eventList.get(i).getString("Desc").replace("*/", "'");
	        	attend = eventList.get(i).getInt("Attend");
	        	place = eventList.get(i).getString("Place");
	        	eventId = eventList.get(i).getObjectId();
	        	
	        	try {
					pic = eventList.get(i).getParseFile("Pic").getData();
				} 
	        	catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	catch (NullPointerException e) {
	        		InputStream ims = null;
	                try {
	                    ims = context.getAssets().open("default_userpic.png");
	                } catch (IOException e2) {
	                    e.printStackTrace();
	                }
	                // load image as Drawable
	                Drawable d = Drawable.createFromStream(ims, null);
	                // set image to ImageView
	                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
	                pic= out.toByteArray(); 
	        	}
	        	
	        	String timeString = ConvertCode.convertTime(timeNow);
	        	
	        	day = date.getDate();
	        	month = date.getMonth();
	        	year = date.getYear();
	        	String fullDate = Integer.toString(month+1)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
	        	System.out.println("fulldate is: "+fullDate);
	        	
	        	//Check that the event is equal to or after the current date
	        	Date currentDate = new Date(System.currentTimeMillis());
	        	int dateCompare = date.compareTo(currentDate);

	        	if (dateCompare > 0 || dateCompare == 0){
	        		if (checkEventBySort(date,typeNow,timeNow) > 0){
	        			eventsArray.add(new Item(nextEvent, 
	    	    				pic, 
	    	    				host, 
	    	    				fullDate, 
	    	    				ConvertCode.convertType(typeNow), 
	    	    				Integer.toString(attend),
	    	    				place,
	    	    				desc,
	    	    				eventId,
	    	    				timeString));
	        			nextEvent++;
	        		}
	        	}
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
    	    	 //JSONArray newJSON = event.getJSONArray("Attendees");
    	    	 
    	    	 event.put("LoginCount",i+1);
    	    	 event.addAllUnique("Attendees", usernameArray);
    		     event.saveInBackground();
    	    }
    	  }
    	});
    	
    	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Roamer");
    	query1.whereEqualTo("Username", usernameArray.get(0));
    	
    	query1.getFirstInBackground(new GetCallback<ParseObject>() {
    	  public void done(ParseObject event, ParseException e) {
    	    if (event == null) {
    	    	 

    	    } else {
    	    	 event.addUnique("MyEvents", parseEventId);
    		     event.saveInBackground();
    	    }
    	  }
    	});
    }
    
    /**
	 * Shows the progress UI and hides the form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int mediumAnimTime = getResources().getInteger(
					android.R.integer.config_mediumAnimTime);

			mAllEventsView.setVisibility(View.VISIBLE);
			mAllEventsView.animate().setDuration(mediumAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mAllEventsView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mAllEventsView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
	
	/*
	public void sort(int time, int date, int type){
		
		
	}
	*/
	
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
	
	//Populate Time Spinner
	public void populateSpinnerTime(Dialog dialog){
		
		spinnerSortTime = (Spinner) dialog.findViewById(R.id.spinnerTimeSort);
        
       	final MyData items1[] = new MyData[5];

       	//Populate times in spinner
       	items1[0] = new MyData("Morning: (6AM - 11:30AM)","Value1");
       	items1[1] = new MyData("Mid-Day: (11:30AM - 1:30PM)","Value2");
       	items1[2] = new MyData("Evening: (5:30PM - 7:30PM)","Value3");
       	items1[3] = new MyData("Night: (8:30PM - 10:30PM)","Value4");
       	items1[4] = new MyData("Late Night: (11:30PM - 2:00AM)","Value5");
      	
           ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
                   android.R.layout.simple_spinner_item, items1);
           adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           spinnerSortTime.setAdapter(adapter1);
           
           spinnerSortTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               public void onItemSelected(AdapterView<?> parent, View view,
                       int position, long id) {
            	   
            	   ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                   MyData d = items1[position];

                   //Get selected value of key 
                   String value = d.getValue();
                   String key = d.getSpinnerText();
                   time = ConvertCode.convertTimeBack(key);
  			        SharedPreferences.Editor editor = preferences.edit();
  			        editor.putInt("time",time);
  			        editor.commit();
               }

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    			}
           });
		
		
	}
	
	//Populate Time Spinner
		public void populateSpinnerType(Dialog dialog){
			
			spinnerSortType = (Spinner) dialog.findViewById(R.id.spinnerTypeSort);
	        
	       	final MyData items2[] = new MyData[6];

	       	//Populate times in spinner
	       	items2[0] = new MyData("At Airport","Value1");
	       	items2[1] = new MyData("Concert/Festival","Value2");
	       	items2[2] = new MyData("Dinner/Meal","Value3");
	       	items2[3] = new MyData("Drinks","Value4");
	       	items2[4] = new MyData("Professional/Seminar","Value5");
	       	items2[5] = new MyData("Sporting Event","Value5");
	      	
	           ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
	                   android.R.layout.simple_spinner_item, items2);
	           adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	           spinnerSortType.setAdapter(adapter1);
	           
	           spinnerSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	               public void onItemSelected(AdapterView<?> parent, View view,
	                       int position, long id) {
	            	   
	            	   ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
	                   MyData d = items2[position];

	                   //Get selected value of key 
	                   String value = d.getValue();
	                   String key = d.getSpinnerText();
	                   type = ConvertCode.convertTypeBack(key); 
   			        SharedPreferences.Editor editor = preferences.edit();
   			        editor.putInt("type",type);
   			        editor.commit();
	               }

	    			@Override
	    			public void onNothingSelected(AdapterView<?> arg0) {
	    			}
	           });
			
			
		}
		
	public int checkEventBySort(Date dateNow, int typeNow, int timeNow){
		int useNow = 1;
		
		//check for checked time
		if (checkTime.isChecked()){
			if (!(timeNow == time)){
				useNow = 0;
			}
		}
		//check for checked type
		if (checkType.isChecked()){
			if (!(typeNow == type)){
				useNow = 0;
			}
		}
		//check for checked date
		if (checkDate.isChecked()){
			if (!(dateNow.compareTo(dateStart) >= 0 && (dateNow.compareTo(dateEnd) <= 0))){
				useNow = 0;
			}
		}
		return useNow;
	}
	@Override
	public void onBackPressed() 
	{
		SharedPreferences.Editor editor = preferences.edit();
	        editor.putInt("typeCheck",0);
	        editor.putInt("timeCheck",0);
	        editor.commit();
		Intent i=new Intent(AllEvents.this,HomeScreenActivity.class);
	    startActivity(i);
	}
}