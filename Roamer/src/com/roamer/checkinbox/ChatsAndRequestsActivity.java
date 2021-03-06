package com.roamer.checkinbox;

import java.util.Locale;

import com.roamer.R;
import com.roamer.HomeScreenActivity;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class ChatsAndRequestsActivity extends TabActivity implements
ActionBar.TabListener {

/**
* The {@link android.support.v4.view.PagerAdapter} that will provide
* fragments for each of the sections. We use a
* {@link android.support.v4.app.Fragment erAdapter} derivative, which
* will keep every loaded fragment in memory. If this becomes too memory
* intensive, it may be best to switch to a
* {@link android.support.v4.app.FragmentStatePagerAdapter}.
* 
* This will provide connection to both the Request Inbox and general 
*/
SectionsPagerAdapter mSectionsPagerAdapter;

/**
* The {@link ViewPager} that will host the section contents.
*/
ViewPager mViewPager;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_events);

Resources ressources = getResources(); 
TabHost tabHost = getTabHost(); 

// My Events tab
Intent intentAndroid = new Intent().setClass(this, InboxActivity.class);
TabSpec tabSpecAndroid = tabHost
  .newTabSpec("Inbox")
  .setIndicator("", ressources.getDrawable(R.drawable.inbox_light))
  .setContent(intentAndroid);

// All Events tab
Intent intentApple = new Intent().setClass(this, RequestsActivity.class);
TabSpec tabSpecApple = tabHost
  .newTabSpec("Requests")
  .setIndicator("", ressources.getDrawable(R.drawable.requests_light))
  .setContent(intentApple);


// add all tabs 
tabHost.addTab(tabSpecAndroid);
tabHost.addTab(tabSpecApple);

//set Windows tab as default (zero based)
tabHost.setCurrentTab(2);

tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_selector);
tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector);

tabHost.getTabWidget().setStripEnabled(false);

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.events, menu);
return true;
}

@Override
public void onTabSelected(ActionBar.Tab tab,
	FragmentTransaction fragmentTransaction) {
// When the given tab is selected, switch to the corresponding page in
// the ViewPager.
mViewPager.setCurrentItem(tab.getPosition());

Resources ressources = getResources(); 
TabHost tabHost = getTabHost(); 

// My Events tab
Intent intentAndroid = new Intent().setClass(this, InboxActivity.class);
TabSpec tabSpecAndroid = tabHost
  .newTabSpec("Inbox")
  .setIndicator("", ressources.getDrawable(R.drawable.inbox_light))
  .setContent(intentAndroid);

// All Events tab
Intent intentApple = new Intent().setClass(this, RequestsActivity.class);
TabSpec tabSpecApple = tabHost
  .newTabSpec("Requests")
  .setIndicator("", ressources.getDrawable(R.drawable.requests_light))
  .setContent(intentApple);


// add all tabs 
tabHost.addTab(tabSpecAndroid);
tabHost.addTab(tabSpecApple);

//set Windows tab as default (zero based)
tabHost.setCurrentTab(2);
}

@Override
public void onTabUnselected(ActionBar.Tab tab,
	FragmentTransaction fragmentTransaction) {
}

@Override
public void onTabReselected(ActionBar.Tab tab,
	FragmentTransaction fragmentTransaction) {
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
	Fragment fragment = new DummySectionFragment();
	Bundle args = new Bundle();
	args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
	fragment.setArguments(args);
	return fragment;
}

@Override
public int getCount() {
	// Show 3 total pages.
	return 2;
}

@Override
public CharSequence getPageTitle(int position) {
	Locale l = Locale.getDefault();
	switch (position) {
	case 0:
		return getString(R.string.title_section1).toUpperCase(l);
	case 1:
		return getString(R.string.title_section2).toUpperCase(l);
	case 2:
		return getString(R.string.title_section3).toUpperCase(l);
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
	View rootView = inflater.inflate(R.layout.fragment_events_dummy,
			container, false);
	TextView dummyTextView = (TextView) rootView
			.findViewById(R.id.section_label);
	dummyTextView.setText(Integer.toString(getArguments().getInt(
			ARG_SECTION_NUMBER)));
	return rootView;
}
}

@Override
public void onBackPressed() 
{
	 Intent i=new Intent(ChatsAndRequestsActivity.this,HomeScreenActivity.class);
    startActivity(i);
}

}
