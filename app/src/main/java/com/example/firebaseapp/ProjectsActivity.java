package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProjectsActivity extends AppCompatActivity {
    private ListView listView;
    private Button new_project_btn;
   private ArrayList<Project> projects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        listView = findViewById(R.id.project_listview);
        new_project_btn = findViewById(R.id.new_project_button);
        projects = new ArrayList<>();
        new_project_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(),projects_edit_and_newActivity.class);
                newIntent.putExtra("mode", "new");
                startActivity(newIntent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference projectsRef = FirebaseDatabase.getInstance().getReference("projects");
        projectsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String location = snapshot.child("location").getValue(String.class);
                    String customer_id = snapshot.child("customer_id").getValue(String.class);
                    String customer_name = snapshot.child("customer_name").getValue(String.class);
                    String createdAt = snapshot.child("created_at").getValue(String.class);
                    Project project = new Project(id,name, customer_id,location, createdAt);
                    project.setCustomer_name(customer_name);
                    projects.add(project);
                }
                ProjectsAdapter adapter = new ProjectsAdapter(ProjectsActivity.this, projects);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });
    }
}
