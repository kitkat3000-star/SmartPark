package com.example.projectapplicationmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ExistingVIPBookingFragment extends Fragment {

    String Time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String licensePlateNumber;
    DocumentReference dREF;
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    String arrival_date;
    TextView arrival;
    boolean[] ifuser = {false};
    String Date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_existingvipbooking, container, false);
        arrival = v.findViewById(R.id.arrivaldate);
        dREF = Fstore.collection("Users").document(userID).collection("VIP Booking").document("VIP booking info");
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                arrival_date = value.getString("arrivalDate");
                arrival.setText(arrival_date);
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.Newvipbooking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_exisitingvipbook_to_navigation_vipbook);
            }
        });

        Button verifybooking = view.findViewById(R.id.verifyVipbookingbutton);
        verifybooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifySlotZoneD_VIP();
                if(ifuser[0]){
                    //add second verify
                    navController.navigate(R.id.action_navigation_exisitingvipbook_to_navigation_verify_vipuser);
                }
                Toast.makeText(getActivity(), "Verification incomplete. Please try again later.", Toast.LENGTH_SHORT).show();
            } 

        });


        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_exisitingvipbook_to_navigation_home);
            }
        });


    }

    public void VerifySlotZoneD_VIP() {

        dREF = Fstore.collection("Users").document(userID);
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                licensePlateNumber = value.getString("licensePlate");
            }
        });

        Fstore.collection("Parking Lot").document("UOWD").collection("Zone D")
                .whereEqualTo("reservationId", licensePlateNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String parkingSpot = document.getId();
                                String parkingZoneD = "Zone D";
                                if(arrival_date.equals(Date)) {
                                    Fstore.collection("Users").document(userID).update("parkingSpot", parkingSpot);
                                    Fstore.collection("Users").document(userID).update("parkingZone", parkingZoneD);
                                    Fstore.collection("Users").document(userID).update("type", "VIP");
                                    Fstore.collection("Users").document(userID).update("checkInTime", Time);
                                    ifuser[0] = true;
                                    break;
                                }

                            }

                        }

                        else {
                            Log.d("Error", "No spots available");
                        }
                    }
                });

    }
}
