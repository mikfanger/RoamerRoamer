package com.example.roamer.events;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.roamer.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
                 
                 String correctString = ModelMyEvents.GetbyId(position+1).Description.replaceAll("&amp&", "'");
                 String correctStringPlace = ModelMyEvents.GetbyId(position+1).Location.replaceAll("&amp&", "'");
                 textViewHost.setText(ModelMyEvents.GetbyId(position+1).Host);
                 textViewDesc.setText(correctString);
                 textViewAttend.setText(ModelMyEvents.GetbyId(position+1).Attend);
                 textViewDate.setText(ModelMyEvents.GetbyId(position+1).Date);
                 //imageView.setBackgroundResource(Model.GetbyId(position+1).IconFile);
                 textViewLocation.setText(correctStringPlace);
                 textViewEventType.setText(ModelMyEvents.GetbyId(position+1).EventType);
            	 final int myId = ModelMyEvents.GetbyId(position+1).Id;
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
		 int C9 = c.getColumnIndex("EventId");
		 
		 System.out.println("value is: " +c.getString(C1));

		 String correctLocation = c.getString(C2).replace("&amp&", "'");
		 String correctDesc = c.getString(C6).replace("&amp&", "'");
		 loadArray.add(new ItemMyEvents(i, c.getBlob(C5), c.getString(C3), c.getString(C1), c.getString(C4), c.getString(C7),correctDesc,correctLocation,c.getString(C9)));
		
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
			  C9 = c.getColumnIndex("EventId");
			  
				 correctLocation = c.getString(C2).replace("&amp&", "'");
				 correctDesc = c.getString(C6).replace("&amp&", "'");
			 
			 loadArray.add(new ItemMyEvents(i, c.getBlob(C5), c.getString(C3), c.getString(C1), c.getString(C4), c.getString(C7),correctDesc,correctLocation,c.getString(C9)));			
		}
		
		myDB.close();
    }
    	else{
    		 System.out.println("Row Count is: " + 1);
    	}
    }
}