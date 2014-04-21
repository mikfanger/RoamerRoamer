package com.example.roamer.profilelist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    ListView listView;
    final Context context = this;
    private ArrayList<Item> roamersArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.roamers_list);

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
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
    	cur.moveToFirst();
    	
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	currentText.setText(getLocationText(cur.getInt(index)));
    	
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
	            	Intent i=new Intent(ProfileListActivity.this,RoamerProfileShortActivity.class);
	                startActivity(i);
	            	
	    			newIcon = Model.GetbyId(position+1).IconFile;
  	                newName = Model.GetbyId(position+1).Name;
  	                newLocation = Model.GetbyId(position+1).Location;
  	                newDate = Model.GetbyId(position+1).StartDate;
  	                
  	                System.out.println("Date from profile is: "+newDate);
  	                addTempRoamer(newIcon,newName,newLocation,newDate);
  	                
	            }
		
	            public void onNothingSelected(AdapterView<?> parent){
				}
	          });
		
    }
    
    //Enter Roamer data in to temp table for retrieval by short profile page
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
    
   public static String getLocationText(int locNum){
	   String curLocation = "";
	   
	   switch(locNum){
   	case 0:
   		curLocation ="Not Selected";
   		break;
   	case 1:
   		curLocation ="Boston";
           break;
   	case 2:
   		curLocation ="San Francisco";
   		break;
   	case 3:
   		curLocation ="Las Vegas";
   		break;
   	case 4:
   		curLocation ="New York";
   		break;
   	case 5:
   		curLocation ="Los Angeles";
   		break;
   	case 6:
   		curLocation ="Houston";
   		break;
   	case 7:
   		curLocation ="Philadelphia";
   		break;
   	case 8:
   		curLocation ="Phoenix";
   		break;
   	case 9:
   		curLocation ="San Antonio";
   		break;
   	case 10:
   		curLocation ="San Diego";
   		break;
   	case 11:
   		curLocation ="Dallas";
   		break;
   	case 12:
   		curLocation ="San Jose";
   		break;
   	case 13:
   		curLocation ="Austin";
   		break;
   	case 14:
   		curLocation ="Jacksonville";
   		break;
   	case 15:
   		curLocation ="Indianapolis";
   		break;
   	case 16:
   		curLocation ="Seattle";
   		break;
   	case 17:
   		curLocation ="Dever";
   		break;
   	case 18:
   		curLocation ="Washington DC";
   		break;
   	case 19:
   		curLocation ="Chicago";
   		break;
   	}
	   
	   return curLocation;
   }
   
   public void loadArray(){
   	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	
   	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid "+"= "+1, null);
   	cur.moveToFirst();
   	int index;
   	index = cur.getColumnIndex("CurrentLocation");
   	
   	int locationInt = cur.getInt(index);
   	roamersArray = new ArrayList<Item>();
   	
   	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
   	query.whereEqualTo("Location", locationInt);
   	try {
   		
			List<ParseObject> roamerList = query.find();
			int i = 0;
		
       	String name;
       	boolean sex;
       	byte[] pic = null;
       	String location;
       	String eventId;
       	Date date;
       	
			if(roamerList.size()>0){
				
	        	
	        	name = roamerList.get(i).getString("Username");
	        	sex = roamerList.get(i).getBoolean("Sex");
	        	location = getLocationText(roamerList.get(i).getInt("Location"));
	        	eventId = roamerList.get(i).getString("objectId");
	        	date = roamerList.get(i).getDate("CreatedDate");
	        	
	        	int day = date.getDay();
	        	int month = date.getMonth();
	        	int year = date.getYear();
	        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
	        	System.out.println("Date before loading is: "+fullDate);
	        	
	        	try {
					pic = roamerList.get(i).getParseFile("Pic").getData();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
	        	
	        	
	    		roamersArray.add(new Item(i+1,pic,name,location,sex,fullDate));
	    		i++;
			}
           
       	
   		while (i < (roamerList.size())){
   			
   			name = roamerList.get(i).getString("Username");
        	sex = roamerList.get(i).getBoolean("Sex");
        	location = getLocationText(roamerList.get(i).getInt("Location"));
        	eventId = roamerList.get(i).getString("objectId");
        	date = roamerList.get(i).getDate("CreatedDate");
        	
        	int day = date.getDay();
        	int month = date.getMonth();
        	int year = date.getYear();
        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
        	
        	System.out.println("Date before loading is: "+fullDate);
        	
        	try {
				pic = roamerList.get(i).getParseFile("Pic").getData();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
    		roamersArray.add(new Item(i+1,pic,name,location,sex,fullDate));
    		i++;
   		}
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	
   }
}