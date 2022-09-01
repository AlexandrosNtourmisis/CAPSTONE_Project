package acg.edu.warningapp.home;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import acg.edu.warningapp.R;

import static java.lang.Math.round;

// Maps Visualization feature
public class MapsActivity_ extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lat_, lon_, latitude_, longitude_;
    float distance;

    Double latitude, longitude, lat, lon, test, test_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //Get values from previous activities
        lat_ = getIntent().getStringExtra("lat");
        lon_ = getIntent().getStringExtra("lon");
        latitude_ = getIntent().getStringExtra("latitude");
        longitude_ = getIntent().getStringExtra("longitude");

        //Convert them to double format
        lat = Double.parseDouble(lat_);
        lon = Double.parseDouble(lon_);
        latitude = Double.parseDouble(latitude_);
        longitude = Double.parseDouble(longitude_);

        //Create two location points A -> Case's location B-> User's location
        Location locationA = new Location("point A");
        locationA.setLatitude(Float.parseFloat(lat_));
        locationA.setLongitude(Float.parseFloat(lon_));

        Location locationB = new Location("point B");
        locationB.setLatitude(Float.parseFloat(latitude_));
        locationB.setLongitude(Float.parseFloat(longitude_));

        //Check distance
        distance = locationA.distanceTo(locationB);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Google map
        mMap = googleMap;

        //If distance is less than 500 meters then...
        if (distance < 500) {
            Toast.makeText(MapsActivity_.this, "You are in danger", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MapsActivity_.this, "You are safe", Toast.LENGTH_LONG).show();
        }


        // Create marker for case
        LatLng case_ = new LatLng(lat, lon);
        //Create marker for user
        LatLng myLocation = new LatLng(latitude, longitude);

        //Case's marker
        mMap.addMarker(new MarkerOptions().position(case_).title("Case")
                .icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_circle_24)));
        //User's marker
        mMap.addMarker(new MarkerOptions().position(myLocation).title("You"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Button to view the user's current location
        googleMap.setMyLocationEnabled(true);
        //Button Visible
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Zoom level
        float zoomLevel = 17.0f;
        //Move camera to the case location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(case_, zoomLevel));
    }


//--------------------------------------------NEW MARKER ICON-------------------------------------------------------------------------------------
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());


        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------
}