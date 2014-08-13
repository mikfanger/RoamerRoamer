package com.roamer.profilelist;

import graphics.FlyOutContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import com.roamer.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.ConvertCode;
import com.roamer.HomeScreenActivity;
import com.roamer.IntroActivity;
import com.roamer.LoginActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class MyRoamersListActivity extends Activity {

    ListView listView;
    int count;
    final Context context = this;
    ArrayList<MyRoamerItem> loadArray;
    
    private View mMyRoamersView;
    FlyOutContainer root;
    
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    		

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.my_roamers_list);
        
        
		mMyRoamersView = findViewById(R.id.progressBarMyRoamers);
		
		try {
			loadArray();
			listView = (ListView) findViewById(R.id.listView);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        
        if (loadArray.size()>0){
        MyRoamerModel.LoadModel(loadArray);
        
        String[] ids = new String[MyRoamerModel.Items.size()];
        for (int i= 0; i < ids.length; i++){
 
            ids[i] = Integer.toString(i+1);
        }
        
       MyRoamerItemAdapter adapter = new MyRoamerItemAdapter(this,R.layout.row_roamer, ids);
        listView.setAdapter(adapter);
        }
       
       
        //Add Roamer if selected
		 listView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {
	            	
	        	    String name = MyRoamerModel.GetbyId(position+1).Name;
	        	    byte[] icon = MyRoamerModel.GetbyId(position+1).IconFile;
	        	    String sex = MyRoamerModel.GetbyId(position+1).Sex;
	        	    String travel = MyRoamerModel.GetbyId(position+1).Travel;
	        	    String industry = MyRoamerModel.GetbyId(position+1).Industry;
	        	    String job = MyRoamerModel.GetbyId(position+1).Job;
	        	    String hotel = MyRoamerModel.GetbyId(position+1).Hotel;
	        	    String air = MyRoamerModel.GetbyId(position+1).Air;
	        	    String location = MyRoamerModel.GetbyId(position+1).Origin;
	        	    String start = MyRoamerModel.GetbyId(position+1).StartDate;
	        	    
	        	    int sexInt = ConvertCode.convertFromSex(sex);
	        	    int travelInt = ConvertCode.convertFromTravel(travel);
	        	    int industryInt = 0;
	        	    int jobInt = 0;
	        	    int hotelInt = 0;
	        	    int airInt = 0;
	        	    
	        	    //Convert sex
	        	    if (sex.equals("male")){
	        	    	sexInt = 1;
	        	    }
	        	    addToTempRoamer(name,icon,sexInt,travelInt,5,5,
	        	    		5,5, location, start, location);
	
	            	Intent i=new Intent(MyRoamersListActivity.this,RoamerProfileActivity.class);
	                startActivity(i);
	    			
	            }

	            public void onNothingSelected(AdapterView<?> parent){
				}
	          });
		 
		 	
				
    }
				
    
   
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void removeRoamer(int eventId) throws JSONException, ParseException{
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	myDB.execSQL("DELETE FROM MyRoamers WHERE rowid="+eventId);
    	
    	//Update count of events in Credentials
    	ContentValues args = new ContentValues();
    	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred" , null);
    	c.moveToFirst();
    	int index = c.getColumnIndex("CountR");
    	args.put("CountR",c.getInt(index)-1);
    	myDB.update("MyCred", args, "rowid"+"="+1, null);
    	
    	myDB.close();
    	
    	 loadArray();

    }
    
    public void loadArray() throws JSONException, ParseException{
    	
		//show progress spinner
		showProgress(true);
		
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("Username");
    	final String userName = cur.getString(index);
    	String credName = userName;
    	
    	myDB.close();
    	
    	loadArray = new ArrayList<MyRoamerItem>();
   	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
      	 query.whereEqualTo("Username", userName);
      	   	
      	 query.findInBackground(new FindCallback<ParseObject>() {
      	    
      		 public void done(List<ParseObject> roamer, ParseException e) {
      	    	
      	    	
      	        if (e == null) {
      	        	
      	    	   	try {
      	    	   		
      	    	   	JSONArray roamerList = roamer.get(0).getJSONArray("MyRoamers");
        			
        			ArrayList<String> nameList = new ArrayList();
        			
        			int i = 0;
        			
        			while (i < roamerList.length()){
        				nameList.add(roamerList.get(i).toString());
        				i++;
        			}
        			
        			Collections.sort(nameList);
        			
        			i = 0;
        		
        			String name;
        			boolean sex;
        			byte[] pic = null;
        			String location;
        			String originLocation;
        			Date date;
        			String airline;
        			String job;
        			String industry;
        			String hotel;
        			String travel;
               	
        			if( roamerList!=null && roamerList.length()!=0){
        			
        	        	name = nameList.get(i);
        	        	
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
        	        	originLocation = ConvertCode.convertFromLocation(newRoamer.getInt("Location"));
        	        	
        	        	
        	        	ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Cities");
        	        	query2.whereEqualTo("Code", newRoamer.getInt("CurrentLocation"));
        	        	ParseObject currentLocation = query2.getFirst();
        	        	
        	        	location = currentLocation.getString("Name");
        	        	
        	        	
    					int day = date.getDay();
    					int month = date.getMonth();
    					int year = date.getYear();
        	        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
        	        	
        	        	//set sex
        	        	String sexString = "";
        	        	if(sex == true){
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
        	   		    loadArray.add(new MyRoamerItem(i+1,pic,name,location,sexString,fullDate,
        	   		    		airline,job,travel,industry,hotel,originLocation));
        	    		i++;
        			
                   
               	
           		while (i < (roamerList.length())){
           			
           			name = nameList.get(i);
    	        	
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
    	        	originLocation = ConvertCode.convertFromLocation(newRoamer.getInt("Location"));
    	        	
    	        	
    	        	ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Cities");
    	        	query3.whereEqualTo("Code", newRoamer.getInt("CurrentLocation"));
    	        	ParseObject currentLocationNew = query3.getFirst();
    	        	
    	        	location = currentLocationNew.getString("Name");
    	        	
    	        	
    	        	day = date.getDay();
    	        	month = date.getMonth();
    	        	year = date.getYear();
    	        	fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
    	        	
    	        	//set sex
    	        	sexString = "";
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
  
    	   		    loadArray.add(new MyRoamerItem(i+1,pic, name, location,sexString,fullDate,
    					 airline,job,travel,industry,hotel,originLocation));
    	    		i++;
           		}
           	}
      	    
      	}
      	catch(ParseException e5){
      	       		
      	} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      	finally{
      		MyRoamerModel.LoadModel(loadArray);
            
            listView = (ListView) findViewById(R.id.listView);
            String[] ids = new String[MyRoamerModel.Items.size()];
            for (int i= 0; i < ids.length; i++){

                ids[i] = Integer.toString(i+1);
            }

            MyRoamerItemAdapter adapter = new MyRoamerItemAdapter(context,R.layout.row_roamer, ids);
            listView.setAdapter(adapter);
            
            //hide progress spinner
         	showProgress(false);
      	}
      }
      else {
  	            System.out.println("Error: " + e.getMessage());
  	  }
     }});
     	  
    }
       	
     
    
    public void addToTempRoamer(String name, byte[] icon, int sex, int travel, int industry, int job,
    		int hotel, int air, String location, String start, String origin){
    	
    	System.out.println("Origin is: "+origin);
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	//clear temp roamer
    	myDB.delete("TempRoamer",null,null);
    	
    	String sql                      =   "INSERT INTO TempRoamer (Username,Pic,Sex,Travel,Industry,Job,Hotel,Air,Loc,Start,origin) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	    SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
	    insertStmt.clearBindings();
	    insertStmt.bindString(1,name);
	    insertStmt.bindBlob(2,icon);
	    insertStmt.bindLong(3,sex);
	    insertStmt.bindLong(4,travel);
	    insertStmt.bindLong(5,industry);
	    insertStmt.bindLong(6,job);
	    insertStmt.bindLong(7,hotel);
	    insertStmt.bindLong(8,air);
	    insertStmt.bindString(9,location);
	    insertStmt.bindString(10,start);
	    insertStmt.bindString(11, origin);
	    
	    insertStmt.executeInsert();
    }
    
    /**
	 * Shows the progress UI and hides the form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int mediumAnimTime = getResources().getInteger(
					android.R.integer.config_mediumAnimTime);

			mMyRoamersView.setVisibility(View.VISIBLE);
			mMyRoamersView.animate().setDuration(mediumAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mMyRoamersView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mMyRoamersView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
    
    
	 @Override
	 public void onBackPressed() 
	 {
		 Intent i=new Intent(MyRoamersListActivity.this,HomeScreenActivity.class);
         startActivity(i);
	 }
}