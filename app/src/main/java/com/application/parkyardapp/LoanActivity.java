package com.application.parkyardapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
    }

    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
