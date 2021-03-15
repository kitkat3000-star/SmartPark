package com.example.SmartParkVol2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {
FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
FirebaseUser YO = mAuth.getCurrentUser();
        if (YO != null ) {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } else {
        Intent i = new Intent(this, LandingActivity.class);
        startActivity(i);
    }
    }
    }

