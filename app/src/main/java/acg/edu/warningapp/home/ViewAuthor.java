package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.Preferences;

public class ViewAuthor extends AppCompatActivity {

    Button report;
    TextView textView_author, report_message;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Cases> options;
    FirebaseRecyclerAdapter<Cases, CasesViewHolder> adapter;
    String author_id, country, author, id;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_author);

        textView_author = findViewById(R.id.author_fname);

        //Get session values
        Preferences preferences = new Preferences(this);
        HashMap<String, String> userData = preferences.userInformation();
        id = userData.get(Preferences.Phone_No);

        //Get values from previous activity
        author_id = getIntent().getStringExtra("author_id");
        country = getIntent().getStringExtra("country");
        author = getIntent().getStringExtra("author");

        //Database paths
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("WarningApp_Users/" + author_id);
        //Query the user by author id
        Query checktotal = FirebaseDatabase.getInstance().getReference("WarningApp_Users/").orderByChild(author_id);
        checktotal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer total = snapshot.child(author_id).child("reports").getValue(Integer.class);


                //If the reports of the author's id profile are more than 10, hide the report button
                if (total > 10) {
                    cardView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        report_message = findViewById(R.id.ReportMessage);

        //Report Button
        cardView = findViewById(R.id.report);
        //Cases RecyclerView
        recyclerView = findViewById(R.id.recyclerCasesAuthor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        textView_author.setText(author);

        samePerson();
       hasReported();
        displayAuthorCases();


    }

    //Display the author's cases
    private void displayAuthorCases() {
        //Query the cases that have the author id value
        Query userData = FirebaseDatabase.getInstance().getReference("Cases/" + country).orderByChild("author_id").equalTo(author_id);
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
                                    //Click a case -> View case in ViewPost activity
                                    Intent intent = new Intent(ViewAuthor.this, ViewPost.class);
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

                    //display the list
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    //-----------------------------------------------------------REPORT USER---------------------------------------------------------------------------------------------------
    public void ReportUser(View view) {

        //Create alertDialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Report this user");
        alert.setMessage("Are you sure you want to report this user?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query userDataD = FirebaseDatabase.getInstance().getReference("WarningApp_Users/" + author_id + "/reports_ids").orderByChild("id").equalTo(id);

                userDataD.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //If user has already reported the author
                        if (snapshot.exists()) {
                            Toast.makeText(ViewAuthor.this, "You have reported this user", Toast.LENGTH_SHORT).show();
                            cardView.setVisibility(View.INVISIBLE);
                            report_message.setVisibility(View.VISIBLE);


                        } else {
                            //Database Path
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("WarningApp_Users/" + author_id + "/reports_ids/" + id);
                            //Save user's id to author's profile - reports database
                            myRef.child("id").setValue(id);

                            //Database Path
                            DatabaseReference verf = database.getReference("WarningApp_Users/" + author_id);
                            Query userData = FirebaseDatabase.getInstance().getReference("WarningApp_Users/").orderByChild(author_id);
                            userData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // get reports value from author's profile in database
                                    Integer ver = snapshot.child(author_id).child("reports").getValue(Integer.class);
                                    //get the first message value
                                    boolean fmessage = snapshot.child(author_id).child("first_message").getValue(boolean.class);
                                    //Add plus one to the reports that the author might have
                                    Integer total = ver + 1;

                                    //add the total reports
                                    verf.child("reports").setValue(total);

                                    //if the first message is not true and the reports are more than 5, the first warning is true
                                    if (!fmessage) {
                                        if (total > 5) {
                                            boolean f_warning = true;
                                            //change first warning to true
                                            verf.child("first_warning").setValue(f_warning);
                                        }
                                    }

                                    //if the reports are more than 10
                                    if (total > 10) {
                                        //update the following to true
                                        verf.child("final_warning").setValue(true);
                                        verf.child("ban_warning").setValue(true);


                                    }

                                    //hide report button
                                    report.setVisibility(View.GONE);
                                    //display the message that user has reported this user
                                    report_message.setVisibility(View.VISIBLE);

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
        });
        //cancel alert box
        alert.setNegativeButton("Cancel", null);
        alert.create().show();
//---------------------------------------------------------------------------------------------------------------------------------------------------------------

    }

    //Check if the user has already reported the author
    private void hasReported() {

        Query userDataD = FirebaseDatabase.getInstance().getReference("WarningApp_Users/" + author_id + "/reports_ids").orderByChild("id").equalTo(id);

        userDataD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //hide report button
                    cardView.setVisibility(View.GONE);
                    report_message.setVisibility(View.VISIBLE);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Check if the user is the author of the case
    private void samePerson() {
        if (author_id.equals(id)) {
            report.setVisibility(View.GONE);
        }
    }

    public void close(View view) {
        finish();
    }
}