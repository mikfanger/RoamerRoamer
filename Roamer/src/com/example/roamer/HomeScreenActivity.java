package com.example.roamer;


import com.example.roamer.checkinbox.ChatsAndRequestsActivity;
import com.example.roamer.checkinbox.InboxActivity;
import com.example.roamer.events.CreateEventActivity;
import com.example.roamer.events.EventsActivity;
import com.example.roamer.profilelist.MyRoamersListActivity;
import com.example.roamer.profilelist.ProfileListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class HomeScreenActivity extends Activity {
	
	Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        
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
        
    }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
   
}