package acg.edu.warningapp.register;

import android.Manifest;
import android.app.Activity;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import acg.edu.warningapp.R;

public class Register extends Activity {
    private Button NextButton;
    private ImageView BackButton;
    TextInputLayout fname, lname, email, password, rpassword;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView title;
    DatePicker age;
    CountryCodePicker countryCode;
    String longitude, latitude, countryc;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Link variables with layouts id
        fname = findViewById(R.id.FirstNameInput);
        title = findViewById(R.id.RegisterTitle);
        lname = findViewById(R.id.LastNameInput);
        email = findViewById(R.id.EmailInput);
        password = findViewById(R.id.PasswordInput);
        rpassword = findViewById(R.id.Re_PasswordInput);
        countryCode = findViewById(R.id.ccp);
        age = findViewById(R.id.age);
        cardView = findViewById(R.id.cardview_register);

        //Start Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cardview_login_anim);
        cardView.startAnimation(animation);


        //Check location permission for both fine location and coarse location
        if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // assign location variable
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            //if the location is enabled
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        //check if the location is not null value
                        if (location != null) {

                            //get latitude and longitude
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());


                            try {
                                //Using geocoder to identify the cities from the longitudes and latitudes values.
                                Geocoder geocoder = new Geocoder(Register.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                countryc = addresses.get(0).getCountryName();

                                // assign the country that the user is located to the country picker
                                countryCode.setAutoDetectedCountry(Boolean.parseBoolean(countryc));

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
                                    latitude = String.valueOf(location1.getLatitude());
                                }
                            };

                            if (ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Register.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }


        //Navigate the user to the next step which is the phone verification
        NextButton = findViewById(R.id.Next_step_register);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check if the inputs are valid
                if (!isValidFName() | !isValidLName() | !isValidEmail() | !isValidPassword() | !isValidRePassword() | !isValidAge()) {
                    return;
                }

                int dayOfBirth = age.getDayOfMonth();
                int monthOfBirth = age.getMonth();
                int yearOfBirth = age.getYear();

                // get the inputs
                String country = countryCode.getSelectedCountryName();
                String f_name = fname.getEditText().getText().toString();
                String l_name = lname.getEditText().getText().toString();
                String email_ = email.getEditText().getText().toString();
                String password_ = password.getEditText().getText().toString();

                String birthDate_ = dayOfBirth + "/" + monthOfBirth + "/" + yearOfBirth;

                Intent launchSecondIntent = new Intent(getBaseContext(), Phone_verification_register.class);

                //pass them to the next activity
                launchSecondIntent.putExtra("fname", f_name);
                launchSecondIntent.putExtra("lname", l_name);
                launchSecondIntent.putExtra("email", email_);
                launchSecondIntent.putExtra("password", password_);
                launchSecondIntent.putExtra("country", country);
                launchSecondIntent.putExtra("age", birthDate_);


                startActivity(launchSecondIntent);
                //finish();
            }
        });
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launchSecondIntent = new Intent(getBaseContext(), MainActivity.class);
                //startActivity(launchSecondIntent);
                finish();
            }
        });


    }

    // check that the fist name is not empty
    private boolean isValidFName() {
        String validate = fname.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            fname.setError("The first name is required");
            return false;
        } else {
            fname.setError(null);
            fname.setErrorEnabled(false);
            return true;
        }
    }

    // check that the last name is not empty
    private boolean isValidLName() {
        String validate = lname.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            lname.setError("The last name is required");
            return false;
        } else {
            lname.setError(null);
            lname.setErrorEnabled(false);
            return true;
        }
    }

    //
    //check that the email is valid
    private boolean isValidEmail() {
        String validate = email.getEditText().getText().toString().trim();
        String EmailValid = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (validate.isEmpty()) {
            email.setError("The email is required");
            return false;
        } else if (!validate.matches(EmailValid)) {
            email.setError("invalid Email... check again");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    //
    //check that the password is valid
    private boolean isValidPassword() {
        String validate = password.getEditText().getText().toString().trim();
        String passValid = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$";


        if (validate.isEmpty()) {
            password.setError("The password is required");
            return false;
        } else if (!validate.matches(passValid)) {
            password.setError(null);
            password.setError(" No spaces at least with: 6 characters, 1 number, 1 upper case letter");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    //
    //check that rePassword is same with the password
    private boolean isValidRePassword() {
        String validate = rpassword.getEditText().getText().toString().trim();
        String revalidate = password.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            rpassword.setError("The re enter password is required");
            return false;
        } else if (!validate.matches(revalidate)) {
            rpassword.setError("Passwords do not match");
            return false;
        } else {
            rpassword.setError(null);
            rpassword.setErrorEnabled(false);
            return true;
        }

    }

    //
    //check that the user is above 12 years old
    private boolean isValidAge() {
        int Age = age.getYear();
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        int getAge = cYear - Age;

        if (getAge < 12) {
            Toast.makeText(this, "You are too young for this app", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


}