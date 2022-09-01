package acg.edu.warningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import acg.edu.warningapp.home.ActivateCasesCheck;
import acg.edu.warningapp.home.BanMessage;
import acg.edu.warningapp.home.FirstMessage;
import acg.edu.warningapp.login.Login;
import acg.edu.warningapp.login.Preferences;

public class MainActivity extends Activity {
    private Button launchLogin;
    ImageView imageView;
    TextView f_title, s_title;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.logo);
        f_title = findViewById(R.id.warning);
        s_title = findViewById(R.id.app);

        //Start Animations
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        imageView.startAnimation(animation);
        Animation animation_t = AnimationUtils.loadAnimation(this, R.anim.warning_anim);
        f_title.startAnimation(animation_t);
        Animation animation_s = AnimationUtils.loadAnimation(this, R.anim.cardview_newuser_anim);
        s_title.startAnimation(animation_s);


        //Read values from session
        Preferences preferences = new Preferences(MainActivity.this);
        HashMap<String, String> userData = preferences.userInformation();
        String id = userData.get(Preferences.Phone_No);

        //Delay Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Check if session is logged in
                if (preferences.isLoggedIn()) {
                    //if the id is null, then the user will have to login
                    if (id == null) {
                        Intent launchSecondIntent = new Intent(getBaseContext(), Login.class);
                        startActivity(launchSecondIntent);
                        finish();

                    } else {
                        //Query the user's profile path in database
                        Query userData = FirebaseDatabase.getInstance().getReference("WarningApp_Users/").orderByChild(id);
                        userData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //get from database
                                boolean fwarning = snapshot.child(id).child("first_warning").getValue(boolean.class);
                                boolean final_warning = snapshot.child(id).child("final_warning").getValue(boolean.class);

                                //If the first_warning is true, start then firstMessage class (user has more than 5 reports)
                                if (fwarning) {
                                    Intent launchSecondIntent = new Intent(getBaseContext(), FirstMessage.class);
                                    startActivity(launchSecondIntent);
                                    finish();
                                } else {
                                    //If the final warning is true, start then BanMessage class (user has more than 10 reports)
                                    if (final_warning) {
                                        Intent launchSecondIntent = new Intent(getBaseContext(), BanMessage.class);
                                        startActivity(launchSecondIntent);
                                        finish();
                                    } else {
                                        //else start the "normal" activity
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


                } else {
                    //Start Login Class
                    Intent launchSecondIntent = new Intent(getBaseContext(), Login.class);
                    startActivity(launchSecondIntent);
                    finish();
                }

            }

            //Delay timer
        }, 4500);
    }
//        });
//
//    }


}