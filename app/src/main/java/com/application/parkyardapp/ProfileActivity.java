package com.application.parkyardapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference userRef;
    public String user_id,email,fname,lname,phone,address;
    EditText userEmail,userFname,userLname,userPhone,userAddress;
    Button updateBtn;

    //static string for use in firestore function
    private static final String TAG = "ProfileActivity";


    // assigner function
    private void setupUI(){
        userEmail = findViewById(R.id.etUserEmail);
        userFname = findViewById(R.id.etFirstName);
        userLname = findViewById(R.id.etLastName);
        userPhone = findViewById(R.id.etUserPhone);
        userAddress = findViewById(R.id.etUserAddress);

        updateBtn = findViewById(R.id.updateBtn);
    }

    //display existing details in edit text
    private void displayCurrentData(){

        userEmail.setText(email);
        userFname.setText(fname);
        userLname.setText(lname);
        userPhone.setText(phone);
        userAddress.setText(address);

    }

    //fetch data from firebase and store in object variables
    private void fetchData(){

        firebaseAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //fetch current user uid
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //fetch document using current user id
        userRef = db.collection("users").document(user_id);

        //fetch details from document and display first and last name of current user
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            fname = documentSnapshot.getString("first_name");
                            lname = documentSnapshot.getString("last_name");
                            phone = documentSnapshot.getString("phone");
                            address = documentSnapshot.getString("address");
                            displayCurrentData();
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this, "User Details do not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupUI();
        fetchData();


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Update values if the editText values are not empty
                if (!userEmail.getText().toString().trim().isEmpty()) {
                    email = userEmail.getText().toString().trim();
                }
                if (!userFname.getText().toString().trim().isEmpty()) {
                    fname = userFname.getText().toString().trim();
                }
                if (!userLname.getText().toString().trim().isEmpty()) {
                    lname = userLname.getText().toString().trim();
                }
                if (!userPhone.getText().toString().trim().isEmpty()) {
                    phone = userPhone.getText().toString().trim();
                }
                if (!userAddress.getText().toString().trim().isEmpty()) {
                    address = userAddress.getText().toString().trim();
                }

                Map<String, Object> user = new HashMap<>();
                user.put("first_name",fname );
                user.put("last_name",lname );
                user.put("phone",phone );
                user.put("address",address );

                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                db.collection("users").document(user_id).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "User Details have been updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG,e.toString());
                            }
                        });

                startActivity(new Intent(ProfileActivity.this,HomeActivity.class));

            }
        });

    }
}
