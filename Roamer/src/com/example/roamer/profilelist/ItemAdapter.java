package com.example.roamer.profilelist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import com.example.roamer.R;

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
        
        //Find current location
        //String location = 

        int id = Integer.parseInt(Ids[position]);
        
        
  
        String imageFile = Model.GetbyId(id).IconFile;
        
        textView.setText(Model.GetbyId(id).Name);
        
        textViewLocation.setText(Model.GetbyId(id).Location);
      
        
        // get input stream
        InputStream ims = null;
        try {
            ims = context.getAssets().open(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(ims, null);
        // set image to ImageView
        imageView.setImageDrawable(d);
        return rowView;

    }

}