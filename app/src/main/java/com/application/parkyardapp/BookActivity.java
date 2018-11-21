package com.application.parkyardapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;

public class BookActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    FloatingActionButton fab_plus,fab_settings,fab_logout;
    Animation fab_open,fab_close,rotate_clockwise,rotate_anticlockwise;

    Boolean fab_isOpen = false;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bookviewadapter adapter = new bookviewadapter(getSupportFragmentManager());

        //adds frags
        adapter.AddFragment(new BookFragActivity(), "Find Your Parking Slot!");



        //adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        fab_plus = findViewById(R.id.fab_plus);
        fab_settings = findViewById(R.id.fab_settings);
        fab_logout = findViewById(R.id.fab_logout);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
        firebaseAuth = FirebaseAuth.getInstance();

        /*-------------------------------------------------------------------COPY THIS FAB CODE FOR ALL PAGES ------------------------------------------------------------------------------------------------*/
        //fab for settings and log out
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fab_isOpen) {
                    fab_settings.startAnimation(fab_close);
                    fab_logout.startAnimation(fab_close);
                    fab_plus.startAnimation(rotate_anticlockwise);
                    fab_settings.setClickable(false);
                    fab_logout.setClickable(false);
                    fab_isOpen = false;
                } else {
                    fab_settings.startAnimation(fab_open);
                    fab_logout.startAnimation(fab_open);
                    fab_plus.startAnimation(rotate_clockwise);
                    fab_settings.setClickable(true);
                    fab_logout.setClickable(true);
                    fab_isOpen = true;
                }

            }
        });

        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(BookActivity.this, LoginActivity.class));
            }
        });

        fab_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookActivity.this, SettingsActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(BookActivity.this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
}
