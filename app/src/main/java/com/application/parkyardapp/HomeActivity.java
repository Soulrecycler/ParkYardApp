package com.application.parkyardapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab_plus,fab_settings,fab_logout;
    Animation fab_open,fab_close,rotate_clockwise,rotate_anticlockwise;
    TextView usernameView;
    private static String TAG = "HomeActivity.this";

    Boolean fab_isOpen = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final Button rentBtn,lendBtn;

        final Handler mHandler = new Handler();

        firebaseAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        rentBtn = findViewById(R.id.rentBtn);
        lendBtn = findViewById(R.id.lendBtn);
        usernameView = findViewById(R.id.usernameView);

        fab_plus = findViewById(R.id.fab_plus);
        fab_settings = findViewById(R.id.fab_settings);
        fab_logout = findViewById(R.id.fab_logout);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        //fetch current user uid
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a reference to the users collection
        CollectionReference usernameRef = db.collection("users");

        // Create a query against the collection.
        Query query = usernameRef.whereEqualTo("user_id",user_id);




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


        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fab_isOpen)
                {
                    fab_settings.startAnimation(fab_close);
                    fab_logout.startAnimation(fab_close);
                    fab_plus.startAnimation(rotate_anticlockwise);
                    fab_settings.setClickable(false);
                    fab_logout.setClickable(false);
                    fab_isOpen=false;
                }
                else
                {
                    fab_settings.startAnimation(fab_open);
                    fab_logout.startAnimation(fab_open);
                    fab_plus.startAnimation(rotate_clockwise);
                    fab_settings.setClickable(true);
                    fab_logout.setClickable(true);
                    fab_isOpen=true;
                }

            }
        });

        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });



    }


    //Close app if back button pressed
    @Override
    public void onBackPressed() {
        System.exit(0);
    }

}
