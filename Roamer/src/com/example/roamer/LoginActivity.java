package com.example.roamer;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.roamer.network.GMailSender;
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
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
						query.whereEqualTo("Email", mEmailView.getText().toString());

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

								final String newpassword = password;
								final GMailSender sender = new GMailSender("roamerroamer1@gmail.com", "Roamer1234");
							    new AsyncTask<Void, Void, Void>() {
							        @Override public Void doInBackground(Void... arg) {
							            try {   
							                sender.sendMail("Password Retrieval",   
							                    "Hello!  Your current password is: "+ newpassword,   
							                    "noreply@roamer.com",   
							                    emailText.getText().toString());   
							                dialog.dismiss();
							                Toast.makeText(getApplicationContext(), "An email has been sent with your password.",
							                		   Toast.LENGTH_LONG).show();
							            } catch (Exception e) {   
							                Log.e("SendMail", e.getMessage(), e);   
							            }
										return null; 
							        }
							    }.execute();
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

	public void saveFromDatabaseToCred() throws ParseException{


		final SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
		final ParseQuery<ParseObject> query = ParseQuery.getQuery("Roamer");
		query.whereEqualTo("Email", mEmail);
		
		ParseObject object;
		
		object = query.getFirst();
				 //Get Data from Parse
				 JSONArray  roamerList = object.getJSONArray("MyRoamers");
				 JSONArray  requestList = object.getJSONArray("SentRequests");
				 JSONArray  eventList = object.getJSONArray("MyEvents");

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
					args.put("SentRequests", sentList);
					args.put("Pic", picByte);

					myDB.update("MyCred", args, "rowid" + "=" + 1, null);

				    //Load MyRoamers
					ArrayList<String> newList = new ArrayList();
					myDB.delete("MyRoamers", null, null);  

					if(eventList!=null){
						int i = 0;			
						while(i < eventList.length()){
							//Get roamer, noted as 'MyRoamer'
							final ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Event");
							try {
								query1.whereEqualTo("objectId", eventList.get(i).toString());
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							query1.getFirstInBackground(new GetCallback<ParseObject>() {

								@Override
								public void done(ParseObject object, ParseException e) {
									 if (e == null) {


										 byte[] picFile = null;
										try {
											picFile = object.getParseFile("Pic").getData();
										} catch (ParseException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

										int day = object.getDate("Date").getDay();
							        	int month = object.getDate("Date").getMonth();
							        	int year = object.getDate("Date").getYear();
							        	String fullDate = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year+1900);

									       	String sql =   "INSERT INTO MyEvents(Type,Location,Time,Date,Host,Attend,EventId,HostPic,Blurb) VALUES(?,?,?,?,?,?,?,?,?)";
									        SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
									        insertStmt.clearBindings();
									        insertStmt.bindString(1,ConvertCode.convertType(object.getInt("Type")));
									        insertStmt.bindString(2,ConvertCode.convertLocation(object.getInt("Location")));
									        insertStmt.bindString(3,ConvertCode.convertType(object.getInt("Time")));
									        insertStmt.bindString(4, fullDate);
									        insertStmt.bindString(5, object.getString("Host"));
									        insertStmt.bindLong(6, object.getLong("Attend")); 
									        insertStmt.bindString(7, object.getObjectId());
									        insertStmt.bindBlob(8, picFile);
									        insertStmt.bindString(9, object.getString("Desc"));
									        insertStmt.executeInsert();

										
									 }

								}
							});
							i++;
						}
					}

					
					if( roamerList!=null ){
						int i = 0;			
						while(i < roamerList.length()){
							//Get roamer, noted as 'MyRoamer'
							final ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Roamer");
							try {
								System.out.println("Roamer is: "+roamerList.get(i).toString());
								query1.whereEqualTo("Username", roamerList.get(i).toString());
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							ParseObject object1 = query1.getFirst();
							
							int sex = 0;
							 if(object1.getBoolean("Sex") == true){
								 sex = 1;
							 }
							 byte[] picFile = null;
							try {
								picFile = object1.getParseFile("Pic").getData();
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						       	String sql =   "INSERT INTO MyRoamers(Pic,Username,Loc,Start,Industry,Sex,Job,Travel,Hotel,Air) VALUES(?,?,?,?,?,?,?,?,?,?)";
						        SQLiteStatement insertStmt      =   myDB.compileStatement(sql);
						        insertStmt.clearBindings();
						        insertStmt.bindBlob(1,picFile);
						        insertStmt.bindString(2,object1.getString("Username"));
						        insertStmt.bindLong(3, object1.getInt("CurrentLocation"));
						        insertStmt.bindString(4, object1.getCreatedAt().toString());
						        insertStmt.bindLong(5, object1.getInt("Industry"));
						        insertStmt.bindLong(6, sex);
						        insertStmt.bindLong(7, object1.getInt("Job"));
						        insertStmt.bindLong(8, object1.getInt("Travel"));
						        insertStmt.bindLong(9, object1.getInt("Hotel"));
						        insertStmt.bindLong(10, object1.getInt("Air"));
						        insertStmt.executeInsert();
								
							i++;
						}
					}
			
	myDB.close();
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