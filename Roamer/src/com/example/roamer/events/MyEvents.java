package com.example.roamer.events;

import java.util.ArrayList;

import com.example.roamer.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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
     int count;
     ArrayList<ItemMyEvents> loadArray;
     
	final Context context = this;
	
	ListView listView;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
 
        TextView textview = new TextView(this);
        textview.setText("This is the list of your events");
        setContentView(R.layout.my_events_list);
        listView = (ListView) findViewById(R.id.listView);
        
        loadArray();
        
        //Check if there are any events, if so load them
        if(count > 0)
        {
        ModelMyEvents.LoadModel(loadArray);
        
        String[] ids = new String[ModelMyEvents.ItemsMyEvents.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        ItemAdapterMyEvents adapter = new ItemAdapterMyEvents(this,R.layout.row, ids);
        listView.setAdapter(adapter);
        
        }
               
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	
              // When clicked, show a dialog with event information
            	final Dialog dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.activity_total_event);
    			dialog.setTitle("My Event");
    			
    			ImageButton imageback = (ImageButton) dialog.findViewById(R.id.imageButtonJoin);
    			imageback.setVisibility(View.INVISIBLE);
    			
            	//Populate dialog with allevent information
            	 imageView = (ImageView) dialog.findViewById(R.id.imageChildImage);
                 textViewHost = (TextView) dialog.findViewById(R.id.textChildHost);
                 
                 textViewEventType = (TextView) dialog.findViewById(R.id.textChildType);
                 textViewDate = (TextView) dialog.findViewById(R.id.textChildDate);
                 textViewAttend = (TextView) dialog.findViewById(R.id.textChildAttend);
                 textViewLocation = (TextView) dialog.findViewById(R.id.textChildLocation);
                 textViewDesc = (TextView) dialog.findViewById(R.id.textChildDesc);                 
                 
                 textViewHost.setText(ModelMyEvents.GetbyId(position+1).Host);
                 textViewDesc.setText(ModelMyEvents.GetbyId(position+1).Description);
                 textViewAttend.setText(ModelMyEvents.GetbyId(position+1).Attend);
                 textViewDate.setText(ModelMyEvents.GetbyId(position+1).Date);
                 //imageView.setBackgroundResource(Model.GetbyId(position+1).IconFile);
                 textViewLocation.setText(ModelMyEvents.GetbyId(position+1).Location);
                 textViewEventType.setText(ModelMyEvents.GetbyId(position+1).EventType);
            	 final int myId = ModelMyEvents.GetbyId(position+1).RowId;

    			dialog.show();
    			
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageBackFromMyEvent);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					dialog.dismiss();
    				}
    			});
    			
    			ImageButton unButton = (ImageButton) dialog.findViewById(R.id.imageButtonUnattend);
    			// if button is clicked, close the custom dialog
    			unButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					
    					removeEvent(myId);
    					dialog.dismiss();
    				}
    			});

            }

            public void onNothingSelected(AdapterView<?> parent){
			}
          });
    }
    
    public void removeEvent(int eventId){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	myDB.execSQL("DELETE FROM MyEvents WHERE rowid="+eventId);
    	
    	//Update count of events in Credentials
    	ContentValues args = new ContentValues();
    	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
    	c.moveToFirst();
    	int index = c.getColumnIndex("CountM");
    	args.put("CountM",c.getInt(index)-1);
    	myDB.update("MyCred", args, "rowid"+"="+1, null);
    	
    	myDB.close();
    	
    	 loadArray();
         
         ModelMyEvents.LoadModel(loadArray);
         
         listView = (ListView) findViewById(R.id.listView);
         String[] ids = new String[ModelMyEvents.ItemsMyEvents.size()];
         for (int i= 0; i < ids.length; i++){

             ids[i] = Integer.toString(i+1);
         }

         ItemAdapterMyEvents adapter = new ItemAdapterMyEvents(this,R.layout.row, ids);
         listView.setAdapter(adapter);
    	
    }
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CountM");
    	count = cur.getInt(index);
    	
    	System.out.println("Checking Count of MyEvents");
    	System.out.println("Count of my events is: " + count);
    	
    	if (count > 0) {
    		
    		
    	loadArray = new ArrayList<ItemMyEvents>();
    	int i = 1;

		Cursor c = myDB.rawQuery("SELECT * FROM " + "MyEvents ", null);
		c.moveToFirst();
		
		 int C1 = c.getColumnIndex("Type");
		 int C2 = c.getColumnIndex("Location");
		 int C3 = c.getColumnIndex("Date");
		 int C4 = c.getColumnIndex("Host");
		 int C5 = c.getColumnIndex("HostPic");
		 int C6 = c.getColumnIndex("Blurb");
		 int C7 = c.getColumnIndex("Attend");
		 int C8 = c.getColumnIndex("rowid");
		 
		 System.out.println("value is: " +c.getString(C1));

		 
		 loadArray.add(new ItemMyEvents(i,c.getInt(C8), c.getString(C5), c.getString(C4), c.getString(C3), c.getString(C1), c.getString(C7),c.getString(C2),c.getString(C6)));
		
		while(c.moveToNext()){
			i++;
			
			  C1 = c.getColumnIndex("Type");
			  C2 = c.getColumnIndex("Location");
			  C3 = c.getColumnIndex("Date");
			  C4 = c.getColumnIndex("Host");
			  C5 = c.getColumnIndex("HostPic");
			  C6 = c.getColumnIndex("Blurb");
			  C7 = c.getColumnIndex("Attend");
			  C8 = c.getColumnIndex("rowid");
			 
			 loadArray.add(new ItemMyEvents(i, c.getInt(C8), c.getString(C5), c.getString(C4), c.getString(C3), c.getString(C1), c.getString(C7),c.getString(C2),c.getString(C6)));			
		}
		
		myDB.close();
    }
    	else{
    		 System.out.println("Row Count is: " + 1);
    	}
    }
}