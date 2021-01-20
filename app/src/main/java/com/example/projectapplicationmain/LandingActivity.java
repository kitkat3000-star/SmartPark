package com.example.projectapplicationmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arubanetworks.meridian.Meridian;

public class LandingActivity extends Activity {

    String StringToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0IjoxNjEwOTk0NTc5LCJ2YWx1ZSI6ImE4YTliNzYzNGJmMWE5ZDMxMzBiMzQ2YjM5OGVmOGRlNTk4ZWNkYTkifQ.y3Kng8S8V9_EWM-48CiWwjDitv-wBfvFmpx-GlZ0adY";


    Button LoginBtn , RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

      Meridian.configure(this, StringToken);

        LoginBtn = findViewById(R.id.frontlogin);
        RegisterBtn = findViewById(R.id.register_btn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(i);            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LandingActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}