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
import android.graphics.BitmapFactory.Options;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.roamer.R;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.roamer.events.CreateEventActivity;

public class HelloGoogleMaps extends FragmentActivity implements LocationListener,
OnMapClickListener, OnMapLongClickListener, OnCameraChangeListener, OnMarkerClickListener{

	private GoogleMap Mmap;
	private double cityLat;
	private double cityLong;
	private int credLocation;
	private EditText searchText;
	
    private Spinner mSprPlaceType;
    
    private String[] mPlaceType=null;
    private String[] mPlaceTypeName=null;
    private List<HashMap<String, String>> places = null;
 
    double mLatitude=0;
    double mLongitude=0;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        
        searchText = (EditText) findViewById(R.id.editTextMap);
        
        
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
	    
		//Set marker on current location
		
		//LatLng newPosition = new LatLng(cityLat, cityLong);
    	//cityLat = Mmap.getCameraPosition().target.latitude;
    	//cityLong = Mmap.getCameraPosition().target.longitude;
	    //Mmap.addMarker(new MarkerOptions().position(newPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_pin_map_gps)));
	    
	    
	    ImageButton searchButton = (ImageButton) findViewById(R.id.imageButtonSearchMap);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	places = null;
            	
            	//Get keyword text and replace with |
            	String searchKeyword = searchText.getText().toString().trim();
            	String newWord;
            	
            	newWord = searchKeyword.replaceAll("\\s+","+");

            	int selectedPosition = mSprPlaceType.getSelectedItemPosition();
            	String type = mPlaceType[selectedPosition];
            	
        		LatLng newPosition = new LatLng(cityLat, cityLong);
            	cityLat = Mmap.getCameraPosition().target.latitude;
            	cityLong = Mmap.getCameraPosition().target.longitude;
        	    Mmap.addMarker(new MarkerOptions().position(newPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_pin_map_gps)));
        	   
            	
            	
            	StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");
            	
            	//sb.append("keyword="+newWord.trim());
            	//sb.append("types=newWord");
            	sb.append("name="+newWord.trim());
            	sb.append("&location="+cityLat+","+cityLong);
            	sb.append("&radius=2000");
            	sb.append("&key=AIzaSyCMO_9EWAfU83za9plSDNxBaUSd_o0R0og");
            	
            	System.out.println("Request is: "+sb);
            	// Creating a new non-ui thread task to download json data
            	PlacesTask placesTask = new PlacesTask();
            	
            	// Invokes the "doInBackground()" method of the class PlaceTask
            	placesTask.execute(sb.toString());
  
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
 
            
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
            //String nextToken = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                
                
                //nextToken= jObject.getString("next_page_token");
                
                /* Getting the parsed data as a List construct 
                
                if (places != null){
                	places.addAll(placeJsonParser.parse(jObject));
                }
                */
                //if (places == null){
                	places = placeJsonParser.parse(jObject);
                //}
                
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            
            /*
            if (nextToken != null){
            	// Creating a new non-ui thread task to download json data
            	PlacesTask placesTask = new PlacesTask();
            	
            	// Invokes the "doInBackground()" method of the class PlaceTask
            	String sb = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+nextToken;
            	System.out.println("new string to execute = "+sb);
            	placesTask.execute(sb);
            }
            */
            
            System.out.println("Places are: "+places);
            return places;
            
        }
 
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){
 
            // Clears all the existing markers
            Mmap.clear();
            
        	cityLat = Mmap.getCameraPosition().target.latitude;
        	cityLong = Mmap.getCameraPosition().target.longitude;
        	LatLng newPosition = new LatLng(cityLat, cityLong);
        	
    	    Mmap.addMarker(new MarkerOptions().position(newPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_pin_map_gps)));
    	    
 
    	    if (list != null){
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
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	
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
    	
    	Mmap.clear();
        System.out.println("Point is: "+ point.toString());
        double lat = point.latitude;
        double longt = point.longitude;

    }

	@Override
	public void onCameraChange(CameraPosition position) {

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
