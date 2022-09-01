package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class LocationChanges extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView BackButton;
    Spinner check_timer, distanceC;
    String country, fname, lname, email, password, phoneNo, notifications, safety_radius;
    String timer_selected, distance_selected;
    LocCheckerTimer locCheckerTimer;
    DistanceRadius distanceRadius;
    CountryCodePicker ccpCountry;
    FusedLocationProviderClient fusedLocationProviderClient;

    String[] timer = {"15", "30 minutes (recommended)", "45", "1 hour", "2 hours", "Manual"};
    String[] distance = {"500", "800", "1000", "1500"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_changes);

        //Get session values
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        ccpCountry = findViewById(R.id.ccp);
        country = userData.get(Preferences.C_country);

        //Check location's permissions
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(LocationChanges.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(LocationChanges.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                //Read location
                                Geocoder geocoder = new Geocoder(LocationChanges.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                //Set the location's country to the country code picker default value
                                ccpCountry.setAutoDetectedCountry(Boolean.parseBoolean(country));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            //Set another request and get the last location
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setFastestInterval(1000)
                                    .setNumUpdates(1);
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();

                                }
                            };
                            if (ActivityCompat.checkSelfPermission(LocationChanges.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationChanges.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                });
            } else {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }


        } else {
            ActivityCompat.requestPermissions(LocationChanges.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        //Back Buttons
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Read from session
        fname = userData.get(Preferences.F_name);
        lname = userData.get(Preferences.L_name);
        email = userData.get(Preferences.E_mail);
        password = userData.get(Preferences.T_password);
        phoneNo = userData.get(Preferences.Phone_No);
        notifications = userData.get(Preferences.N_otifications);
        safety_radius = userData.get(Preferences.Safety_radius);


        check_timer = findViewById(R.id.checkLspinner);
        distanceC = findViewById(R.id.radius_spinner);

        // SPINNERS FOR notification timer and safety distance
        check_timer.setOnItemSelectedListener(this);
        distanceC.setOnItemSelectedListener(this);

        locCheckerTimer = new LocCheckerTimer();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timer);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_timer.setAdapter(arrayAdapter);

        distanceRadius = new DistanceRadius();
        ArrayAdapter arrayAdapter_2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, distance);
        arrayAdapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceC.setAdapter(arrayAdapter_2);

        int timeSpinnerPosition = arrayAdapter.getPosition(notifications);
        int distanceSpinnerPosition = arrayAdapter_2.getPosition(safety_radius);

        //set the default according to value
        check_timer.setSelection(timeSpinnerPosition);
        distanceC.setSelection(distanceSpinnerPosition);

    }

    // Spinners actions--------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timer_selected = check_timer.getSelectedItem().toString();
        distance_selected = distanceC.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void UpdateLocationSettings(View view) {
        String country_selected = ccpCountry.getSelectedCountryName();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WarningApp_Users");
        myRef.child(phoneNo).child("notifications").setValue(timer_selected);
        myRef.child(phoneNo).child("safety_radius").setValue(distance_selected);
        myRef.child(phoneNo).child("country").setValue(country_selected);

        Preferences preferences = new Preferences(LocationChanges.this);
        preferences.LoginS(fname, lname, email, phoneNo, password, country_selected, timer_selected, distance_selected);
        Intent intent = new Intent(getBaseContext(), ViewProfile.class);
        Toast.makeText(LocationChanges.this, "Location Settings were updated", Toast.LENGTH_SHORT).show();
        startActivity(intent);


    }
//------------------------------------------------------------------------------------
}