package com.example.projectapplicationmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Home2Fragment extends Fragment {

    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    DocumentReference dREF;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    bookingHistory booking = new bookingHistory();
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    String Date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    String Time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    TextView username;

    CollectionReference cREF = Fstore.collection("Users").document(userID).collection("Booking History");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home2,container,false);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);


        Button button1 = view.findViewById(R.id.Carwash);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home2_to_navigation_carwash);
            }
        });

        Button button2 = view.findViewById(R.id.Carlocation);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home2_to_navigation_carlocation);
            }
        });



        Button exitparking = view.findViewById(R.id.ExitButton);
        exitparking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dREF = Fstore.collection("Users").document(userID);
                //Fstore.collection("Users").document(userID).update("leftParkingLot", true);
                dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        String parkingSpot = value.getString("parkingSpot");
                        String parkingZone = value.getString("parkingZone");
                        String Type = value.getString("type");
                        boolean leftparkinglot = value.getBoolean("leftParkingLot");
                        String duration;

                        if(Type.equals("VIP"))
                        {
                            duration = "Full Day";
                        }
                        else
                        {
                            duration = booking.getDuration();
                        }

                        if(leftparkinglot) {
                            bookingHistory book = new bookingHistory(duration, booking.getLocation(), parkingSpot, Type, parkingZone, booking.getLevel(), Date, Time);
                            cREF.add(book);
//                            Toast.makeText(getActivity(), "Exiting....", Toast.LENGTH_LONG).show();
//                            Toast.makeText(getActivity(), "You have exited the parking lot", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_navigation_home2_to_navigation_home);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Your Request couldn't be completed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button button5 = view.findViewById(R.id.Carrequest);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home2_to_navigation_carrequest);
            }
        });



    }
}