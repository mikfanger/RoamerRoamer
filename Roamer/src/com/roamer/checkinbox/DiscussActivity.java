package com.roamer.checkinbox;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.roamer.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class DiscussActivity extends Activity{
	
	private static DiscussArrayAdapter adapter;
	private static ListView lv;
	private EditText editText1;
	private String chatName = "none";
	private String myName = "none";
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_discuss);
		
		//Set chat name from Temp Roamer
		final SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
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
 
		adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);
		
		addItems();
    	
    	//Load list from saved chats with user (if any)
		lv = (ListView) findViewById(R.id.listView1);



		lv.setAdapter(adapter);
		lv.setDivider(null);

		editText1 = (EditText) findViewById(R.id.editTextMessage);
		
		//Scroll to bottom of list
		lv.smoothScrollToPosition(adapter.getCount());

		ImageButton sendButton = (ImageButton) findViewById(R.id.imageButtonSendMessage);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	//get username of current user
            	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
            	cur.moveToFirst();

            	int indexUser = cur.getColumnIndex("Username");
            	String currentUsername = cur.getString(indexUser);
            	
            	adapter.add(new OneComment(false, editText1.getText().toString(),"Me"));

				String phrase = editText1.getText().toString();
				writeChatToDb(phrase, 2);
				editText1.setText("");
				
				//Scroll to bottom of list
				lv.smoothScrollToPosition(adapter.getCount());
				
				JSONObject data = null; 
				
				try {
					data = new JSONObject();
					data.put("from", currentUsername);
					data.put("badge", "Increment");
					data.put("alert", phrase);
					data.put("action", "UPDATE_STATUS");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Set the first name/pair
				
				       		
        		ParsePush push = new ParsePush();
            	push.setChannel(chatName);
            	push.setData(data);

            	//push.setMessage(phrase);
            	push.sendInBackground();   
            	
            	
            	//update date of latest chat
            	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
            	String currentDateandTime = sdf.format(new Date());
            	
            	
            	ContentValues values = new ContentValues();
            	values.put("Field3", currentDateandTime);
            	
            	myDB.update("ChatTable", values, "Field1 = '" + chatName +"'", null);            	
            	            	            		  
            }
        });
        		
		ImageButton inboxButton = (ImageButton) findViewById(R.id.imageBackFromMessages);
        inboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  	
            	
            	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
            	query.whereEqualTo("Username", chatName);
            	
            	query.getFirstInBackground(new GetCallback<ParseObject>() {
             		  public void done(ParseObject Roamer, ParseException e) {
             		    if (Roamer == null) {
             		    	Log.d("score", "Error: " + e.getMessage()); 
             		    	
             		    } else {
             		    	Roamer.put("newMessage", true);
             		    	Roamer.saveInBackground();
             		    }
             		  }
             		});
            	
            	ContentValues values = new ContentValues();
            	values.put("Field4", 0);
            	
            	myDB.update("ChatTable", values, "Field1 = '" + chatName +"'", null);
            	
            	finish();
            	Intent i=new Intent(DiscussActivity.this,ChatsAndRequestsActivity.class);
                startActivity(i);
                myDB.close();
            }
        });
        
        InputMethodManager imm = (InputMethodManager)getSystemService(
        	      this.INPUT_METHOD_SERVICE);
        	imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
        lv.smoothScrollToPosition(adapter.getCount());
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

		   Cursor c = myDB.rawQuery("SELECT COUNT(*) FROM "+chatName, null);
				   

		   int isNotEmpty = 0;
	    	
	    	
	    	if (c != null) {
	    	    c.moveToFirst();                       // Always one row returned.
	    	    if (c.getInt (0) != 0) {               // Zero count means empty table.
	    	    	isNotEmpty = 1;
	    	    }
	    	}
	    	
	    	c = myDB.rawQuery("SELECT * FROM " + chatName, null);
	    	
		   int Column1 = c.getColumnIndex("Field1");
		   
		   int Column2 = c.getColumnIndex("Field2");

		   // Check if our result was valid.
		   c.moveToFirst();

		   if (isNotEmpty == 1) {
		    // Loop through all Results
		    do {
		     String Comment = c.getString(Column1);
		     
		     int Side = c.getInt(Column2);
		     
		     if(Side == 1){
		    	 side = true;
		     }
		     if(Side == 2){
		    	 side = false;
		     }
		     
		     String newComment = Comment.replace("%@%","'");
		     
		     adapter.add(new OneComment(side, newComment, chatName));
		     
		    }while(c.moveToNext());
		   }
		   
		   myDB.close();
		} 
	
	static void addForeignChat(String name, String message){
		
		if (adapter != null){
			adapter.add(new OneComment(true, message, name));
			lv.smoothScrollToPosition(adapter.getCount());
	
		}
	}
	
	public void onBackPressed() 
	{
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
		ContentValues values = new ContentValues();
    	values.put("Field4", 0);
    	
    	myDB.update("ChatTable", values, "Field1 = '" + chatName +"'", null);
    	
    	myDB.close();
    	
    	finish();
		 Intent i=new Intent(DiscussActivity.this,ChatsAndRequestsActivity.class);
	    startActivity(i);
	}
}
