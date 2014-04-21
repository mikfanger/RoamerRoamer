package com.example.roamer;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	CheckBox cred;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;


	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	
	private String userName = "1";
	private String passWord = "1";

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	//private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	
	//Set detault user preferences

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_login);
		
		// Add your initialization code here
		Parse.initialize(this, "aK2KQsRgRhGl9HeQrmdQqsW1nNBtXqFSn8OIwgCV", "mN9kJJF96z4Qg5ypejlIqbBplY1zcXMYHYACJEFp");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
			    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
				
		ParseACL.setDefaultACL(defaultACL, true);
		
		
		
		cred = (CheckBox)findViewById(R.id.checkSaveLogin);
		
		
		
		int checkStatus = checkForSavedCred();
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.fillLocation);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.passwordLogin);	
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		
		if (checkStatus == 1){
			
			getCredLocally();
			mEmailView.setText(userName);
			mPasswordView.setText(passWord);
			cred.setChecked(true);
			
		}
		mLoginStatusView = findViewById(R.id.progressBar1);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.login).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
						query.whereEqualTo("Email", "jon@roamer.com");
						
						query.getFirstInBackground(new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							 if (e == null) {
								 							 
								//Start roamer creation
							    	attemptLogin();
								 
							    } else {
							    	
									 //Show toast of lacking network connection
									 Toast.makeText(getApplicationContext(), "No network connection!",
			    							   Toast.LENGTH_LONG).show();
									 
									 System.out.println("Network error is: "+e);
							    }		
						}
						});
						//attemptLogin();
					}
				});
		
		findViewById(R.id.newUser).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						
						//Check for network connection
						final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
						query.whereEqualTo("Email", "jon@roamer.com");
						
						query.getFirstInBackground(new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object, ParseException e) {
							 if (e == null) {
								 							 
								//Start roamer creation
							    	Intent i=new Intent(LoginActivity.this,ExplainationActivity.class);
					                startActivity(i);
								 
							    } else {
							    	
									 //Show toast of lacking network connection
									 Toast.makeText(getApplicationContext(), "No network connection!",
			    							   Toast.LENGTH_LONG).show();
									 
									 System.out.println("Network error is: "+e);
							    }		
						}
						});
						
						
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		System.out.println("Email from login is: "+mEmail);
		System.out.println("Password from login is: "+ mPassword);
		
		boolean cancel = false;
		View focusView = null;
		
		//Check for email and password in database
		checkPassword();
		
		
		checkEmail();
		mAuthTask = null;
		showProgress(false);
				
	
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		//Check that email address match
			if (!userName.equals(mEmail)) {
				
				mEmailView.setError("No record of email address");
				focusView = mEmailView;
				cancel = true;
			}
	
		
		//Check that password matches
		if (!passWord.equals(mPassword.trim())) {

			mPasswordView.setError("Password does not match");
			focusView = mPasswordView;
			cancel = true;
			}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) { 
				return false;
			}

			
				if (userName.equals(mEmail)) {
					// Account exists, return true if the password matches.
					return passWord.equals(mPassword);
				}

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				
				saveCredIfChecked();
				
				saveFromDatabaseToCred();
				
				//Update Roamer profile to increase login count
				updateLoginCount();
				
				finish();
				Intent i=new Intent(LoginActivity.this,HomeScreenActivity.class);
                startActivity(i);
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	public void saveCredIfChecked(){
		
		
		//Check to see if 'cred saved' box is checked
		int credSave = 0;
		
		SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		

		if(credSave == 1){
			cred.setChecked(true);
		}

		if(cred.isChecked()){
			
			Cursor c = myDB.rawQuery("SELECT  *  FROM " + "" + "MyCred", null); 
			
			if(c.getCount() > 1){
				
				ContentValues args = new ContentValues();
				args.put("Save", 1);
				myDB.update("MyCred", args, "rowid" + "=" + 1, null);
			}
			
			
			else{
				
				myDB.execSQL("INSERT INTO "
					       + "MyCred "
					       + "(Email,Password,Save) "
					       + "VALUES ('"+userName+"','"+passWord+"',"+1+");");
				
				myDB.close();	
			}
			
	        	
		}
		else{
			
			Cursor c = myDB.rawQuery("SELECT  *  FROM " + "" + "MyCred", null); 
			
			if(c.getCount() > 1){
				
				ContentValues args = new ContentValues();
				args.put("Save", 0);
				myDB.update("MyCred", args, "rowid" + "=" + 1, null);
			}
					
			else{
				
				myDB.execSQL("INSERT INTO "
					       + "MyCred "
					       + "(Email,Password,Save) "
					       + "VALUES ('"+userName+"','"+passWord+"',"+0+");");
				
				myDB.close();	
			}
		}
	}
	
	public int checkForSavedCred(){
		  int cred = 0;
		
		  SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);

		  Cursor c = myDB.rawQuery("SELECT * FROM MyCred WHERE rowid = "+ 1, null);
		  
		  c.moveToFirst();
		  int Column1 = c.getColumnIndex("Save");
		  
		  cred = c.getInt(Column1);
		      
		  myDB.close();
		  
		return cred;
	}
	
	public void getCredLocally(){
		  SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		  Cursor c = myDB.rawQuery("SELECT * FROM " + "MyCred ", null);
		  c.moveToFirst();
		  
		  int user = c.getColumnIndex("Email");
		  int pass = c.getColumnIndex("Password");
		  
		  userName = c.getString(user);
		  passWord = c.getString(pass);
		  
		  myDB.close();
	}
	
	public void saveFromDatabaseToCred(){
		
		
		final SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", mEmail);
		
		query.getFirstInBackground(new GetCallback<ParseObject>() {

		@Override
		public void done(ParseObject object, ParseException e) {
			 if (e == null) {
				 
				 //Get Data from Parse
				 String pEmail = object.getString("Email");
				 String pUsername = object.getString("Username");
				 String pPassword = object.getString("Password");
				 int pIndustry = object.getInt("Industry");
				 int pJob = object.getInt("Job");
				 int pAirline = object.getInt("Airline");
				 int pHotel = object.getInt("Hotel");
				 int pTravel = object.getInt("Travel");
				 int pLocation = object.getInt("Location");
				 int pCurrentLocation = object.getInt("CurrentLocation");
				 boolean pSex = object.getBoolean("Sex");
				 
				 int pSex1 = 1;
				 
				 if (!pSex){
					 pSex1 = 0;
				 }
				 
				 
				 ContentValues args = new ContentValues();
					args.put("Email", pEmail);
					args.put("Username", pUsername);
					args.put("Password", pPassword);
					args.put("Industry", pIndustry);
					args.put("Sex", pSex1);
					args.put("Job", pJob);
					args.put("Travel", pTravel);
					args.put("Hotel", pHotel);
					args.put("Air", pAirline);
					args.put("Loc", pLocation);
					args.put("CurrentLocation", pCurrentLocation);
					
					myDB.update("MyCred", args, "rowid" + "=" + 1, null);
					
				 myDB.close();
				 
			    } else {
			    	//Do nothing
			    }		
		}
		});
	}
	
public void checkEmail(){

		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", mEmail);
		

		try {
			query.getFirst();
			userName = mEmail;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

public void checkPassword(){
	
	final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
	query.whereEqualTo("Password", mPassword);
	
	try {
		query.getFirst();
		passWord = mPassword;
		
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public void updateLoginCount(){
	
	
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
	query.whereEqualTo("Email", mEmail);
	
	query.getFirstInBackground(new GetCallback<ParseObject>() {
	  public void done(ParseObject roamer, ParseException e) {
	    if (roamer == null) {
	    	Log.d("score", "Error: " + e.getMessage()); 

	    } else {
	    	 int i = roamer.getInt("LoginCount");
	    	 roamer.put("LoginCount",i+1);
		     roamer.saveInBackground();
	    }
	  }
	});
	
	}
	
}
