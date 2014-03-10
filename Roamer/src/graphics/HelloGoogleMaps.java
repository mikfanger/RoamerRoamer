package graphics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.example.roamer.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.gwt.canvas.client.Canvas;

public class HelloGoogleMaps extends FragmentActivity {

	private GoogleMap Mmap;
	
	private LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    setUpMapIfNeeded();
	    
	    /*
	    locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, (LocationListener) this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER 
	     */
	    
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

	}
	
	@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
    
    
    private void getShaKey() {

    	String TAG = "new tag";
    	 try {
    	 PackageInfo info = getPackageManager().getPackageInfo("com.example.roamer",
    	 PackageManager.GET_SIGNATURES);
    	 for (Signature signature : info.signatures) {
    	 MessageDigest md = MessageDigest.getInstance("SHA");
    	 md.update(signature.toByteArray());
    	 Log.v(TAG, "KeyHash:" + Base64.encodeToString(md.digest(),
    	 Base64.DEFAULT));
    	 }
    	 } catch (NameNotFoundException e) {
    	 e.printStackTrace();

    	 } catch (NoSuchAlgorithmException e) {
    	 e.printStackTrace();

    	 }

    	 }
    
    class MapOverlay extends com.google.android.maps.Overlay
    {
        public boolean draw(Canvas canvas, MapView mapView, 
        boolean shadow, long when) 
        {
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
	
}