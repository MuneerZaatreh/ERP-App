package com.example.firebaseapp;

import android.annotation.SuppressLint;
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

public class homeActivity extends AppCompatActivity {
    private ListView listView;
    private Button new_customer_btn;
   private ArrayList<Customer> customers;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.customer_listview);
        customers = new ArrayList<>();
        Button new_customer_btn = findViewById(R.id.new_customer_button);
        new_customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(),CustomerEditAndNewActivity.class);
                newIntent.putExtra("mode", "new");
                startActivity(newIntent);
            }
        });
        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("customers");
        customersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String name = snapshot.child("name").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    String createdAt = snapshot.child("created_at").getValue(String.class);
                    String url = snapshot.child("url").getValue(String.class);
                    customers.add(new Customer(id,name, age, createdAt,url));
                }
                CustomersAdapter adapter = new CustomersAdapter(homeActivity.this, customers);
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });
    }

}