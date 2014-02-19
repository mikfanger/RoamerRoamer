package com.example.roamer.checkinbox;


import java.util.ArrayList;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.example.roamer.profilelist.Item;
import com.example.roamer.profilelist.MyRoamerModel;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
    ArrayList<Item> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.inbox_list);


        MyRoamerModel.LoadModel(loadArray);
        
        listView = (ListView) findViewById(R.id.listView);
        String[] ids = new String[MyRoamerModel.Items.size()];
        for (int i= 0; i < ids.length; i++){

            ids[i] = Integer.toString(i+1);
        }

        ItemAdapter adapter = new ItemAdapter(this,R.layout.row_message, ids);
        listView.setAdapter(adapter);
        
        listView.setLongClickable(true);
        
        ImageButton inboxButton = (ImageButton) findViewById(R.id.inboxBackButton);
        inboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(InboxActivity.this,HomeScreenActivity.class);
                startActivity(i);
            		  
            }
        });
        
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
        
        //Populate spinner with rows of MyRoamer database or just single row      
       
        
        Button messageButton = (Button) findViewById(R.id.newMessageButton);
        messageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.select_roamer_for_message);
    			dialog.setTitle("Select Roamer");

    			dialog.show();
    			
    			populateRoamers(dialog);
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageStartMessage);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    	            	
    					createTable(selectedName);
    					    					
    					dialog.dismiss();
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
		db.close();
    }
    
    public void createTable(String tableName){
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	db.execSQL("CREATE TABLE IF NOT EXISTS "
		          + tableName
		          + " (Field1 VARCHAR,Field2 INT(1));");
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
    
    public void populateRoamers(Dialog dialog){
    	
    
       position = (Spinner) dialog.findViewById(R.id.spinnerSelectRoamer);
        
       /*
        int rowCount = 1;
        
        System.out.println("MyRoamers RowCount: "+getRowCount("MyRoamers"));
        if(getRowCount("MyRoamers") == 0)
        {
        	return;
        }
        else
        {
        	rowCount = getRowCount("MyRoamers");
        }
        final MyData items1[] = new MyData[rowCount];
        
        
        if (getRowCount("MyRoamers") == 0)
        {
        	items1[0] = new MyData("name","value");
        }
        else
        {
        	for(int i = 0; i<getRowCount("MyRoamers");i++)
            {
            	String name =Integer.toString(i);
            	items1[i] = new MyData(getRoamerName(name),"value");
            }
        }
        */
       
       final MyData items1[] = new MyData[5];
       items1[0] = new MyData("Midwest", "value1");
       items1[1] = new MyData("West", "value2");
       items1[2] = new MyData("Southwest", "value3");
       items1[3] = new MyData("Southeast", "value2");
       items1[4] = new MyData("Northeast", "value3");
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
               String value = d.getValue();
               String key = d.getSpinnerText();
               selectedName = key;
           }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
       });
        
        System.out.println("Items length is: "+items1.length);
        
    }
}