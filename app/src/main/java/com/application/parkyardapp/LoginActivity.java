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


    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter=5;
    private TextView userRegister;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.etName);
        Password = (EditText) findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvinfo);
        Login = (Button) findViewById(R.id.btnLogin);
        userRegister = (TextView) findViewById(R.id.tvsign);

        Info.setText("No of attempts remaining:5");

        //firebase
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();

        //progressdialogue
        progressDialog=new ProgressDialog(this);


        if (user!=null){
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));//change the redirect activity once clinton has done
        }



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });



        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }






    private void validate(String userName, String userPassword) {

        progressDialog.setMessage("wait bitch waaaait! patience is key.");
        progressDialog.show();



        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));//go to homepage
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"damn bruh,looks like we have an error or maybe its just you :)", Toast.LENGTH_LONG).show();
                    counter--;
                    Info.setText("no of attempts remaining:" + String.valueOf(counter));
                    if (counter==0){
                        Login.setEnabled(false);
                    }
                }


            }
        });

        /* if ((userName.equals("Admin") && (userPassword.equals("1234")))) {

            Intent second = new Intent(this, RegisterActivity.class);
            startActivity(second);
        }
        else
        {





        }*/
    }


}
