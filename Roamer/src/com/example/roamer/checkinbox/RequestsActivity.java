package com.example.roamer.checkinbox;


import java.util.ArrayList;

import com.example.roamer.R;
import com.example.roamer.profilelist.Item;
import com.example.roamer.profilelist.MyRoamerModel;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;


public class RequestsActivity extends Activity {

    ListView listView;
    final Context context = this;
    private int count;
    ArrayList<Item> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.request_list);
        loadArray();

        MyRoamerModel.LoadModel(loadArray);
        
        listView = (ListView) findViewById(R.id.listView);
        String[] ids = new String[MyRoamerModel.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        ItemAdapter adapter = new ItemAdapter(this,R.layout.row_message, ids);
        listView.setAdapter(adapter);
              
        
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
    
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CountR");
    	count = cur.getInt(index);
    	
    	
    	if (count > 0) {
    		
    		
    	loadArray = new ArrayList<Item>();
    	int i = 1;

		Cursor c = myDB.rawQuery("SELECT * FROM " + "MyRoamers ", null);
		c.moveToFirst();
		
		 int C1 = c.getColumnIndex("Pic");
		 int C2 = c.getColumnIndex("Name");
		 int C3 = c.getColumnIndex("Loc");
		 int C4 = c.getColumnIndex("Sex");
		 
		 System.out.println("value is: " +c.getString(C1));

		 
		 loadArray.add(new Item(i,c.getString(C1), c.getString(C2), c.getString(C3),c.getInt(C4)));
		
		while(c.moveToNext()){
			i++;
			
			  C1 = c.getColumnIndex("Pic");
			  C2 = c.getColumnIndex("Name");
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