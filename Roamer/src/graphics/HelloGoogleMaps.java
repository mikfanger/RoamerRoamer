package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.*;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roamer.R;
import com.example.roamer.events.CreateEventActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HelloGoogleMaps extends FragmentActivity implements LocationListener,
OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener, OnMarkerClickListener{

	private GoogleMap Mmap;
	private double cityLat;
	private double cityLong;
	private int credLocation;
	
    private Spinner mSprPlaceType;
    
    private String[] mPlaceType=null;
    private String[] mPlaceTypeName=null;
 
    double mLatitude=0;
    double mLongitude=0;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	 // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            
        }
        else{
	    // Getting reference to the SupportMapFragment
        SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mymap);

        // Getting Google Map
        Mmap = fragment.getMap();
	    
	    //Get lat long for current set location
	    getCredLocation();
	    try {
			getCurrentLocationLatLong();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //Set my location
	    //Mmap.setMyLocationEnabled(true);
	    
	    // Zoom in, animating the camera to current Cred Location
	    Mmap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(cityLat,cityLong) , 10.0f) );

	    // Array of place types
        mPlaceType = getResources().getStringArray(R.array.place_type);
 
        // Array of place type names
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);
 
        // Creating an array adapter with an array of Place types
        // to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mPlaceTypeName);
 
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Getting reference to the Spinner
        mSprPlaceType = (Spinner) findViewById(R.id.spr_place_type);
 
        // Setting adapter on Spinner to set place types
        mSprPlaceType.setAdapter(adapter);
	    
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        

	    //Set up map options
	    GoogleMapOptions options = new GoogleMapOptions();
	    options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
	    .compassEnabled(false)
	    .rotateGesturesEnabled(false)
	    .tiltGesturesEnabled(false);
	    
	    ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearchMap);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	//String checkLocation = searchText.getText().toString();
            	
            	int selectedPosition = mSprPlaceType.getSelectedItemPosition();
            	String type = mPlaceType[selectedPosition];
            	
            	
            	StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            	sb.append("location="+cityLat+","+cityLong);
            	sb.append("&radius=10000");
            	sb.append("&types="+type);
            	//sb.append("&types=airport");
            	//sb.append("&name="+checkLocation);
            	sb.append("&sensor=false");
            	sb.append("&key=AIzaSyCMO_9EWAfU83za9plSDNxBaUSd_o0R0og");
            	
            	// Creating a new non-ui thread task to download json data
            	PlacesTask placesTask = new PlacesTask();
            	
            	// Invokes the "doInBackground()" method of the class PlaceTask
            	placesTask.execute(sb.toString());
            	
            	System.out.println(sb.toString());
            	//placesTask.doInBackground(sb.toString());
            	
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
      
        
        Mmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker point) {

            	showAlert(point);
            	
            }       
    });
        
        setUpMapIfNeeded();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.hello_google_maps, menu);
        return true;
    }
    
    private void setUpMapIfNeeded() {
        if (Mmap == null) {
            Mmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mymap))
                    .getMap();
            if (Mmap != null) {
                setUpMap();
            }
        }
    }
    
    private void setUpMap() {
        Mmap.setOnMapClickListener(this);
        Mmap.setOnMapLongClickListener(this);
        Mmap.setOnCameraChangeListener(this);
    }
 
    

    public void getCurrentLocationLatLong() throws ParseException {
    	
    	
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Cities");
    	query.whereEqualTo("Code", credLocation);
    	
    	ParseObject object = query.getFirst();
    	
    	ParseGeoPoint geo = object.getParseGeoPoint("LatLong");
    	cityLat = geo.getLatitude();
    	cityLong = geo.getLongitude();
    	System.out.println("City corrdinates are: "+cityLat+","+cityLong);

    }
    
    private int getCredLocation(){
    	
    	SQLiteDatabase myDB = this.openOrCreateDatabase("RoamerDatabase", MODE_PRIVATE, null);
    	Cursor cur = myDB.rawQuery("SELECT * FROM MyCred", null);
    	cur.moveToFirst();
    	int index;
    	index = cur.getColumnIndex("CurrentLocation");
    	credLocation = cur.getInt(index);
    	
    	myDB.close();
    	
    	return credLocation;
    }
    
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb  = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
 
        return data;
    }
 
    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String>{
 
        String data = null;
 
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();
 
            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
 
    }
 
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
 
        JSONObject jObject;
 
        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {
 
            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
 
            try{
                jObject = new JSONObject(jsonData[0]);
 
                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
 
            // Clears all the existing markers
            Mmap.clear();
 
            for(int i=0;i<list.size();i++){
 
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
 
                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);
 
                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));
 
                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));
 
                // Getting name
                String name = hmPlace.get("place_name");
 
                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");
 
                LatLng latLng = new LatLng(lat, lng);
 
                // Setting the position for the marker
                markerOptions.position(latLng);
 
                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name + " : " + vicinity);
 
                // Placing a marker on the touched position
                Mmap.addMarker(markerOptions);
            }
        }
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	
    	/*
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);
 
        Mmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        Mmap.animateCamera(CameraUpdateFactory.zoomTo(12));
        
        */
    }
 
   @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onMapClick(LatLng point) {
        System.out.println("Point is: "+ point.toString());
        double lat = point.latitude;
        double longt = point.longitude;
    }

	@Override
	public void onCameraChange(CameraPosition position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		 Toast.makeText(getApplicationContext(), arg0.getTitle(), Toast.LENGTH_LONG).show();
		return false;
	}
	
	 public void onInfoWindowClick(Marker marker) {
		 showAlert(marker);
	        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
	    }

	 public void showAlert(final Marker marker){
		 
		 AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
         builder1.setMessage("Are you sure you want: "+marker.getTitle()+"?");
         builder1.setCancelable(true);
         builder1.setPositiveButton("Yes",
                 new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
                 
                saveLocation(marker.getTitle());
                 
                 finish();
             	 Intent i=new Intent(HelloGoogleMaps.this,CreateEventActivity.class);
                 startActivity(i);
                 
             }
         });
         builder1.setNegativeButton("No",
                 new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.cancel();
             }
         });

         AlertDialog alert11 = builder1.create();
         alert11.show();
	 }
	 
	 public void saveLocation(String location){
		 //Move back to create event activity and save the location
         SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
         SharedPreferences.Editor editor = preferences.edit();
         editor.putString("location",location);
         editor.commit();
	 }
    
}
