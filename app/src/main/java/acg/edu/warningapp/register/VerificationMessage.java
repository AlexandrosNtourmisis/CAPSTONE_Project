package acg.edu.warningapp.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import acg.edu.warningapp.MainActivity;
import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Login;

public class VerificationMessage extends Activity {
    private Button LoginButton;
    CardView cardView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verification_message);
        cardView = findViewById(R.id.cardview_register);
        imageView = findViewById(R.id.VerifiedIcon);

        //Start Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.verification_anim);
        cardView.startAnimation(animation);
        Animation animation_ = AnimationUtils.loadAnimation(this, R.anim.verification_icon_anim);
        imageView.startAnimation(animation_);

        //Login Button
        LoginButton = findViewById(R.id.VerifiedLogin);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSecondIntent = new Intent(getBaseContext(), Login.class);
                startActivity(launchSecondIntent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent launchSecondIntent = new Intent(getBaseContext(), Login.class);
        startActivity(launchSecondIntent);

    }
}