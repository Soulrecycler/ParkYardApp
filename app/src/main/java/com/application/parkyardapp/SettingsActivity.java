package com.application.parkyardapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsActivity extends AppCompatActivity {


    Button changeProfileBtn,changePassBtn,supportBtn,logoutBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changeProfileBtn = findViewById(R.id.changeProfileBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        supportBtn=findViewById(R.id.supportBtn);

        firebaseAuth = FirebaseAuth.getInstance();


        changeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,ProfileActivity.class));
            }
        });

            supportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
                }
            });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();

    }
}

