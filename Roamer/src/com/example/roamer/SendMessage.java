package com.example.roamer;


import com.example.roamer.profilelist.ItemAdapter;
import com.example.roamer.profilelist.Model;
import com.example.roamer.profilelist.ProfileListActivity;
import com.example.roamer.profilelist.ProfileListActivity.MyData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;


public class SendMessage extends Activity {
	
	public String message;
    public ListView listView;

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
    	            	
    	            	Intent i=new Intent(SendMessage.this,HomeScreenActivity.class);
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

    	         ItemAdapter adapter = new ItemAdapter(this,R.layout.row, ids);
    	         listView.setAdapter(adapter);
    	    }
    	}