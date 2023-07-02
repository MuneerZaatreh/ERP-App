package com.example.firebaseapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
public class SalaryReportActivity extends AppCompatActivity {
    private ListView listView;
   private int totalHoursSum = 0;
    private ArrayAdapter<Employee> adapter;
    private List<Employee> employees;
    private List<SalaryEmployee> employees_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_report);
        listView = findViewById(R.id.salary_list_view);
        employees = new ArrayList<>();
        employees_data = new ArrayList<>();
        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("employees");
        DatabaseReference workDaysRef = FirebaseDatabase.getInstance().getReference().child("WorkDays");
        employeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                employees.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   totalHoursSum = 0;
                    String id = snapshot.child("id").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String salary_by_hour = snapshot.child("salary_by_hour").getValue(String.class);
                    workDaysRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotWorkDays) {
                            for (DataSnapshot snapshotWorkDays : dataSnapshotWorkDays.getChildren()) {
                                String days_employee_id = snapshotWorkDays.child("employee_id").getValue(String.class);
                                String work_day_hours = snapshotWorkDays.child("totalHours").getValue(String.class);
                                if (work_day_hours != null && days_employee_id.equals(id)) {
                                    totalHoursSum+= Integer.parseInt(work_day_hours);
                                }
                            }
                            if(totalHoursSum > 0 && salary_by_hour != null ){
                                employees.add(new Employee(name,totalHoursSum *  Integer.parseInt(salary_by_hour) + ""));
                            }else{
                                employees.add(new Employee(name,"0"));
                            }
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
              adapter = new ArrayAdapter<Employee>(getApplicationContext(),
                        android.R.layout.simple_list_item_2, android.R.id.text1, employees) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView name = view.findViewById(android.R.id.text1);
                        TextView salary = view.findViewById(android.R.id.text2);
                        name.setText(employees.get(position).getName());
                        salary.setText(employees.get(position).getTotal() + "₪");
                        return view;
                    }
                };
                listView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors that occur
            }
        });

    }
}
