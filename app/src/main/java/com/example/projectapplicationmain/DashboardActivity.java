package com.example.projectapplicationmain;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashboardActivity extends FragmentActivity {

    AppBarConfiguration appBarConfiguration;
    static String DocumentID;
    String  userID;
    FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    FirebaseFirestore FirestoreNode = FirebaseFirestore.getInstance();
    DocumentReference  dREF, SREF;
    String LicensePlate = "";
    String FCM = "";


    public DashboardActivity() {
        //public no-arg constructor needed
    }
    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String DocID) {
        DocumentID = DocID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_chatsupport)
                .build();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }
    private void setupActionBarWithNavController(DashboardActivity dashboardActivity, NavController navController, AppBarConfiguration appBarConfiguration) {
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(DashboardActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }


}