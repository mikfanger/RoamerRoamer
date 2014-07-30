package com.example.roamer.checkinbox;

import android.content.Context;
import android.content.Intent;

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

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.example.roamer.events.AllEvents;
import com.example.roamer.events.EventsActivity;
import com.example.roamer.profilelist.RoamerProfileShortActivity;
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
					deleteFromArray((String) textView.getText());
					addToArray((String) textView.getText());
					
				    
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	Intent intent = new Intent();
            	
            	
            	intent.setClass(context, ChatsAndRequestsActivity.class);
            	startActivity(intent);
            	
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
       	
       	Roamer.addUnique("MyRoamers", name);
       	
       	Roamer.save();
       	
       	
       	//Add to his roamer list as well
       	ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Roamer");
    	
       	query2.whereEqualTo("Username", name);
       	
       	final ParseObject Roamer2 = query2.getFirst();
       	
       	//Add this to MyRoamers database
       	RequestsActivity request = new RequestsActivity();
       	request.addToDatabase(name);
       	
       	Roamer2.addUnique("MyRoamers", credName);

       	Roamer2.save();
    }

private void startActivity(Intent i) {
	// TODO Auto-generated method stub
	
}
}