package com.example.roamer.checkinbox;


import java.util.ArrayList;
import java.util.Date;


import org.json.JSONArray;
import org.json.JSONException;

import com.example.roamer.ConvertCode;
import com.example.roamer.IntroActivity;
import com.example.roamer.LoginActivity;
import com.example.roamer.R;
import com.example.roamer.profilelist.ProfileListActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;


public class RequestsActivity extends Activity {

    ListView listView;
    final Context context = this;
    private int count;
    ArrayList<ItemRequest> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.request_list);
        
        try {
			loadArray();
		} catch (JSONException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        ModelRequest.LoadModel(loadArray);
        
        listView = (ListView) findViewById(R.id.listView);
        String[] ids = new String[ModelRequest.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        ItemAdapterRequest adapter = new ItemAdapterRequest(this,R.layout.row_request, ids);
        listView.setAdapter(adapter);
        
        /*
        ImageButton acceptButton = (ImageButton) findViewById(R.id.StartRoamerButton);
        acceptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	           	
            	
            	//deleteFromArray(ModelRequest.GetbyId(position+1).Na);
            }
         
        });
        */
        /*
        ImageButton rejectButton = (ImageButton) findViewById(R.id.StartRoamerButton);
        rejectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	           	
            	deleteFromArray(ModelRequest.GetbyId(id).name);
            }
        });
         */   
        
    }
    
    
    public void clearChat(String name){
    	
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		db.delete("ChatTable", null, null);
		db.close();
    }
    
    public int getRowCount(String tableName){
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	int count = 0;
    	Cursor c = db.rawQuery("select * from "+tableName,null);
    	count = c.getCount();
    			return count;
    }
    
    public String getRoamerName(String row){
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor c = db.rawQuery("SELECT Field1 FROM MyRoamers WHERE rowid="+row,null);
    	return c.toString();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void loadArray() throws JSONException, ParseException{
    	
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("Username");
    	String userName = cur.getString(index);
    	
    	
    	loadArray = new ArrayList<ItemRequest>();
    	
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
       	query.whereEqualTo("Username", userName);
       	
       	final ParseObject Roamer = query.getFirst();
       	
       	try {
       		
    			JSONArray roamerList = Roamer.getJSONArray("Requests");
    			
    			int i = 0;
    		
    			String name;
    			boolean sex;
    			byte[] pic = null;
    			String location;
    			Date date;
    			String airline;
    			String job;
    			String industry;
    			String hotel;
    			String travel;
           	
    			if( roamerList!=null ){
    			
    				System.out.println("Requests are: "+roamerList);
    	        	name = (String) roamerList.get(i);
    	        	System.out.println("First roamer name is: "+name);
    	        	
    	        	
    	        	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Roamer");
    	        	query1.whereEqualTo("Username", name);
    	        	
    	        	ParseObject newRoamer = query1.getFirst();
    	        	sex = newRoamer.getBoolean("Sex");
    	        	date = newRoamer.getCreatedAt();
    	        	travel = ConvertCode.convertTravel(newRoamer.getInt("Travel"));
    	        	industry = ConvertCode.convertIndustry(newRoamer.getInt("Industry"));
    	        	job = ConvertCode.convertJob(newRoamer.getInt("Job"));
    	        	hotel = ConvertCode.convertHotel(newRoamer.getInt("Hotel"));
    	        	airline = ConvertCode.convertAirline(newRoamer.getInt("Airline"));
    	        	location = ProfileListActivity.getLocationText(newRoamer.getInt("Location"));
    	        	
    	        	
    	        	int day = date.getDay();
    	        	int month = date.getMonth();
    	        	int year = date.getYear();
    	        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
    	        	System.out.println("Full date is: "+fullDate);
    	        	
    	        	//set sex
    	        	String sexString = "";
    	        	if(sex){
    	        		sexString = "male";
    	        	}
    	        	else{
    	        		sexString = "female";
    	        	}
    	        	
    	        	

    	        	
    	        	try {
    					pic = newRoamer.getParseFile("Pic").getData();
    				} catch (ParseException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    	        	
    	        	
    	        	
    	    		loadArray.add(new ItemRequest(i+1,pic,name,fullDate,sexString,travel,industry,hotel,job,location,airline));
    	    		i++;
    			
               
           	
       		while (i < (roamerList.length())){
       			
       			name = (String) roamerList.get(i);
	        	
	        	query1 = ParseQuery.getQuery("Roamer");
	        	query1.whereEqualTo("Username", name);
	        	
	        	newRoamer = query1.getFirst();
	        	sex = newRoamer.getBoolean("Sex");
	        	date = newRoamer.getCreatedAt();
	        	travel = ConvertCode.convertTravel(newRoamer.getInt("Travel"));
	        	industry = ConvertCode.convertIndustry(newRoamer.getInt("Industry"));
	        	job = ConvertCode.convertJob(newRoamer.getInt("Job"));
	        	hotel = ConvertCode.convertHotel(newRoamer.getInt("Hotel"));
	        	airline = ConvertCode.convertAirline(newRoamer.getInt("Airline"));
	        	location = ProfileListActivity.getLocationText(newRoamer.getInt("Location"));
	        	
	        	day = date.getDay();
	        	month = date.getMonth();
	        	year = date.getYear();
	        	fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
	        	System.out.println("Full date is: "+fullDate);
	        	
	        	//set sex
	        	sexString = "";
	        	if(sex){
	        		sexString = "male";
	        	}
	        	else{
	        		sexString = "female";
	        	}
	        	location = ProfileListActivity.getLocationText(newRoamer.getInt("Location"));
	        	
	        	
	        	try {
					pic = newRoamer.getParseFile("Pic").getData();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	

	    		loadArray.add(new ItemRequest(i+1,pic,name,fullDate,sexString,travel,industry,hotel,job,location,airline));
	    		i++;
       		}
    	}
       	}
       	catch(ParseException e){
       		
       	}
       
    }
    
    public void deleteFromArray(String name){
    	
    }
}