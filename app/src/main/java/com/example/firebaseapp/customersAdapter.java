package com.example.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

class CustomersAdapter extends ArrayAdapter<Customer> {
    private ArrayList<Customer> customers;
    private Context context;
    public CustomersAdapter(Context context, ArrayList<Customer> customers) {
        super(context, 0, customers);
        this.context = context;
        this.customers = customers;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.table_listview, parent, false);
        }
        Customer customer = customers.get(position);
        TextView tv_ans_name = convertView.findViewById(R.id.tv_ans_name);
        Button edit = convertView.findViewById(R.id.customer_button_edit);
        Button delete = convertView.findViewById(R.id.customer_button_delete);
        TextView tv_ans_age = convertView.findViewById(R.id.tv_ans_age);
        TextView tv_ans_created_at = convertView.findViewById(R.id.tv_ans_created_at);
        ImageView tvImage = convertView.findViewById(R.id.imageCustomerView);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), CustomerEditAndNewActivity.class);
                intent.putExtra("mode","edit");
                intent.putExtra("customer", customer);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteSweetAlertDialog(customer);
            }
        });
        // Set the customer data to the TextViews
        tv_ans_name.setText(customer.getName());
        tv_ans_age.setText(customer.getAge());
        tv_ans_created_at.setText(customer.getCreated_at());
        String imageUrl = customer.getUrl(); // Assuming `customer.getUrl()` returns the image URL as a string
        Picasso.get().load(imageUrl).into(tvImage);
        return convertView;
    }
    private void DeleteSweetAlertDialog(Customer customer) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setCancelText("Cancel")
                .setConfirmText("Yes,delete it!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                  @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                      String customerId = customer.getId();
                      DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("customers").child(customerId);
                      customersRef.removeValue()
                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      sweetAlertDialog.dismiss();
                                      showSweetAlertDialog("Delete Customer", "Updated successfully");
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
