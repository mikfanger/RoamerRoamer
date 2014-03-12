package com.example.roamer.profilelist;

import java.util.ArrayList;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class MyRoamersListActivity extends Activity {

    ListView listView;
    int count;
    final Context context = this;
    ArrayList<Item> loadArray;

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
    	
    }
    
    public void addTempRoamer(String icon, String name, String sex){
      	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
      	 
      	myDB.execSQL("INSERT INTO "
   			       + "TempRoamer "
   			       + "(rowid,Pic,Username,Loc) "
   			       + "VALUES ("+01+",'"+icon+"','"+name+"','"+sex+"');");
      	
      	myDB.close();
      }
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CountR");
    	count = cur.getInt(index);
    	
    	System.out.println("Checking Count of MyRoamers");
    	System.out.println("Count of my roamers is: " + count);
    	
    	if (count > 0) {
    		
    		
    	loadArray = new ArrayList<Item>();
    	int i = 1;

		Cursor c = myDB.rawQuery("SELECT * FROM " + "MyRoamers ", null);
		c.moveToFirst();
		
		 int C1 = c.getColumnIndex("Pic");
		 int C2 = c.getColumnIndex("Username");
		 int C3 = c.getColumnIndex("Loc");
		 int C4 = c.getColumnIndex("Sex");
		 
		 
		 System.out.println("value of location is: " +c.getString(C3));

		 
		 loadArray.add(new Item(i,c.getString(C1), c.getString(C2), c.getString(C3),c.getInt(C4)));
		
		while(c.moveToNext()){
			i++;
			
			  C1 = c.getColumnIndex("Pic");
			  C2 = c.getColumnIndex("Username");
			  C3 = c.getColumnIndex("Loc");
			  C4 = c.getColumnIndex("Sex");

			 
			 loadArray.add(new Item(i,c.getString(C1), c.getString(C2), c.getString(C3),c.getInt(C4)));			
		}
		
		myDB.close();
    }
    	else{
    		 System.out.println("Row Count is: " + 0);
    	}
    }
}