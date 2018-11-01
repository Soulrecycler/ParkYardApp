package com.application.parkyardapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;



public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final Button rentBtn,lendBtn;




        final Handler mHandler = new Handler();


        rentBtn = findViewById(R.id.rentBtn);
        lendBtn = findViewById(R.id.lendBtn);




        rentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Intent intBook = new Intent(getApplicationContext(),BookActivity.class);

                Runnable intBookTimer = new Runnable() {
                    @Override
                    public void run() {
                        Pair[] pair = new Pair[1];
                        pair[0] = new Pair<View, String>(rentBtn, "rentTransition");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, pair);
                            startActivity(intBook,activityOptions.toBundle());

                        }
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                };

                mHandler.postDelayed(intBookTimer,200);

            }
        });


        lendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intLoan = new Intent(getApplicationContext(),LoanActivity.class);

                Runnable intBookTimer = new Runnable() {
                    @Override
                    public void run() {
                        Pair[] pair = new Pair[1];
                        pair[0] = new Pair<View, String>(lendBtn, "lendTransition");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, pair);
                            startActivity(intLoan,activityOptions.toBundle());

                        }
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                };

                mHandler.postDelayed(intBookTimer,200);

            }
        });



    }

}
