package com.roamer;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.roamer.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.checkinbox.ChatsAndRequestsActivity;
import com.roamer.events.CreateEventActivity;
import com.roamer.events.EventsActivity;
import com.roamer.profilelist.MyRoamersListActivity;
import com.roamer.profilelist.ProfileListActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


// TODO: Auto-generated Javadoc
/**
 * The Class HomeScreenActivity.
 */
public class HomeScreenActivity extends Activity {
	
	/** The p. */
	Point p;
	
	/** The cur location. */
	private String curLocation = "";
	
	/** The username. */
	private String username;
	
	/** The email. */
	private String email;
	
	/** The dialog. */
	private Dialog dialog;
	
	/** The context. */
	private Context context = this;
    
    /** The selected name. */
    private int selectedName;
    
    /** The position. */
    private Spinner position;
    
    /** The location. */
    private TextView location;
    
    /** The locations. */
    private ArrayList<String> locations;
    
    /** The text city. */
    private TextView textCity;
    
    /** The new mail button. */
    private ImageButton newMailButton;
    
    /** The send button. */
    private ImageButton sendButton;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	

    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	SharedPreferences.Editor editor = preferences.edit();
	    editor.putInt("typeCheck",0);
	    editor.putInt("timeCheck",0);
	    editor.putInt("dateCheck",0);
	    
