package com.example.roamer.profilelist;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ProfileListActivity extends Activity {

	String newIcon;
	String newName;
	String newLocation;
    ListView listView;
    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.roamers_list);

        /*
        Model.LoadModel();
        
        listView = (ListView) findViewById(R.id.listView);
        String[] ids = new String[Model.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        ItemAdapter adapter = new ItemAdapter(this,R.layout.row, ids);
        listView.setAdapter(adapter);
        */
        
        ImageButton backButton = (ImageButton) findViewById(R.id.inboxBackButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(ProfileListActivity.this,HomeScreenActivity.class);
                startActivity(i);
            		  
            }
        });
              
        TextView currentText = (TextView) findViewById(R.id.currentLocation);
        
        SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	currentText.setText(cur.getString(index));
    	
    	myDB.close();
        
    	String currentLocation = currentText.getText().toString();
    	createListView(currentLocation);
      
    }
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void createListView(String Location)
    {
    	 Model.LoadModel(Location);
         
         listView = (ListView) findViewById(R.id.listView);
         String[] ids = new String[Model.Items.size()];
         for (int i= 0; i < ids.length; i++){
        	 
        	 
             ids[i] = Integer.toString(i+1);
         }

         ItemAdapter adapter = new ItemAdapter(this,R.layout.row_roamer, ids);
         listView.setAdapter(adapter);
         
         //Add Roamer if selected
		 listView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	            	
	              // When clicked, show a dialog with event information
	            	Intent i=new Intent(ProfileListActivity.this,RoamerProfileShortActivity.class);
	                startActivity(i);
	            	
	    			newIcon = Model.GetbyId(position+1).IconFile;
  	                newName = Model.GetbyId(position+1).Name;
  	                newLocation = Model.GetbyId(position+1).Location;
  	                addTempRoamer(newIcon,newName,newLocation);
  	                
	            }
		
	            public void onNothingSelected(AdapterView<?> parent){
				}
	          });
		
    }
    
    //Enter Roamer data in to temp table for retrieval by short profile page
    public void addTempRoamer(String icon, String name, String sex){
   	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	 
   	myDB.delete("TempRoamer", null, null);
   	myDB.execSQL("INSERT INTO "
			       + "TempRoamer "
			       + "(rowid,Pic,Username,Loc) "
			       + "VALUES ("+01+",'"+icon+"','"+name+"','"+sex+"');");
   	
   	myDB.close();
   }
}