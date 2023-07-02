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

class ProjectsAdapter extends ArrayAdapter<Project> {
    private ArrayList<Project> projects;
    private Context context;
    public ProjectsAdapter(Context context, ArrayList<Project> projects) {
        super(context, 0, projects);
        this.context = context;
        this.projects = projects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.table_projects_listview, parent, false);
        }
        Project project = projects.get(position);
        TextView tv_ans_name = convertView.findViewById(R.id.tv_ans_name);
        Button edit                   = convertView.findViewById(R.id.button_edit);
        Button delete                 = convertView.findViewById(R.id.button_delete);
        Button New_Day                = convertView.findViewById(R.id.plus_new_day);
        TextView tv_ans_customer_name = convertView.findViewById(R.id.tv_ans_project_customer_name);
        TextView tv_ans_location = convertView.findViewById(R.id.tv_ans_location);
        TextView tv_ans_created_at = convertView.findViewById(R.id.tv_ans_created_at);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), projects_edit_and_newActivity.class);
                intent.putExtra("mode","edit");
                intent.putExtra("project", project);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        New_Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), NewDayWorkActivity.class);
                intent.putExtra("project_id", project.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteSweetAlertDialog(project);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteSweetAlertDialog(project);
            }
        });
        // Set the customer data to the TextViews
        tv_ans_name.setText(project.getName());
        tv_ans_customer_name.setText(project.getCustomer_name());
        tv_ans_location.setText(project.getLocation());
        tv_ans_created_at.setText(project.getCreated_at());
        return convertView;
    }
    private void DeleteSweetAlertDialog(Project project) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setCancelText("Cancel")
                .setConfirmText("Yes,delete it!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        String projectId = project.getId();
                        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("projects").child(projectId);
                        customersRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        sweetAlertDialog.dismiss();
                                        showSweetAlertDialog("Delete Project", "Updated successfully");
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
