package com.roamer.checkinbox;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.View;


public class ParseBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "MyCustomReceiver";
	
	private SQLiteDatabase myDB;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		myDB = context.openOrCreateDatabase("RoamerDatabase", 0, null);
		try {
			if (intent == null)
			{
				Log.d(TAG, "Receiver intent null");
			}
			else
			{
				String action = intent.getAction();
				Log.d(TAG, "got action " + action );
				if (action.equals("UPDATE_STATUS"))
				{
					String channel = intent.getExtras().getString("com.parse.Channel");
					JSONObject data = new JSONObject(intent.getExtras().getString("com.parse.Data"));
					
					String fromName = data.getString("from");
					String message = data.getString("alert");
					
					createTable(fromName);
					
					setNewMessage(fromName);
					
					DiscussActivity.addForeignChat(fromName,message);
					myDB.execSQL("INSERT INTO "
						       + fromName
						       + " (Field1,Field2) "
						       + "VALUES ('"+message.replace("'","%@%")+"',"+1+");");
					
					//Set message notification to show
					ContentValues values = new ContentValues();
	            	values.put("Field4", 1);
	            	
	            	myDB.update("ChatTable", values, "Field1 = '" + fromName +"'", null);
	            	
					myDB.close();	
					
				}
			}

		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createTable(String tableName) throws ParseException{
    	boolean nameExists = false;
    	Cursor c = null;
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
       	query.whereEqualTo("Username", tableName);
       	
       	ParseObject roamer = query.getFirst();
       	
    	byte[] picFile = null;
    	try {
				picFile = roamer.getParseFile("Pic").getData();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    	String currentDateandTime = sdf.format(new Date());
    	
    	//check if there is an open chat with this user
    	int index = 0;
    	c = myDB.rawQuery("SELECT * FROM " + "MyCred ", null);
		c.moveToFirst();
		index = c.getColumnIndex("ChatCount");
		int count = c.getInt(index);
    	int isNotEmpty = 0;
    	c = myDB.rawQuery("SELECT COUNT(*) FROM ChatTable", null);
    	
    	if (c != null) {
    	    c.moveToFirst();                       // Always one row returned.
    	    if (c.getInt (0) != 0) {               // Zero count means empty table.
    	    	isNotEmpty = 1;
    	    }
    	}
    	
    	String tempName = "";
    	
    	if (isNotEmpty == 1){
    		
    		c = myDB.rawQuery("SELECT * FROM ChatTable", null);
    		c.moveToFirst();
    		
    		int i = c.getColumnIndex("Field1");
    		tempName = c.getString(i);
    		
    		//Check that name is not already an existing chat
    		if(tempName.trim().equals(tableName.trim())){
    			nameExists = true;
    		}
    		
    		while(c.moveToNext() && !tempName.trim().equals(tableName.trim())){
        		i = c.getColumnIndex("Field1");
        		tempName = c.getString(i);
        		if(tempName.trim().equals(tableName.trim())){
        			nameExists = true;
        		}
        	}
    	}
    	
    	if (nameExists == false){
    		
    		String sql                      =   "INSERT INTO ChatTable (Field1,Field2,Field3,Field4) VALUES(?,?,?,?)";
    	    SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
    	    insertStmt.clearBindings();
    	    insertStmt.bindString(1,tableName);
    	    insertStmt.bindBlob(2,picFile);
    	    insertStmt.bindString(3,currentDateandTime);
    	    insertStmt.bindDouble(4,1);

    	    insertStmt.executeInsert();
    	
    	
    	ContentValues args1 = new ContentValues();
    	
    	c = myDB.rawQuery("SELECT * FROM " + "MyCred ", null);
		c.moveToFirst();
		index = c.getColumnIndex("ChatCount");
		int countNew = c.getInt(index);
    	args1.put("ChatCount",countNew+1);
		myDB.update("MyCred", args1, "rowid" + "=" + 1, null);
		
    	}
    }	
	
	public void setNewMessage(String chatName){
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
	}
}
