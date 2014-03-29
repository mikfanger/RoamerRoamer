package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.json.*;


import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
	private double cityLat;
	private double cityLong;
	private String credLocation;
	
	private LocationManager locationManager;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    setUpMapIfNeeded();
	    
	    //Get lat long for current set location
	    getCredLocation();
	    getCurrentLocationLatLong();
	    
	    //Set my location
	    Mmap.setMyLocationEnabled(true);
	    
	    // Zoom in, animating the camera to current Cred Location
	    //Mmap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    
	    Mmap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(cityLat,cityLong) , 14.0f) );
	    curLocation = new LatLng(cityLat,cityLong);
    	Marker locMarker = Mmap.addMarker(new MarkerOptions().position(curLocation)
    			.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));  
	    
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
                
                }
        }
        catch(Exception ee)
        {

        }
    }

    public void getCurrentLocationLatLong() {
    	
    	switch(credLocation){
    	case "Dallas":
    		cityLong = -96.7967;
            cityLat = 32.7758;
            break;
    	case "Boston":
    		cityLong = -71.059773;
            cityLat = 42.358431;
    		break;
    	case "Chicago":
    		cityLong = -87.629798;
            cityLat = 41.878114;
    		break;
    	case "New York":
    		cityLong = -74.005973;
            cityLat = 40.714353;
    		break;
    	case "San Francisco":
    		cityLong = -122.419416;
            cityLat = 37.774929;
    		break;
    	case "Los Angeles":
    		cityLong = -118.243685;
            cityLat = 34.052234;
    		break;
    	case "Los Vegas":
    		cityLong = -115.238349;
            cityLat = 36.255123;
    		break;
    	case "Houston":
    		cityLong = -95.369390;
            cityLat = 29.760193;
    		break;
    	case "Philadelphia":
    		cityLong = -75.163789;
            cityLat = 39.952335;
    		break;
    	case "Phoenix":
    		cityLong = -112.074037;
            cityLat = 33.448377;
    		break;
    	case "San Antonio":
    		cityLong = -98.493628;
            cityLat = 29.424122;
    		break;
    	case "San Jose":
    		cityLong = -121.894955;
            cityLat = 37.339386;
    		break;
    	case "San Diego":
    		cityLong = -117.157255;
            cityLat = 32.715329;
    		break;
    	case "Austin":
    		cityLong = -97.743061;
            cityLat = 30.267153;
    		break;
    	case "Jacksonville":
    		cityLong = -81.655651;
            cityLat = 30.332184;
    		break;
    	case "Indianapolis":
    		cityLong = -86.158068;
            cityLat = 39.768403;
    		break;
    	case "Seattle":
    		cityLong = -122.332071;
            cityLat = 47.606209;
    		break;
    	case "Denver":
    		cityLong = -104.984718;
            cityLat = 39.737567;
    		break;
    	case "Washington DC":
    		cityLong = -77.036464;
            cityLat = 38.907231;
    		break;
    	}


    }
    
    private String getCredLocation(){
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	credLocation = cur.getString(index);
    	
    	myDB.close();
    	
    	return credLocation;
    }
    
    private void GetDistance(GeoPoint src, GeoPoint dest) throws IOException, JSONException {

        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json?");
        urlString.append("origin=");//from
        urlString.append( Double.toString((double)src.getLatitudeE6() / 1E6));
        urlString.append(",");
        urlString.append( Double.toString((double)src.getLongitudeE6() / 1E6));
        urlString.append("&destination=");//to
        urlString.append( Double.toString((double)dest.getLatitudeE6() / 1E6));
        urlString.append(",");
        urlString.append( Double.toString((double)dest.getLongitudeE6() / 1E6));
        urlString.append("&mode=walking&sensor=true");
        Log.d("xxx","URL="+urlString.toString());

        // get the JSON And parse it to get the directions data.
        HttpURLConnection urlConnection= null;
        URL url = null;

        url = new URL(urlString.toString());
        urlConnection=(HttpURLConnection)url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.connect();

        InputStream inStream = urlConnection.getInputStream();
        BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

        String temp, response = "";
        while((temp = bReader.readLine()) != null){
            //Parse data
            response += temp;
        }
        //Close the reader, stream & connection
        bReader.close();
        inStream.close();
        urlConnection.disconnect();

        //Sortout JSONresponse 
        JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
        JSONArray array = object.getJSONArray("routes");
            //Log.d("JSON","array: "+array.toString());

        //Routes is a combination of objects and arrays
        JSONObject routes = array.getJSONObject(0);
            //Log.d("JSON","routes: "+routes.toString());

        String summary = routes.getString("summary");
            //Log.d("JSON","summary: "+summary);

        JSONArray legs = routes.getJSONArray("legs");
            //Log.d("JSON","legs: "+legs.toString());

        JSONObject steps = legs.getJSONObject(0);
                //Log.d("JSON","steps: "+steps.toString());

        JSONObject distance = steps.getJSONObject("distance");
            //Log.d("JSON","distance: "+distance.toString());

                String sDistance = distance.getString("text");
                int iDistance = distance.getInt("value");

    }
		
}