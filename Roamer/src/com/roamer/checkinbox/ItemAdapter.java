package com.roamer.checkinbox;

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
        
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageInboxPicture);
        TextView textView = (TextView) rowView.findViewById(R.id.textInboxName);
        TextView textDate= (TextView) rowView.findViewById(R.id.textRecentDate);
        ImageView imageNewMessage = (ImageView) rowView.findViewById(R.id.imageViewNewMessage);

        int id = Integer.parseInt(Ids[position]);
        byte[] imageFile = Model.GetbyId(id).IconFile;

        textView.setText(Model.GetbyId(id).Name);
        textDate.setText(Model.GetbyId(id).Date);
        
        if(Model.GetbyId(id).NewMessage == 1){
        	imageNewMessage.setVisibility(View.VISIBLE);
        }
        if(Model.GetbyId(id).NewMessage == 0){
        	imageNewMessage.setVisibility(View.INVISIBLE);
        }
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
        return rowView;

    }

}