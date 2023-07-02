package com.example.firebaseapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CustomerEditAndNewActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView uploadCustomerPhoto;
    private EditText ed_name,ed_age;
    private Button submit_customer_action;
    private TextView Customer_title;
    private  boolean flag = false;
    String imageUrl = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit_and_new);
        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("customers");
        Customer_title = findViewById(R.id.Header_title);
        submit_customer_action = findViewById(R.id.new_edit_customer_action);
        ed_name =findViewById(R.id._name_customer_name);
        ed_age = findViewById(R.id._name_customer_age);
        uploadCustomerPhoto = findViewById(R.id.uploadCustomerImage);
        uploadCustomerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        Customer customer = (Customer) intent.getSerializableExtra("customer");
        if ("edit".equals(mode)) {
            ed_name.setText(customer.getName());
            ed_age.setText(customer.getAge());
            Picasso.get().load(customer.getUrl()).into(uploadCustomerPhoto);
        } else if ("new".equals(mode)) {
            Customer_title.setText("New Customer");
            submit_customer_action.setText("Create");
        }
        submit_customer_action.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(currentDate);
                if(ed_name.getText().toString().isEmpty() || ed_age.getText().toString().isEmpty() || !flag ){
                    Toast.makeText(CustomerEditAndNewActivity.this, "Some Fields are Empty", Toast.LENGTH_SHORT).show();
                return;
                }
                else if("new".equals(mode)){
                    /*
                          This Funcation Is For Ordering the name
                          And Check If The Name exists
                          If not exist Insert Data
                     */
                    Query query = customersRef.orderByChild("name").equalTo(ed_name.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                String customerId = customersRef.push().getKey();
                                Customer customer = new Customer(customerId,ed_name.getText().toString(), ed_age.getText().toString(), formattedDate,imageUrl);
                                customersRef.child(customerId).setValue(customer);
                                showSweetAlertDialog("New Customer","Created successfully");
                            }else{
                                Toast.makeText(CustomerEditAndNewActivity.this, "Name Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(CustomerEditAndNewActivity.this, "DataBase Erorr", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if ("edit".equals(mode)){
                    String customerId = customer.getId();
                    DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("customers").child(customerId);
                    Map<String, Object> updatedValues = new HashMap<>();
                    updatedValues.put("name", ed_name.getText().toString());
                    updatedValues.put("age", ed_age.getText().toString());
                    updatedValues.put("created_at",formattedDate);
                    updatedValues.put("url",imageUrl);
                    customersRef.updateChildren(updatedValues)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showSweetAlertDialog("Edit Customer","Updated successfully");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CustomerEditAndNewActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
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
    private void uploadImage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images/" + UUID.randomUUID().toString());
        imagesRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                 imageUrl = uri.toString();
                                flag = true;
                                Toast.makeText(CustomerEditAndNewActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerEditAndNewActivity.this, "No", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // Call this method when the user wants to select an image
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);
            uploadCustomerPhoto.setImageURI(imageUri);
        }
    }
}