		editor.putString("location", "");
		editor.putLong("eventType", 0);
		editor.putLong("eventTime", 0);
		editor.putLong("eventDay", 1);
		editor.putLong("eventMonth", 1);
		editor.putLong("eventYear", 2014);
		editor.putString("eventComment", "");
	    editor.commit();
	    
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        try {
			getCurrentLocation();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        location = (TextView) findViewById(R.id.textCurLocations);
        textCity = (TextView) findViewById(R.id.textProfileTravel);
        
        if (curLocation.equals("")){
        	location.setText("None Selected");
        }
        if (!curLocation.equals("")){
        	location.setText(curLocation);
        }
        
        
        sendButton = (ImageButton) findViewById(R.id.checkInboxButton);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HomeScreenActivity.this,ChatsAndRequestsActivity.class);
                startActivity(i);
            		  
            }
        });
        
        newMailButton = (ImageButton) findViewById(R.id.imageButtonNewMail);
        newMailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HomeScreenActivity.this,ChatsAndRequestsActivity.class);
                startActivity(i);
            		  
            }
        });
        
        //check to see if there are pending requests
        checkForRequests();
        
        ImageButton findButton = (ImageButton) findViewById(R.id.findRoamers);
        findButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HomeScreenActivity.this,ProfileListActivity.class);
                startActivity(i);
            		  
            }
        });
        
        ImageButton setLocationButton = (ImageButton) findViewById(R.id.getMyRoamersButton);
        setLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HomeScreenActivity.this,MyRoamersListActivity.class);
                startActivity(i);
            		  
            }
        }); 
        
        ImageButton inboxButton = (ImageButton) findViewById(R.id.checkEventsButton);
        inboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HomeScreenActivity.this,EventsActivity.class);
                startActivity(i);
            		  
            }
        });
        
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(HomeScreenActivity.this,SettingsActivity.class);
                startActivity(i);
            		  
            }
        });
        
        ImageButton postButton = (ImageButton) findViewById(R.id.imageButtonPost);
        postButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(HomeScreenActivity.this,CreateEventActivity.class);
                startActivity(i);
            		  
            }
        });
        
        ImageButton exitButton = (ImageButton) findViewById(R.id.imageLogOut);
        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HomeScreenActivity.this,LoginActivity.class);
                startActivity(i);
            		  
            }
        });
        
        //ImageButton locationButton = (ImageButton) findViewById(R.id.imageButtonChangeLocation);
        textCity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.change_location_dialog);
    			dialog.setTitle("Select your city!");

    			dialog.show();
    			
    			populateCities(dialog);
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageStartMessage);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    	            	
    					try {
							setCity(selectedName);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					try {
							getCurrentLocation();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					location.setText(curLocation);
    					    					
    					dialog.dismiss(); 		  
    				}
    			});

            }
        });
        
    }
    
    /**
     * Gets the current location.
     *
     * @return the current location
     * @throws ParseException the parse exception
     */
    public void getCurrentLocation() throws ParseException{
    	
    	locations = new ArrayList();
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Cities");
    	query.whereNotEqualTo("Code", -1);

    	try {
			List<ParseObject> eventList = query.find();
			
			int i = 0;
			while (i < eventList.size()){
				locations.add(eventList.get(i).getString("Name"));
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
    	cur.moveToFirst();

    	int index, indexEmail;
    	index = cur.getColumnIndex("CurrentLocation");
    	indexEmail = cur.getColumnIndex("Username");
    	int indexUsername = cur.getColumnIndex("Email");
    	
    	username = cur.getString(indexEmail);
    	email = cur.getString(indexUsername);
    	int locNum = cur.getInt(index);
    	
    	if (locations.size() > 0){
    		curLocation = locations.get(locNum);
    	}
    		
    	//Add tables for MyRoamers
    			query = ParseQuery.getQuery("Roamer");
    	       	query.whereEqualTo("Email", email);
    	       	
    	       	System.out.println("Email is: "+email);
    	       	
    	       	ParseObject Roamer = null;
    	       	try{
    	       	Roamer = query.getFirst();
    	       	}
    	       	catch (NullPointerException e ) {
    	       		
    	       	}
    	       		
    	       	  
    	       	    if (Roamer == null) {
    	       	      Log.d("score", "The getFirst request failed.");
    	       	    } else {
    	       	    	JSONArray roamerList = Roamer.getJSONArray("MyRoamers");
	    				
	    				int i = 0;
	    				
	    				if (roamerList != null){
	    					
	    					while (i < roamerList.length()){
	    			        	try {
									myDB.execSQL("CREATE TABLE IF NOT EXISTS "
									          + roamerList.getString(i)
									          + " (Field1 VARCHAR,Field2 VARCHAR);");
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	    			        	i++;
	    					}
	    					myDB.close();
	    				}
    	       	    }
    			
    }
     
    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Populate cities.
     *
     * @param dialog the dialog
     */
    public void populateCities(Dialog dialog){
    	
        
        position = (Spinner) dialog.findViewById(R.id.spinnerSelectRoamer);
         
       	final MyData items1[] = new MyData[locations.size()];

       	//Populate cities in spinner
       	int i = 0;
       	
       	while (i < items1.length){
       	
       		items1[i] = new MyData(locations.get(i),"Value2");
       		i++;
       	}
      	
           ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
                   android.R.layout.simple_spinner_item, items1);
           adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           position.setAdapter(adapter1);
           
           position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               public void onItemSelected(AdapterView<?> parent, View view,
                       int position, long id) {
            	   
            	   ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                   MyData d = items1[position];

                   //Get selected value of key 
                   String value = d.getValue();
                   String key = d.getSpinnerText();
                   selectedName = position;
               }

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    			}
           });
            
        }
    
    /**
     * Sets the city.
     *
     * @param name the new city
     * @throws ParseException the parse exception
     */
    public void setCity(int name) throws ParseException{
    	
    	final int name1 = name;
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	ContentValues args = new ContentValues();
    	args.put("CurrentLocation",name1);
    	
    	myDB.update("MyCred", args, "rowid" + "=" + 1, null);
    	
    	//Update database with current location
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
    	query.whereEqualTo("Email", email);
    	
    	query.getFirstInBackground(new GetCallback<ParseObject>() {
   		  public void done(ParseObject Roamer, ParseException e) {
   		    if (Roamer == null) {
   		    	Log.d("score", "Error: " + e.getMessage()); 

   		    } else {
   		    	Roamer.put("CurrentLocation",name1); 
   		    	
   			    Roamer.saveInBackground();
   		    }
   		  }
   		});
    	myDB.close();	
    }
    
    /**
     * The Class MyData.
     */
    class MyData {
        
        /**
         * Instantiates a new my data.
         *
         * @param spinnerText the spinner text
         * @param value the value
         */
        public MyData(String spinnerText, String value) {
            this.spinnerText = spinnerText;
            this.value = value;
        }

        /**
         * Gets the spinner text.
         *
         * @return the spinner text
         */
        public String getSpinnerText() {
            return spinnerText;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return spinnerText;
        }

        /** The spinner text. */
        String spinnerText;
        
        /** The value. */
        String value;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
	public void onBackPressed()
    {
    	//Do nothing
	}
    
    /**
     * Check for requests.
     */
    public void checkForRequests(){
    	//Update database with current location
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
    	query.whereEqualTo("Email", email);
    	
    	query.getFirstInBackground(new GetCallback<ParseObject>() {
   		  public void done(ParseObject Roamer, ParseException e) {
   		    if (Roamer == null) {
   		    	Log.d("score", "Error: " + e.getMessage()); 

   		    } else {
   		    	
   		    	JSONArray roamerList = Roamer.getJSONArray("Requests");
   		    	if (roamerList != null && roamerList.length() != 0){
   	   			    sendButton.setVisibility(View.INVISIBLE);
   	   			    newMailButton.setVisibility(View.VISIBLE);
   		    	}
   		    	if (roamerList != null && roamerList.length() == 0){
   	   			    sendButton.setVisibility(View.VISIBLE);
   	   			    newMailButton.setVisibility(View.INVISIBLE);
   		    	}
   		    	if (Roamer.getBoolean("newMessage") == true){
   		    		sendButton.setVisibility(View.VISIBLE);
   	   			    newMailButton.setVisibility(View.INVISIBLE);
   		    	}
   		    	if (Roamer.getBoolean("newMessage") == false){
   		    		sendButton.setVisibility(View.VISIBLE);
   	   			    newMailButton.setVisibility(View.INVISIBLE);
   		    	}
   		    }
   		  }
   		});
    }
      
}