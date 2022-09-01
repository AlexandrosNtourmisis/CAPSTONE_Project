package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class ViewPost extends AppCompatActivity {

    //Variables
    private ImageView imageView;
    CardView verify;
    TextView textView_type, textView_city, textView_country, textView_time, textView_date, textView_author, textView_author_id, verified, verified_message, details, severity;
    DatabaseReference myRef, ref;
    EditText comment_textInput;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<CommentsListViewHelper> options;
    FirebaseRecyclerAdapter<CommentsListViewHelper, CommentsViewHolder> adapter;
    String country, caseAuthor___id;
    Date date, date_;
    DateFormat dateFormat, dateFormat2;
    String cAth;
    Double latitude_d, longitude_d;
    String caseKey, longitude, latitude, lat, lon;
    String caseAuthor_id, caseType, iconURL, caseCity, caseCountry, caseTime, caseDate, caseAuthor, comment_id, fname, lname, id, verifiedNumber, details_, severity_;
    String verifications;
    CardView cardView;

    Color color;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        textView_type = findViewById(R.id.typeCase_post);
        imageView = findViewById(R.id.iconType_post);
        textView_city = findViewById(R.id.city_post);
        verify = findViewById(R.id.verify);
        textView_country = findViewById(R.id.country_post);
        textView_time = findViewById(R.id.time_post);
        textView_date = findViewById(R.id.date_post);
        textView_author = findViewById(R.id.author_post);
        comment_textInput = findViewById(R.id.CommentInput);
        verified = findViewById(R.id.verificationsNumber);
        verified_message = findViewById(R.id.verifiedMessage);
        details = findViewById(R.id.details);
        severity = findViewById(R.id.severitytitle);
        cardView = findViewById(R.id.sev);
        String ccountry = getIntent().getStringExtra("country");

        //Get values from session
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        fname = userData.get(Preferences.F_name);
        lname = userData.get(Preferences.L_name);
        id = userData.get(Preferences.Phone_No);
        country = userData.get(Preferences.C_country);

        //RecyclerView for comments display
        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Database path for the case with the specific case id
        myRef = FirebaseDatabase.getInstance().getReference().child("Cases/" + country);
        caseKey = getIntent().getStringExtra("post_id");


        myRef.child(caseKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Get case's information
                    caseType = snapshot.child("type").getValue().toString();
                    iconURL = snapshot.child("iconURL").getValue().toString();
                    caseCity = snapshot.child("city").getValue().toString();
                    lat = snapshot.child("latitude").getValue().toString();
                    lon = snapshot.child("longitude").getValue().toString();
                    caseCountry = snapshot.child("country").getValue().toString();
                    caseTime = snapshot.child("time").getValue().toString();
                    caseDate = snapshot.child("date").getValue().toString();
                    caseAuthor = snapshot.child("author").getValue().toString();
                    caseAuthor_id = snapshot.child("author_id").getValue().toString();
                    verifications = snapshot.child("verifications").getValue().toString();
                    details_ = snapshot.child("details").getValue().toString();
                    severity_ = snapshot.child("severity").getValue().toString();

                    //change background color depending on the severity of the case
                    if (severity_.equals("low")) {
                        cardView.setCardBackgroundColor(Color.GREEN);

                    } else if (severity_.equals("medium")) {
                        cardView.setCardBackgroundColor(Color.YELLOW);
                    } else {
                        cardView.setCardBackgroundColor(Color.RED);
                    }

                    //Display the verifications
                    if (verifications.equals("0")) {

                        verified.setAlpha(0);
                    } else if (verifications.equals("1")) {
                        verified.setAlpha(1);
                        verifiedNumber = verifications + " verification";
                        verified.setText(verifiedNumber);
                    } else {
                        verified.setAlpha(1);
                        verifiedNumber = verifications + " verifications";
                        verified.setText(verifiedNumber);
                    }

                    //Display the values
                    Picasso.get().load(iconURL).into(imageView);
                    textView_type.setText(caseType);
                    textView_city.setText(caseCity);
                    textView_country.setText(caseCountry);
                    textView_time.setText(caseTime);
                    textView_date.setText(caseDate);
                    textView_author.setText(caseAuthor);
                    severity.setText(severity_);
                    details.setText(details_);

                    //Database path for comments
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    ref = database.getReference("Cases/" + caseCountry + "/" + caseKey + "/comments");
//                    samePerson();
                    displayComments();
                    hasVerified();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    //View Author button -- pass the values to the ViewAuthor class
    public void viewAuthor(View view) {
        Intent intent = new Intent(getBaseContext(), ViewAuthor.class);
        intent.putExtra("author_id", caseAuthor_id);
        intent.putExtra("country", caseCountry);
        intent.putExtra("author", caseAuthor);
        startActivity(intent);
    }

    //Display Comments
    private void displayComments() {
        options = new FirebaseRecyclerOptions.Builder<CommentsListViewHelper>().setQuery(ref, CommentsListViewHelper.class).build();
        adapter = new FirebaseRecyclerAdapter<CommentsListViewHelper, CommentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull CommentsListViewHelper model) {
                holder.textView_comment.setText(model.getComment());
                holder.textView_commenter.setText(model.getCommenter());
                holder.textView_tm.setText(model.getTime());
                holder.textView_d.setText(model.getDate());
                String commenter = model.getCommenter();
                String ccountry = model.getCountry();
                String commenter_id = model.getCommenter_id();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //click on comment --> view author's profile
                        Intent intent = new Intent(ViewPost.this, ViewAuthor.class);
                        intent.putExtra("author_id", commenter_id);
                        intent.putExtra("author", commenter);
                        intent.putExtra("country", ccountry);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_listview, parent, false);
                return new CommentsViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    //--------------------------------------ADD COMMENT---------------------------------------------------------------------
    public void addCOM(View view) {
        //if comment is not empty
        if (!CommentCheck()) {
            return;
        }
        //get information of comment
        String comment = comment_textInput.getText().toString();
        String commenter = fname + " " + lname;
        //Get current time and date
        date = Calendar.getInstance().getTime();
        date_ = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aaa");
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        String time = dateFormat.format(date);
        String date = dateFormat2.format(date_);

        //Database path for comments
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        comment_id = database.getReference("Cases" + country + "/" + caseKey + "/comments").push().getKey();
        DatabaseReference myRef = database.getReference("Cases/" + caseCountry + "/" + caseKey + "/comments");
        //add comment to the database along with its information
        myRef.child(comment_id).child("comment").setValue(comment);
        myRef.child(comment_id).child("commenter").setValue(commenter);
        myRef.child(comment_id).child("commenter_id").setValue(id);
        myRef.child(comment_id).child("country").setValue(country);
        myRef.child(comment_id).child("time").setValue(time);
        myRef.child(comment_id).child("date").setValue(date);

        //Set empty the comment input area
        comment_textInput.setText("");
    }
//----------------------------------------------------------------------------------------------------------------------

    //--------------------------------------VERIFY CASE-----------------------------------------------------------------------------------------------------------------------
    public void verify(View view) {
        //Query the verification ids from database
        Query userDataD = FirebaseDatabase.getInstance().getReference("Cases/" + caseCountry + "/" + caseKey + "/verifications_ids").orderByChild("id").equalTo(id);

        userDataD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if user's id exist in the database of the verification ids
                if (snapshot.exists()) {

                    Toast.makeText(ViewPost.this, "You have already verified this ", Toast.LENGTH_SHORT).show();
                    verify.setVisibility(View.GONE);


                } else {

                    //Database Path for id in the verifications
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Cases/" + caseCountry + "/" + caseKey + "/verifications_ids/" + id);
                    //Set user's id to the database of the verifications
                    myRef.child("id").setValue(id);
                    //Database Path for verifications
                    DatabaseReference verf = database.getReference("Cases/" + caseCountry + "/" + caseKey);
                    Query userData = FirebaseDatabase.getInstance().getReference("Cases/" + caseCountry).orderByChild(caseKey);
                    userData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Add plus one to the verifications
                            Integer ver = snapshot.child(caseKey).child("verifications").getValue(Integer.class);
                            Integer total = ver + 1;
                            verf.child("verifications").setValue(total);
                            verify.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    //-------------------------------------------------CHECK IF THE USER HAS VERIFIED-----------------------------------------------------------------------------------------
    private void hasVerified() {

        Query userDataD = FirebaseDatabase.getInstance().getReference("Cases/" + caseCountry + "/" + caseKey + "/verifications_ids").orderByChild("id").equalTo(id);

        userDataD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    verify.setVisibility(View.GONE);
                    verified_message.setVisibility(View.VISIBLE);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Check if the user is the author
    private void samePerson() {
        if (caseAuthor_id.equals(id)) {
            verify.setVisibility(View.GONE);
        }
    }

    public void close(View view) {
        finish();

    }

    //check if comment input is empty
    private boolean CommentCheck() {

        String pass = comment_textInput.getText().toString().trim();
        if (pass.isEmpty()) {
            comment_textInput.setError("The comment cant be empty");
            return false;
        } else {
            comment_textInput.setError(null);
            return true;
        }

    }

    public void CheckSafe(View view) {

        //Check location's permissions
        if (ActivityCompat.checkSelfPermission(ViewPost.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                && ActivityCompat.checkSelfPermission(ViewPost.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(ViewPost.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        //Get user's location
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());

                        try {
                            Geocoder geocoder = new Geocoder(ViewPost.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            longitude_d = addresses.get(0).getLongitude();
                            latitude_d = addresses.get(0).getLatitude();
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                            Intent intent = new Intent(getApplicationContext(), MapsActivity_.class);
                            //Pass these values to the MapsActivity_
                            intent.putExtra("lat", lat);
                            intent.putExtra("lon", lon);
                            intent.putExtra("latitude", latitude);
                            intent.putExtra("longitude", longitude);
                            startActivity(intent);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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
                                latitude = String.valueOf(location1.getLatitude());
                            }
                        };
                        if (ActivityCompat.checkSelfPermission(ViewPost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewPost.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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