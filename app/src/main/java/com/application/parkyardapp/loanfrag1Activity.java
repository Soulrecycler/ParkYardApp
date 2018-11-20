package com.application.parkyardapp;

import android.content.Intent;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;



public class loanfrag1Activity extends Fragment {

    View view;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    public String user_id,cityData,stateData,priceData;

    //static string for use in firestore function
    private static final String TAG = "loanfrag1Activity";
    EditText cityText,stateText,priceText;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_loanfrag1,container,false);

        Button Mapbtn=(Button)view.findViewById(R.id.mapsbtn);
         cityText=(EditText) view.findViewById(R.id.cityText);
         stateText=(EditText) view.findViewById(R.id.stateText);
         priceText=(EditText) view.findViewById(R.id.priceText);

        firebaseAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //fetch current user uid
        user_id = firebaseAuth.getCurrentUser().getUid();





        Mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityData=cityText.getText().toString().trim();
                stateData=stateText.getText().toString().trim();
                priceData=priceText.getText().toString().trim();

                if (!validate(cityData,stateData,priceData)) {
                    Intent intent = new Intent(getContext(), RegMapActivity.class);
                    intent.putExtra("city", cityData);
                    intent.putExtra("state", stateData);
                    intent.putExtra("price", priceData);
                    startActivity(intent);
                }

            }
        });



        return view;
    }

    private Boolean validate(String cityData,String stateData,String priceData) {

        if (cityData.isEmpty()) {
            cityText.setError("City required");
            cityText.requestFocus();
            return true;
        }
        if (stateData.isEmpty()) {
            stateText.setError("State required");
            stateText.requestFocus();
            return true;
        }
        if (priceData.isEmpty()) {
            priceText.setError("Price required");
            priceText.requestFocus();
            return true;
        }
        int convertedVal = Integer.parseInt(priceData);
        if (convertedVal >= 100){
            priceText.setError("Price Max limit is 100");
            priceText.requestFocus();
            return true;
        }
        return false;
    }

}