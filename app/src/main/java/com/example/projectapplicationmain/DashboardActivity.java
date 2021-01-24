package com.example.projectapplicationmain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends FragmentActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_chatsupport)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
       Button logoutButton = (Button) findViewById(R.id.userLogout);
       logoutButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mAuth.signOut();
               //   FirebaseUser currentUser = mAuth.getCurrentUser();
               Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
               startActivity(i);

           }
       });
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