package com.example.projectapplicationmain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import static android.service.controls.ControlsProviderService.TAG;

public class LoginActivity extends Activity {
    EditText LOG_username, LOG_password;
    Button SigninBtn;
    FirebaseAuth mAuth;
    String fcmTOKEN = "";
    FirebaseFirestore FirestoreNode = FirebaseFirestore.getInstance();
    DocumentReference dREF;
    String userID;
    String LicensePlate;
    DocumentReference SREF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LOG_username = (EditText) findViewById(R.id.editTextloginemail);
        LOG_password = (EditText) findViewById(R.id.edittextloginPass);
        SigninBtn = (Button) findViewById(R.id.SignInBtn);
        mAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token

                        fcmTOKEN = task.getResult();
                        Log.d("Apple", fcmTOKEN);
                    }
                });

        Button NewUser = (Button) findViewById(R.id.CreateAccBtn);
        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);


            }
        });

        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEnteredUsername = LOG_username.getText().toString();
                String userEnteredPassword = LOG_password.getText().toString();



                mAuth.signInWithEmailAndPassword(userEnteredUsername,userEnteredPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        userID =mAuth.getCurrentUser().getUid();
                                               dREF = FirestoreNode.collection("Users").document(userID);
                                               dREF.update("FCM_TOKEN",fcmTOKEN);
                        Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(i);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Login unsuccessfull, re enter password ", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });
    }





}