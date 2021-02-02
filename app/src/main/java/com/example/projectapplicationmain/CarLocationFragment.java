package com.example.projectapplicationmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class CarLocationFragment extends Fragment {
    FirebaseFirestore firenode = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userID =  Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    DocumentReference dREF;
    String UserZone;
    String UserSpot;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carlocation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView zoneLocation = (TextView) view.findViewById(R.id.userZone);
        TextView zoneSpot = (TextView) view.findViewById(R.id.userParkedSpot);
        dREF = firenode.collection("Users").document(userID);
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                      UserZone = value.getString("parkingZone");
                      UserSpot = value.getString("parkingSpot");
            }
        });

        zoneLocation.setText(UserZone);
        zoneSpot.setText(UserSpot);

        NavController navController = Navigation.findNavController(view);

        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_carlocation_to_navigation_home2);
            }
        });

        ImageView userprofile = view.findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_carlocation_to_navigation_profile);
            }
        });


    }
}
