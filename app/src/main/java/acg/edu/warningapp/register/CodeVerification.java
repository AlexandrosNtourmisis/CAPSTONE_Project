package acg.edu.warningapp.register;

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
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.CodeVerificationForgotPass;

public class CodeVerification extends Activity {
    private ImageView BackButton;
    private Button NextButton;

    //    TextInputLayout VerifyingCodeInput;
    String codeSent;
    TextView phoneGG;
    PinView pinFromUser;
    String fname, lname, email, password, age, phoneNo, country, notifications, safety_radius;
    Integer reports;
    boolean first_warning, final_warning, first_message, ban_message;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_code_verification);
        cardView = findViewById(R.id.cardview_register);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cardvie_slide_anim);
        cardView.startAnimation(animation);

        //link the layouts
        pinFromUser = findViewById(R.id.pin_view);
        //TESTING
//        phoneGG = findViewById(R.id.PhoneVerificationTitle);

        //get from previous activities
        phoneNo = getIntent().getStringExtra("phoneNo");
        fname = getIntent().getStringExtra("fname");
        country = getIntent().getStringExtra("country");
        lname = getIntent().getStringExtra("lname");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        age = getIntent().getStringExtra("age");
        first_warning = false;
        final_warning = false;
        first_message = false;
        ban_message = false;
        reports = 0;
        notifications = "15";
        safety_radius = "500";

        //TESTING
//        phoneGG.setText(phoneNo);

        //send the 6 digit code
        sendCode(phoneNo);

        //back button
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

    //Code send
    private void sendCode(String phoneNo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);


    }

    //Callbacks Firebase
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    //assign code sent
                    codeSent = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String codeC = phoneAuthCredential.getSmsCode();
                    //if the code is not null, set the code to the input automatically... then call validation method
                    if (codeC != null) {
                        pinFromUser.setText(codeC);
                        validate(codeC);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    //in case sms failed
                    Toast.makeText(CodeVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void validate(String codeC) {
        // assign the codes sent and inputs
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeC);
        signInWithPhoneAuthCredential(credential);
    }

    //validate that the code is correct
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //if the code is correct, then call register method
                            registerUser();


                        } else {
                            //if verification code is not correct
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(CodeVerification.this, "Something went wront... Please try again", Toast.LENGTH_SHORT);
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    //register user's information to the database
    private void registerUser() {
        //Connect database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Define database path
        DatabaseReference myRef = database.getReference("WarningApp_Users");
        //Using setters and getters from RegisterUserWithPhone class (helper class)
        RegisterUserWithPhone newUser = new RegisterUserWithPhone(fname, lname, email, phoneNo, password, age, country, reports, first_warning, final_warning, first_message, ban_message, notifications, safety_radius);
        //order them by their unique phone number
        myRef.child(phoneNo).setValue(newUser);
        //Navigate to the next activity
        Intent launchSecondIntent = new Intent(getBaseContext(), VerificationMessage.class);
        startActivity(launchSecondIntent);
        finish();
    }

    public void SuccessMessageVerified(View view) {
        //get the input
        String codeC = pinFromUser.getText().toString();
        if (!codeC.isEmpty()) {
            //call validate method
            validate(codeC);
            ;

        }
    }
}