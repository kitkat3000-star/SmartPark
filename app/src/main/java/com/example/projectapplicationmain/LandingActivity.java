package com.example.projectapplicationmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arubanetworks.meridian.Meridian;
import com.arubanetworks.meridian.editor.EditorKey;

public class LandingActivity extends Activity {

    String StringToken =  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0IjoxNjEwOTk0NTc5LCJ2YWx1ZSI6ImE4YTliNzYzNGJmMWE5ZDMxMzBiMzQ2YjM5OGVmOGRlNTk4ZWNkYTkifQ.y3Kng8S8V9_EWM-48CiWwjDitv-wBfvFmpx-GlZ0adY";
public static final EditorKey appKEY =  EditorKey.forApp("5656090511540224");
public static final  EditorKey mapKEY = EditorKey.forMap("6487331234250752", LandingActivity.appKEY.getId());

    Button LoginBtn , RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

      Meridian.configure(this, StringToken);
        Meridian.getShared().showMaps();
        Meridian.getShared().setForceSimulatedLocation(true);


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