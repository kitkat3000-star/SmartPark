package com.example.SmartParkVol2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;



public class InformationFragment extends Fragment {

    FirebaseFirestore firenode = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userID =  Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseUser currentUser =  mAuth.getCurrentUser();
    DocumentReference dREF,AREF;
    String name,email,mobile;
    TextView username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username =  view.findViewById(R.id.username);
        dREF = firenode.collection("Users").document(userID);
        dREF.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // Only works if you have ocr input
                String name = value.getString("name");
                username.setText(name);
            }
        });

        NavController navController = Navigation.findNavController(view);
        TextView userName = (TextView) view.findViewById(R.id.usernameText);
        TextView userEmail = (TextView) view.findViewById(R.id.userEmailText);
        TextView userMobile = (TextView) view.findViewById(R.id.userMobileText);
        dREF = firenode.collection("Users").document(userID);

        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                email = value.getString("email");
                mobile = value.getString("phoneNumber");

                userName.setText(name);
                userEmail.setText(email);
                userMobile.setText(mobile);
            }
        });

        Button updateInfo = (Button) view.findViewById(R.id.updateFragBtn);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_navigation_information_to_navigation_updateinfo);
            }
        });


        Button del_ACC = (Button) view.findViewById(R.id.deleteAccountButton);
        del_ACC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AREF = firenode.collection("Users").document(userID);
             AREF.delete();
                currentUser.delete();

                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);

            }
        });


        ImageView back = view.findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_information_to_navigation_profile);
            }
        });


    }

}
