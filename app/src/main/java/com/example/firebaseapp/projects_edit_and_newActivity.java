package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class projects_edit_and_newActivity extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private DatabaseReference optionsRef,projectref;
    private ArrayAdapter<String> adapter;
    private EditText ed_name,ed_location;
    private TextView header_title;
    private Button submit_project_action;
    String customerId = "";
    String selectedName ="";
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_edit_and_new);
        autoCompleteTextView = findViewById(R.id.select2_project_customer_name);
        projectref = FirebaseDatabase.getInstance().getReference("projects");
        header_title = findViewById(R.id.Header_title);
        ed_name = findViewById(R.id._name_project_name);
        ed_location = findViewById(R.id._name_customer_location);
        submit_project_action = findViewById(R.id.new_edit_project_action);
        optionsRef = FirebaseDatabase.getInstance().getReference("customers");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedName = (String) parent.getItemAtPosition(position);
                Query query = optionsRef.orderByChild("name").equalTo(selectedName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                            customerId = customerSnapshot.getKey();
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        optionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot optionSnapshot : dataSnapshot.getChildren()) {
                    String option = optionSnapshot.child("name").getValue(String.class);
                    adapter.add(option);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if any
            }
        });
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        Project project = (Project) intent.getSerializableExtra("project");
        if ("edit".equals(mode)) {
            ed_name.setText(project.getName());
            autoCompleteTextView.setText(project.getCustomer_name());
            ed_location.setText(project.getLocation());
        } else if ("new".equals(mode)) {
            header_title.setText("New Project");
            submit_project_action.setText("Create");
        }
        submit_project_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(currentDate);
                if(ed_name.getText().toString().isEmpty() || ed_location.getText().toString().isEmpty()  ){
                    Toast.makeText(projects_edit_and_newActivity.this, "Some Fields are Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if("new".equals(mode)){
                    Query query = projectref.orderByChild("name").equalTo(ed_name.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                String project_id = projectref.push().getKey();
                                Project insert_project = new Project(ed_name.getText().toString(),customerId, ed_location.getText().toString(), formattedDate);
                                insert_project.setCustomer_name(selectedName);
                                projectref.child(project_id).setValue(insert_project);
                                showSweetAlertDialog("New Project","Created successfully");
                            }else{
                                Toast.makeText(projects_edit_and_newActivity.this, "Name Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(projects_edit_and_newActivity.this, "DataBase Erorr", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if ("edit".equals(mode)){
                    String projectId = project.getId();
                    DatabaseReference projectRefz = FirebaseDatabase.getInstance().getReference("projects").child(projectId);
                    Map<String, Object> updatedValues = new HashMap<>();
                    updatedValues.put("name", ed_name.getText().toString());
                    updatedValues.put("location", ed_location.getText().toString());
                    updatedValues.put("customer_name",selectedName);
                    updatedValues.put("customer_id",customerId);
                    updatedValues.put("created_at",formattedDate);
                    projectRefz.updateChildren(updatedValues)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showSweetAlertDialog("Edit Project","Updated successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(projects_edit_and_newActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
    private void showSweetAlertDialog(String title,String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
}