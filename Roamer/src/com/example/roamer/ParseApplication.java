package com.example.roamer;



import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
	super.onCreate();

	// Initialize the Parse SDK.
	Parse.initialize(this, "aK2KQsRgRhGl9HeQrmdQqsW1nNBtXqFSn8OIwgCV", "mN9kJJF96z4Qg5ypejlIqbBplY1zcXMYHYACJEFp"); 
	PushService.setDefaultPushCallback(this, IntroActivity.class);

	// Specify an Activity to handle all pushes by default.
	PushService.setDefaultPushCallback(this, IntroActivity.class);
  }

}
