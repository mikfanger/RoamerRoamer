package com.example.roamer;


import java.io.FileNotFoundException;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class CreateAccountActivityPic extends Activity {

	
	ImageView ivGalImg;
	Bitmap bmp = null;;
	
	public int spinnerPos;
	public String pictureUri = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account_pic);
		
		ivGalImg     =     (ImageView)findViewById(R.id.mapImage);
		
		ImageButton introButton = (ImageButton) findViewById(R.id.findPicture);
        introButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            	       	
            }
        });
        
        ImageButton finishButton = (ImageButton) findViewById(R.id.finishProfile);
        finishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	//commit selections
            	Spinner position = (Spinner) findViewById(R.id.spinnerIndustry);
            	spinnerPos = position.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerIndustry", spinnerPos).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerIndustry",1));
            	
            	Spinner position2 = (Spinner) findViewById(R.id.spinnerJob);
            	spinnerPos = position2.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerJob", spinnerPos).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerJob",1));
            	
            	Spinner position3 = (Spinner) findViewById(R.id.spinnerAirline);
            	spinnerPos = position3.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerAirline", spinnerPos).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerAirline",1));
            	
            	Spinner position4 = (Spinner) findViewById(R.id.spinnerHotel);
            	spinnerPos = position4.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerHotel", spinnerPos).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerHotel",1));
            	
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("RoamerPicture", pictureUri).commit();
            	System.out.println("Uri location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("RoamerPicture",""));
            	
            	//Move to Home Screen
            	Intent i=new Intent(CreateAccountActivityPic.this,HomeScreenActivity.class);
                startActivity(i);
            	           	
            }
        });
        
        ImageButton backButton = (ImageButton) findViewById(R.id.backProfile2);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(CreateAccountActivityPic.this,CreateAccountActivity2.class);
                startActivity(i);
            	
            	
            }
        });
        
        Spinner position = (Spinner) findViewById(R.id.spinnerJob);
        //Prepare adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items[] = new MyData[6];
        items[0] = new MyData("Select Position", "value1");
        items[1] = new MyData("Accounting", "value2");
        items[2] = new MyData("Marketing", "value3");
        items[3] = new MyData("Consultant", "value4");
        items[4] = new MyData("Garbage Man", "value5");
        items[5] = new MyData("Sales", "value6");
        ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, items);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(adapter1);
        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = items[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items2[] = new MyData[6];
        items2[0] = new MyData("Select Industry", "value1");
        items2[1] = new MyData("Medical", "value2");
        items2[2] = new MyData("Finance", "value3");
        items2[3] = new MyData("Software", "value4");
        items2[4] = new MyData("Manufacturing", "value5");
        items2[5] = new MyData("Biotech", "value6");
        ArrayAdapter<MyData> adapter2 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        industry.setAdapter(adapter2);
        industry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = items2[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        Spinner airline = (Spinner) findViewById(R.id.spinnerAirline);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items3[] = new MyData[6];
        items3[0] = new MyData("Select Airline", "value1");
        items3[1] = new MyData("Jet Blue", "value2");
        items3[2] = new MyData("Southwest", "value3");
        items3[3] = new MyData("Delta", "value2");
        items3[4] = new MyData("United", "value3");
        items3[5] = new MyData("American", "value3");
        ArrayAdapter<MyData> adapter3 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, items3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        airline.setAdapter(adapter3);
        airline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = items3[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        Spinner hotel = (Spinner) findViewById(R.id.spinnerHotel);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items4[] = new MyData[6];
        items4[0] = new MyData("Select Hotel", "value1");
        items4[1] = new MyData("Hilton", "value2");
        items4[2] = new MyData("Starwood", "value3");
        items4[3] = new MyData("Marriott", "value2");
        items4[4] = new MyData("Doubletree", "value3");
        items4[5] = new MyData("Brothel", "value3");
        ArrayAdapter<MyData> adapter4 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, items4);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hotel.setAdapter(adapter4);
        hotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = items4[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_account_activity_pic, menu);
		return true;
	}
	
	//Meant for processing the photo
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	      
	      if (requestCode == 1) 
	      {
	          if (data != null && resultCode == RESULT_OK) 
	          {              
	              
	                Uri selectedImage = data.getData();
	                
	                String[] filePathColumn = {MediaStore.Images.Media.DATA};
	                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	                cursor.moveToFirst();
	                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                //pictureUri = cursor.getString(columnIndex);
	                pictureUri = selectedImage.toString();
	                
	                cursor.close();
	              
	                if(bmp != null && !bmp.isRecycled())
	                {
	                    bmp = null;                
	                }
	                                
	                try {
						bmp = decodeUri(selectedImage);
					} catch (FileNotFoundException e) { 
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                ivGalImg.setBackgroundResource(0);
	                ivGalImg.setImageBitmap(bmp);              
	          }
	          else 
	          {
	              Log.d("Status:", "Photopicker canceled");            
	          }
	      }
     
     
    }

    //UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
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
    
    public void importPicture(){
    	
    }
    
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
               || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }

}
