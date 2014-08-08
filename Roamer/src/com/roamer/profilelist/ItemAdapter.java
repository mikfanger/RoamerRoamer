package com.roamer.profilelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import com.roamer.R;

public class ItemAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] Ids;
    private final int rowResourceId;

    public ItemAdapter(Context context, int textViewResourceId, String[] objects) {

        super(context, textViewResourceId, objects);

        this.context = context;
        this.Ids = objects;
        this.rowResourceId = textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(rowResourceId, parent, false);
        
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageHostPicture);
        TextView textView = (TextView) rowView.findViewById(R.id.textViewName);
        TextView textViewLocation = (TextView) rowView.findViewById(R.id.textViewPosition);
        
        textViewLocation.setText("Nothing");
        textView.setText("Nothing");
        byte[] imageFile = null;
        int id = Integer.parseInt(Ids[position]);
        
        try{
        	
        	textViewLocation.setText(Model.GetbyId(id).Location);
        }
        catch(NullPointerException e){
        	textViewLocation.setText("none");
        }
        try{
        	
        	textView.setText(Model.GetbyId(id).Name);
        }
        catch(NullPointerException e){
        	textView.setText("none");
        }

        int noPic = 0;
        try{
        	imageFile = Model.GetbyId(id).IconFile;
        }
        catch (NullPointerException e){
        	noPic = 1;
        }
        
        
        
      //Set images either from database or default user.
        if(noPic == 0){
        	Bitmap bmp = BitmapFactory.decodeByteArray(imageFile, 0, imageFile.length);
    	    imageView.setBackgroundResource(0);
    	    imageView.setImageBitmap(bmp);
        }
        if(noPic == 1){
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
        return rowView;

    }

}