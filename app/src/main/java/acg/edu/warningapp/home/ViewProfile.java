package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Login;
import acg.edu.warningapp.login.Preferences;

public class ViewProfile extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textView_fname, textView_lname, textView_country;
    FirebaseRecyclerOptions<Cases> options;
    FirebaseRecyclerAdapter<Cases, CasesViewHolder> adapter;
    String country, fname, lname, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

//-------------------------------------------------MENU-------------------------------------------------------------------------------
        //Menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenuBar);

        //Set homePage selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

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
                        startActivity(new Intent(getApplicationContext(), Guidelines.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;


                }
                return false;
            }
        });
//----------------------------------------------------------------------------------------------------------------------------------------

        //Read session values
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        country = userData.get(Preferences.C_country);
        fname = userData.get(Preferences.F_name);
        lname = userData.get(Preferences.L_name);
        id = userData.get(Preferences.Phone_No);


        textView_fname = findViewById(R.id.profileFname);
        textView_lname = findViewById(R.id.profileLname);
        //Display name
        textView_fname.setText(fname);
        textView_lname.setText(lname);


        //RecyclerView for user's cases
        recyclerView = findViewById(R.id.displayMyCases);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse order
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Call Method
        displayMyCases();

    }

    //Display user's cases
    private void displayMyCases() {
        //Query the cases that have the user id as case id
        Query userData = FirebaseDatabase.getInstance().getReference("Cases/" + country).orderByChild("author_id").equalTo(id);

        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    options = new FirebaseRecyclerOptions.Builder<Cases>().setQuery(userData, Cases.class).build();
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
                                    //display the case's information in ViewMyPost
                                    Intent intent = new Intent(ViewProfile.this, ViewMyPost.class);
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

                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Edit Profile button
    public void EditProf(View view) {
        Intent intent = new Intent(getBaseContext(), EditProfile.class);
        startActivity(intent);
    }

    //Change Password button
    public void ChangePass(View view) {
        Intent intent = new Intent(getBaseContext(), ChangePassword.class);
        startActivity(intent);
    }

    //Change Location Settings button
    public void ChangeLocationSet(View view) {
        Intent intent = new Intent(getBaseContext(), LocationChanges.class);
        startActivity(intent);
    }

    //Log out button
    public void Logout(View view) {
        Preferences preferences = new Preferences(ViewProfile.this);
        preferences.logOUT();
        startActivity(new Intent(getApplicationContext(), Login.class));
    }
}