package com.application.parkyardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BookActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);









    }

    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

}
