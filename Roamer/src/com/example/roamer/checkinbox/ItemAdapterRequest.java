package com.example.roamer.checkinbox;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.roamer.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ItemAdapterRequest extends ArrayAdapter<String> {

	
    private final Context context;
    private final String[] Ids;
    private final int rowResourceId;
    private String credName;

    public ItemAdapterRequest(Context context, int textViewResourceId, String[] objects) {

        super(context, textViewResourceId, objects);

        this.context = context;
        this.Ids = objects;
        this.rowResourceId = textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(rowResourceId, parent, false);
        
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageInboxPicture);
        final TextView textView = (TextView) rowView.findViewById(R.id.textInboxName);
        TextView textDate= (TextView) rowView.findViewById(R.id.textRecentDate);

        int id = Integer.parseInt(Ids[position]);
        byte[] imageFile = ModelRequest.GetbyId(id).IconFile;
        credName = ModelRequest.GetbyId(id).CredName;

        textView.setText(ModelRequest.GetbyId(id).Name);
        //textDate.setText(ModelRequest.GetbyId(id).StartDate);
        // get input stream
        if(imageFile != null){
        	Bitmap bmp = BitmapFactory.decodeByteArray(imageFile, 0, imageFile.length);
    	    imageView.setBackgroundResource(0);
    	    imageView.setImageBitmap(bmp);
        }
        if(imageFile == null){
        	InputStream ims = null;
            try {
                ims = context.getAssets().open("default_userpic.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        }
        
        ImageButton acceptButton = (ImageButton) rowView.findViewById(R.id.imageButtonAccept);
        acceptButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	           	
            	
            	try {
					addToArray((String) textView.getText());
					deleteFromArray((String) textView.getText());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	Toast.makeText(context, "Roamer added to MyRoamers!",
            			   Toast.LENGTH_LONG).show();
            }
        });
        
        
        
        ImageButton rejectButton = (ImageButton) rowView.findViewById(R.id.imageButtonDecline);
        rejectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	           	
            	try {
					deleteFromArray((String) textView.getText());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	Toast.makeText(context, "Roamer request rejected!",
            			   Toast.LENGTH_LONG).show();
            }
        });
        return rowView;

    }
    
    public void deleteFromArray(String name) throws ParseException, JSONException{
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
    	
    	System.out.println("Username to delete is: "+name);
       	query.whereEqualTo("Username", credName);
       	
       	final ParseObject Roamer = query.getFirst();
       	
       	JSONArray roamerList = Roamer.getJSONArray("Requests");
		ArrayList<String> newList = new ArrayList();
		    	
		if( roamerList!=null ){
			int i = 0;			
			while(i < roamerList.length()){
				System.out.println("Roamer being analyzed is: "+roamerList.get(i).toString());
				if(!(roamerList.get(i).equals(name))){
					newList.add(roamerList.get(i).toString()); 
				}
				i++;
			}
		}
		
		//Update roamer with new list.
       	Roamer.put("Requests", newList);
       	Roamer.save();
       	
    }
    
 public void addToArray(String name) throws ParseException, JSONException{
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
    	
    	System.out.println("Username to add is: "+name);
       	query.whereEqualTo("Username", credName);
       	
       	final ParseObject Roamer = query.getFirst();
       	
       	JSONArray roamerList = Roamer.getJSONArray("MyRoamers");
		ArrayList<String> newList = new ArrayList();
		    	
		if( roamerList!=null ){
			int i = 0;			
			while(i < roamerList.length()){
					newList.add(roamerList.get(i).toString()); 
				i++;
			}
		}
		
		newList.add(name);
		
		//Update roamer with new list.
       	Roamer.put("MyRoamers", newList);
       	Roamer.save();
       	
       	
       	//Add to his roamer list as well
       	ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Roamer");
    	
       	query2.whereEqualTo("Username", name);
       	
       	final ParseObject Roamer2 = query2.getFirst();
       	
       	AddActivity.addToDatabase(name);
       	
       	JSONArray roamerList2= Roamer2.getJSONArray("MyRoamers");
		ArrayList<String> newList2 = new ArrayList();
		    	
		if( roamerList!=null ){
			int i = 0;			
			while(i < roamerList.length()){
					newList2.add(roamerList.get(i).toString()); 
				i++;
			}
		}
		
		newList2.add(credName);
		
		//Update roamer with new list.
       	Roamer2.put("MyRoamers", newList2);
       	Roamer2.save();
    }
 
 
 public static class AddActivity extends Activity {
	 
	 	
	 public static void addToDatabase(String name) throws ParseException{
		 ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Roamer");
		 	
	    	query2.whereEqualTo("Username", name);
	    	
	    	final ParseObject Roamer2 = query2.getFirst();
	    	
			int sex = 0;
			 if(Roamer2.getBoolean("Sex") == true){
				 sex = 1;
			 }
			 byte[] picFile = null;
			try {
				picFile = Roamer2.getParseFile("Pic").getData();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//Convert Start date to actual string
			int day = Roamer2.getCreatedAt().getDay();
	   	int month = Roamer2.getCreatedAt().getMonth();
	   	int year = Roamer2.getCreatedAt().getYear();
	   	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);
			
	   	SQLiteDatabase myDB2 = SQLiteDatabase.openOrCreateDatabase("RoamerDatabase", null);
		       	String sql =   "INSERT INTO MyRoamers(Pic,Username,Loc,Start,Industry,Sex,Job,Travel,Hotel,Air) VALUES(?,?,?,?,?,?,?,?,?,?)";
		        SQLiteStatement insertStmt      =   myDB2.compileStatement(sql);
		        insertStmt.clearBindings();
		        insertStmt.bindBlob(1,picFile);
		        insertStmt.bindString(2,Roamer2.getString("Username"));
		        insertStmt.bindLong(3, Roamer2.getInt("CurrentLocation"));
		        insertStmt.bindString(4, fullDate);
		        insertStmt.bindLong(5, Roamer2.getInt("Industry"));
		        insertStmt.bindLong(6, sex);
		        insertStmt.bindLong(7, Roamer2.getInt("Job"));
		        insertStmt.bindLong(8, Roamer2.getInt("Travel"));
		        insertStmt.bindLong(9, Roamer2.getInt("Hotel"));
		        insertStmt.bindLong(10, Roamer2.getInt("Air"));
		        insertStmt.executeInsert();
				
		        myDB2.close();
		 
	 }	 		 
 }

}