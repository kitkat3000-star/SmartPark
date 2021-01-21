package com.example.projectapplicationmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arubanetworks.meridian.Meridian;
import com.arubanetworks.meridian.editor.EditorKey;

public class LandingActivity extends Activity {

    String StringToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsIjo1NjU2MDkwNTExNTQwMjI0LCJ0IjoxNjEwOTIzNzAwfQ.5JyHjp7fJm530YrOHjsur7WQa0W3dWN4aaXq-zIpO94";
public static final EditorKey appKEY =  EditorKey.forApp("5656090511540224");
public static final  EditorKey mapKEY = EditorKey.forMap("6487331234250752", LandingActivity.appKEY.getId());

    Button LoginBtn , RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

      Meridian.configure(this, StringToken);
        Meridian.getShared().showMaps();

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