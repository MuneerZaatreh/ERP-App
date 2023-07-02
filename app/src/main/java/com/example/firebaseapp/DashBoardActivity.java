package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class DashBoardActivity extends AppCompatActivity {
    private GridView gridView;
    private String[] categoryNames = {"Employees", "Projects", "Customers", "Salary Report"};
    private int[] categoryIcons = {R.drawable.icon_users, R.drawable.icon_projects, R.drawable.icon_customers, R.drawable.icon_work_days};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        gridView = findViewById(R.id.gridView);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, categoryNames, categoryIcons);
        gridView.setAdapter(categoryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 if(i == 0){
                    startActivity(new Intent(DashBoardActivity.this,EmployeesActivity.class));
                }else if(i == 1){
                     startActivity(new Intent(DashBoardActivity.this,ProjectsActivity.class));
                 }
                 else  if(i == 2){
                  startActivity(new Intent(DashBoardActivity.this,homeActivity.class));
              }
              else if(i == 3){
                  startActivity(new Intent(DashBoardActivity.this,SalaryReportActivity.class));
              }
            }
        });
    }
}