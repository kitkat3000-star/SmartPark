package com.example.SmartParkVol2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.arubanetworks.meridian.Meridian;
import com.arubanetworks.meridian.editor.EditorKey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LandingActivity extends Activity  {
    String StringToken =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0IjoxNjEwOTk0NTc5LCJ2YWx1ZSI6ImE4YTliNzYzNGJmMWE5ZDMxMzBiMzQ2YjM5OGVmOGRlNTk4ZWNkYTkifQ.y3Kng8S8V9_EWM-48CiWwjDitv-wBfvFmpx-GlZ0adY";
    public static final EditorKey appKey = new EditorKey("5656090511540224");
     public static final EditorKey mapKey = EditorKey.forMap("6487331234250752", appKey.getId());
    Button LoginBtn , RegisterBtn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg =  "Test";
                        if (!task.isSuccessful()) {
                            msg = "Test2";
                        }

                    }
                });



        Meridian.configure(LandingActivity.this, StringToken);
      //  Meridian.getShared().setForceSimulatedLocation(true);
        LoginBtn = findViewById(R.id.frontlogin);
        RegisterBtn = findViewById(R.id.register_btn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LandingActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
   protected void onStart() {

        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser!= null)
        {
            Intent i = new Intent(LandingActivity.this, MainSplashActivity.class);
            startActivity(i);
        }

    }

 }