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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;
import acg.edu.warningapp.main.CasePostHelper;

public class AddCase_secondPart extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView type, location_, time_, date_;
    Spinner spinner;
    ImageView imageView;
    TextInputLayout textInputLayout, details_;
    String severity_selected, type_, icon, iconURL, other;
    String[] severe = {"low", "medium", "high"};
    Severity severity;
    String post_id, longitude, latitude, country, city, comments, author, author_id, searchvalue, time, date, details;
    Integer verifications;
    Date date1, date2;
    Double longitude_d, latitude_d;
    FusedLocationProviderClient fusedLocationProviderClient;
    long timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case_second_part);

        //Link variables with layouts id
        type = findViewById(R.id.typeofCase);
        location_ = findViewById(R.id.location);
        time_ = findViewById(R.id.time);
        date_ = findViewById(R.id.date);
        imageView = findViewById(R.id.imageView);
        spinner = findViewById(R.id.checkSevespinner);
        textInputLayout = findViewById(R.id.type_input);
        details_ = findViewById(R.id.details);
        spinner.setOnItemSelectedListener(this);

        //hide the type input
        textInputLayout.setAlpha(0);

        //format of date and time
        date1 = Calendar.getInstance().getTime();
        date2 = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

        //Session values
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        String phoneNom = userData.get(Preferences.Phone_No);
        String fname = userData.get(Preferences.F_name);
        String lname = userData.get(Preferences.L_name);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //create a random unique key for a case in the database
        post_id = database.getReference("Cases").push().getKey();
        //get the user id from session
        author_id = userData.get(Preferences.Phone_No);
        //author is the fname and lname
        author = fname + " " + lname;
        //get current time and date
        time = dateFormat.format(date1);
        date = dateFormat2.format(date2);
        //verifications of the case are always zero when created
        verifications = 0;
        //comments of the case are always empty when created
        comments = "";
        //timestamp of the case
        timestamp = Calendar.getInstance().getTimeInMillis();

        //get values from the previous activity
        type_ = getIntent().getStringExtra("type");
        iconURL = getIntent().getStringExtra("iconURL");
        other = getIntent().getStringExtra("other");

        //If the type of the case (from previous activity is "other") -> then input is visible -- so the user can type the case type)
        if (type_.equals("Other")) {
            type.setAlpha(0);
            textInputLayout.setAlpha(1);
        }

        //Check if location services are allowed
        if (ActivityCompat.checkSelfPermission(AddCase_secondPart.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(AddCase_secondPart.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


        } else {
            ActivityCompat.requestPermissions(AddCase_secondPart.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        //get latitude and longitude
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());

                        try {
                            //Geocoder to translate the latitude and longitudes to city and country
                            Geocoder geocoder = new Geocoder(AddCase_secondPart.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            longitude_d = addresses.get(0).getLongitude();
                            latitude_d = addresses.get(0).getLatitude();
                            country = addresses.get(0).getCountryName();
                            city = addresses.get(0).getLocality();

                            //city to lowercase to be used in search feature (in case user types in lowercase)
                            searchvalue = city.toLowerCase();
                            location_.setText(city);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Send a new location request (in case location services are not allowed by the user)
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                latitude = String.valueOf(location1.getLatitude());
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(AddCase_secondPart.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddCase_secondPart.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


        //Display the values to the user interface
        type.setText(type_);
        time_.setText(time);
        date_.setText(date);


        //assign the appropriate icon for the case's type
        switch (type_) {
            case "Fire":
                imageView.setBackgroundResource(R.drawable.icons8_fire_96);
                break;
            case "Flood":
                imageView.setBackgroundResource(R.drawable.icons8_floods_96);
                break;
            case "Wildfire":
                imageView.setBackgroundResource(R.drawable.icons8_fire_hazard_96);
                break;
            case "Hurricane":
                imageView.setBackgroundResource(R.drawable.icons8_hurricane_96);
                break;
            case "Earthquake":
                imageView.setBackgroundResource(R.drawable.icons8_earthquakes_90);
                break;
            case "Tornado":
                imageView.setBackgroundResource(R.drawable.icons8_tornado_96);
                break;
            case "Landslide":
                imageView.setBackgroundResource(R.drawable.icons8_landslide_96);
                break;
            case "Tsunami":
                imageView.setBackgroundResource(R.drawable.icons8_tsunami_96);
                break;
            case "Icy road":
                imageView.setBackgroundResource(R.drawable.icons8_road_90);
                break;
            case "Car accident":
                imageView.setBackgroundResource(R.drawable.icons8_crashed_car_96);
                break;
            case "Robbery":
                imageView.setBackgroundResource(R.drawable.icons8_black_ski_mask_96);
                break;
            case "Thunderstorm":
                imageView.setBackgroundResource(R.drawable.icons8_lightning_bolt_90);
                break;
            case "Terrorism":
                imageView.setBackgroundResource(R.drawable.icons8_robbery_96);
                break;
            default:
                imageView.setImageAlpha(0);
                break;
        }

//--------------------------------------------------------SPINNER FOR SEVERITY-----------------------------------------------------------------------
        //spinner with the options for severity
        severity = new Severity();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, severe);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {


        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        severity_selected = spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
//-----------------------------------------------------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------POST CASE METHOD (user presses post button)--------------------------------------------------------------------------------------------
    public void PostTheCase(View view) {

        //If type = equals "other" then get the text that user typed
        if (type_.equals("Other")) {
            type_ = textInputLayout.getEditText().getText().toString();
        }

        //Read the location to store it to the database
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location permissions checks
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
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
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        //Get latitude and longitude
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());

                        try {
                            //Geocoder to get city and country from latitude and longitude
                            Geocoder geocoder = new Geocoder(AddCase_secondPart.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            longitude_d = addresses.get(0).getLongitude();
                            latitude_d = addresses.get(0).getLatitude();
                            country = addresses.get(0).getCountryName();
                            city = addresses.get(0).getLocality();

                            searchvalue = city.toLowerCase();

                            //get details that the user may have typed
                            details = details_.getEditText().getText().toString();

                            //set the database path
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Cases").child(country);

                            //Use the setters and getters from CasePostHelper to store the values to the database
                            CasePostHelper newCase = new CasePostHelper(post_id, type_, time, date, longitude, latitude, country, city, severity_selected, comments, author, author_id, iconURL, verifications, searchvalue, timestamp, details);
                            //Store the values by ordering them with the post unique id
                            myRef.child(post_id).setValue(newCase);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        //Send new location request if have not allowed yet
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                latitude = String.valueOf(location1.getLatitude());
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(AddCase_secondPart.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddCase_secondPart.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        //Navigate the user to the homepage
        Toast.makeText(AddCase_secondPart.this, "The case has been posted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomePage.class));
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //If user presses cancel instead of post
    public void Cancel(View view) {
        startActivity(new Intent(getApplicationContext(), AddCase.class));
    }
}