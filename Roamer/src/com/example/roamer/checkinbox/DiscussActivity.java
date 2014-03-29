package com.example.roamer.checkinbox;

import com.example.roamer.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class DiscussActivity extends Activity {
	
	private DiscussArrayAdapter adapter;
	private ListView lv;
	private EditText editText1;
	private String chatName;
	

     

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_discuss);
		
		//Set chat name from Temp Roamer
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
		Cursor c = myDB.rawQuery("SELECT * FROM TempRoamer", null);
    	
    	c.moveToFirst();
    	int index;
    	index = c.getColumnIndex("Username");
    	chatName = c.getString(index);

    	//Load list from saved chats with user (if any)
		lv = (ListView) findViewById(R.id.listView1);

		adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);

		lv.setAdapter(adapter);
		lv.setDivider(null);

		editText1 = (EditText) findViewById(R.id.editTextMessage);
		editText1.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					adapter.add(new OneComment(false, editText1.getText().toString()));
					
					String phrase = editText1.getText().toString();
					writeChatToDb(phrase, 2);
					editText1.setText("");
					
					return true;
					
				}
				return false;
			}
			
		});

		addItems();
		
		ImageButton inboxButton = (ImageButton) findViewById(R.id.imageBackFromMessages);
        inboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(DiscussActivity.this,ChatsAndRequestsActivity.class);
                startActivity(i);
            		  
            }
        });
	}
	
	void writeChatToDb(String phrase, int type) {
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
		myDB.execSQL("INSERT INTO "
			       + chatName
			       + " (Field1,Field2) "
			       + "VALUES ('"+phrase+"',"+type+");");
			
		myDB.close();		
	}

	private void addItems() {
		boolean side = true;
		
		/*retrieve data from database */
		
		   SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);

		   Cursor c = myDB.rawQuery("SELECT * FROM " + chatName+" ", null);

		   int Column1 = c.getColumnIndex("Field1");
		   
		   int Column2 = c.getColumnIndex("Field2");

		   // Check if our result was valid.
		   c.moveToFirst();
		   if (c != null && Column1 !=0 && c.getCount()>1) {
		    // Loop through all Results
		    do {
		     String Comment = c.getString(Column1);
		     int Side = c.getInt(Column2);
		     if(Side == 1){
		    	 side = true;
		     }
		     else{
		    	 side = false;
		     }
		     adapter.add(new OneComment(side, Comment));
		    }while(c.moveToNext());
		   }
		   
		   myDB.close();
		}
}
