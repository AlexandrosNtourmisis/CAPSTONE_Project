package acg.edu.warningapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import acg.edu.warningapp.R;

public class ViewGuideline extends AppCompatActivity {
    TextView title, guidelines;
    String type, guideline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guideline);

        //get values from previous activity
        type = getIntent().getStringExtra("type");
        guideline = getIntent().getStringExtra("guidelines");


        title = findViewById(R.id.guidelines_);
        guidelines = findViewById(R.id.guide);
        //Display guideline
        title.setText(type);
        guidelines.setText(guideline);
    }
}