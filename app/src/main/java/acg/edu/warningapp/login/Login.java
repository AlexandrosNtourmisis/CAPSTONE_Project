package acg.edu.warningapp.login;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import acg.edu.warningapp.MainActivity;
import acg.edu.warningapp.R;
import acg.edu.warningapp.home.ActivateCasesCheck;
import acg.edu.warningapp.home.BanMessage;
import acg.edu.warningapp.home.FirstMessage;
import acg.edu.warningapp.register.Register;

public class Login extends Activity {
    private Button LoginButton;
    private Button Register;
    private Button ForgotPassButton;
    CountryCodePicker ccpCountry;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView textview;
    TextInputLayout phone, password;
    String country;
    CardView cardViewLogin, cardViewNewUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //link the layouts
        phone = findViewById(R.id.PhoneInputLogin);
        password = findViewById(R.id.PasswordInput);
        LoginButton = findViewById(R.id.LoginButton);
        ccpCountry = findViewById(R.id.ccp);
        textview = findViewById(R.id.Welcome);
        cardViewLogin = findViewById(R.id.login_card);
        cardViewNewUser = findViewById(R.id.cardview_newuser);

        //Start Animations
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_anim);
        Animation animation_login = AnimationUtils.loadAnimation(this, R.anim.cardview_login_anim);
        Animation animation_newUser = AnimationUtils.loadAnimation(this, R.anim.cardview_newuser_anim);
        textview.startAnimation(animation);
        cardViewLogin.startAnimation(animation_login);
        cardViewNewUser.startAnimation(animation_newUser);



        //Check location's permissions
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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


                            try {
                                //Using geocoder to identify the cities from the longitudes and latitudes values.
                                Geocoder geocoder = new Geocoder(Login.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                country = addresses.get(0).getCountryName();
                                // assign the country that the user is located to the country picker
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
                            if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            }else {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }


        }
        else {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }






        //Login Button
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check that the input is not empty
                if (!isValidInput()) {
                    return;
                }
                //get inputs
                String phoneLogin = phone.getEditText().getText().toString();
                String pickerr = ccpCountry.getSelectedCountryCode();
                String completePhoneNumberr = "+" + pickerr + phoneLogin;
                String passwordLogin = password.getEditText().getText().toString().trim();

                //Query from database to find if there is an account with that phone number
                Query userData = FirebaseDatabase.getInstance().getReference("WarningApp_Users").orderByChild("phoneNo").equalTo(completePhoneNumberr);
                userData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //if the phone number exists
                        if (snapshot.exists()) {
                            phone.setError(null);
                            phone.setErrorEnabled(false);
                            //get the password that this phone number has as value
                            String passwordMatch = snapshot.child(completePhoneNumberr).child("password").getValue(String.class);
                            //check if the password is the same with the input password
                            if (passwordMatch.equals(passwordLogin)) {
                                password.setError(null);
                                password.setErrorEnabled(false);

                                //get user's information from database to pass them for Preferences class (To be used for session)
                                String fname= snapshot.child(completePhoneNumberr).child("fname").getValue(String.class);
                                String lname= snapshot.child(completePhoneNumberr).child("lname").getValue(String.class);
                                String email= snapshot.child(completePhoneNumberr).child("email").getValue(String.class);
                                String phoneNo= snapshot.child(completePhoneNumberr).child("phoneNo").getValue(String.class);
                                String password_d= snapshot.child(completePhoneNumberr).child("password").getValue(String.class);
                                String country= snapshot.child(completePhoneNumberr).child("country").getValue(String.class);
                                String notifications= snapshot.child(completePhoneNumberr).child("notifications").getValue(String.class);
                                String safety_radius= snapshot.child(completePhoneNumberr).child("safety_radius").getValue(String.class);
                                //Start Session - pass the values
                                Preferences preferences = new Preferences(Login.this);
                                preferences.LoginS(fname, lname, email, phoneNo, password_d, country, notifications, safety_radius);

//                                startActivity(new Intent(getApplicationContext(), Menu.class));
                                Query userData = FirebaseDatabase.getInstance().getReference("WarningApp_Users/").orderByChild(completePhoneNumberr);
                                userData.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        //boolean fmessage = snapshot.child(id).child("first_message").getValue(boolean.class);
                                        boolean fwarning = snapshot.child(completePhoneNumberr).child("first_warning").getValue(boolean.class);
                                        boolean final_warning = snapshot.child(completePhoneNumberr).child("final_warning").getValue(boolean.class);

                                        if (fwarning) {
                                            Intent launchSecondIntent = new Intent(getBaseContext(), FirstMessage.class);
                                            startActivity(launchSecondIntent);
                                            finish();
                                        } else {
                                            if (final_warning) {
                                                Intent launchSecondIntent = new Intent(getBaseContext(), BanMessage.class);
                                                startActivity(launchSecondIntent);
                                                finish();
                                            } else {
                                                Intent launchSecondIntent = new Intent(getBaseContext(), ActivateCasesCheck.class);
                                                startActivity(launchSecondIntent);
                                                finish();
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Intent launchSecondIntent = new Intent(getBaseContext(), MainActivity.class);
                                        startActivity(launchSecondIntent);
                                        finish();
                                    }
                                });


                            }
                            else {
                                Toast.makeText(Login.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Login.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                Intent launchSecondIntent = new Intent(getBaseContext(), MainActivity.class);
//                startActivity(launchSecondIntent);
//                finish();
            }
        });
        Register = findViewById(R.id.CreateAcc);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSecondIntent = new Intent(getBaseContext(), Register.class);
                startActivity(launchSecondIntent);
                //finish();
            }
        });
        ForgotPassButton = findViewById(R.id.forgetpass);
        ForgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSecondIntent = new Intent(getBaseContext(), ForgotPasswordRequestPhoneNumber.class);
                startActivity(launchSecondIntent);
                //finish();
            }
        });
    }



        //check that the input is not empty
    private boolean isValidInput() {
        String phoneLogin = phone.getEditText().getText().toString().trim();
        String passwordLogin = password.getEditText().getText().toString().trim();

        if (phoneLogin.isEmpty()) {
            phone.setError("The phone number is required");
            return false;
        } else if (passwordLogin.isEmpty()) {
            password.setError("The phone number is required");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}