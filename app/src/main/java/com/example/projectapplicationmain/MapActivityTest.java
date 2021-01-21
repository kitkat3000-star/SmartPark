package com.example.projectapplicationmain;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.arubanetworks.meridian.maps.MapFragment;

public class MapActivityTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        MapFragment mapFragment = new MapFragment.Builder()
                .setAppKey(LandingActivity.appKEY)
                .setMapKey(LandingActivity.mapKEY)
                .build();
    }
}