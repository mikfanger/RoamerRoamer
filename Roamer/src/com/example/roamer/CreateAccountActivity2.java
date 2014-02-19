package com.example.roamer;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CreateAccountActivity2 extends Activity {

	public int spinnerPos;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	        setContentView(R.layout.activity_create_account2);
	        
	        
	        ImageButton introButton = (ImageButton) findViewById(R.id.submitInfo2);
	        introButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Spinner position = (Spinner) findViewById(R.id.spinnerRegion);
	            	spinnerPos = position.getSelectedItemPosition();
	            	PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("RoamerRegion", spinnerPos).commit();
	            	Intent i=new Intent(CreateAccountActivity2.this,CreateAccountActivityPic.class);
	                startActivity(i);
	            }
	        });
	        
	        ImageButton backButton = (ImageButton) findViewById(R.id.backButton2);
	        backButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	Intent i=new Intent(CreateAccountActivity2.this,CreateAccountActivity.class);
	                startActivity(i);
	            }
	        });
	        
	        //Region
	        Spinner position = (Spinner) findViewById(R.id.spinnerRegion);
	        //Prepar adapter 
	        //HERE YOU CAN ADD ITEMS WHICH COMES FROM SERVER.
	        final MyData items1[] = new MyData[5];
	        items1[0] = new MyData("Midwest", "value1");
	        items1[1] = new MyData("West", "value2");
	        items1[2] = new MyData("Southwest", "value3");
	        items1[3] = new MyData("Southeast", "value2");
	        items1[4] = new MyData("Northeast", "value3");
	        ArrayAdapter<MyData> adapter1 = new ArrayAdapter<MyData>(this,
	                android.R.layout.simple_spinner_item, items1);
	        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        position.setAdapter(adapter1);
	        
	        position.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            public void onItemSelected(AdapterView<?> parent, View view,
	                    int position, long id) {
	               // MyData d = items1[position];

	                //Get selected value of key 
	                //String value = d.getValue();
	                //String key = d.getSpinnerText();
	            }

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
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
	    
	    public void importPicture(){
	    	
	    }

	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	    
	    
	}
