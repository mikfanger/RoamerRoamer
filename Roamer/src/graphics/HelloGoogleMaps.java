package graphics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.SupportMapFragment;
import com.example.roamer.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HelloGoogleMaps extends FragmentActivity {

	private GoogleMap Mmap;
	
	  static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	  static final LatLng KIEL = new LatLng(53.551, 9.993);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    setUpMapIfNeeded();
	    
	        Marker hamburg = Mmap.addMarker(new MarkerOptions().position(HAMBURG)
	            .title("Hamburg"));
	        Marker kiel = Mmap.addMarker(new MarkerOptions()
	            .position(KIEL)
	            .title("Kiel")
	            .snippet("Kiel is cool")
	            .icon(BitmapDescriptorFactory
	                .fromResource(R.drawable.ic_launcher)));

	        // Move the camera instantly to hamburg with a zoom of 15.
	        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

	        // Zoom in, animating the camera.
	       Mmap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    
	    //Set up map options
	    GoogleMapOptions options = new GoogleMapOptions();
	    options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
	    .compassEnabled(false)
	    .rotateGesturesEnabled(false)
	    .tiltGesturesEnabled(false);
	   

        LatLng sydney = new LatLng(-33.867, 151.206);

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
    
    private void getShaKey() {

    	String TAG = "new tag";
    	 try {
    	 PackageInfo info = getPackageManager().getPackageInfo("your.package.name",
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
	
}