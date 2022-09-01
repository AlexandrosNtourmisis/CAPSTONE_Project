package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class FirstMessage extends AppCompatActivity {
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_message);

        //Read id from the session
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        id = userData.get(Preferences.Phone_No);
    }

    //Pressing next button
    public void firstMessageActivated(View view) {
        //Get Database path
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("WarningApp_Users/" + id);

        //Query the user's profile
        Query userData = FirebaseDatabase.getInstance().getReference("WarningApp_Users/").orderByChild(id);
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                boolean fmessage = snapshot.child(id).child("first_message").getValue(boolean.class);
//                boolean fwarning = snapshot.child(id).child("first_warning").getValue(boolean.class);

                //Set that the first message was view by the user and set false the first warning
                databaseRef.child("first_message").setValue(true);
                databaseRef.child("first_warning").setValue(false);
                //Start next activity
                Intent launchSecondIntent = new Intent(getBaseContext(), ActivateCasesCheck.class);
                startActivity(launchSecondIntent);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}