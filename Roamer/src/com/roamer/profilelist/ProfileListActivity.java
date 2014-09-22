package com.roamer.profilelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.roamer.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.ConvertCode;
import com.roamer.HomeScreenActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ProfileListActivity extends Activity {

	private byte[] newIcon;
	String newName;
	String newDate;
	String newLocation;
	int newIndustry;
    ListView listView;
    Context context = this;
    private ArrayList<Item> roamersArray;
    private View roamersProgressView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.roamers_list);
       
        roamersProgressView = findViewById(R.id.progressBarFindRoamers);
              
        TextView currentText = (TextView) findViewById(R.id.locationNames);
        
        SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
    	cur.moveToFirst();
    	
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	
    	String myLocation = getLocationFromText(cur.getInt(index));
    	
    	if (myLocation.equals("")){
    					 //Show toast of lacking network connection
    					 Toast.makeText(context, "No network connection!",
    							   Toast.LENGTH_LONG).show();
    	}
    	currentText.setText(myLocation);
    	
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
    	
    	
    	if (!Location.equals("")){
    		listView = (ListView) findViewById(R.id.listView);
       	 
       	 //Load all roamers in area
       	 loadArray();
       	 
            //Add Roamer if selected
   		 listView.setOnItemClickListener(new OnItemClickListener() {
   	            public void onItemClick(AdapterView<?> parent, View view,
   	                int position, long id) {
   	            	
   	              // When clicked, show a dialog with event information
   	            	finish();
   	            	Intent i=new Intent(ProfileListActivity.this,RoamerProfileShortActivity.class);
   	                startActivity(i);
   	            	
   	    			newIcon = Model.GetbyId(position+1).IconFile;
     	                newName = Model.GetbyId(position+1).Name;
     	                newLocation = Model.GetbyId(position+1).Location;
     	                newDate = Model.GetbyId(position+1).StartDate;
     	                newIndustry = Model.GetbyId(position+1).Industry;
     	                
     	                //Add user to temp roamer
     	                addTempRoamer(newIcon,newName,newLocation,newDate,newIndustry);
     	                
   	            }
   		
   	            public void onNothingSelected(AdapterView<?> parent){
   				}
   	          });
    	}
    	 		
    }
    
    //Enter Roamer data in to temp table for retrieval by short profile page
    public void addTempRoamer(byte[] icon, String name, String sex, String date, int industry){
   	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	
   	
   	myDB.delete("TempRoamer", null, null);

   	String sql                      =   "INSERT INTO TempRoamer (rowid,Pic,Username,Sex,Start,Industry) VALUES(?,?,?,?,?,?)";
    SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
    insertStmt.clearBindings();
    insertStmt.bindLong(1,01);
    insertStmt.bindBlob(2,icon);
    insertStmt.bindString(3, name);
    insertStmt.bindString(4, sex);
    insertStmt.bindString(5, date);
    insertStmt.bindLong(6, industry);
    insertStmt.executeInsert();
   	
   	myDB.close();
   }
    
   public static String getLocationText(int locNum){
	   String curLocation = "";
	   
	   curLocation = ConvertCode.convertFromLocation(locNum);
	   return curLocation;
   }
   
   public static String getLocationFromText(int locNum){
	   String curLocation = "";
	   
	   curLocation = ConvertCode.convertLocation(locNum);
	 
	   return curLocation;
   }
   
   public void loadArray(){
	   
	showProgress(true);
	
   	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	
   	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid "+"= "+1, null);
   	cur.moveToFirst();
   	int index, indexName;
   	index = cur.getColumnIndex("CurrentLocation");
   	indexName = cur.getColumnIndex("Username");
   	final String myName = cur.getString(indexName);
   	
   	int locationInt = cur.getInt(index);
   	roamersArray = new ArrayList<Item>();
   	
   	//Only proceed if a location is not 'Not Selected'
   	if (locationInt != 0){
   	 ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
   	 query.whereEqualTo("CurrentLocation", locationInt);
   	 query.orderByAscending("Username");  	
   	 query.findInBackground(new FindCallback<ParseObject>() {
   	    public void done(List<ParseObject> roamerList, ParseException e) {
   	    	
   	    	
   	        if (e == null) {
   	        	
   	    	   	try {
   	    	   		
   	   			int i = 0;
   	   			
   	   	       	String name;
   	   	       	boolean sex;
   	   	       	byte[] pic = null;
   	   	       	String location;
   	   	       	String eventId;
   	   	       	Date date;
   	   	       	int industry;
   	   	       	
   	   				if(roamerList.size()>0){
   	   					
   	   		        	name = roamerList.get(i).getString("Username");
   	   		        	
   	   		        	sex = roamerList.get(i).getBoolean("Sex");
   	   		        	location = getLocationText(roamerList.get(i).getInt("Location"));
   	   		        	eventId = roamerList.get(i).getString("objectId");
   	   		        	date = roamerList.get(i).getCreatedAt();
   	   		        	industry = roamerList.get(i).getInt("Industry");
   	   		        	
   	   		        	int day = date.getDay();
   	   		        	int month = date.getMonth();
   	   		        	int year = date.getYear();
   	   		        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
   	   		        	
   	   		        	try {
   	   						pic = roamerList.get(i).getParseFile("Pic").getData();
   	   					} catch (ParseException e1) {
   	   						// TODO Auto-generated catch block
   	   						e1.printStackTrace();
   	   					}
   	   		        	catch (NullPointerException e2) {
   	   		        		InputStream ims = null;
   	   		                try {
   	   		                    ims = context.getAssets().open("default_userpic.png");
   	   		                } catch (IOException e3) {
   	   		                    e.printStackTrace();
   	   		                }
   	   		                // load image as Drawable
   	   		                Drawable d = Drawable.createFromStream(ims, null);
   	   		                // set image to ImageView
   	   		                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

   	   		                ByteArrayOutputStream out = new ByteArrayOutputStream();
   	   		                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
   	   		                pic= out.toByteArray(); 
   	   		        	}
   	   		        	
   	   		        	
   	   		        	if (!name.equals(myName)){
   	   		        		roamersArray.add(new Item(i+1,pic,name,location,sex,fullDate,industry));
   	   	   		    		
   	   		        	}
   	   		    		i++;
   	   				}
   	   	           
   	   	       	
   	   	   		while (i < (roamerList.size())){
   	   	   			
   	   	   			name = roamerList.get(i).getString("Username");
   	   	        	sex = roamerList.get(i).getBoolean("Sex");
   	   	        	location = getLocationText(roamerList.get(i).getInt("Location"));
   	   	        	eventId = roamerList.get(i).getString("objectId");
   	   	        	date = roamerList.get(i).getCreatedAt();
   	   	        	industry = roamerList.get(i).getInt("Industry");
   	   	        	
   	   	        	int day = date.getDay();
   	   	        	int month = date.getMonth();
   	   	        	int year = date.getYear();
   	   	        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
   	   	        	
   	   	        	try {
   	   					pic = roamerList.get(i).getParseFile("Pic").getData();
   	   				} catch (ParseException e1) {
   	   					// TODO Auto-generated catch block
   	   					e1.printStackTrace();
   	   				}
   	   	        	catch (NullPointerException e4) {
   		        		InputStream ims = null;
   		                try {
   		                    ims = context.getAssets().open("default_userpic.png");
   		                } catch (IOException e2) {
   		                    e.printStackTrace();
   		                }
   		                // load image as Drawable
   		                Drawable d = Drawable.createFromStream(ims, null);
   		                // set image to ImageView
   		                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();

   		                ByteArrayOutputStream out = new ByteArrayOutputStream();
   		                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
   		                pic= out.toByteArray(); 
   		        	}
   	   	        	
   	   	        	
   	   	        	if (!name.equals(myName)){
   			        		roamersArray.add(new Item(i+1,pic,name,location,sex,fullDate,industry));
   		   		    		
   			        }
   	   	        	i++;
   	   	   		}
   	   	   		
   	   			} 
   	    	   	finally{
   	    	   		
   	    	   	Model.LoadModel(roamersArray);
   	         
   	    	   	String[] ids = new String[Model.Items.size()];
   	    	   	for (int i= 0; i < ids.length; i++){
   	    	   		ids[i] = Integer.toString(i+1);
   	    	   	}
   	    	   	
   	    	   	ItemAdapter adapter = new ItemAdapter(context,R.layout.row_roamer, ids);
   	    	   	listView.setAdapter(adapter);
   	    	   		
   	    	   	showProgress(false);
   	    	   	}
   	    	   	
   	        } else {
   	            System.out.println("Error: " + e.getMessage());
   	        }
   	    }
   	});
   	}

   	else{
   		
   		Toast toast = Toast.makeText(context, "Need to set your location!",
 			   Toast.LENGTH_LONG);
   		toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
   		toast.show();
   	}
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

			roamersProgressView.setVisibility(View.VISIBLE);
			roamersProgressView.animate().setDuration(mediumAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							roamersProgressView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			roamersProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}
   
   @Override
   public void onBackPressed() 
   {
   	 Intent i=new Intent(ProfileListActivity.this,HomeScreenActivity.class);
       startActivity(i);
   }
}