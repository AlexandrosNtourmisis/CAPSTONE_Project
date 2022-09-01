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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.hbb20.CCPCountry;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Login;
import acg.edu.warningapp.login.Preferences;
import acg.edu.warningapp.register.Register;

public class EditProfile extends AppCompatActivity {
    private Button NextButton;
    private ImageView BackButton;
    TextInputLayout fname_, lname_, email_, password_;
    FusedLocationProviderClient fusedLocationProviderClient;
    String _country, _fname, _lname, _email, password, phoneNo, notifications, safety_radius;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Link variables with layouts id
        fname_ = findViewById(R.id.FirstName);
        lname_ = findViewById(R.id.LastNameInput);
        email_ = findViewById(R.id.EmailInput);
        password_ = findViewById(R.id.PasswordInput);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        //Read the session's values
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        _country = userData.get(Preferences.C_country);
        _fname = userData.get(Preferences.F_name);
        _lname = userData.get(Preferences.L_name);
        _email = userData.get(Preferences.E_mail);
        password = userData.get(Preferences.T_password);
        phoneNo = userData.get(Preferences.Phone_No);
        notifications = userData.get(Preferences.N_otifications);
        safety_radius = userData.get(Preferences.Safety_radius);


        //set the values to the inputs.
        fname_.getEditText().setText(_fname);
        lname_.getEditText().setText(_lname);
        email_.getEditText().setText(_email);


        NextButton = findViewById(R.id.Next_step_register);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if password is correct
                if (!PasswordMatches()) {
                    return;
                }

                //get the values from the inputs
                String fname = fname_.getEditText().getText().toString().trim();
                String lname = lname_.getEditText().getText().toString().trim();
                String email = email_.getEditText().getText().toString().trim();
                String password_d = password;

                //Update the values to the database
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WarningApp_Users");
                myRef.child(phoneNo).child("fname").setValue(fname);
                myRef.child(phoneNo).child("lname").setValue(lname);
                myRef.child(phoneNo).child("email").setValue(email);

                //Update the session values
                Preferences preferences = new Preferences(EditProfile.this);
                preferences.LoginS(fname, lname, email, phoneNo, password_d, _country, notifications, safety_radius);

                //Start ViewProfile Activity
                Intent intent = new Intent(getBaseContext(), ViewProfile.class);
                Toast.makeText(EditProfile.this, "Profile information updated", Toast.LENGTH_SHORT).show();
                startActivity(intent);


            }
        });

        //Back Button Pressed
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    private boolean isValidPassword() {
//
//        String validate = password_.getEditText().getText().toString().trim();
//
//        if (validate.isEmpty()) {
//            password_.setError("The new password is required");
//            return false;
//        } else {
//            password_.setError(null);
//            password_.setErrorEnabled(false);
//            return true;
//        }
//
//    }


    private boolean PasswordMatches() {
        //checks if the password is the same from the session's one
        String pass = password_.getEditText().getText().toString().trim();
        if (pass.equals(password)) {
            password_.setError(null);
            password_.setErrorEnabled(false);
            return true;
        } else if (pass.isEmpty()) {
            password_.setError("The password is required");
            return false;
        } else {
            password_.setError("The password is incorrect");
            return false;
        }

    }
}