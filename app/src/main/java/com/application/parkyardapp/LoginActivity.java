package com.application.parkyardapp;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private TextView Info;
    private Button Login;
    private int counter=4;
    private TextView userRegister;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.etmail);
        password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvinfo);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegister = (TextView) findViewById(R.id.tvsign);

        //firebase
        firebaseAuth= FirebaseAuth.getInstance();

        //progressdialogue
        progressDialog=new ProgressDialog(this);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emptyFieldValidate(email.getText().toString(),password.getText().toString())){
                    validate(email.getText().toString(), password.getText().toString());
                }
            }
        });



        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }





    // validate if user is already registered
    private void validate(String userEMail, String userPassword) {

        progressDialog.setMessage("Please Wait! patience is key.");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEMail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));//go to homepage
                }
                else{
                    if (counter==4){
                        Info.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Looks like we have an error here!!!", Toast.LENGTH_LONG).show();
                    counter--;
                    Info.setText("no of attempts remaining:" + String.valueOf(counter));
                    if (counter==0){
                        Login.setEnabled(false);
                    }
                }


            }
        });


    }


    // validate if login fields are empty
    private Boolean emptyFieldValidate(String userEMail, String userPassword){

        if (userEMail.isEmpty()) {
            email.setError("Email required");
            email.requestFocus();
            return true;
        }

        if (userPassword.isEmpty()) {
            password.setError("Password required");
            password.requestFocus();
            return true;
        }
        return false;

    }


}

