package acg.edu.warningapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import acg.edu.warningapp.MainActivity;
import acg.edu.warningapp.R;

public class NewPasswordChangedMessage extends Activity {
    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_password_changed_message);

        //Login Button
        LoginButton = findViewById(R.id.VerifiedNewPassLogin);
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
        finish();
    }
}