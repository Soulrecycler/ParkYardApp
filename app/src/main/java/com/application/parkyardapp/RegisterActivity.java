package com.application.parkyardapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, userpassword, useremail;

    private Button btnRegister;
    private TextView userlogin;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUI();


        firebaseAuth = FirebaseAuth.getInstance(); //gets instance of Firebase into the varaible firebaseAuth


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()){
                    //upload data to database


                    String user_email=useremail.getText().toString().trim();
                    String user_password=userpassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Registration Successful Bruh!",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class) );
                            }
                            else{

                                Toast.makeText(RegisterActivity.this, "Registration Failed",Toast.LENGTH_SHORT).show();

                            }



                        }
                    });

                }
            }
        });


        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void setupUI() {
        username = (EditText) findViewById(R.id.etName);
        userpassword = (EditText) findViewById(R.id.etUserpass);
        useremail = (EditText) findViewById(R.id.etUserEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        userlogin = (TextView) findViewById(R.id.tvuserlogin);


    }

    private Boolean validate() {

        Boolean result = false;

        String name = username.getText().toString();
        String password = userpassword.getText().toString();
        String email = useremail.getText().toString();

        if (name.isEmpty() && password.isEmpty() && email.isEmpty()) {

            Toast.makeText(this,"kindly enter user credentials (email/password/username) ", Toast.LENGTH_SHORT).show();

        }
        else{
            result=true;
        }
        return result;

    }




}