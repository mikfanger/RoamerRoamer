package graphics;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roamer.R;
import com.example.roamer.events.CreateEventActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class HelloGoogleMaps extends FragmentActivity implements LoaderCallbacks<Cursor>{

	private GoogleMap Mmap;
	private EditText searchText;
	private double latitude= 0.0, longtitude= 0.0;
	private LatLng curLocation;
	private TextView locText;
	
	private LocationManager locationManager;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    setUpMapIfNeeded();
	    
	    //Set my location
	    Mmap.setMyLocationEnabled(true);
	    
	    // Zoom in, animating the camera.
	    Mmap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    
	    
	    //Set up map options
	    GoogleMapOptions options = new GoogleMapOptions();
	    options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
	    .compassEnabled(false)
	    .rotateGesturesEnabled(false)
	    .tiltGesturesEnabled(false);
	    
	    searchText = (EditText) findViewById(R.id.editTextSearchMap);
	    ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearchMap);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	getFromLocation(searchText.getText().toString());
            	
            	Mmap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longtitude) , 14.0f) );
            	curLocation = new LatLng(latitude,longtitude);
            	Marker locMarker = Mmap.addMarker(new MarkerOptions().position(curLocation)
            			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));          	
            	
            }
        });

	    ImageButton backButton = (ImageButton) findViewById(R.id.imageBackFromMap);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	finish();
            	Intent i=new Intent(HelloGoogleMaps.this,CreateEventActivity.class);
                startActivity(i);
            		  
            }
        });
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
	
	private void handleIntent(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            doSearch(intent.getStringExtra(SearchManager.QUERY));
        }else if(intent.getAction().equals(Intent.ACTION_VIEW)){
            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
        }
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }
 
    private void doSearch(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(0, data, this);
    }
    
    private void getPlace(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(1, data, this);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.hello_google_maps, menu);
        return true;
    }
 
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()){
        case R.id.action_search:
            onSearchRequested();
            break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
 
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        CursorLoader cLoader = null;
        if(arg0==0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
        else if(arg0==1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
        return cLoader;
    }
 
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        showLocations(c);
    }
 
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }
 
    private void showLocations(Cursor c){
        MarkerOptions markerOptions = null;
        LatLng position = null;
        Mmap.clear();
        while(c.moveToNext()){
            markerOptions = new MarkerOptions();
            position = new LatLng(Double.parseDouble(c.getString(1)),Double.parseDouble(c.getString(2)));
            markerOptions.position(position);
            markerOptions.title(c.getString(0));
            Mmap.addMarker(markerOptions);
        }
        if(position!=null){
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
            Mmap.animateCamera(cameraPosition);
        }
    }

    private void setUpMapIfNeeded() {
        if (Mmap != null) {
            return;
        }
        Mmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mymap)).getMap();
        if (Mmap == null) {
            return;
        }
        
        
    }
    
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        Mmap.animateCamera(cameraUpdate);
        locationManager.removeUpdates((LocationListener) this);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) { }

    public void onProviderEnabled(String provider) { }

    public void onProviderDisabled(String provider) { }
    
    
    class MapOverlay extends com.google.android.maps.Overlay
    {
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {  
        	
        	GeoPoint p = null;
            super.draw(canvas, mapView, shadow);                   

        //---translate the GeoPoint to screen pixels---
        Point screenPts = new Point();
        mapView.getProjection().toPixels(p, screenPts);

        //---add the marker---
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.x);            
        canvas.drawBitmap(bmp, screenPts.x, screenPts.y-32, null);
			return shadow;
           //...
        }
 
        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) 
        {   
            //---when user lifts his finger---
            if (event.getAction() == 1) {                
                GeoPoint p = mapView.getProjection().fromPixels(
                    (int) event.getX(),
                    (int) event.getY());
                    Toast.makeText(getBaseContext(), 
                        p.getLatitudeE6() / 1E6 + "," + 
                        p.getLongitudeE6() /1E6 , 
                        Toast.LENGTH_SHORT).show();
            }                            
            return false;
        }        
    }
    
    public void onMapClick(LatLng point) {
        // TODO Auto-generated method stub
        // tvLocInfo.setText(point.toString());
        Mmap.animateCamera(CameraUpdateFactory.newLatLng(point));
        Mmap.clear();

        Marker Kiel = Mmap.addMarker(new MarkerOptions()
            .position(point)
            .title("Kiel")
            .snippet("Kiel is cool").draggable(true)
            .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.ic_launcher)));
    }
    
    public GeoPoint searchAddress() throws IOException{
    	Geocoder coder = new Geocoder(this);
    	List<Address> address;

    	try {
    	    address = coder.getFromLocationName(searchText.getText().toString(),5);
    	    if (address == null) {
    	        return null;
    	    }
    	    Address location = address.get(0);
    	    location.getLatitude();
    	    location.getLongitude();

    	   GeoPoint p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
    	                      (int) (location.getLongitude() * 1E6));

    	     return p1;
    	}
    	finally{
    		
    	}           
    }
    
    private  void getFromLocation(String address)
    {

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
        try 
        {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0) 
            {            
                GeoPoint p = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));

                latitude=p.getLatitudeE6()/1E6;
                longtitude=p.getLongitudeE6()/1E6;
                
                //locText = (TextView) findViewById(R.id.textFinalLocation);
            	//locText.setText(searchText.toString());


                }
        }
        catch(Exception ee)
        {

        }
    }

    
		
}