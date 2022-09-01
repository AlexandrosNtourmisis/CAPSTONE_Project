package acg.edu.warningapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class ChangePassword extends AppCompatActivity {
    ImageView BackButton;
    TextInputLayout newPassword, rPassword, oldPassword;
    String country, fname, lname, email, password, phoneNo, notifications, safety_radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = findViewById(R.id.NewPasswordInput);
        rPassword = findViewById(R.id.NewRe_PasswordInput);
        oldPassword = findViewById(R.id.OldPasswordInput);


        //Read values from session
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        country = userData.get(Preferences.C_country);
        fname = userData.get(Preferences.F_name);
        lname = userData.get(Preferences.L_name);
        email = userData.get(Preferences.E_mail);
        password = userData.get(Preferences.T_password);
        phoneNo = userData.get(Preferences.Phone_No);
        notifications = userData.get(Preferences.N_otifications);
        safety_radius = userData.get(Preferences.Safety_radius);



        //When pressing back
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//------------------------------------------CHANGE PASSWORD-------------------------------------------------------
    public void Change_Pass(View view) {
        //If requirements are met
        if (!isValidPassword() | !isValidRePassword()) {
            return;
        }
        //if the old password is correct
        if (!PasswordMatches()) {
            return;
        }
        //Get input for the new password
        String password_d = newPassword.getEditText().getText().toString().trim();
        //Store it to database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WarningApp_Users");
        myRef.child(phoneNo).child("password").setValue(password_d);

        //Update the variable of password to the session
        Preferences preferences = new Preferences(ChangePassword.this);
        preferences.LoginS(fname, lname, email, phoneNo, password_d, country, notifications, safety_radius);

        //Direct the user to the view profile activity
        Intent intent = new Intent(getBaseContext(), ViewProfile.class);
        Toast.makeText(ChangePassword.this, "Password has been updated", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
//--------------------------------------------------------------------------------------------------------------------


//----------------------------------------PASSWORD REQUIREMENTS----------------------------------------------------------------------------------
    private boolean isValidPassword() {
        String validate = newPassword.getEditText().getText().toString().trim();
        //Password requirements
        String passValid = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$";

        //if the input is empty
        if (validate.isEmpty()) {
            newPassword.setError("The new password is required");
            return false;
            //if it matches the requirements
        } else if (!validate.matches(passValid)) {
            newPassword.setError(null);
            newPassword.setError(" No spaces at least with: 6 characters, 1 number, 1 upper case letter");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }

    }
    //Check if it matches the password input
    private boolean isValidRePassword() {
        String validate = rPassword.getEditText().getText().toString().trim();
        String revalidate = newPassword.getEditText().getText().toString().trim();

        if (validate.isEmpty()) {
            rPassword.setError("The re enter password is required");
            return false;
        } else if (!validate.matches(revalidate)) {
            rPassword.setError("Passwords do not match");
            return false;
        } else {
            rPassword.setError(null);
            rPassword.setErrorEnabled(false);
            return true;
        }

    }
//----------------------------------------------------------------------------------------------------------------------------------------------------


    //Check if the old password input is correct in order to apply the changes
    private boolean PasswordMatches() {

        String pass = oldPassword.getEditText().getText().toString().trim();
        if (pass.equals(password)) {
            oldPassword.setError(null);
            oldPassword.setErrorEnabled(false);
            return true;
        } else if (pass.isEmpty()){
            oldPassword.setError("The password is required");
            return false;
        }
        else{
            oldPassword.setError("The password is incorrect");
            return false;
        }

    }

}