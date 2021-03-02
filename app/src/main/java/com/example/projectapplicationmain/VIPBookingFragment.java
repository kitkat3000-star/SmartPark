package com.example.projectapplicationmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arubanetworks.meridian.maps.directions.DirectionsDestination;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class VIPBookingFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String licensePlateNumber;
    DocumentReference dREF;
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    TextView datearray;
    String arrival_date = "";
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    // String Date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_vipbooking, container, false);
        datearray = view.findViewById(R.id.datearray);
        datearray.setText(getCalculatedDate("dd/MM/yyyy", 1));
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.Existingvipbooking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dREF = Fstore.collection("Users").document(userID).collection("VIP Booking").document("VIP booking info");
                dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                        arrival_date = value.getString("arrivalDate");

                        if(arrival_date.isEmpty())
                        {
                            Toast.makeText(getActivity(), "No existing booking yet", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            navController.navigate(R.id.action_navigation_vipbook_to_navigation_exisitingvipbook);
                        }
                    }
                });

            }
        });

        Button button1 = view.findViewById(R.id.confirmVipbookingbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillAvailableSlotZoneD_VIP();
                navController.navigate(R.id.action_navigation_vipbook_to_navigation_home);
            }
        });

        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_vipbook_to_navigation_home);
            }
        });


    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public void fillAvailableSlotZoneD_VIP()
    {
        dREF = Fstore.collection("Users").document(userID);
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                licensePlateNumber = value.getString("licensePlate");

            }
        });

        Fstore.collection("Parking Lot").document("UOWD").collection("Zone D")
                .whereEqualTo("status", true)
                .whereEqualTo("reservationType", "VIP")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Toast.makeText(getActivity(), "Spot Allocated " + document.getId(), Toast.LENGTH_SHORT).show();
                                String parkingSpot = document.getId();

                                Fstore.collection("Users").document(userID).collection("VIP Booking").document("VIP booking info").update("parkingSpot",parkingSpot);
                                Fstore.collection("Users").document(userID).collection("VIP Booking").document("VIP booking info").update("arrivalDate",datearray.getText());
                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
                                        .update("status", false);
                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
                                        .update("reservationId", licensePlateNumber);
                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
                                        .update("arrivalDate", datearray.getText());
                                break;
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "No spots available", Toast.LENGTH_SHORT).show();
                            Log.d("Error", "No spots available");
                        }
                    }
                });
    }
}
