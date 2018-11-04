package com.application.parkyardapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userfname, userlname, userpassword, useremail, userphone, useraddress;
    private String user_email, user_password,user_fname, user_lname, user_phone, user_address;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private static final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();  // assigning objects to XML contents


        firebaseAuth = FirebaseAuth.getInstance(); //gets instance of Firebase into the variable firebaseAuth
        db = FirebaseFirestore.getInstance(); //gets instance of Firebase into the variable firebaseAuth

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.tvuserlogin).setOnClickListener(this);

    }

    private void setupUI() {
        userfname = findViewById(R.id.etFirstName);
        userlname = findViewById(R.id.etLastName);
        userpassword = findViewById(R.id.etUserpass);
        useremail = findViewById(R.id.etUserEmail);
        userphone = findViewById(R.id.etUserPhone);
        useraddress = findViewById(R.id.etUserAddress);
    }

    private Boolean validate(String user_email,String user_password,String user_fname,String user_lname,String user_phone,String user_address) {

        if (user_email.isEmpty()) {
            useremail.setError("Email required");
            useremail.requestFocus();
            return true;
        }
        if (user_password.isEmpty()) {
            userpassword.setError("Password required");
            userpassword.requestFocus();
            return true;
        }
        if (user_fname.isEmpty()) {
            userfname.setError("First Name required");
            userfname.requestFocus();
            return true;
        }
        if (user_lname.isEmpty()) {
            userlname.setError("Last Name required");
            userlname.requestFocus();
            return true;
        }
        if (user_phone.isEmpty()) {
            userphone.setError("Phone required");
            userphone.requestFocus();
            return true;
        }
        if (user_address.isEmpty()) {
            useraddress.setError("Address required");
            useraddress.requestFocus();
            return true;
        }
        return false;
    }

    //function to save user details to the users collection in firestore db
    private void saveUserInfo(){
        Map<String, Object> user = new HashMap<>();
        user.put("first_name",user_fname );
        user.put("last_name",user_lname );
        user.put("phone",user_phone );
        user.put("address",user_address );

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        db.collection("users").document(user_id).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "User Information Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });

    }


    //Function to register User
    private void regUserFunc(){

        //store user info as string
        user_email=useremail.getText().toString().trim();
        user_password=userpassword.getText().toString().trim();
        user_fname=userfname.getText().toString().trim();
        user_lname=userlname.getText().toString().trim();
        user_phone= userphone.getText().toString().trim();
        user_address= useraddress.getText().toString().trim();


        //If all fields are not empty according to validation, then db upload function is carried out
        if (!validate(user_email,user_password,user_fname,user_lname,user_phone,user_address)){
            //upload data to database

            //upload email,password to authentication
            firebaseAuth
                    .createUserWithEmailAndPassword(user_email,user_password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        //upload user details to firestore db collection users
                        saveUserInfo();

                        Toast.makeText(RegisterActivity.this,"Registration Successful, Login to Continue",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class) );
                    }
                    else{

                        Toast.makeText(RegisterActivity.this, "Registration Failed",Toast.LENGTH_SHORT).show();

                    }



                }
            });

        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnRegister:
                regUserFunc();
                break;
            case R.id.tvuserlogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }

    }

    //Go to MainActivity on back button pressed
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();

    }
}