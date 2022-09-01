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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import acg.edu.warningapp.MainActivity;
import acg.edu.warningapp.R;

public class NewPassword extends Activity {
    private Button ChangePassButton;
    TextInputLayout password, rpassword;
    String phoneNo;
    CardView cardView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_password);
        cardView = findViewById(R.id.forgotPasswordCard);

        //Start Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cardvie_slide_anim);
        cardView.startAnimation(animation);

        //Get value from previous activity
        phoneNo = getIntent().getStringExtra("phoneNo");

        password = findViewById(R.id.NewPasswordInput);
        rpassword = findViewById(R.id.NewRe_PasswordInput);

        //Change Password button
        ChangePassButton = findViewById(R.id.Verify_Code_ForgotPass);
        ChangePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If password's requirements are met
                if ( !isValidPassword() | !isValidRePassword()) {
                    return;
                }

                //get password
                String pass = password.getEditText().getText().toString().trim();
                //Update user's password to database
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WarningApp_Users");
                myRef.child(phoneNo).child("password").setValue(pass);

                //Direct the user to the successful message
                Intent launchSecondIntent = new Intent(getBaseContext(), NewPasswordChangedMessage.class);
                startActivity(launchSecondIntent);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent launchSecondIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(launchSecondIntent);
    }

//-----------------------------------Password Valid-----------------------------------------------------------
    private boolean isValidPassword() {

        String validate = password.getEditText().getText().toString().trim();
        String passValid = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$";


        if (validate.isEmpty()) {
            password.setError("The new password is required");
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
//---------------------------------------------------------------------------------------------------------------

//-------------------------------RE_ENTER PASSWORD - MATCH PASSWORD----------------------------------------------
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
//---------------------------------------------------------------------------------------------------------------

}