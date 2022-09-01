package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import acg.edu.warningapp.R;

public class Guidelines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidelines);

//-------------------------------------------------MENU-------------------------------------------------------------------------------
        //Menu link
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenuBar);

        //Set homePage selected
        bottomNavigationView.setSelectedItemId(R.id.guidelines);

        //Item Selected change
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
                        startActivity(new Intent(getApplicationContext(), AddCase.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.guidelines:
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

//------------------------------------------------------------------BUTTONS METHODS (OnClick)-------------------------------------------------------------------------------------------------------
    public void viewFireGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.fire_guideline_title);
        String guidelines = getResources().getString(R.string.fire_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewTerrorismGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.terrorism_guideline_title);
        String guidelines = getResources().getString(R.string.terrorism_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewFloodGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.flood_guideline_title);
        String guidelines = getResources().getString(R.string.flood_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewWildfireGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.wildfire_guideline_title);
        String guidelines = getResources().getString(R.string.wildfire_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewHurricaneTornadoGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.hurricane_tornado_guideline_title);
        String guidelines = getResources().getString(R.string.hurricane_tornado_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewEarthquakeGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.earthquake_guideline_title);
        String guidelines = getResources().getString(R.string.earthquake_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewLandslideGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.landslide_guideline_title);
        String guidelines = getResources().getString(R.string.landslide_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewTsunamiGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.tsunami_guideline_title);
        String guidelines = getResources().getString(R.string.tsunami_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewIcyroadGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.icyroad_guideline_title);
        String guidelines = getResources().getString(R.string.icyroad_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewThunderstormGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.thunderstorm_guideline_title);
        String guidelines = getResources().getString(R.string.thunderstorm_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewCarAccidentGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.carAccident_guideline_title);
        String guidelines = getResources().getString(R.string.carAccident_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }

    public void viewRobberyGuideline(View view) {
        Intent viewguideline = new Intent(getBaseContext(), ViewGuideline.class);
        String type = getResources().getString(R.string.robbery_guideline_title);
        String guidelines = getResources().getString(R.string.robbery_guideline);
        viewguideline.putExtra("type", type);
        viewguideline.putExtra("guidelines", guidelines);

        startActivity(viewguideline);
    }
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}