package com.roamer;



import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import com.roamer.R;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CreateAccountActivityPic extends Activity {

	
	ImageView ivGalImg;
	Bitmap bmp = null;;
	
	public int spinnerPos;
	public String pictureUri = "";
	public int airline;
	public int hotel;
	public int job;
	public int industry;
	public int travel;
	public byte[] picFile;
	private ImageButton finishButton;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_account_pic);
		
		ivGalImg     =     (ImageView)findViewById(R.id.mapImage);
		
		//initially set the user pic to the default picture.
		Drawable d = ivGalImg.getDrawable();
		Bitmap bitmap  = ((BitmapDrawable)d).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		picFile = stream.toByteArray();
		
		ImageButton introButton = (ImageButton) findViewById(R.id.findPicture);
        introButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            	       	
            }
        });
        
        finishButton = (ImageButton) findViewById(R.id.finishProfile);
        finishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finishButton.setEnabled(false);
            	//commit selections
            	Spinner position = (Spinner) findViewById(R.id.spinnerIndustry);
            	industry = position.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerIndustry", industry).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerIndustry",1));
            	
            	Spinner position2 = (Spinner) findViewById(R.id.spinnerJob);
            	job = position2.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerJob", job).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerJob",1));
            	
            	Spinner position3 = (Spinner) findViewById(R.id.spinnerAirline);
            	airline = position3.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerAirline", airline).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerAirline",1));
            	
            	Spinner position4 = (Spinner) findViewById(R.id.spinnerHotel);
            	hotel = position4.getSelectedItemPosition();
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerHotel", hotel).commit();
            	System.out.println("Spinner location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt("RoamerHotel",1));
            	
            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("RoamerPicture", pictureUri).commit();
            	System.out.println("Uri location is:   " +PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("RoamerPicture",""));
            	
            	Spinner position5 = (Spinner) findViewById(R.id.spinnerSetTravelStatus);
            	travel = position5.getSelectedItemPosition();
            	
            	
            	if (industry == 0 || job == 0){
            		
            		finishButton.setEnabled(true);
            		Toast.makeText(getApplicationContext(), "All starred fields must be updated!",
         				   Toast.LENGTH_LONG).show();
            	}
            	else{
            		
            		Toast toast = Toast.makeText(getApplicationContext(), "New user created!  An email has been sent.",
         				   Toast.LENGTH_LONG);
            		toast.setGravity(Gravity.CENTER, 0, 0);
            		toast.show();
            		
            		enterInfo();
            		
                	//Move back to login screen
                	Intent i=new Intent(CreateAccountActivityPic.this,LoginActivity.class);
                    startActivity(i);
            	}           	        	           	
            }
        });
        
        Spinner position = (Spinner) findViewById(R.id.spinnerJob);
        //Prepare adapter 
        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
        final MyData items[] = new MyData[17];
        items[0] = new MyData("Not Selected", "value1");
        items[1] = new MyData("Accounting", "value2");
        items[2] = new MyData("Customer Service", "value3");
        items[3] = new MyData("Engineering/Manufacturing", "value4");
        items[4] = new MyData("Finance", "value5");
        items[5] = new MyData("Health/Human Services", "value6");
        items[6] = new MyData("IT", "value7");
        items[7] = new MyData("Legal", "value8");
        items[8] = new MyData("Maintenance", "value9");        
        items[9] = new MyData("Management", "value9");
        items[10] = new MyData("Marketing", "value9");
        items[11] = new MyData("Operations", "value9");
        items[12] = new MyData("Research and Development", "value9");
        items[13] = new MyData("Sales", "value9");
        items[14] = new MyData("Shipping/Logistics", "value9");
        items[15] = new MyData("Transportation", "value9");
        items[16] = new MyData("Other", "value9");

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
        final MyData items2[] = new MyData[34];
        items2[0] = new MyData("Select Industry", "value1");
        items2[1] = new MyData("Agriculture", "value2");
        items2[2] = new MyData("Accounting", "value3");
        items2[3] = new MyData("Advertising", "value4");
        items2[4] = new MyData("Aeorspace & Defense", "value5");
        items2[5] = new MyData("Aircraft & Airline", "value6");
        items2[6] = new MyData("Automotive", "value7");
        items2[7] = new MyData("Banking & Finance", "value8");
        items2[8] = new MyData("Biotechnology", "value9");
        items2[9] = new MyData("Consumer Products", "value10");
        items2[10] = new MyData("Chemical", "value11");
        items2[11] = new MyData("Consulting", "value12");
        items2[12] = new MyData("Education", "value13");
        items2[13] = new MyData("Engineering", "value14");       
        items2[14] = new MyData("Entertainment", "value1");
        items2[15] = new MyData("Government", "value2");
        items2[16] = new MyData("Healthcare", "value3");
        items2[17] = new MyData("Insurance", "value4");
        items2[18] = new MyData("Legal", "value5");
        items2[19] = new MyData("Marketing", "value6");
        items2[20] = new MyData("Medical Products", "value7");
        items2[21] = new MyData("Media & Design", "value8");
        items2[22] = new MyData("Oil & Gas", "value9");
        items2[23] = new MyData("Power & Utilities", "value10");
        items2[24] = new MyData("Recruiting", "value11");
        items2[25] = new MyData("Real Estate", "value12");
        items2[26] = new MyData("Retail & Wholesale", "value13");
        items2[27] = new MyData("Service", "value14");        
        items2[28] = new MyData("Sports", "value9");
        items2[29] = new MyData("Student", "value10");
        items2[30] = new MyData("Technology", "value11");
        items2[31] = new MyData("Transportation", "value12");
        items2[32] = new MyData("Travel & Hospitality", "value13");
        items2[33] = new MyData("Other", "value13");
        
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
        
        Spinner hotel = (Spinner) findViewById(R.id.spinnerHotel);
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
        
        Spinner travel = (Spinner) findViewById(R.id.spinnerSetTravelStatus);
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
	                picFile= stream.toByteArray();

	                //picFile = buffer.array(); //Get the underlying array containing the data.
	                
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
    
    public void enterInfo(){
    	 SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	 
    	 myDB.execSQL("INSERT INTO "
			       + "MyCred "
			       + "(Email) "
			       + "VALUES ('"+"temp"+"');");
  		
		 ContentValues args = new ContentValues();
		 args.put("Pic", pictureUri);
		 myDB.update("TempRoamer", args, "rowid" + "=" + 1, null);
		
		 ContentValues args1 = new ContentValues();
		 args1.put("Pic", pictureUri);
		 args1.put("CurrentLocation", "Boston");
		 
		 
		 Cursor c = myDB.rawQuery("SELECT  *  FROM " + "" + "TempRoamer", null);
 		 c.moveToFirst();
 		 
 		int index1 = c.getColumnIndex("Email");
 		int index2 = c.getColumnIndex("Password");
 		int index3 = c.getColumnIndex("Username");
 		int index4 = c.getColumnIndex("Sex");
 		int index5 = c.getColumnIndex("Loc");
 		
 		String email = c.getString(index1);
 		String password = c.getString(index2);
 		String username = c.getString(index3);
 		int sex = c.getInt(index4);
 		String loc = c.getString(index5);
 		
		//enter user information into ParseUser
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		 		 
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		      // Hooray! Let them use the app now.
		    } else {
		      // Sign up didn't succeed. Look at the ParseException
		      // to figure out what went wrong
		    }
		  }
		});
 		
 		boolean sexbool = true;
 		
 		if(sex == 0){
 			sexbool = false;
 		}
 		
 		args1.put("Email",email);
 		args1.put("Password",password);
 		args1.put("Username",username);
 		args1.put("Sex",sexbool);
 		args1.put("Loc",loc);
 		args1.put("Travel",travel);
 		args1.put("Industry",industry);
 		args1.put("Job",job);
 		args1.put("Hotel",hotel);
 		args1.put("Air",airline);
 		args1.put("ChatCount",00);
 		args1.put("CurrentLocation",00);
 		args1.put("Start","today");
 		myDB.update("MyCred", args1, "rowid" + "=" + 1, null);
 		 
 		
 		int location = c.getInt(index4);
 		myDB.close();	
 		
 		//Enter data into parse as new user
 		
 		ParseFile imgFile = new ParseFile (username+".png", picFile);
 		try {
			imgFile.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 		ParseObject user1 = new ParseObject("Roamer");
		user1.put("Username", username);
		user1.put("Password", password);
		user1.put("Email", email);
		user1.put("Location", location);
		user1.put("Travel", travel);
		user1.put("Industry", industry);
		user1.put("CurrentLocation", 0);
		user1.put("Job", job);
		user1.put("Hotel", hotel);
		user1.put("Male", sexbool);
		user1.put("Airline", airline);
		user1.put("LoginCount", 1);
		user1.put("EmailVerified",0);
		user1.put("Pic", imgFile);
		
		ParseUser.enableAutomaticUser();
		
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access while disabling public write access.
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		user1.setACL(defaultACL);
        
		user1.saveInBackground();
 	     
    }
    
}
