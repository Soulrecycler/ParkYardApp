package com.application.parkyardapp;


import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    LatLng markedLocation;
    public String city,state,price;
    public Double latitude,longitude;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    public String user_id;

    //static string for use in firestore function
    private static final String TAG = "RegMapActivity";

    Button saveBtn;

    //getting extras from input
    private void intent_input(){
        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        price = getIntent().getStringExtra("price");

    }

    //storing place info into db
    private void save_to_db(){

        latitude = markedLocation.latitude;
        longitude = markedLocation.longitude;
        Map<String, Object> user = new HashMap<>();
        user.put("city",city);
        user.put("state",state);
        user.put("price",price);
        user.put("latitude",latitude);
        user.put("longitude",longitude);

        db.collection("users").document(user_id).collection("places").document().set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegMapActivity.this, "User Information Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegMapActivity.this,LoanActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegMapActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();

        saveBtn = findViewById(R.id.saveLocId);

        firebaseAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //fetch current user uid
        user_id = firebaseAuth.getCurrentUser().getUid();

        intent_input();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!markedLocation.equals(null))
                {
                    save_to_db();
                }
            }
        });
    }

    public void goToMap(View view){
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", 15.3, 73.5, "Home Sweet Home", 15.8, 73.8, "Where the party is at");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("RegMapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("RegMapActivity", "Can't find style. Error: ", e);
        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        setCamera();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Reset marker when long pressed on map
                mMap.clear(); //clears extra markers

                listPoints.clear();
                //Save first point select
                listPoints.add(latLng);
                //Create marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                    //Add first marker to the map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markerOptions);

                markedLocation = markerOptions.getPosition();
                saveBtn.setVisibility(View.VISIBLE);


            }
        });

    }

    private void setCamera() {
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(provider);
//        if (location != null)
//        {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(15.4909, 73.8278), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(16, 78))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
    }

}
