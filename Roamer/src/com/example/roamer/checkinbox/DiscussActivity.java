package com.example.roamer.checkinbox;


import org.json.JSONException;
import org.json.JSONObject;

import com.example.roamer.R;
import com.google.gson.JsonObject;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class DiscussActivity extends Activity {
	
	private DiscussArrayAdapter adapter;
	private ListView lv;
	private EditText editText1;
	private String chatName = "none";
	private String myName = "none";
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_discuss);
		
		//Set chat name from Temp Roamer
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
		Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
    	cur.moveToFirst();
    	
    	int myNameIndex = cur.getColumnIndex("Username");
    	myName = cur.getString(myNameIndex);
		
		Cursor c = myDB.rawQuery("SELECT  *  FROM " + "" + "TempRoamer", null);
    	
    	c.moveToFirst();
    	int index;
    	int index2;
    	index = c.getColumnIndex("Username");

    	chatName = c.getString(index);
    	System.out.println("Chat name is: "+chatName);
		adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
		
		addItems();
    	
    	//Load list from saved chats with user (if any)
		lv = (ListView) findViewById(R.id.listView1);



		lv.setAdapter(adapter);
		lv.setDivider(null);

		editText1 = (EditText) findViewById(R.id.editTextMessage);
		
		ImageButton sendButton = (ImageButton) findViewById(R.id.imageButtonSendMessage);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	adapter.add(new OneComment(false, editText1.getText().toString()));

				String phrase = editText1.getText().toString();
				writeChatToDb(phrase, 2);
				editText1.setText("");
				
            	ParsePush push = new ParsePush();
            	
            	JSONObject data = null;
				try {
					data = new JSONObject(myName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            	push.setChannel(chatName);
            	//push.setData(data);
            	push.setData(data);
            	push.setMessage(phrase);
            	push.sendInBackground();   
            	
            	
            		  
            }
        });
        		
		ImageButton inboxButton = (ImageButton) findViewById(R.id.imageBackFromMessages);
        inboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
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
			       + "VALUES ('"+phrase.replace("'","%@%")+"',"+type+");");
			
		myDB.close();		
	}

	private void addItems() {
		boolean side = true;
		
		/*retrieve data from database */
		
		   SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);

		   Cursor c = myDB.rawQuery("SELECT * FROM " + chatName, null);

		   int Column1 = c.getColumnIndex("Field1");
		   
		   int Column2 = c.getColumnIndex("Field2");

		   // Check if our result was valid.
		   c.moveToFirst();

		   if (c != null && c.getCount()>1) {
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
		     
		     String newComment = Comment.replace("%@%","'");
		     
		     adapter.add(new OneComment(side, newComment));
		    }while(c.moveToNext());
		   }
		   
		   myDB.close();
		}
}
