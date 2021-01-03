package com.example.projectapplicationmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity3 extends Activity {
    EditText regName;
    EditText regEmail;
    EditText regPass;
    EditText regPhoneNo;
    EditText regLicenseNo;
    Button Signin_Btn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        regName =  (EditText) findViewById(R.id.editTextName);
        regEmail = (EditText) findViewById(R.id.editTextloginemail);
        regPass = (EditText) findViewById(R.id.edittextloginPass);
        regPhoneNo = (EditText) findViewById(R.id.editTextPhone);
        regLicenseNo =(EditText)  findViewById(R.id.editTextLicense);
     Signin_Btn = (Button) findViewById(R.id.SignInBtn);

     Signin_Btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             String txt_email = regEmail.getText().toString();
             String txt_password = regPass.getText().toString();

             String name = regName.getText().toString();
             String phone_Num = regPhoneNo.getText().toString();
             String license_Num = regLicenseNo.getText().toString();

             registerUser(txt_email,txt_password);
             User helperClass = new User (name, txt_email, txt_password, license_Num, phone_Num);
             reference.child(license_Num).setValue(helperClass);

                      }
     });


    }
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email , password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   Toast.makeText(MainActivity3.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity3.this, MainActivity4.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity3.this, "Registration unsucessfull", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}


