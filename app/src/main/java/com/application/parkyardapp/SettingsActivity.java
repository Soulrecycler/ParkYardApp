package com.application.parkyardapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsActivity extends AppCompatActivity {


    Button changeProfileBtn,changePassBtn,supportBtn,logoutBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logoutBtn = findViewById(R.id.logoutBtn);

        firebaseAuth = FirebaseAuth.getInstance();


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });

    }
}
