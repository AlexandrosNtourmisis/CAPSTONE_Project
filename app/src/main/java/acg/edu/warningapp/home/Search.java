package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class Search extends AppCompatActivity {

    EditText searchBar;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Cases> options;
    FirebaseRecyclerAdapter<Cases, CasesViewHolder> adapter;
    DatabaseReference myRef;
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//-------------------------------------------------MENU-------------------------------------------------------------------------------
        //Menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomMenuBar);

        //Set homePage selected
        bottomNavigationView.setSelectedItemId(R.id.search_bar);

        //Item Selected change
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_bar:

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
                        startActivity(new Intent(getApplicationContext(), ViewProfile.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });
//----------------------------------------------------------------------------------------------------------------------------------------

        //Read session's values
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        country = userData.get(Preferences.C_country);

        //Database path
        myRef = FirebaseDatabase.getInstance().getReference("Cases/" + country);

        searchBar = findViewById(R.id.search_bar);

        //RecyclerView for cases
        recyclerView = findViewById(R.id.recyclerCases);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Display the cases in reverse order than on the database
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //Display cases search value
        displayCases("");
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String lower = s.toString().toLowerCase();
                // if user types in search value -> display the cases that contain the search value
                if (s.toString() != null) {

                    displayCasesP(lower);
                } else {
                    //no value in search -> display all cases
                    displayCases("");
                }

            }
        });


    }

    //Display all cases
    private void displayCases(String searchData) {
        options = new FirebaseRecyclerOptions.Builder<Cases>().setQuery(myRef, Cases.class).build();
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

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    //Display searched city's cases
    private void displayCasesP(String searchData) {

        //Get from database the search value of every case
        Query data = myRef.orderByChild("searchvalue").startAt(searchData).endAt(searchData + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Cases>().setQuery(data, Cases.class).build();
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
                        //On the clicked case -> start ViewPost class and pass the two values
                        Intent intent = new Intent(Search.this, ViewPost.class);

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
        //If the search input is empty -> change the searchData
        if (searchData.isEmpty()) {
            displayCases("");
        }
    }

}