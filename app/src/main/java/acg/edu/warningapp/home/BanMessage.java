package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class BanMessage extends AppCompatActivity {
    String id, country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_message);


//        Preferences preferences = new Preferences(this);
//        HashMap<String, String> userData = preferences.userInformation();
//        id = userData.get(Preferences.Phone_No);
//        country = userData.get(Preferences.C_country);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}