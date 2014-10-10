package com.roamer.checkinbox;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roamer.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.HomeScreenActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
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
    private ImageButton dialogButton;
    private TextView textRoamers;
    private Dialog dialog;
    private int count;
    private String chatName;
    ArrayList<Item> loadArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.inbox_list);
        
        //Remove notifications.
		NotificationManager notifManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancelAll();
		

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
            	final String name1 = Model.GetbyId(pos+1).Name;
            	final Dialog dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.dialog_clear_chat);
    			dialog.setTitle("Clear Chat History");
    			
    			dialog.show();
    			
    			Button dialogButton = (Button) dialog.findViewById(R.id.imageClearChat);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    		
    					
    					//name = MyRoamerModel.GetbyId(pos+1).Name;
    					clearChat(name1);
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
        		addToTempRoamer(chatName);
        		
        		try {
					createTable(chatName);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		getChatFromTable(chatName);
        		finish();
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
    			
    			try {
					populateRoamers(getDialog());
				} catch (JSONException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			//position = (Spinner) dialog.findViewById(R.id.spinnerSelectRoamer);
    			
    			
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    	           
    					String newName = getSelectedName();
    					try {
							createTable(newName);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					addToTempRoamer(newName);    	
    					
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
		db.delete(name, null, null);
		db.delete("ChatTable", "Field1" + "='" + name + "'", null);
		
		Cursor c = db.rawQuery("SELECT * FROM MyCred WHERE rowid = 1", null);
    	
    	c.moveToFirst();
    	int index;
    	index = c.getColumnIndex("ChatCount");
    	int count = c.getInt(index);
		
		ContentValues args = new ContentValues();
    	args.put("ChatCount",count-1);
		db.update("MyCred", args, "rowid" + "=" + 1, null);
		
		db.close();
		finish();
		Intent i=new Intent(InboxActivity.this,ChatsAndRequestsActivity.class);
        startActivity(i);
    }
    
    public void getChatFromTable(String name){
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);

    	db.delete("TempRoamer",null,null);
    	//insert name into temp roamer
    	db.execSQL("INSERT INTO "
			       + "TempRoamer "
			       + "(Username) "
			       + "VALUES ('"+name+"');");
    }
    
    
    public void createTable(String tableName) throws ParseException{
    	boolean nameExists = false;
    	SQLiteDatabase db = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	String tableName2 = tableName.replace(" ", "");
    	System.out.println("table name will be: "+tableName2);
    	
    	db.execSQL("CREATE TABLE IF NOT EXISTS "
		          + tableName2
		          + " (Field1 VARCHAR,Field2 VARCHAR);");
    	
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
    	Cursor c = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    	String currentDateandTime = sdf.format(new Date());
    	
    	//check if there is an open chat with this user
    	int index = 0;
    	c = db.rawQuery("SELECT * FROM " + "MyCred ", null);
		c.moveToFirst();
		index = c.getColumnIndex("ChatCount");
		int count = c.getInt(index);
    	int isNotEmpty = 0;
    	c = db.rawQuery("SELECT COUNT(*) FROM ChatTable", null);
    	
    	if (c != null) {
    	    c.moveToFirst();                       // Always one row returned.
    	    if (c.getInt (0) != 0) {               // Zero count means empty table.
    	    	isNotEmpty = 1;
    	    }
    	}
    	
    	String tempName = "";
    	
    	if (isNotEmpty == 1){
    		
    		c = db.rawQuery("SELECT * FROM ChatTable", null);
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
    		
    		String sql                      =   "INSERT INTO ChatTable (Field1,Field2,Field3) VALUES(?,?,?)";
    	    SQLiteStatement insertStmt      =   db.compileStatement(sql);
    	    insertStmt.clearBindings();
    	    insertStmt.bindString(1,tableName);
    	    insertStmt.bindBlob(2,picFile);
    	    insertStmt.bindString(3,currentDateandTime);

    	    insertStmt.executeInsert();
    	
    	
    	ContentValues args1 = new ContentValues();
    	
    	c = db.rawQuery("SELECT * FROM " + "MyCred ", null);
		c.moveToFirst();
		index = c.getColumnIndex("ChatCount");
		int countNew = c.getInt(index);
    	args1.put("ChatCount",countNew+1);
		db.update("MyCred", args1, "rowid" + "=" + 1, null);
		
    	}
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
    
    public void populateRoamers(Dialog dialog) throws JSONException, ParseException{
    	
    
    position = (Spinner) dialog.findViewById(R.id.spinnerSelectRoamer);
    dialogButton = (ImageButton) getDialog().findViewById(R.id.imageStartMessage);
    textRoamers = (TextView) getDialog().findViewById(R.id.textViewNoRoamers);
     
    SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
	
	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
	cur.moveToFirst();
	int index;
	index = cur.getColumnIndex("Username");
	String userName = cur.getString(index);
	String credName = userName;
		
	
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
   	query.whereEqualTo("Username", userName);
   	
   	final ParseObject Roamer = query.getFirst();
   	
   	
   	JSONArray roamerList = Roamer.getJSONArray("MyRoamers");
  	
	int i = 0;
	if (roamerList != null && roamerList.length()>0) {
		
	    System.out.println("Roamer list is not null or 0");
		dialogButton.setVisibility(View.VISIBLE);
		position.setVisibility(View.VISIBLE);
		textRoamers.setVisibility(View.INVISIBLE);
		
		final MyData items1[] = new MyData[roamerList.length()];
		items1[i] = new MyData(roamerList.get(i).toString(),"Value1");
		
		while(i < roamerList.length()-1){
			i=i+1;
			items1[i] = new MyData(roamerList.get(i).toString(),"Value1");
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
		   	    	
		position.setVisibility(View.INVISIBLE);
		dialogButton.setVisibility(View.INVISIBLE);
		textRoamers.setVisibility(View.VISIBLE);
	}
  	   myDB.close();
    }
    
    public void loadArray(){
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	
    	cur.moveToFirst();
    	int index;
    	int indexName;
    	index = cur.getColumnIndex("ChatCount");
    	indexName = cur.getColumnIndex("Username");
    	chatName = cur.getString(indexName);
    	count = cur.getInt(index);
    	
    	//If there are entries in MyRoamers, populate the list
    	loadArray = new ArrayList<Item>();
    	loadArray.clear();
    	
    	System.out.println("Chat Count is: "+count);
    	
    	Cursor chatCount = myDB.rawQuery("SELECT COUNT(*) FROM ChatTable", null);
    	int isNotEmpty = 0;
    	if (chatCount != null){
    		chatCount.moveToFirst();
    		if (chatCount.getInt(0) != 0){
    			isNotEmpty = 1;
    		}
    	}
		
		
    	if (isNotEmpty == 1) {
    		
    	int i = 1;

		Cursor c = myDB.rawQuery("SELECT * FROM " + "ChatTable", null);
		c.moveToFirst();
		
		 
		 int C1 = c.getColumnIndex("Field2");
		 int C2 = c.getColumnIndex("Field1");
		 int C3 = c.getColumnIndex("Field3");
		 int C4 = c.getColumnIndex("Field4");
		 		 
		 loadArray.add(new Item(i,c.getBlob(C1), c.getString(C2), c.getString(C3), c.getInt(C4)));
		
		while(c.moveToNext()){
			i++;
			
			
			  C1 = c.getColumnIndex("Field2");
			  C2 = c.getColumnIndex("Field1");
			  C3 = c.getColumnIndex("Field3");
			  C4 = c.getColumnIndex("Field4");
			 
			 loadArray.add(new Item(i,c.getBlob(C1), c.getString(C2), c.getString(C3),c.getInt(C4)));			
		}
		
		myDB.close();
		
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

	db.close();
	}
	
	public void onBackPressed() 
	{
		 Intent i=new Intent(InboxActivity.this,HomeScreenActivity.class);
	    startActivity(i);
	}
	
	//Process the push message and move to the discussion
	public void onResume(){
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			
			String fromData = "";
			String alertData = "";
			boolean actionChat = false;
			
			try {
				JSONObject json = new JSONObject(bundle.getString("com.parse.Data"));
				
				Iterator itr = json.keys();
			      while (itr.hasNext()) {
			        String key = (String) itr.next();
			        
			        if (key.equals("alert")){
			        	alertData = json.getString(key);
			        }
			        if (key.equals("from")){
			        	fromData = json.getString(key);
			        }
			        if(key.equals("action")){
			        	if(json.getString(key) == "UPDATE_STATUS"){
			        		actionChat = true;
			        	}
			        }
			      }
			      
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    String notificationData = bundle.getString("com.parse.Data");
		    if (notificationData != null) {
		        
		        //add this message to the table of the current chat if it is an action
		        if (!fromData.equals("") && actionChat == true ){
		        	addToTempRoamer(fromData);
			        
			        //either add this to the chat table or do not if it already exists
			        try {
						createTable(fromData);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			        finish();
			        Intent i=new Intent(InboxActivity.this,DiscussActivity.class);
				    startActivity(i);
			        
		        }  
		    }
		}
		super.onResume();
	}
	
	//Write chat to DB
	void writeChatToDb(String phrase, int type, String chatname) {
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		
		myDB.execSQL("INSERT INTO "
			       + chatname
			       + " (Field1,Field2) "
			       + "VALUES ('"+phrase.replace("'","%@%")+"',"+type+");");
			
		myDB.close();		
	}
}