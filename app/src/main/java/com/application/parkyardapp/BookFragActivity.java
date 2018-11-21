package com.application.parkyardapp;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BookFragActivity extends Fragment {


    private String mCurrentUser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static final String FIRE_LOG = "Fire_Log";
    private static final String TAG = "BookFragActivity";
    View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.activity_book_frag,container,false);

        final LinearLayout my_places_list=(LinearLayout) view.findViewById(R.id.my_places_list);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // retrieving the places which are present in the user db
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String mUserId = document.getId();
                                String mUserName = document.getString("first_name") +" "+ document.getString("last_name");
                                if(!mUserId.equals(mCurrentUser)) {
                                    getPlaces(my_places_list, inflater, container, mUserId, mUserName);
                                }
                            }
                        }
                        else
                        {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return view;
    }

    private void getPlaces(final LinearLayout my_places_list, final LayoutInflater inflater, final ViewGroup container, String mUserId, final String mUsername){

        view=inflater.inflate(R.layout.activity_book_frag,container,false);


        db.collection("users").document(mUserId).collection("places")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                final View Card = inflater.inflate(R.layout.book_card, container, false);
                                final Button showBtn = (Button) Card.findViewById(R.id.placeBtn);
                                final TextView ownerView = (TextView) Card.findViewById(R.id.ownerView);
                                final TextView cityView = (TextView) Card.findViewById(R.id.cityView);
                                final TextView stateView = (TextView) Card.findViewById(R.id.stateView);
                                final TextView priceView = (TextView) Card.findViewById(R.id.priceView);


                                ownerView.setText("Owner: "+mUsername);
                                cityView.setText("City: "+document.getString("city"));
                                stateView.setText("State: "+document.getString("state"));
                                priceView.setText("Price: "+document.getString("price"));


                                my_places_list.addView(Card);

//                                showBtn.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        startActivity(new Intent(getContext(), RoutesActivity.class));
//                                    }
//                                });

                            }
                        }
                        else
                        {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
