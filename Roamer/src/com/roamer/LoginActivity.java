package com.roamer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.roamer.R;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.RequestPasswordResetCallback;
import com.roamer.checkinbox.InboxActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
	private ImageButton mLogin;
	private EditText mPasswordView;
	//private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	final Context context = this;


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
		mLogin = (ImageButton) findViewById(R.id.login);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.passwordLogin);	
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							
							mLogin.setEnabled(false);
							try {
								attemptLogin();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
							    	try {
										attemptLogin();
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

							    } else {
							    	
									 //Show toast of lacking network connection
									 Toast.makeText(context, "No network connection!",
			    							   Toast.LENGTH_LONG).show();

									 System.out.println("Network error is: "+e);
							    }		
						}
						});
						//attemptLogin();
					}
				});

		findViewById(R.id.imageButtonForgotPassword).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						 //Start roamer creation
						final Dialog dialog = new Dialog(context);
						dialog.setContentView(R.layout.activity_forgot_password);
		    			dialog.setTitle("Forgot Password");

		    			dialog.show();

		    			final EditText emailText = (EditText) dialog.findViewById(R.id.editTextForgotPassword);
		    			ImageButton submit= (ImageButton) dialog.findViewById(R.id.imageButtonRequestPassword);
		    			submit.setOnClickListener(new OnClickListener() {
		    				@Override
		    				public void onClick(View v) {

		    					final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
								query.whereEqualTo("Email", emailText.getText().toString());
								String password = "does not exist";

								try {
									ParseObject roamer = query.getFirst();
									password = roamer.getString("Password");
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								//Request password reset
								ParseUser.requestPasswordResetInBackground(emailText.getText().toString(),
									new RequestPasswordResetCallback() {
									public void done(ParseException e) {
										if (e == null) {
											// An email was successfully sent with reset instructions.
											Toast.makeText(getApplicationContext(), "An email has been sent with your password.",
							                		   Toast.LENGTH_LONG).show();
											
											dialog.dismiss();
										} else {
											// Something went wrong. Look at the ParseException to see what's up.
											
											Toast.makeText(getApplicationContext(), "Could not find the email address!",
							                		   Toast.LENGTH_LONG).show();
										}
									}
								});      		   
		    				}
		    			});

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
								    finish();
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
	 * @throws ParseException 
	 */
	
	public void attemptLogin() throws ParseException {

		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		final View focusViewFinal = mEmailView;
		View focusView = null;

		//Check for email and password in database
		//checkEmailandPassword();
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
		
		ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	
			    	if(user.getBoolean("emailVerified")){
				    	mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
				    	
				    	userName = mEmail;
				    	passWord = mPassword;
						showProgress(true);
						mAuthTask = new UserLoginTask();
						mAuthTask.execute((Void) null);
					    // Hooray! The user is logged in.
			    	}
			    	else{
					    // Signup failed. Look at the ParseException to see what happened.
				    	mEmailView.setError(getString(R.string.error_email_not_verified));
				    	focusViewFinal.requestFocus();
						mLogin.setEnabled(true);
			    	}

			    } else {
			        // Signup failed. Look at the ParseException to see what happened.
			    	mEmailView.setError(getString(R.string.error_invalid_email_or_password));
			    	focusViewFinal.requestFocus();
					mLogin.setEnabled(true);
			    }
			  }
			});
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
			int mediumAnimTime = getResources().getInteger(
					android.R.integer.config_mediumAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(mediumAnimTime)
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
				Thread.sleep(3000);
			} catch (InterruptedException e) { 
				return false;
			}

				System.out.println("Username and email are: "+userName+", "+mEmail);
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

				try {
					saveFromDatabaseToCred();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
		
		//empty the myroamers table.
		myDB.delete("MyRoamers", null, null);

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

				//myDB.close();	
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

				//myDB.close();	
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

	public void saveFromDatabaseToCred() throws ParseException{


		final SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", mEmail);
		
		Cursor checkEmail = myDB.rawQuery("SELECT * FROM " + "MyCred ", null);
		checkEmail.moveToFirst();

		int user = checkEmail.getColumnIndex("Email");

		String userEmailNew = checkEmail.getString(user);
		
		if (!mEmail.equals(userEmailNew)){
			myDB.delete("ChatTable", null, null);
		}
		
		
		ParseObject object = query.getFirst();

			      //Get data from Parse
			    	JSONArray  requestList = object.getJSONArray("SentRequests");
					 JSONArray  eventList = object.getJSONArray("MyEvents");
					 
					 myDB.delete("MyRoamers", null, null);  
					 myDB.delete("MyEvents", null, null); 

				    	String sentList = "none,none";
				    	int newIndex = 0;
				    	if(requestList != null){
				    		while (newIndex < requestList.length()){
					    		try {
									sentList = sentList+","+requestList.get(newIndex).toString();
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
					    		newIndex++;
					    	}
				    	}


					 String pEmail = object.getString("Email");
					 String pUsername = object.getString("Username");
					 System.out.println("Username is: "+ pUsername);
					 String pPassword = object.getString("Password");
					 int pIndustry = object.getInt("Industry");
					 int pJob = object.getInt("Job");
					 int pAirline = object.getInt("Airline");
					 int pHotel = object.getInt("Hotel");
					 int pTravel = object.getInt("Travel");
					 int pLocation = object.getInt("Location");
					 int pCurrentLocation = object.getInt("CurrentLocation");
					 boolean pSex = object.getBoolean("Sex");
					 byte[] picByte = null;
					 try {
						picByte = object.getParseFile("Pic").getData();
					} catch (ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					 
					 //set email verified to true for searchability
					 object.put("EmailVerified", 1);
					 object.saveInBackground();


					 int pSex1 = 1;

					 if (!pSex){
						 pSex1 = 0;
					 }

					 int eventListCount = 0;
					 int roamerListCount = 0;
					 
					 if (eventList != null){
						 eventListCount = eventList.length();
					 }
					 
					 ContentValues args = new ContentValues();
						args.put("Email", pEmail);
						args.put("CountR", roamerListCount);
						args.put("Username", pUsername);
						args.put("Password", mPassword);
						args.put("Industry", pIndustry);
						args.put("Sex", pSex1);
						args.put("Job", pJob);
						args.put("Travel", pTravel);
						args.put("Hotel", pHotel);
						args.put("Air", pAirline);
						args.put("Loc", pLocation);
						args.put("CurrentLocation", pCurrentLocation);
						args.put("SentRequests", sentList);
						args.put("Pic", picByte);

						myDB.update("MyCred", args, "rowid" + "=" + 1, null);

						myDB.close();
		
		 //Set push notification to this installation
		 ParseInstallation installation = ParseInstallation.getCurrentInstallation();
			
		 ArrayList<String> channelList = new ArrayList();
		 
		 channelList.add("none");
		 installation.put("channels", channelList);
		 try {
			installation.save();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 ParseInstallation installation2 = ParseInstallation.getCurrentInstallation();
		 installation2.addUnique("channels", pUsername);
		 installation2.saveInBackground();
		 
		 PushService.subscribe(context, pUsername, InboxActivity.class);

	}

public void checkEmailandPassword(){

		View focusView = null;
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", mEmail);


		try {
			ParseObject newName = query.getFirst();
			userName = mEmail;
			if (mPassword.equals(newName.getString("Password"))){
				passWord = mPassword;
			}
			else{
				//set error
				mPasswordView.setError("Password does not match");
				focusView = mPasswordView;
			}
		} catch (ParseException e1) {
			// set error
			mEmailView.setError("Email not found");
			focusView = mEmailView;
			e1.printStackTrace();
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


	public void onBackPressed() 
	{
		finish();
		Intent homeIntent= new Intent(Intent.ACTION_MAIN);
		homeIntent.addCategory(Intent.CATEGORY_HOME);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(homeIntent);
	}

}