package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.CodeVerificationForgotPass;
import acg.edu.warningapp.login.Preferences;

import static acg.edu.warningapp.home.Channels.CHANNEL_1_ID;

public class HomePage extends Activity {

    TextView textView;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Cases> options;
    FirebaseRecyclerAdapter<Cases, CasesViewHolder> adapter;
    DatabaseReference myRef;
    Date date_;
    String country, messageCase, time, radius, phoneNo, date, longitude, latitude;
    List<String> displayList, id_list, id_match_list, id_all, checktypesize, checkidsize;
    FusedLocationProviderClient fusedLocationProviderClient;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//-------------------------------------------------MENU-------------------------------------------------------------------------------
        //Menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenuBar);

        //Set homePage selected
        bottomNavigationView.setSelectedItemId(R.id.home);

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
                        return true;
                    case R.id.add_case:
                        startActivity(new Intent(getApplicationContext(), AddCase.class));
                        overridePendingTransition(0, 0);
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
//----------------------------------------------------------------------------------------------------------------------------------------------

        //Get current date
        date_ = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(date_);


        notificationManager = NotificationManagerCompat.from(this);
        displayList = new ArrayList<>();
        id_list = new ArrayList<>();
        id_match_list = new ArrayList<>();
        id_all = new ArrayList<>();
        checkidsize = new ArrayList<>();
        checktypesize = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Get values from session
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        time = userData.get(Preferences.N_otifications);
        radius = userData.get(Preferences.Safety_radius);
        phoneNo = userData.get(Preferences.Phone_No);
        textView = findViewById(R.id.texxxtttt);
        country = userData.get(Preferences.C_country);


        //set database path
        myRef = FirebaseDatabase.getInstance().getReference("Cases/" + country);

        //RecyclerView to display cases
        recyclerView = findViewById(R.id.recyclerCases);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

//------------------------------------AUTO DELETE CASES----------------------------------------------------------------------
        //Delete cases that the timestamp is older than seven days
        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS);
        Query oldItems = myRef.orderByChild("timestamp").endAt(cutoff);
        oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
//----------------------------------------------------------------------------------------------------------------------------
        //Search data from display cases method --Testing-purposes--
        displayCases("");
    }

    private void displayCases(String searchData) {

        //Display cases that their date is equal to current date
        Query data = myRef.orderByChild("date").equalTo(date);
        //Use the getters and setters from Cases class
        options = new FirebaseRecyclerOptions.Builder<Cases>().setQuery(data, Cases.class).build();
        //Use the CasesViewHolder class to display them
        adapter = new FirebaseRecyclerAdapter<Cases, CasesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CasesViewHolder holder, int position, @NonNull Cases model) {
                holder.textView_t.setText(model.getType());
                holder.textView_c.setText(model.getCity());
                holder.textView_co.setText(model.getCountry());
                holder.textView_tm.setText(model.getTime());
                holder.textView_d.setText(model.getDate());
                Picasso.get().load(model.getIconURL()).into(holder.imageView);
                holder.view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //On click of the case -> Start the ViewPost class by passing two variables values
                        Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                        intent.putExtra("post_id", getRef(position).getKey());
                        intent.putExtra("country", country);
                        startActivity(intent);


                    }
                });

            }

            @NonNull
            @Override
            public CasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_caseslistview, parent, false);
                return new CasesViewHolder(view);
            }
        };

        //Display the cases
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    //-----------------------------------------------------MANUAL AM I SAFE - BUTTON------------------------------------------------------
    public void checksafe(View view) {


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check location's permissions if allowed
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        //get user's location
                        Location locationA = new Location("point A");
                        locationA.setLatitude(location.getLatitude());
                        locationA.setLongitude(location.getLongitude());

                        //Query the cases
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Cases");
                        Query query = myRef.child(country).orderByKey();

                        //convert to float
                        float f_radius = Float.parseFloat(radius);

                        //clear lists
                        displayList.clear();
                        id_list.clear();
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot item : dataSnapshot.getChildren()) {

                                    if (!dataSnapshot.hasChildren()) {

                                        return;
                                    }


                                    //Get case's location information
                                    Location locationB = new Location("point B");
                                    if (dataSnapshot.exists()) {

                                        String latituded = item.child("latitude").getValue(String.class);
                                        String longitued = item.child("longitude").getValue(String.class);
                                        String cit = item.child("city").getValue(String.class);
                                        String type = item.child("type").getValue(String.class);
                                        String id = item.child("post_id").getValue(String.class);

                                        locationB.setLatitude(Float.parseFloat(latituded));
                                        locationB.setLongitude(Float.parseFloat(longitued));

                                        //Message for the CasesNearAlert class
                                        String city_type = cit + " (" + type + ")" + " - less than " + radius + "meters from you";

                                        //check distance between the two points
                                        float distance = locationA.distanceTo(locationB);
                                        //Compare distance
                                        if (distance < f_radius) {
                                            //add the cases that have less distance than the safety distance
                                            displayList.add(city_type);
                                            id_list.add(id);

                                        }

                                        //Check list size for notification's message
                                        if (displayList.size() < 2) {
                                            messageCase = "There is " + displayList.size() + " case near you";
                                        } else {
                                            messageCase = "There are " + displayList.size() + " cases near you";
                                        }

                                        //Notification's Title
                                        String title = "Cases Near you";
                                        //Create Notification
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(HomePage.this, CHANNEL_1_ID)
                                                .setSmallIcon(R.mipmap.logotriangle)
                                                .setContentTitle(title)
                                                .setContentText(messageCase)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

                                        //On click action
                                        Intent intent = new Intent(HomePage.this, CasesNearAlert.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        //Pass these values to the next activity
                                        intent.putExtra("message", messageCase);
                                        intent.putExtra("country", country);
                                        ArrayList<String> myList = new ArrayList<String>(displayList);
                                        ArrayList<String> mySList = new ArrayList<String>(id_list);
                                        intent.putExtra("array", myList);
                                        intent.putExtra("id_list", mySList);

                                        //Send Notification
                                        PendingIntent pendingIntent = PendingIntent.getActivity(HomePage.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent);
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0, builder.build());

                                    }

                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else {
                        //Set another request and get the last location
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomePage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


}



