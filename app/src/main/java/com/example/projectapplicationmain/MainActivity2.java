package com.example.projectapplicationmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectapplicationmain.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends Activity {
    EditText LOG_username, LOG_password;
    Button SigninBtn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LOG_username = (EditText) findViewById(R.id.editTextloginemail);
        LOG_password = (EditText) findViewById(R.id.edittextloginPass);
        SigninBtn = (Button) findViewById(R.id.SignInBtn);
        mAuth = FirebaseAuth.getInstance();


        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEnteredUsername = LOG_username.getText().toString();
                String userEnteredPassword = LOG_password.getText().toString();

                mAuth.signInWithEmailAndPassword(userEnteredUsername,userEnteredPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity2.this, "Login successfull", Toast.LENGTH_SHORT).show();
                     //   FirebaseUser currentUser = mAuth.getCurrentUser();
                        Intent i = new Intent(MainActivity2.this, MainActivity4.class);
                        startActivity(i);
                        finish();


                    }
                });

            }
        });
    }



}