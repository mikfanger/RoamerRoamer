package com.example.roamer.profilelist;

import java.util.ArrayList;

import com.example.roamer.ConvertCode;
import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;

import android.app.Activity;
import android.content.ContentValues;
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


public class MyRoamersListActivity extends Activity {

    ListView listView;
    int count;
    final Context context = this;
    ArrayList<MyRoamerItem> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.my_roamers_list);
        listView = (ListView) findViewById(R.id.listView);
        
        loadArray();
        
        if (count>0){
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
	        	    String location = MyRoamerModel.GetbyId(position+1).Location;
	        	    String start = MyRoamerModel.GetbyId(position+1).StartDate;
	        	    
	        	    addToTempRoamer(name,icon,2,5,5,5,
	        	    		5,5, location, start);
	        	    
	            	Intent i=new Intent(MyRoamersListActivity.this,RoamerProfileActivity.class);
	                startActivity(i);
	    			
	            }

	            public void onNothingSelected(AdapterView<?> parent){
				}
	          });
        
        ImageButton backButton = (ImageButton) findViewById(R.id.roamerListBackButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(MyRoamersListActivity.this,HomeScreenActivity.class);
                startActivity(i);
            		  
            }
        });      
        

    }
    
   
    
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void removeRoamer(int eventId){
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
         
         MyRoamerModel.LoadModel(loadArray);
         
         listView = (ListView) findViewById(R.id.listView);
         String[] ids = new String[MyRoamerModel.Items.size()];
         for (int i= 0; i < ids.length; i++){

             ids[i] = Integer.toString(i+1);
         }

         MyRoamerItemAdapter adapter = new MyRoamerItemAdapter(this,R.layout.row_roamer, ids);
         listView.setAdapter(adapter);
    	 finish();
    }
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CountR");
    	count = cur.getInt(index);
    	
    	
    	Cursor c = myDB.rawQuery("SELECT * FROM " + "MyRoamers ", null);
    	
    	count = c.getCount();
    	System.out.println("MyRoamer count is: "+count);
    	if (count > 0) {
    		
    		
    	loadArray = new ArrayList<MyRoamerItem>();
    	int i = 1;

		
		c.moveToFirst();
		
		 int C1 = c.getColumnIndex("Pic");
		 int C2 = c.getColumnIndex("Username");
		 int C3 = c.getColumnIndex("Loc");
		 int C4 = c.getColumnIndex("Sex");
		 int C5 = c.getColumnIndex("Travel");
		 int C6 = c.getColumnIndex("Industry");
		 int C7 = c.getColumnIndex("Hotel");
		 int C8 = c.getColumnIndex("Air");
		 int C10 = c.getColumnIndex("Job");
		 int C11 = c.getColumnIndex("Start");
		 
		 byte[] tempPic = c.getBlob(C1);
		 String tempName = c.getString(C2);
		 String tempSex = ConvertCode.converSex(c.getInt(C4));
		 String tempTravel = ConvertCode.convertTravel(c.getInt(C5));
		 String tempIndustry = ConvertCode.convertIndustry(c.getInt(C6));
		 String tempHotel = ConvertCode.convertHotel(C7);
		 String tempAir = ConvertCode.convertAirline(C8);
		 String tempJob = ConvertCode.convertJob(C10);
		 String tempStart = c.getString(C11);
		 String tempLoc = ConvertCode.convertLocation(C3);
		 

		 
		 loadArray.add(new MyRoamerItem(i,tempPic, tempName, tempLoc,tempSex,tempStart,
				 tempAir,tempJob,tempTravel,tempIndustry,tempHotel));
		
		while(c.moveToNext()){
			i++;
			
			 C1 = c.getColumnIndex("Pic");
			 C2 = c.getColumnIndex("Username");
			 C3 = c.getColumnIndex("Loc");
			 C4 = c.getColumnIndex("Sex");
			 C5 = c.getColumnIndex("Travel");
			 C6 = c.getColumnIndex("Industry");
			 C7 = c.getColumnIndex("Hotel");
			 C8 = c.getColumnIndex("Air");
			 C10 = c.getColumnIndex("Job");
			 C11 = c.getColumnIndex("Start");
			 
			 tempPic = c.getBlob(C1);
			 tempName = c.getString(C2);
			 tempSex = ConvertCode.converSex(c.getInt(C4));
			 tempTravel = ConvertCode.convertTravel(c.getInt(C5));
			 tempIndustry = ConvertCode.convertIndustry(c.getInt(C6));
			 tempHotel = ConvertCode.convertHotel(C7);
			 tempAir = ConvertCode.convertAirline(C8);
			 tempJob = ConvertCode.convertJob(C10);
			 tempStart = c.getString(C11);
			 tempLoc = ConvertCode.convertLocation(C3);
			
			 
			  loadArray.add(new MyRoamerItem(i,tempPic, tempName, tempLoc,tempSex,tempStart,
						 tempAir,tempJob,tempTravel,tempIndustry,tempHotel));			
		}
		
		myDB.close();
    }
    	else{
    		 
    	}
    }
    
    public void addToTempRoamer(String name, byte[] icon, int sex, int travel, int industry, int job,
    		int hotel, int air, String location, String start){
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	String sql                      =   "INSERT INTO TempRoamer (Username,Pic,Sex,Travel,Industry,Job,Hotel,Air,Loc,Start) VALUES(?,?,?,?,?,?,?,?,?,?)";
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
	    
	    insertStmt.executeInsert();
    }
}