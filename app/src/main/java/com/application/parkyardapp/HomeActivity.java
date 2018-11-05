package com.application.parkyardapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab_plus,fab_settings,fab_logout;
    Animation fab_open,fab_close,rotate_clockwise,rotate_anticlockwise;

    Boolean fab_isOpen = false;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    TextView usernameView;


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

        //fetch current user uid
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //fetch document using current user id
        DocumentReference userRef = db.collection("users").document(user_id);

        //fetch details from document and display first and last name of current user
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            String fname = documentSnapshot.getString("first_name");
                            String lname = documentSnapshot.getString("last_name");
                            usernameView.setText(fname+" "+lname);
                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "User Details do not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        fab_plus = findViewById(R.id.fab_plus);
        fab_settings = findViewById(R.id.fab_settings);
        fab_logout = findViewById(R.id.fab_logout);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);


        //Onclick function for Booking a space
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


        //Onclick function for keeping a space for rent
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


/*-------------------------------------------------------------------COPY THIS FAB CODE FOR ALL PAGES ------------------------------------------------------------------------------------------------*/
        //fab for settings and log out
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

        fab_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
            }
        });

/*------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    }
    //Close app if back button pressed
    @Override
    public void onBackPressed() {
        System.exit(0);
    }

}