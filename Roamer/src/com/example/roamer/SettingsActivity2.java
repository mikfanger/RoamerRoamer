package com.example.roamer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class SettingsActivity2 extends Activity {

	ImageView ivGalImg;
	Bitmap bmp = null;;
	
	public int spinnerPos;
	public String pictureUri = "";
	public String emailAddress = "";
	public String userName = "";
	public byte[] picBytes;
	private View mSettingsView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings2);
		
		mSettingsView = findViewById(R.id.progressBarSettings);
		
		emailAddress = getEmailAddress();
		//Recall profile picture
		ivGalImg     =     (ImageView)findViewById(R.id.mapImage);
		
		//Get image from database
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", emailAddress);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		  public void done(ParseObject roamer, ParseException e) {
		    if (roamer == null) {
		    	Log.d("roamer", "Error: " + e.getMessage()); 

		    } else {
		    	 
		    	 roamer.getParseFile("Pic");
		    	 try {
					picBytes = roamer.getParseFile("Pic").getData();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	 
		    	 System.out.println("Byte array is: " + picBytes);
		    	 
		    	 bmp = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
		 	     ivGalImg.setBackgroundResource(0);
		 	     ivGalImg.setImageBitmap(bmp);
		    }
		  }
		});

		
		
		ImageButton introButton = (ImageButton) findViewById(R.id.findPicturePros);
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
            	showProgress(true); 	
            	
            	
            	final int industry;
				final int job;
				final int airline;
				final int hotel;
				final int travel;
            	
            	//commit selections
            	Spinner position = (Spinner) findViewById(R.id.spinnerIndustryProf);
            	spinnerPos = position.getSelectedItemPosition();
            	industry = spinnerPos;
            	
            	Spinner position2 = (Spinner) findViewById(R.id.spinnerJobProf);
            	spinnerPos = position2.getSelectedItemPosition();
            	job = spinnerPos;
            	
            	Spinner position3 = (Spinner) findViewById(R.id.spinnerAirlineProf);
            	spinnerPos = position3.getSelectedItemPosition();
            	airline = spinnerPos;
            	
            	Spinner position4 = (Spinner) findViewById(R.id.spinnerHotelProf);
            	spinnerPos = position4.getSelectedItemPosition();
            	hotel = spinnerPos;
            	
            	Spinner position5 = (Spinner) findViewById(R.id.spinnerTravelStatus);
            	spinnerPos = position4.getSelectedItemPosition();
            	travel = spinnerPos;
            	
            	//Save updated data to DB
            	ParseObject Roamer = new ParseObject("Roamer");
            	
            	final ParseFile imgFile = new ParseFile (userName+".png", picBytes);
         		try {
        			imgFile.save();
        		} catch (ParseException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}

         		
         		ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
         		query.whereEqualTo("Email", emailAddress);
         		
         		query.getFirstInBackground(new GetCallback<ParseObject>() {
         		  public void done(ParseObject Roamer, ParseException e) {
         		    if (Roamer == null) {
         		    	Log.d("score", "Error: " + e.getMessage()); 

         		    } else {
         		    	Roamer.put("Travel",travel);
                  		Roamer.put("Industry",industry);
                  		Roamer.put("Job",job);
                  		Roamer.put("Hotel",hotel);
                  		Roamer.put("Air",airline);
                  		Roamer.put("Pic",imgFile);
                  		
                  		showProgress(false);
         			    Roamer.saveInBackground();
         		    }
         		  }
         		});
            	
            	//Move to Home Screen
         		
         		
         		finish();
            	Intent i=new Intent(SettingsActivity2.this,HomeScreenActivity.class);
                startActivity(i);
            	           	
            }
        });
        
        ImageButton backButton = (ImageButton) findViewById(R.id.backProfile2);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent i=new Intent(SettingsActivity2.this,SettingsActivity.class);
                startActivity(i);
            	
            	
            }
        });
        
        Spinner job = (Spinner) findViewById(R.id.spinnerJobProf);
        //Prepare adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData itemss[] = new MyData[8];
        itemss[0] = new MyData("Select Position", "value1");
        itemss[1] = new MyData("Accounting", "value2");
        itemss[2] = new MyData("Marketing", "value3");
        itemss[3] = new MyData("Consultant", "value4");
        itemss[4] = new MyData("Lawyer", "value5");
        itemss[5] = new MyData("Sales", "value6");
        itemss[6] = new MyData("Doctor", "value7");
        itemss[7] = new MyData("Scientist", "value8");
        ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, itemss);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        job.setAdapter(adapter1);
        job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = itemss[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustryProf);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items2[] = new MyData[14];
        items2[0] = new MyData("Select Industry", "value1");
        items2[1] = new MyData("Aerospace/Defense", "value2");
        items2[2] = new MyData("Automotive", "value3");
        items2[3] = new MyData("Banking", "value4");
        items2[4] = new MyData("Consumer Products", "value5");
        items2[5] = new MyData("Insurance", "value6");
        items2[6] = new MyData("Media & Design", "value7");
        items2[7] = new MyData("Oil & Gas", "value8");
        items2[8] = new MyData("Power & Utilities", "value9");
        items2[9] = new MyData("Real Estate", "value10");
        items2[10] = new MyData("Government", "value11");
        items2[11] = new MyData("Student", "value12");
        items2[12] = new MyData("Travel/Hospitality", "value13");
        items2[13] = new MyData("Information Technology", "value14");
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
        
        Spinner travel = (Spinner) findViewById(R.id.spinnerTravelStatus);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items5[] = new MyData[6];
        items5[0] = new MyData("Select Status", "value1");
        items5[1] = new MyData("0%-10%   Explorer", "value2");
        items5[2] = new MyData("10%-30%  Excursionist", "value3");
        items5[3] = new MyData("40%-60%  Wanderer", "value4");
        items5[4] = new MyData("60%-80%  Nomad", "value5");
        items5[5] = new MyData("80%-100% Globetrotter", "value6");
        ArrayAdapter<MyData> adapter5 = new ArrayAdapter<MyData>(this,
                android.R.layout.simple_spinner_item, items5);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travel.setAdapter(adapter5);
        travel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                MyData d = items5[position];

                //Get selected value of key 
                String value = d.getValue();
                String key = d.getSpinnerText();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        });
        
        Spinner airline = (Spinner) findViewById(R.id.spinnerAirlineProf);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items3[] = new MyData[11];
        items3[0] = new MyData("Select Airline", "value1");
        items3[1] = new MyData("Frontier", "value2");
        items3[2] = new MyData("Virgin", "value3");
        items3[3] = new MyData("JetBlue", "value2");
        items3[4] = new MyData("Alaska", "value3");
        items3[5] = new MyData("Southwest", "value3");
        items3[6] = new MyData("Delta", "value3");
        items3[7] = new MyData("Airtran", "value3");
        items3[8] = new MyData("U.S. Airways", "value3");
        items3[9] = new MyData("American Airlines", "value3");
        items3[10] = new MyData("United", "value3");
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
        
        Spinner hotel = (Spinner) findViewById(R.id.spinnerHotelProf);
        //Prepar adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items4[] = new MyData[8];
        items4[0] = new MyData("Select Hotel", "value1");
        items4[1] = new MyData("Hilton", "value2");
        items4[2] = new MyData("Marriott", "value3");
        items4[3] = new MyData("Wyndham", "value4");
        items4[4] = new MyData("Choice", "value5");
        items4[5] = new MyData("Starwood", "value6");
        items4[6] = new MyData("Hyatt", "value7");
        items4[7] = new MyData("Intercontinental", "value8");
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
        
        
		//Recall spinner data
        
        SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);

		Cursor c = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
		  
		c.moveToFirst();
		int Column1 = c.getColumnIndex("Industry");
		int Column2 = c.getColumnIndex("Job");
		int Column3 = c.getColumnIndex("Air");
		int Column4 = c.getColumnIndex("Hotel");
		int Column5 = c.getColumnIndex("Travel");
		  
		int jobPos = c.getInt(Column2);
		int industryPos = c.getInt(Column1);
		int hotelPos = c.getInt(Column4);
		int airPos = c.getInt(Column3);
		int travelPos = c.getInt(Column5);
		      
		myDB.close();
    	Spinner position = (Spinner) findViewById(R.id.spinnerIndustryProf);
    	position.setSelection(industryPos);
    	
    	Spinner position2 = (Spinner) findViewById(R.id.spinnerJobProf);
    	position2.setSelection(jobPos);
    	
    	Spinner position3 = (Spinner) findViewById(R.id.spinnerAirlineProf);
    	position3.setSelection(airPos);
    	
    	Spinner position4 = (Spinner) findViewById(R.id.spinnerHotelProf);
    	position4.setSelection(hotelPos);
    	
    	Spinner position5 = (Spinner) findViewById(R.id.spinnerTravelStatus);
    	position5.setSelection(travelPos);
	}
	
	
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_activity2, menu);
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
		                
		                
		                ByteArrayOutputStream stream = new ByteArrayOutputStream();
		                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		                // get byte array here
		                picBytes= stream.toByteArray();

		                //picFile = buffer.array(); //Get the underlying array containing the data.
		                
		          }
		          else 
		          {
		              Log.d("Status:", "Photopicker canceled");            
		          }
		      }
	     
	     
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
	 public String getEmailAddress(){
		 String email = "";
		 
		 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
	     Cursor cur = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
	     cur.moveToFirst();

	     int indexEmail;
	     int indexName;
	     indexEmail = cur.getColumnIndex("Email");
	     indexName = cur.getColumnIndex("Username");
	    	
	     email = cur.getString(indexEmail);
	     userName = cur.getString(indexName);
		 
		 return email;
	 }
	 
	 /**
		 * Shows the progress UI and hides the form.
		 */
		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		private void showProgress(final boolean show) {
			// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
			// for very easy animations. If available, use these APIs to fade-in
			// the progress spinner.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				int mediumAnimTime = getResources().getInteger(
						android.R.integer.config_mediumAnimTime);

				mSettingsView.setVisibility(View.VISIBLE);
				mSettingsView.animate().setDuration(mediumAnimTime)
						.alpha(show ? 1 : 0)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mSettingsView.setVisibility(show ? View.VISIBLE
										: View.GONE);
							}
						});

			} else {
				// The ViewPropertyAnimator APIs are not available, so simply show
				// and hide the relevant UI components.
				mSettingsView.setVisibility(show ? View.VISIBLE : View.GONE);
			}
		}

}
