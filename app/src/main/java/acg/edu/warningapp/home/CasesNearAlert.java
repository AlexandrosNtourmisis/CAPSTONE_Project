package acg.edu.warningapp.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import acg.edu.warningapp.R;

public class CasesNearAlert extends AppCompatActivity {
    String country, message;
    List<String> displayList, id_list;
    ListView listv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_near_alert);

        listv = findViewById(R.id.listview_nearCases);

        //get values from previous activities
        country = getIntent().getStringExtra("country");
        message = getIntent().getStringExtra("message");
        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("array");
        ArrayList<String> mySList = (ArrayList<String>) getIntent().getSerializableExtra("id_list");


        //display the list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CasesNearAlert.this, android.R.layout.simple_list_item_1, myList);
        listv.setAdapter(arrayAdapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //display the case that the user clicks (ViewPost class)
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) mySList.get(position);
                Intent intent = new Intent(getApplicationContext(), ViewPost.class);
                intent.putExtra("country", country);
                intent.putExtra("post_id", selectedItem);
                startActivity(intent);

            }
        });

    }
}