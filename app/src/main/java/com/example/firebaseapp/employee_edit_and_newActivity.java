package com.example.firebaseapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class employee_edit_and_newActivity extends AppCompatActivity {
    private EditText ed_name,ed_role,ed_salary_by_hour;
    private Button submit_employee_action;
    private TextView employee_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_and_new);
        DatabaseReference employeesRef = FirebaseDatabase.getInstance().getReference("employees");
        ed_name = findViewById(R.id._name_employee_name);
        ed_role = findViewById(R.id._name_employee_role);
        employee_title = findViewById(R.id.employee_Header_title);
        ed_salary_by_hour = findViewById(R.id._name_employee_salary_by_hour);
        submit_employee_action = findViewById(R.id.new_edit_employee_action);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        Employee employee = (Employee) intent.getSerializableExtra("employee");
        if ("edit".equals(mode)) {
            ed_name.setText(employee.getName());
            ed_role.setText(employee.getRole());
            ed_salary_by_hour.setText(employee.getSalary_by_hour());
        } else if ("new".equals(mode)) {
            employee_title.setText("New Employee");
            submit_employee_action.setText("Create");
        }
        submit_employee_action.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(ed_name.getText().toString().isEmpty() || ed_role.getText().toString().isEmpty() || ed_salary_by_hour.getText().toString().isEmpty()  ){
                    Toast.makeText(employee_edit_and_newActivity.this, "Some Fields are Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if("new".equals(mode)){
                    Query query = employeesRef.orderByChild("name").equalTo(ed_name.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                String employee_id = employeesRef.push().getKey();
                                Employee employeeData = new Employee(employee_id,ed_name.getText().toString(), ed_role.getText().toString(),ed_salary_by_hour.getText().toString());
                                employeesRef.child(employee_id).setValue(employeeData);
                                showSweetAlertDialog("New Employee","Created successfully");
                            }else{
                                Toast.makeText(employee_edit_and_newActivity.this, "Name Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(employee_edit_and_newActivity.this, "DataBase Erorr", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if ("edit".equals(mode)){
                    String employeeId = employee.getId();
                    DatabaseReference employeesRef = FirebaseDatabase.getInstance().getReference("employees").child(employeeId);
                    Map<String, Object> updatedValues = new HashMap<>();
                    updatedValues.put("name", ed_name.getText().toString());
                    updatedValues.put("role", ed_role.getText().toString());
                    updatedValues.put("salary_by_hour", ed_salary_by_hour.getText().toString());
                    employeesRef.updateChildren(updatedValues)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showSweetAlertDialog("Edit employee","Updated successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(employee_edit_and_newActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
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
