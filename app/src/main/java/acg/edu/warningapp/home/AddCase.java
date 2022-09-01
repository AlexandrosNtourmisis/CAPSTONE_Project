package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import acg.edu.warningapp.R;
import acg.edu.warningapp.register.Phone_verification_register;

public class AddCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case);

//-------------------------------------------------MENU-------------------------------------------------------------------------------
        //Menu link
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenuBar);

        //Set homePage selected
        bottomNavigationView.setSelectedItemId(R.id.add_case);

        //Item Selected change -- If new button from the menu is pressed -> start the designated activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_bar:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomePage.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add_case:

                        return true;
                    case R.id.guidelines:
                        startActivity(new Intent(getApplicationContext(), Guidelines.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ViewProfile.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });
//----------------------------------------------------------------------------------------------------------------------------------------

    }// END OF onCreate

//-------------------------------------------------------------BUTTONS METHODS (OnClick)-------------------------------------------------------------------------------------------------------

    //Fire case
    public void FireCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Fire";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-fire-96.png?alt=media&token=fb5b42f8-6528-4a28-8d45-67ff686b83a8";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Flood case
    public void FloodCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Flood";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-floods-96.png?alt=media&token=65925624-b218-4fd9-add7-8025857a409b";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Wildfire case
    public void WildfireCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Wildfire";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-fire-hazard-96.png?alt=media&token=3bc54aac-317a-4fb3-88fe-9128e0ad86d7";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Hurricane case
    public void HurricaneCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Hurricane";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-hurricane-96.png?alt=media&token=fe862081-c5e8-464e-ac76-4f7a42de292c";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Earthquake case
    public void EarthquakeCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Earthquake";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-earthquakes-90.png?alt=media&token=6f570af0-352b-4517-baec-65637aa09f93";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Tornado case
    public void TornadoCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Tornado";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-tornado-96.png?alt=media&token=abe9ee42-92b3-4bbc-800d-10a39a24d953";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    // Landslide case
    public void LandslideCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Landslide";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-landslide-96.png?alt=media&token=53487429-0e6e-4af7-86c1-e8e04b7c3339";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    // Tsunami case
    public void TsunamiCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Tsunami";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-tsunami-96.png?alt=media&token=3075348a-7aa4-4776-81ae-5d193bdc76a8";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    // IcyRoad case
    public void IcyRoadCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Icy Road";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-road-90.png?alt=media&token=f240e244-015a-4842-8d7c-00f56e87a41a";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    // Car Accident case
    public void CarAccidentCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Car Accident";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-crashed-car-96.png?alt=media&token=86c6fbf6-476b-45cc-bca3-c1ea12cdd2be";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Robbery Case
    public void RobberyCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Robbery";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-black-ski-mask-96.png?alt=media&token=180fcd3d-037a-4484-bfa0-823b1e41fce6";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Thunderstorm case
    public void Thunderstorm(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Thunderstorm";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-lightning-bolt-90.png?alt=media&token=ce195f62-2331-41db-9a8b-e72d67b7779f";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Terrorism case
    public void Terrorism(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Terrorism";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-robbery-96.png?alt=media&token=c0c7f5a6-4efa-42cd-bf5c-b63a04d035d6";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }

    //Other case than the suggested ones
    public void OtherCase(View view) {
        Intent postCase = new Intent(getBaseContext(), AddCase_secondPart.class);
        String type = "Other";
        String iconURL= "https://firebasestorage.googleapis.com/v0/b/warningapp-715fd.appspot.com/o/icons8-general-mandatory-action-96.png?alt=media&token=a7ef99cc-b781-4f65-84fd-fa4294f29ec8";
        //pass them to the next activity
        postCase.putExtra("type", type);
        postCase.putExtra("iconURL", iconURL);
        startActivity(postCase);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}//end of class