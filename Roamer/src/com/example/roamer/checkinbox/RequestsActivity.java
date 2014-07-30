package com.example.roamer.checkinbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.roamer.ConvertCode;
import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.example.roamer.profilelist.MyRoamerItemAdapter;
import com.example.roamer.profilelist.ProfileListActivity;
import com.example.roamer.profilelist.RoamerProfileShortActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class RequestsActivity extends Activity {

    ListView listView;
    private byte[] newIcon;
	String newName;
	String newDate;
	String newLocation;
    final Context context = this;
    ArrayList<ItemRequest> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.request_list);
        
        try {
			createListView();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    
    public void createListView() throws IOException{
    	
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
        
  
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	
            	Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
            			   Toast.LENGTH_LONG).show();
            	
            	//Add to temp roamer
            	newIcon = ModelRequest.GetbyId(position+1).IconFile;
	            newName = ModelRequest.GetbyId(position+1).Name;
	            newLocation = ModelRequest.GetbyId(position+1).Location;
	            newDate = ModelRequest.GetbyId(position+1).StartDate;
            	
	            addTempRoamer(newIcon,newName,newLocation,newDate);
	            
            	String chatName = ModelRequest.GetbyId(position+1).Name;
            	System.out.println("Request is from: "+chatName);
            	
            	//Show profile of roamer
            	finish();
            	Intent i=new Intent(RequestsActivity.this,RoamerProfileShortActivity.class);
                startActivity(i);
            	
            }
        });
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
    
    
    @SuppressWarnings("deprecation")
	public void loadArray() throws JSONException, ParseException, IOException{
    	
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("Username");
    	String userName = cur.getString(index);
    	String credName = userName;
    	
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
           	
    			if( roamerList!=null && roamerList.length()!=0){
    			
    	        	name = roamerList.get(i).toString();
    	        	
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
    	        	
    	        	//set sex
    	        	String sexString = "";
    	        	if(sex){
    	        		sexString = "male";
    	        	}
    	        	else{
    	        		sexString = "female";
    	        	}
	
    	        	try {
    	        		System.out.println("Roamer is: "+name);
    					pic = newRoamer.getParseFile("Pic").getData();
    				} catch (ParseException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    					finish();
    				}
    	        	finally {
    	        		AssetManager assetManager = getAssets();
    	        		InputStream is = assetManager.open("default_userpic.png");
    	        		
    	        		pic = new byte[is.available()];
    	        	}
    	        	
    	    		loadArray.add(new ItemRequest(i+1,pic,name,fullDate,sexString,travel,industry,hotel,job,location,airline, credName));
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
	        	

	    		loadArray.add(new ItemRequest(i+1,pic,name,fullDate,sexString,travel,industry,hotel,job,location,airline,credName));
	    		i++;
       		}
    	  }
       	}
       	catch(ParseException e){
       		
       	}
       
    }
    
    public void addTempRoamer(byte[] icon, String name, String sex, String date){
       	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
       	
       	
       	myDB.delete("TempRoamer", null, null);

       	String sql                      =   "INSERT INTO TempRoamer (rowid,Pic,Username,Loc,Start) VALUES(?,?,?,?,?)";
        SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindLong(1,01);
        insertStmt.bindBlob(2,icon);
        insertStmt.bindString(3, name);
        insertStmt.bindString(4, sex);
        insertStmt.bindString(5, date);
        insertStmt.executeInsert();
       	
       	myDB.close();
       }
    
    public void addToDatabase(String name) throws ParseException{
    	ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Roamer");
	 	
    	query2.whereEqualTo("Username", name);
    	
    	final ParseObject Roamer2 = query2.getFirst();
    	
		int sex = 0;
		 if(Roamer2.getBoolean("Sex") == true){
			 sex = 1;
		 }
		 byte[] picFile = null;
		try {
			picFile = Roamer2.getParseFile("Pic").getData();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Convert Start date to actual string
		int day = Roamer2.getCreatedAt().getDay();
		int month = Roamer2.getCreatedAt().getMonth();
		int year = Roamer2.getCreatedAt().getYear();
		String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
	
    }
    public void onBackPressed() 
    {
    	 Intent i=new Intent(RequestsActivity.this,HomeScreenActivity.class);
        startActivity(i);
    }
}