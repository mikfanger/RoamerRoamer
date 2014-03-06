package com.example.roamer.checkinbox;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import com.example.roamer.HomeScreenActivity;
import com.example.roamer.R;
import com.example.roamer.checkinbox.InboxActivity.MyData;
import com.example.roamer.profilelist.Item;
import com.example.roamer.profilelist.MyRoamerModel;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class ChatsAndRequestsActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	final Context context = this;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats_and_requests);
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		Button messageButton = (Button) findViewById(R.id.newMessageButton);
        messageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	dialog = new Dialog(context);
            	
    			dialog.setContentView(R.layout.select_roamer_for_message);
    			dialog.setTitle("Select Roamer");

    			dialog.show();
    			
    			//populateRoamers(dialog);
    			ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.imageStartMessage);
    			// if button is clicked, close the custom dialog
    			dialogButton.setOnClickListener(new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					    	            	
    					//createTable(selectedName);
    					    					
    					dialog.dismiss();
    					Intent i=new Intent(ChatsAndRequestsActivity.this,DiscussActivity.class);
    	                startActivity(i);
    	            		  
    				}
    			});

            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chats_and_requests, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch(position) {

            case 0: 
            	Fragment fragment = new DummySectionFragment();
    			Bundle args = new Bundle();
    			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
    			fragment.setArguments(args);
    			return fragment;
            case 1: 
            	Fragment fragment1 = new RequestSectionFragment();
            	Bundle args1 = new Bundle();
            	args1.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            	fragment1.setArguments(args1);
            	return fragment1;
            default:
            	Fragment fragment2 = new DummySectionFragment();
    			Bundle args2 = new Bundle();
    			args2.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
    			fragment2.setArguments(args2);
    			return fragment2;
			}
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				//return "Open Chats".toUpperCase(l);
				return "";
			case 1:
				//return "Open Requests".toUpperCase(l);
				return "";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.inbox_list, container,
					false);

			return rootView;
		}
	}
	
	public static class RequestSectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		public RequestSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.request_list, container,
					false);

			return rootView;
		}
	}

}
