package com.application.parkyardapp;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class loanfrag2Activity extends Fragment {

    private FirebaseUser mCurrentUser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference userRef=db.collection("users");
    private CollectionReference placeRef;
    private static final String FIRE_LOG = "Fire_Log";
    View view;

    public loanfrag2Activity(){

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        placeRef = userRef.document(mCurrentUser.getUid()).collection("places");

        view=inflater.inflate(R.layout.activity_loanfrag2,container,false);

        final LinearLayout my_places_list=(LinearLayout) view.findViewById(R.id.my_places_list);

        // retrieving the places which are present in the user db
        placeRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String places_id=documentSnapshot.getId();

                    final View Card = inflater.inflate(R.layout.my_place_card, container, false);
                    final Button showBtn=(Button)Card.findViewById(R.id.placeBtn);
                    final TextView cityView = (TextView) Card.findViewById(R.id.cityView);
                    final TextView stateView = (TextView) Card.findViewById(R.id.stateView);
                    final TextView priceView = (TextView) Card.findViewById(R.id.priceView);


                    cityView.setText(documentSnapshot.getString("city"));
                    stateView.setText(documentSnapshot.getString("state"));
                    priceView.setText(documentSnapshot.getString("price"));


                    my_places_list.addView(Card);



                }

            }
        });

        return view;
    }

}