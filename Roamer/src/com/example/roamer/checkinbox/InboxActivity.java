package com.example.roamer.checkinbox;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.roamer.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class InboxActivity extends Activity {

    ListView listView;
    final Context context = this;
    private String selectedName;
    private Spinner position;
    private Dialog dialog;
    private int count;
    private String chatName;
    ArrayList<Item> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.inbox_list);
        
        loadArray();

        Model.LoadModel(loadArray);
        
        listView = (ListView) findViewById(R.id.listView);
        String[] ids = new String[Model.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        ItemAdapter adapter = new ItemAdapter(this,R.layout.row_message, ids);
        listView.setAdapter(adapter);
        
        listView.setLongClickable(true);
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
                // TODO Auto-generated method stub
            	
            	final Dialog dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.dialog_clear_chat);
    			dialog.setTitle("Clear Chat History");
    			
    			dialog.show();
    			
    			Button dialogButton = (Button) dialog.findViewById(R.id.imageClearChat);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					
    					String name = "None";
    					//name = MyRoamerModel.GetbyId(pos+1).Name;
    					clearChat(name);
    					dialog.dismiss();
    				}
    			});

                Log.v("long clicked","pos: " + pos);

                return true;
            }
        }); 
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	
            	String chatName = Model.GetbyId(position+1).Name;
            	System.out.println("Date is: "+Model.GetbyId(position+1).Date);
            	System.out.println("table name will be: "+chatName);
        		addToTempRoamer(chatName);
        		
        		createTable(chatName);

            	Intent i=new Intent(InboxActivity.this,DiscussActivity.class);
                startActivity(i);
            	
            }
        });
        
        //Populate spinner with rows of MyRoamer database or just single row      
       
        
        ImageButton messageButton = (ImageButton) findViewById(R.id.newMessageButton);
        messageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	setDialog(new Dialog(context));
            	
    			getDialog().setContentView(R.layout.select_roamer_for_message);
    			getDialog().setTitle("Select Roamer");

    			getDialog().show();
    			
    			populateRoamers(getDialog());
    			ImageButton dialogButton = (ImageButton) getDialog().findViewById(R.id.imageStartMessage);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    	            	
    					createTable(getSelectedName());
    					    					
    					getDialog().dismiss();
    					Intent i=new Intent(InboxActivity.this,DiscussActivity.class);
    	                startActivity(i);
    	            		  
    				}
    			});

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
    
    public void clearChat(String name){
    	
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		db.delete("ChatTable", null, null);
		
		ContentValues args = new ContentValues();
    	args.put("ChatCount",0);
		db.update("MyCred", args, "rowid" + "=" + 1, null);
		
		db.close();
		finish();
    }
    
    public void createTable(String tableName){
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	System.out.println("table name will be: "+tableName);
    	db.execSQL("CREATE TABLE IF NOT EXISTS "
		          + tableName
		          + " (Field1 VARCHAR,Field2 VARCHAR);");
    	
    	Cursor c = db.rawQuery("SELECT * FROM MyRoamers", null);
    	
    	c.moveToFirst();
    	int index;
    	index = c.getColumnIndex("Username");
    	chatName = c.getString(index);

    	while(!chatName.equals(tableName) && c.moveToNext()){
    		index = c.getColumnIndex("Username");
        	chatName = c.getString(index);
    	}
    	
    	index = c.getColumnIndex("Pic");
    	byte[] picFile = c.getBlob(index);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    	String currentDateandTime = sdf.format(new Date());
    	
    	//check if there is an open chat with this user
    	
    	c = db.rawQuery("SELECT * FROM " + "ChatTable ", null);
    	c.moveToFirst();
    	String tempName = "";
    	boolean nameExists = false;
    	if (c!= null && c.getCount() > 1){
    		int i = c.getColumnIndex("Field1");
    		tempName = c.getString(i);
    		
    		
    		while(c.moveToNext() && !tempName.equals(tableName)){
        		i = c.getColumnIndex("Field1");
        		tempName = c.getString(i);
        		if(tempName.equals(tableName)){
        			nameExists = true;
        		}
        	}
    	}
    	
    	if (!nameExists){
    		
    		String sql                      =   "INSERT INTO ChatTable (Field1,Field2,Field3) VALUES(?,?,?)";
    	    SQLiteStatement insertStmt      =   db.compileStatement(sql);
    	    insertStmt.clearBindings();
    	    insertStmt.bindString(1,tableName);
    	    insertStmt.bindBlob(2,picFile);
    	    insertStmt.bindString(3,currentDateandTime);

    	    insertStmt.executeInsert();
    	}
    	
    	ContentValues args1 = new ContentValues();
    	
    	c = db.rawQuery("SELECT * FROM " + "MyCred ", null);
		c.moveToFirst();
		index = c.getColumnIndex("ChatCount");
		int count = c.getInt(index);
    	args1.put("ChatCount",count+1);
		db.update("MyCred", args1, "rowid" + "=" + 1, null);
		
		//Update temp roamer to pass table name to discussion activity
		
		//db.delete("TempRoamer",null,null);
		
		db.execSQL("INSERT INTO "
			       + "TempRoamer "
			       + "(Username) "
			       + "VALUES ('"+getSelectedName()+"');");
		
		db.close();
    }
    
    public int getRowCount(String tableName){
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	int count = 0;
    	Cursor c = db.rawQuery("select * from "+tableName,null);
    	count = c.getCount();
    	db.close();
    	
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
    
    public void populateRoamers(Dialog dialog){
    	
    
    position = (Spinner) dialog.findViewById(R.id.spinnerSelectRoamer);
     
    SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
   	
   	Cursor cur = myDB.rawQuery("SELECT * FROM MyRoamers", null);
   	
   	cur.moveToFirst();
   	int index;
   	int i = 0;
   	
   	    if (cur != null && cur.getCount()>0) {
   	    	
   	        
   	        
   	    	final MyData items1[] = new MyData[cur.getCount()];
  	    	cur.moveToFirst();
   	    	index = cur.getColumnIndex("Username");
   	    	items1[i] = new MyData(cur.getString(index),"Value1");
   	 		
   	    	while(cur.moveToNext()){
   	    		i=i+1;
   	    		index = cur.getColumnIndex("Username");
   	    		items1[i] = new MyData(cur.getString(index),"Value1");
   	    	}
   	    	
   	    		ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
   	                 android.R.layout.simple_spinner_item, items1);
   	    		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
   	    		position.setAdapter(adapter1);
   	         
   	    		position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
   	             public void onItemSelected(AdapterView<?> parent, View view,
   	                     int position, long id) {
   	          	   
   	          	   ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
   	                 MyData d = items1[position];

   	                 //Get selected value of key 
   	                 //String value = d.getValue();
   	                 setSelectedName(d.getSpinnerText());
   	                 
   	             }

   	  			@Override
   	  			public void onNothingSelected(AdapterView<?> arg0) {
   	  			}
   	         });
   	    	
   	    }
   	    else{
   	    	final MyData items1[] = new MyData[1];
   	    	items1[i] = new MyData("None","Value1");
   	    	   	    	
   	    	position.setVisibility(0);
   	    }
   	   myDB.close();
    }
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("ChatCount");
    	count = cur.getInt(index);
    	
    	//If there are entries in MyRoamers, populate the list
    	loadArray = new ArrayList<Item>();
    	
    	System.out.println("Chat Count is: "+count);
    	if (count > 0) {
    		
    	int i = 1;

		Cursor c = myDB.rawQuery("SELECT * FROM " + "ChatTable", null);
		c.moveToFirst();
		
		 int C1 = c.getColumnIndex("Field2");
		 int C2 = c.getColumnIndex("Field1");
		 int C3 = c.getColumnIndex("Field3");
		 		 
		 loadArray.add(new Item(i,c.getBlob(C1), c.getString(C2), c.getString(C3)));
		
		while(c.moveToNext()){
			i++;
			
			  C1 = c.getColumnIndex("Field2");
			  C2 = c.getColumnIndex("Field1");
			  C3 = c.getColumnIndex("Field3");

			 
			 loadArray.add(new Item(i,c.getBlob(C1), c.getString(C2), c.getString(C3)));			
		}
		
		myDB.close();
    }
    	else{
    		// System.out.println("Row Count is: " + 0);
    		// loadArray.add(new Item(1,"default_userpic.png", "none", "none"));
    	}
    }

	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	public String getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(String selectedName) {
		this.selectedName = selectedName;
	}
	
	public void addToTempRoamer(String roamerName){

	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
	db.delete("TempRoamer",null,null);
	
	db.execSQL("INSERT INTO "
		       + "TempRoamer "
		       + "(Username) "
		       + "VALUES ('"+roamerName+"');");
	
	Cursor c = db.rawQuery("SELECT  *  FROM " + "" + "TempRoamer", null);
	c.moveToFirst();
	int i = c.getColumnIndex("Username");
	System.out.println("Username in temp roamer is: "+c.getString(i));

	db.close();
	}
    
}