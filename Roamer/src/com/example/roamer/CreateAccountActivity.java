package com.example.roamer;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




public class CreateAccountActivity extends Activity {

	private EditText newEmailAddress;
	private EditText newUsername;
	private EditText newPassword;
	private EditText newConfirmPassword;
	private ImageView termsButton;
	private ImageButton exitTermsButton;
	
	final Context context = this;
	
	private String mEmailAddress;
	private String mUsername;
	private String mPassword;
	private String mConfirmPassword;
	private String error;
	private int sex;
	
	private static final String passNotEqual = "Passwords must match!";
	private static final String noTerms = "You must agree to Terms and Conditions!";
	private static final String sexWrong = "Cannot be male and female!";
	private static final String noSex = "Must select a sex!";
	private static final String fieldEmpty = "All fields must be filled!";
    private static final String invalidEmail = "Email addresses much contain @ symbol!";
    private static final String emailExists = "Email address already exists!";
    private static final String invalidUsername = "Username must not have spaces!";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_account);
        
        ImageButton introButton = (ImageButton) findViewById(R.id.submitInfo);
        introButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	submitInfo();
            }
        });
        
        exitTermsButton = (ImageButton) findViewById(R.id.imageFinishTerms1);
        termsButton = (ImageView) findViewById(R.id.imageTermsAndConditions);
        ImageButton terms = (ImageButton) findViewById(R.id.buttonTermsConditions);
        
        terms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	
            	Dialog dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.terms_and_conditions);
    			dialog.setTitle("Terms and Conditions");
    			dialog.show();
    			
            	//exitTermsButton.setVisibility(View.VISIBLE);
            	//termsButton.setVisibility(View.VISIBLE);
            	//termsButton.bringToFront();
            	//exitTermsButton.bringToFront();
            	
            }
        });
        
        
        exitTermsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	exitTermsButton.setVisibility(View.INVISIBLE);
            	termsButton.setVisibility(View.INVISIBLE);
            }
        });
        
        TextView myTextView=(TextView)findViewById(R.id.textStepTwo);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Eurostile.ttf");
        myTextView.setTypeface(typeFace);
        
       // TextView t2 = (TextView) findViewById(R.id.textWhereFrom);
       //t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
    

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void submitInfo()
    {
    	newPassword = (EditText) findViewById(R.id.newPassword);
    	newUsername = (EditText) findViewById(R.id.newUsername);
    	newConfirmPassword = (EditText) findViewById(R.id.newConfirmPassword);
    	newEmailAddress = (EditText) findViewById(R.id.newEmail);
    	
    	mEmailAddress = newEmailAddress.getText().toString();
    	mPassword = newPassword.getText().toString();
    	mUsername = newUsername.getText().toString();
    	mConfirmPassword = newConfirmPassword.getText().toString();
    	
    	boolean cancel = false;
    	
    	if (TextUtils.isEmpty(mEmailAddress)) {
			cancel = true;
			error = fieldEmpty;
		}
    	if (TextUtils.isEmpty(mPassword)) {
			cancel = true;
			error = fieldEmpty;
		}
    	if (TextUtils.isEmpty(mConfirmPassword)) {
			cancel = true;
			error = fieldEmpty;
		}
    	if (TextUtils.isEmpty(mUsername)) {
			cancel = true;
			error = fieldEmpty;
		}
    	if(!mConfirmPassword.equals(mPassword))
    	{
    		cancel = true;
    		error = passNotEqual;
    	}
    	if (!mEmailAddress.contains("@"))
    	{
    		 cancel = true;
    		 error = invalidEmail;
    	}
    	if (mUsername.contains(" "))
    	{
    		cancel = true;
    		error = invalidUsername;
    	}
    	
    	//Check that email address does not yet exist
    	final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", mEmailAddress);
		
		try {
			query.getFirst();
			cancel = true;
			error = emailExists;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	 
    	 CheckBox female = (CheckBox)findViewById(R.id.checkFemale);
    	 CheckBox male = (CheckBox)findViewById(R.id.checkMale);
    	 CheckBox terms = (CheckBox)findViewById(R.id.agreeCheck);
    	 if (female.isChecked() && male.isChecked())
    	 {
    		 cancel = true;
    		 error = sexWrong;
    	 }
    	 
    	 if (!female.isChecked() && !male.isChecked())
    	 {
    		 cancel = true;
    		 error = noSex;
    	 }
    	 if (!terms.isChecked())
    	 {
    		 cancel = true;
    		 error = noTerms;
    	 }
    	 if (female.isChecked())
    	 {
    		 sex = 0;
    	 }
    	 if (male.isChecked())
    	 {
    		 sex = 1;
    	 }
		
    	//Check that all fields are filled out
    	if (cancel == false)
    	{
    		//Commit user preferences to database
    		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    		
    		myDB.delete("TempRoamer",null,null);
    	    myDB.execSQL("INSERT INTO "
    				       + "TempRoamer "
    				       + "(Email,Password,Username,Sex) "
    				       + "VALUES ('"+mEmailAddress+"','"+mPassword+"','"+mUsername+"',"+sex+");");
    			
    			myDB.close();	
    			
        		Intent i=new Intent(CreateAccountActivity.this,CreateAccountActivity2.class);
                startActivity(i);   			
    		}
    		

    	else
    	{
    		Context context = getApplicationContext();
            CharSequence text = error;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, 250, 130);
            toast.show();
    	}
    	
    }
    
    
    
}
