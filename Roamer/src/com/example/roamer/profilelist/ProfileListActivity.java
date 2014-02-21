package com.example.roamer.profilelist;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

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
        setContentView(R.layout.profile_list);

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
        
        //Region
        final Spinner position = (Spinner) findViewById(R.id.locationSpinner);
        //Prepare adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData locations[] = new MyData[5];
        locations[0] = new MyData("Boston", "value1");
        locations[1] = new MyData("Chicago", "value2");
        locations[2] = new MyData("Cleveland", "value3");
        locations[3] = new MyData("Oakland", "value2");
        locations[4] = new MyData("Detroit", "value3");
        ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, locations);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(adapter1);
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = locations[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
    				
    				
    				Object item = parent.getItemAtPosition(pos);
    				String currentLocation = item.toString();
    				createListView(currentLocation);
    			}
    			public void onNothingSelected(AdapterView<?> parent){
    			}
		});
        
       

    }
    
    public class MyData {
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
	            	/*
	            	final Dialog dialog = new Dialog(context);
	            	
	    			dialog.setContentView(R.layout.activity_roamer_profile_short);
	    			dialog.setTitle("Roamer Profile");
	    			newIcon = Model.GetbyId(position+1).IconFile;
  	                newName = Model.GetbyId(position+1).Name;
  	                newLocation = Model.GetbyId(position+1).Location;
	            
	    			dialog.show();
	    			
	    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageAddRoamer);
	    			// if button is clicked, close the custom dialog
	    			dialogButton.setOnClickListener(new OnClickListener() {
	    				
	    				  
	    	                 
	    				@Override
	    				public void onClick(View v) {
	    					addToMyRoamers(newIcon, newName, newLocation);
	    					dialog.dismiss();
	    				}
	    				
	    			});
			*/
	            }
		
	            public void onNothingSelected(AdapterView<?> parent){
				}
	          });
		
    }
    
    public void addToMyRoamers(String icon, String name, String location){
   	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	 
   	myDB.execSQL("INSERT INTO "
			       + "MyRoamers "
			       + "(Pic,Name,Loc) "
			       + "VALUES ('"+icon+"','"+name+"','"+location+"');");
   	
   	//Update count of events in Credentials
   	ContentValues args = new ContentValues();
   	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
   	c.moveToFirst();
   	int index = c.getColumnIndex("CountR");
   	args.put("CountR",c.getInt(index)+1);
   	myDB.update("MyCred", args, "rowid"+"="+1, null);
   	
   	myDB.close();
   }
}