package acg.edu.warningapp.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import acg.edu.warningapp.R;

public class Phone_verification_register extends Activity {
    private ImageView BackButton;
    TextInputLayout phone;
    TextInputLayout fname, lname, email, password;
    DatePicker age;
    boolean exists = true;
    TextView countryy;
    CountryCodePicker countryCode;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_phone_verification_register);
        cardView = findViewById(R.id.cardview_register);

        //Start Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cardvie_slide_anim);
        cardView.startAnimation(animation);

        //Link variables with layouts id
        phone = findViewById(R.id.phoneInput);
        countryCode = findViewById(R.id.CountryCode);

        //BackButton
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }


    //Button to send code is pressed
    public void VerifyNumber(View view) {
        //if phone number is valid
        if (!isValidPhone()) {
            return;
        }
        //Call Method
        readDB();

    }


    //check that the input is not empty
    private boolean isValidPhone() {
        String validate = phone.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            phone.setError("The phone number is required");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }

    }

    //read the database to find if the phone number exists
    public void readDB() {

        //get inputs
        String phoneNumber = phone.getEditText().getText().toString().trim();
        String pickerr = countryCode.getSelectedCountryCode();
        String completePhoneNumberr = "+" + pickerr + phoneNumber;

        //Query the user's id - phone number
        Query userData = FirebaseDatabase.getInstance().getReference("WarningApp_Users").orderByChild("phoneNo").equalTo(completePhoneNumberr);
        String validate = phone.getEditText().getText().toString().trim();
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if the phone does not exists
                if (!snapshot.exists()) {

                    phone.setError(null);
                    phone.setErrorEnabled(false);

                    //get from previous activities
                    String country = getIntent().getStringExtra("country");
                    String f_name = getIntent().getStringExtra("fname");
                    String l_name = getIntent().getStringExtra("lname");
                    String email_ = getIntent().getStringExtra("email");
                    String password_ = getIntent().getStringExtra("password");
                    String birthDate_ = getIntent().getStringExtra("age");

                    //get phone and country code inputs
                    String phoneNumber = phone.getEditText().getText().toString().trim();
                    String picker = countryCode.getSelectedCountryCode();
                    String completePhoneNumber = "+" + picker + phoneNumber;

                    Intent launchSecondIntent = new Intent(getBaseContext(), CodeVerification.class);

                    //push to next activities
                    launchSecondIntent.putExtra("fname", f_name);
                    launchSecondIntent.putExtra("lname", l_name);
                    launchSecondIntent.putExtra("email", email_);
                    launchSecondIntent.putExtra("country", country);
                    launchSecondIntent.putExtra("password", password_);
                    launchSecondIntent.putExtra("age", birthDate_);
                    launchSecondIntent.putExtra("phoneNo", completePhoneNumber);
                    launchSecondIntent.putExtra("phone", phoneNumber);


                    //navigate to the next input --> code verification
                    startActivity(launchSecondIntent);
                    finish();

                    //if the phone exists
                } else {
                    phone.setError("There is already an account with this phone number");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isExisting() {
        readDB();
        if (exists) {
            phone.setError("There is already an account with this phone number");
            return false;

        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }

    }

}