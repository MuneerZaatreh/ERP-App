package com.example.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

class EmployeesAdapter extends ArrayAdapter<Employee> {
    private ArrayList<Employee> employees;
    private Context context;
    public EmployeesAdapter(Context context, ArrayList<Employee> employees) {
        super(context, 0, employees);
        this.context = context;
        this.employees = employees;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.table_listview_employees, parent, false);
        }
        Employee employee = employees.get(position);
        TextView tv_employees_ans_name = convertView.findViewById(R.id.tv_employees_ans_name);
        Button edit = convertView.findViewById(R.id.employee_button_edit);
        Button delete = convertView.findViewById(R.id.employee_button_delete);
        TextView tv_ans_role = convertView.findViewById(R.id.tv_employee_ans_role);
        TextView tv_ans_salary_by_hour = convertView.findViewById(R.id.tv_employee_ans_salary_by_hour);
        TextView tv_ans_created_at = convertView.findViewById(R.id.tv_employee_ans_created_at);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), employee_edit_and_newActivity.class);
                intent.putExtra("mode","edit");
                intent.putExtra("employee", employee);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteSweetAlertDialog(employee);
            }
        });
        tv_employees_ans_name.setText(employee.getName());
        tv_ans_role.setText(employee.getRole());
        tv_ans_salary_by_hour.setText(employee.getSalary_by_hour());
        tv_ans_created_at.setText(employee.getCreated_at());
        return convertView;
    }
    private void DeleteSweetAlertDialog(Employee employee) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setCancelText("Cancel")
                .setConfirmText("Yes,delete it!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String employee_id = employee.getId();
                        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("employees").child(employee_id);
                        employeeRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sweetAlertDialog.dismiss();
                                        showSweetAlertDialog("Deleted Employee", "Updated successfully");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .show();
    }

    private void showSweetAlertDialog(String title,String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }
}
