package com.example.roamer.events;

import java.util.Calendar;

import graphics.FlyOutContainer;

import com.example.roamer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
     
     String newHost;
     String newType;
     String newDate;
     String newAttend;
     String newLocation;
     String newDesc;
     String newImage;
     
     FlyOutContainer root;
     
     //Look at what we have done!
     
	final Context context = this;
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.events_list, null);
		
		this.setContentView(root);
        
        Model.LoadModel();
        
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
                 final String hostImage = Model.GetbyId(position+1).IconFile;
                 textViewLocation.setText(Model.GetbyId(position+1).Location);
                 textViewEventType.setText(Model.GetbyId(position+1).EventType);
            	

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
    					newImage = hostImage;
    					
    					addToMyEvents(newHost, newType, newDate, newAttend, newLocation, newDesc, newImage);
    					
    					//Add this event to Calendar
    					Calendar beginTime = Calendar.getInstance();
    				
    					beginTime.set(2014, 5, 19, 7, 30);
    					Calendar endTime = Calendar.getInstance();
    					endTime.set(2014, 5, 19, 8, 30);
    					Intent intent = new Intent(Intent.ACTION_INSERT)
    					    .setData(Events.CONTENT_URI)
    					    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
    					    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
    					    .putExtra(Events.TITLE, newType)
    					    .putExtra(Events.DESCRIPTION, newDesc)
    					    .putExtra(Events.EVENT_LOCATION, newLocation)
    					    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
    					    .putExtra(Intent.EXTRA_EMAIL, "michael@vdiagnostics.com");
    					startActivity(intent);
    					
    					dialog.dismiss();
    					
    					Toast.makeText(getApplicationContext(), "Event added to Calendar!",
    							   Toast.LENGTH_LONG).show();
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
    public void addToMyEvents(String host, String type, String date, String attend, String location, String desc, String image){
    	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	 
    	myDB.execSQL("INSERT INTO "
			       + "MyEvents "
			       + "(Type,Location,Date,Host,HostPic,Blurb,Attend) "
			       + "VALUES ('"+type+"','"+location+"','"+date+"','"+host+"','"+image+"','"+desc+"','"+attend+"');");
    	
    	//Update count of events in Credentials
    	ContentValues args = new ContentValues();
    	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
    	c.moveToFirst();
    	int index = c.getColumnIndex("CountM");
    	args.put("CountM",c.getInt(index)+1);
    	myDB.update("MyCred", args, "rowid"+"="+1, null);
    	
    	myDB.close();
    }
}