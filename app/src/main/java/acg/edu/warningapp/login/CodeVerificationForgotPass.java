package acg.edu.warningapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import acg.edu.warningapp.R;
import acg.edu.warningapp.register.CodeVerification;
import acg.edu.warningapp.register.RegisterUserWithPhone;

public class CodeVerificationForgotPass extends Activity {
    private Button VerifyButton;
    private ImageView BackButton;
    String codeSent;
    TextView phoneGG;
    TextInputLayout sixdigit;
    String fname, lname, email, password, age, phoneNo, phone, id;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_code_verification_forgot_pass);
        cardView = findViewById(R.id.forgotPasswordCard);

        //Create and load animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cardvie_slide_anim);
        cardView.startAnimation(animation);

        sixdigit = findViewById(R.id.sixdigitcode_fp);

        //get value from previous activity -ForgotPasswordRequestPhoneNumber-
        phoneNo = getIntent().getStringExtra("phoneNo");


        //Call Method sendCode
        sendCode(phoneNo);

        //Verify button
        VerifyButton = findViewById(R.id.Verify_Code_FP);
        VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codeC = sixdigit.getEditText().getText().toString();
                //check if not empty
                if (!codeC.isEmpty()) {
                    //call method validate
                    validate(codeC);
                    ;

                }
            }

        });
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.e("*** First Activity", "onDestroy()");
        super.onDestroy();
    }

    //Send code from firebase authentication
    private void sendCode(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
//        Toast.makeText(CodeVerificationForgotPass.this, "The code has been sent", Toast.LENGTH_SHORT).show();

    }

    //Callbacks Firebase
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeSent = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String codeC = phoneAuthCredential.getSmsCode();
                    //if the code is not null, set the code to the input automatically... then call validation method
                    if (codeC != null) {
                        sixdigit.getEditText().setText(codeC);
                        validate(codeC);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    //in case sms failed
                    Toast.makeText(CodeVerificationForgotPass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    // assign the codes sent and inputs
    private void validate(String codeC) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeC);
        signInWithPhoneAuthCredential(credential);
    }

    //check if the input code is the same with the code that was sent
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            goToChange();

                        } else {
                            // The verification code entered was invalid

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(CodeVerificationForgotPass.this, "Something went wrong... Please try again", Toast.LENGTH_SHORT);

                            }
                        }
                    }
                });
    }

    //Success = start the next activity
    private void goToChange() {
        Intent launchSecondIntent = new Intent(getBaseContext(), NewPassword.class);

        //push to next activities

        launchSecondIntent.putExtra("phoneNo", phoneNo);


        startActivity(launchSecondIntent);
        finish();
    }

}