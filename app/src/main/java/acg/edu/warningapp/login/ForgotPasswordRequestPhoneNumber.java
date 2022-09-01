package acg.edu.warningapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import acg.edu.warningapp.MainActivity;
import acg.edu.warningapp.R;
import acg.edu.warningapp.register.CodeVerification;
import acg.edu.warningapp.register.Register;

public class ForgotPasswordRequestPhoneNumber extends Activity {
    private Button NextButton;
    private ImageView BackButton;
    TextInputLayout phone;
    TextInputLayout fname, lname, email, password;
    DatePicker age;
    boolean exists = true;
    CountryCodePicker countryCode;
    CardView cardView;
    TextView title, subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password_request_phone_number);

        //Link variables with layouts id
        cardView = findViewById(R.id.forgotPasswordCard);
        title = findViewById(R.id.ForgotPassTitle);
        subtitle = findViewById(R.id.ForgotPassTitle_);
        phone = findViewById(R.id.phoneInput_FP);
        countryCode = findViewById(R.id.CountryCodePicker);


        //Start Animations
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cardvie_slide_anim);
        cardView.startAnimation(animation);
        Animation animation_ = AnimationUtils.loadAnimation(this, R.anim.forgot_pass_anim_title);
        title.startAnimation(animation_);
        Animation animation_sub = AnimationUtils.loadAnimation(this, R.anim.forgot_pass_sbu_title_anim);
        subtitle.startAnimation(animation_sub);


        //Button to send code is pressed
        NextButton = findViewById(R.id.NextStep_verifyPhone);
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if phone number is valid
                if (!isValidPhone()) {
                    return;
                }
                //Call Method
                phoneExists();
            }

        });
        //BackButton
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
    public void phoneExists() {
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
                if (snapshot.exists()) {

                    phone.setError(null);
                    phone.setErrorEnabled(false);

                    //get phone and country code inputs
                    String phoneNumber = phone.getEditText().getText().toString().trim();
                    String picker = countryCode.getSelectedCountryCode();
                    String completePhoneNumber = "+" + picker + phoneNumber;

                    Intent launchSecondIntent = new Intent(getBaseContext(), CodeVerificationForgotPass.class);

                    //push to next activities
                    launchSecondIntent.putExtra("phoneNo", completePhoneNumber);
                    startActivity(launchSecondIntent);
                    finish();

                } else {
                    //There is no account in the database
                    phone.setError("There is no account with such number");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}