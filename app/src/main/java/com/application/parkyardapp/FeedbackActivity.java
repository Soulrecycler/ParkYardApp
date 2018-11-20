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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {


    private EditText name,email,feedbackbox;
    private String  string_email,string_name, string_feedbackbox;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Button Submit;


    //static string for use in firestore function
    private static final String TAG = "FeedbackActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        setupUI();  // assigning objects to XML contents


        firebaseAuth = FirebaseAuth.getInstance(); //gets instance of Firebase into the variable firebaseAuth
        db = FirebaseFirestore.getInstance(); //gets instance of Firebase into the variable firebaseAuth

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
                Toast.makeText(FeedbackActivity.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FeedbackActivity.this, SettingsActivity.class));
            }
        });



    }

    private void setupUI() {
        name=findViewById(R.id.ETname);
        email=findViewById(R.id.ETemail);
        feedbackbox=findViewById(R.id.editText2);


    }

    private Boolean validate(String user_email,String user_name,String user_text) {

        if (user_email.isEmpty()) {
            email.setError("Email required");
            email.requestFocus();
            return true;
        }
        if (user_name.isEmpty()) {
            name.setError("name required");
            name.requestFocus();
            return true;
        }
        if (user_text.isEmpty()) {
            feedbackbox.setError("atleast 10 characters required");
            feedbackbox.requestFocus();
            return true;
        }

        return false;
    }

    //function to save user details to the users collection in firestore db
    private void saveUserInfo(){

        //store user info as string
        string_email=email.getText().toString().trim();
        string_name=name.getText().toString().trim();
        string_feedbackbox=feedbackbox.getText().toString().trim();



        //If all fields are not empty according to validation, then db upload function is carried out
        if (!validate(string_email,string_name,string_feedbackbox)) {
            //upload data to database
            Map<String, Object> feedback = new HashMap<>();
            feedback.put("email", email);
            feedback.put("name", name);
            feedback.put("feedback", feedbackbox);


            db.collection("feedbacks").document().set(feedback)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(FeedbackActivity.this, "User Information Saved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FeedbackActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });
        }

    }

//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId())
//        {
//            case R.id.btnRegister:
//                regUserFunc();
//                break;
//            case R.id.tvuserlogin:
//                startActivity(new Intent(FeedbackActivity.this, LoginActivity.class));
//                break;
//        }
//
//    }

    //Go to MainActivity on back button pressed

}
