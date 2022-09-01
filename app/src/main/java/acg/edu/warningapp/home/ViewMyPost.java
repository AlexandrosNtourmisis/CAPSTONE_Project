package acg.edu.warningapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import acg.edu.warningapp.R;
import acg.edu.warningapp.login.CodeVerificationForgotPass;
import acg.edu.warningapp.login.NewPasswordChangedMessage;

public class ViewMyPost extends AppCompatActivity {

    private ImageView imageView;
    TextView textView_type, textView_city, textView_country, textView_time, textView_date, textView_author, details, severity;
    DatabaseReference myRef, deleteRef, ref;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<CommentsListViewHelper> options;
    FirebaseRecyclerAdapter<CommentsListViewHelper, CommentsViewHolder> adapter;
    String caseKey;
    String caseAuthor_id, caseType, iconURL, caseCity, caseCountry, caseTime, caseDate, caseAuthor, details_, severity_;
    CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_post);
        textView_type = findViewById(R.id.typeCase_post);
        imageView = findViewById(R.id.iconType_post);
        textView_city = findViewById(R.id.city_post);
        textView_country = findViewById(R.id.country_post);
        textView_time = findViewById(R.id.time_post);
        textView_date = findViewById(R.id.date_post);
        textView_author = findViewById(R.id.author_post);
        details = findViewById(R.id.details);
        severity = findViewById(R.id.severitytitle);
        cardView = findViewById(R.id.sev);

        //Recycler view for user's cases
        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Get intent from previous activity - ViewProfile class -
        String country = getIntent().getStringExtra("country");
        caseKey = getIntent().getStringExtra("post_id");
        //Database Path
        myRef = FirebaseDatabase.getInstance().getReference().child("Cases/" + country);

        //Database ref that will be deleted
        deleteRef = FirebaseDatabase.getInstance().getReference().child("Cases/" + country).child(caseKey);

        myRef.child(caseKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //get case's details
                    caseType = snapshot.child("type").getValue().toString();
                    iconURL = snapshot.child("iconURL").getValue().toString();
                    caseCity = snapshot.child("city").getValue().toString();
                    caseCountry = snapshot.child("country").getValue().toString();
                    caseTime = snapshot.child("time").getValue().toString();
                    caseDate = snapshot.child("date").getValue().toString();
                    caseAuthor = snapshot.child("author").getValue().toString();
                    caseAuthor_id = snapshot.child("author_id").getValue().toString();
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

                    //Display the comments
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    ref = database.getReference("Cases/" + caseCountry + "/" + caseKey + "/comments");
//                    samePerson();
                    displayComments();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//-----------------------------------MANUAL DELETE CASE---------------------------------------------
    public void deletePOST(View view) {
        //delete
        deleteRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Intent launchSecondIntent = new Intent(getBaseContext(), ViewProfile.class);
                Toast.makeText(ViewMyPost.this, "The case has been deleted", Toast.LENGTH_SHORT).show();
                startActivity(launchSecondIntent);
                finish();
            }
        });
    }
//--------------------------------------------------------------------------------------------------

//---------------------------------------------------------------DISPLAY COMMENTS--------------------------------------------------------------------
    private void displayComments() {
        //display the comments of the case
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
                        //if click on comment -> display the author of the comment
                        Intent intent = new Intent(ViewMyPost.this, ViewAuthor.class);
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
//----------------------------------------------------------------------------------------------------------------------------------------------------------

    public void close(View view) {
        Intent intent = new Intent(ViewMyPost.this, ViewProfile.class);
        startActivity(intent);
    }
}