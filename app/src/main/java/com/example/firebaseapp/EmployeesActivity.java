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

public class EmployeesActivity extends AppCompatActivity {
    private ListView listView;
    private Button new_employee_btn;
    private ArrayList<Employee> employees;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        listView = findViewById(R.id.employee_listview);
        new_employee_btn = findViewById(R.id.new_employee_button);
        employees = new ArrayList<>();
        new_employee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(),employee_edit_and_newActivity.class);
                newIntent.putExtra("mode", "new");
                startActivity(newIntent);
            }
        });
        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("employees");
        employeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employees.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String created_at = snapshot.child("created_at").getValue(String.class);
                    String id = snapshot.child("id").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);
                    String salary_by_hour = snapshot.child("salary_by_hour").getValue(String.class);
                    Employee employee = new Employee(id, name, role, salary_by_hour,created_at);
                    employees.add(employee);
                }
                 EmployeesAdapter adapter = new EmployeesAdapter(EmployeesActivity.this, employees);
                 listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors that occur
            }
        });
    }
}