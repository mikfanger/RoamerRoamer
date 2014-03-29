package com.example.roamer;


import com.example.roamer.checkinbox.ChatsAndRequestsActivity;
import com.example.roamer.events.CreateEventActivity;
import com.example.roamer.events.EventsActivity;
import com.example.roamer.profilelist.MyRoamersListActivity;
import com.example.roamer.profilelist.ProfileListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


public class HomeScreenActivity extends Activity {
	
	Point p;
	private String curLocation;
	private Dialog dialog;
	private Context context = this;
    private String selectedName;
    private Spinner position;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        getCurrentLocation();
        location = (TextView) findViewById(R.id.textCurLocation);
        location.setText(curLocation);
        
        ImageButton sendButton = (ImageButton) findViewById(R.id.checkInboxButton);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(HomeScreenActivity.this,ChatsAndRequestsActivity.class);
                startActivity(i);
            		  
            }
        });
        
        ImageButton findButton = (ImageButton) findViewById(R.id.findRoamers);
        findButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(HomeScreenActivity.this,ProfileListActivity.class);
                startActivity(i);
            		  
            }
        });
        
        ImageButton setLocationButton = (ImageButton) findViewById(R.id.getMyRoamersButton);
        setLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(HomeScreenActivity.this,MyRoamersListActivity.class);
                startActivity(i);
            		  
            }
        }); 
        
        ImageButton inboxButton = (ImageButton) findViewById(R.id.checkEventsButton);
        inboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
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
        
        ImageButton locationButton = (ImageButton) findViewById(R.id.imageButtonChangeLocation);
        locationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.change_location_dialog);
    			dialog.setTitle("Select Roamer");

    			dialog.show();
    			
    			populateCities(dialog);
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageStartMessage);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    	            	
    					setCity(selectedName);
    					getCurrentLocation();
    					location.setText(curLocation);
    					    					
    					dialog.dismiss(); 		  
    				}
    			});

            }
        });
        
    }
    
    public void getCurrentLocation(){
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	curLocation = cur.getString(index);
    	
    	myDB.close();
    }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void populateCities(Dialog dialog){
    	
        
        position = (Spinner) dialog.findViewById(R.id.spinnerSelectRoamer);
         
       	final MyData items1[] = new MyData[20];

       	//Populate cities in spinner
       	items1[0] = new MyData("None Selected","Value1");
       	items1[1] = new MyData("Boston","Value2");
       	items1[2] = new MyData("San Francisco","Value3");
       	items1[3] = new MyData("Las Vegas","Value4");
       	items1[4] = new MyData("New York","Value5");
       	items1[5] = new MyData("Los Angeles","Value6");
       	items1[6] = new MyData("Houston","Value7");
       	items1[7] = new MyData("Philadelphia","Value8");
       	items1[8] = new MyData("Phoenix","Value9");
       	items1[9] = new MyData("San Antonio","Value10");
       	items1[10] = new MyData("San Diego","Value111");
       	items1[11] = new MyData("Dallas","Value12");
       	items1[12] = new MyData("San Jose","Value13");
       	items1[13] = new MyData("Austin","Value14");
       	items1[14] = new MyData("Jacksonville","Value15");
       	items1[15] = new MyData("Indianapolis","Value16");
       	items1[16] = new MyData("Seattle","Value17");
       	items1[17] = new MyData("Denver","Value18");
       	items1[18] = new MyData("Washington DC","Value19");
       	items1[19] = new MyData("Chicago","Value20");
       	
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
                   selectedName = key;
               }

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    			}
           });
            
        }
    public void setCity(String name){
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	ContentValues args = new ContentValues();
    	args.put("CurrentLocation",name);
    	
    	myDB.update("MyCred", args, "rowid" + "=" + 1, null);
    	
    	
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
    
    
   
}