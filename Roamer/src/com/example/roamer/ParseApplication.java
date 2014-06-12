package com.example.roamer;


import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
	super.onCreate();

	// Initialize the Parse SDK.
	Parse.initialize(this, "aK2KQsRgRhGl9HeQrmdQqsW1nNBtXqFSn8OIwgCV", "mN9kJJF96z4Qg5ypejlIqbBplY1zcXMYHYACJEFp"); 
	
	ParseUser.enableAutomaticUser();
	ParseACL defaultACL = new ParseACL();
	// Optionally enable public read access while disabling public write access.
	defaultACL.setPublicReadAccess(true);
	defaultACL.setPublicWriteAccess(true);
	ParseACL.setDefaultACL(defaultACL, true);

	// Specify an Activity to handle all pushes by default.
	//PushService.setDefaultPushCallback(this, InboxActivity.class);
	
	
	
  }

}
