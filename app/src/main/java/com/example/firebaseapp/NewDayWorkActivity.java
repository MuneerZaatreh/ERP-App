package com.example.firebaseapp;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NewDayWorkActivity extends AppCompatActivity {
    private LinearLayout container;
    private Button btnAdd;
    private Button btnSubmit;
    private String  projectId ;
    private List editTextList;
    private DatabaseReference databaseReference;
    private String selectedId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_day_work);
        projectId = getIntent().getStringExtra("project_id");
        databaseReference = FirebaseDatabase.getInstance().getReference("WorkDays");
        container = findViewById(R.id.container);
        btnAdd = findViewById(R.id.btnAdd);
        btnSubmit = findViewById(R.id.btnSubmit);
        editTextList = new ArrayList<>();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewInput();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }
    private void addNewInput() {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        final Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(layoutParams);
        DatabaseReference employeesRef = FirebaseDatabase.getInstance().getReference().child("employees");
        employeesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> nameList = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                for (DataSnapshot employeeSnapshot : dataSnapshot.getChildren()) {
                    String id = employeeSnapshot.child("id").getValue(String.class);
                    String name = employeeSnapshot.child("name").getValue(String.class);
                    nameList.add(name);
                    idList.add(id);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewDayWorkActivity.this, android.R.layout.simple_spinner_item, nameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedId = idList.get(position);
                        spinner.setTag(selectedId); // Set the selected ID as a tag for the Spinner
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle the case when nothing is selected
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
        container.addView(spinner);
        final EditText startTimeEditText = new EditText(this);
        startTimeEditText.setLayoutParams(layoutParams);
        startTimeEditText.setHint("Start Time");
        startTimeEditText.setFocusable(false);
        container.addView(startTimeEditText);
        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeEditText);
            }
        });
        final EditText endTimeEditText = new EditText(this);
        endTimeEditText.setLayoutParams(layoutParams);
        endTimeEditText.setHint("End Time");
        endTimeEditText.setFocusable(false);
        container.addView(endTimeEditText);
        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeEditText);
            }
        });
        // Add to editTextList
        editTextList.add(spinner);            //0 // 3
        editTextList.add(startTimeEditText); //1 // 4
        editTextList.add(endTimeEditText); // 2 // 5
        final Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(layoutParams);
        deleteButton.setText("Delete");
        container.addView(deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(spinner);
                container.removeView(startTimeEditText);
                container.removeView(endTimeEditText);
                container.removeView(deleteButton);
                editTextList.remove(spinner);
                editTextList.remove(startTimeEditText);
                editTextList.remove(endTimeEditText);
            }
        });
    }
    private void showTimePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        editText.setText(selectedTime);
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }
    @SuppressLint("NewApi")
    private void submitData() {
        ArrayList<WorkDays> Insert_work_days = new ArrayList<WorkDays>();
        for (int i = 0; i < editTextList.size(); i += 3) {
            Spinner nameEditText = (Spinner) editTextList.get(i);
            String employee_id = nameEditText.getTag().toString();
            EditText startTimeEditText = (EditText) editTextList.get(i + 1);
            EditText endTimeEditText = (EditText) editTextList.get(i + 2);
            String name = nameEditText.getSelectedItem().toString().trim();
            String startTime = startTimeEditText.getText().toString().trim();
            String endTime = endTimeEditText.getText().toString().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTimeObj = LocalTime.parse(startTime, formatter);
            LocalTime endTimeObj = LocalTime.parse(endTime, formatter);
            if (startTime != null && endTimeObj != null && !startTime.isEmpty() && !endTime.isEmpty() && endTimeObj.isBefore(startTimeObj)) {
                Toast.makeText(this, "End Time is earlier than Start Time", Toast.LENGTH_SHORT).show();
                break;
            } else if (name != null && !name.isEmpty()) {
                Insert_work_days.add(new WorkDays(name, startTime, endTime ,employee_id));
            }
        }
        if (!Insert_work_days.isEmpty()) {
            for (WorkDays workDays : Insert_work_days) {
                String id = databaseReference.push().getKey();
                workDays.setId(id);
                workDays.setProject_id(projectId);
                databaseReference.child(id).setValue(workDays);
            }
            showSweetAlertDialog("New Day","Created Successfully");
        }
        else{
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }

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
