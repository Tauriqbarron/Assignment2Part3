package com.example.assignment2part3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button btnGetLocation;
    //Vars
    private int requestLocation = 99;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String TAG = "Map App";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocation(this);
    }
    private void getLocation(final Context context){
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check permissions
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MapsActivity.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},requestLocation);
                }
                //This Function finds the last knw location and then moves the camera to that location
                // finally its displays a blue dot at the location.
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                try{
                   final Task <Location> currentLocation =mFusedLocationProviderClient.getLastLocation();
                    currentLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            LatLng currentLatLng = new LatLng((double)currentLocation.getResult().getLatitude(),
                                    (double)currentLocation.getResult().getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15f));
                            mMap.setMyLocationEnabled(true);
                        }
                    });
                }catch(SecurityException e){
                    Log.e(TAG,"Get Location: Security Exception: "+e.getMessage());
                }
            }
        });
    }
    // Once the map is ready this moves the camera to the Auckland area
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng auckland = new LatLng(-37, 175);
        float zoom = 11f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(auckland,zoom));
    }
}
