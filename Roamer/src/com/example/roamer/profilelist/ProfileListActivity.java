package com.example.roamer.profilelist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.roamer.ConvertCode;
import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

	private byte[] newIcon;
	String newName;
	String newDate;
	String newLocation;
	int newIndustry;
    ListView listView;
    final Context context = this;
    private ArrayList<Item> roamersArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.roamers_list);
       
              
        TextView currentText = (TextView) findViewById(R.id.currentLocation);
        
        SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
    	cur.moveToFirst();
    	
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	currentText.setText(getLocationFromText(cur.getInt(index)));
    	
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
    	
    	 loadArray();
    	 Model.LoadModel(roamersArray);
         
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
   	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	
   	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid "+"= "+1, null);
   	cur.moveToFirst();
   	int index, indexName;
   	index = cur.getColumnIndex("CurrentLocation");
   	indexName = cur.getColumnIndex("Username");
   	String myName = cur.getString(indexName);
   	
   	int locationInt = cur.getInt(index);
   	roamersArray = new ArrayList<Item>();
   	
   	//Only proceed if a location is not 'Not Selected'
   	if (locationInt != 0){
   	   	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
   	   	query.whereEqualTo("CurrentLocation", locationInt);
   	   	try {
   	   		
   				List<ParseObject> roamerList = query.find();
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
   		        	catch (NullPointerException e) {
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
   	           
   	       	
   	   		while (i < (roamerList.size()-1)){
   	   			
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
   	        	catch (NullPointerException e) {
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
   			} catch (ParseException e2) {
   				// TODO Auto-generated catch block
   				e2.printStackTrace();
   			}
   	}
	
   }
   
   @Override
   public void onBackPressed() 
   {
   	 Intent i=new Intent(ProfileListActivity.this,HomeScreenActivity.class);
       startActivity(i);
   }
}