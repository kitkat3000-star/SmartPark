package com.example.SmartParkVol2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ParkingFragment extends Fragment {

    private Spinner zonespinner;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int licensePlateNumber;
    String TOKEN;
     boolean cardropped = false;
    boolean spotchecked;
    DocumentReference dREF, SREF,ZREF, GREF, LREF;
    DocumentReference SpotRef;
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    String valetId = null;
    int counter = 0;
    TextView availableSpotsTextView;
    String parkingType;
    String Time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_parking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        SREF = Fstore.collection("Notifications").document(userID);


        //ZONE SPINNER
        zonespinner = view.findViewById(R.id.zonespinner);
        NavController navController = Navigation.findNavController(view);
        Button button = (Button) view.findViewById(R.id.parkingbutton);
        availableSpotsTextView = (TextView) view.findViewById(R.id.availableSpotsTextView);


        //LOGGED-IN USER LICENSE PLATE EXTRACTION
        dREF = Fstore.collection("Users").document(userID);
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error)
            {
                assert value != null;
                licensePlateNumber = value.getLong("licensePlate").intValue();
                TOKEN = value.getString("FCM_TOKEN");

            }
        });


        //AVAILABLE SPOTS IN EACH ZONE
        zonespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Object item = parent.getItemAtPosition(position);
                String selectedZone = item.toString();
                if (selectedZone.equals("Zone A"))
                {
                    parkingType = "standard";
                    displayAvailableSlots("Zone A",parkingType);
                }
                else if (selectedZone.equals("Zone B"))
                {
                    parkingType = "standard";
                    displayAvailableSlots("Zone B",parkingType);
                }
                else if (selectedZone.equals("Valet"))
                {
                    parkingType = "valet";
                    displayAvailableSlots("Zone Valet",parkingType);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


        //OPTION SELECTION FROM DROP DOWN LIST
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (zonespinner.getSelectedItemPosition())
                {
                    case 0:
                        fillAvailableSlotZoneA();
                        navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                        break;
                    case 1:
                        fillAvailableSlotZoneB();
                        navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                        break;
                    case 2:
                        fillAvailableSlotZoneValet();
                        navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                        break;
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_navigation_parking_to_navigation_verifyUser);
            }
        });

    }


    //SPOT ALLOCATION FOR ZONE A
    public void fillAvailableSlotZoneA()
    {
        Fstore.collection("Parking Lot").document("UOWD").collection("Zone A")
                .whereEqualTo("status", true)
                .whereEqualTo("reservationType", "standard")
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

                                Map<String, String> Notify = new HashMap<String, String>();
                                Notify.put("ParkingSpot", parkingSpot);
                                SREF.set(Notify);

                                ZREF = Fstore.collection("Tokens").document(parkingSpot);
                                Map<String, String> tokens = new HashMap<String, String>();
                                tokens.put("fcm_token", TOKEN);
                                ZREF.set(tokens);

                                LREF = Fstore.collection("SpotCheck").document(userID);
                                Map<String, Object> spotcheck = new HashMap<>();
                                spotcheck.put("ParkingSpot", parkingSpot);
                                LREF.set(spotcheck);
                                LREF.update("parkedinCorrectSpot","");
                                Fstore.collection("Users").document(userID).update("parkingSpot", parkingSpot);
                                Fstore.collection("Users").document(userID).update("parkingZone", "Zone A");
                                Fstore.collection("Users").document(userID).update("type", "Standard");
                                Fstore.collection("Users").document(userID).update("checkInTime", Time);

                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone A").document(parkingSpot)
                                        .update("status", false);
                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone A").document(parkingSpot)
                                        .update("reservationId", licensePlateNumber);
                                break;
                            }
                        }
                        else
                        {
                            Log.d("Error", "No spots available");
                        }
                    }
                });
    }


    //SPOT ALLOCATION FOR ZONE B
    public void fillAvailableSlotZoneB()
    {
        Fstore.collection("Parking Lot").document("UOWD").collection("Zone B")
                .whereEqualTo("status", true)
                .whereEqualTo("reservationType", "standard")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Toast.makeText(getActivity(), "Spot Allocated " + document.getId(), Toast.LENGTH_SHORT).show();
                                String parkingSpot = document.getId();

                                Map<String, String> Notify = new HashMap<String, String>();
                                Notify.put("ParkingSpot", parkingSpot);
                                SREF.set(Notify);

                                ZREF = Fstore.collection("Tokens").document(parkingSpot);
                                Map<String, String> tokens = new HashMap<String, String>();
                                tokens.put("fcm_token", TOKEN);
                                ZREF.set(tokens);

                                LREF = Fstore.collection("SpotCheck").document(userID);
                                Map<String, Object> spotcheck = new HashMap<>();
                                spotcheck.put("ParkingSpot", parkingSpot);
                                LREF.set(spotcheck);
                                LREF.update("parkedinCorrectSpot","");

                                Fstore.collection("Users").document(userID).update("parkingSpot", parkingSpot);
                                Fstore.collection("Users").document(userID).update("parkingZone", "Zone B");
                                Fstore.collection("Users").document(userID).update("type", "Standard");
                                Fstore.collection("Users").document(userID).update("checkInTime", Time);

                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone B").document(parkingSpot)
                                        .update("status", false);
                                Fstore.collection("Parking Lot").document("UOWD").collection("Zone B").document(parkingSpot)
                                        .update("reservationId", licensePlateNumber);
                                break;
                            }
                        }
                        else
                        {
                            Log.d("Error", "No spots available");
                        }
                    }
                });
    }


    //SPOT ALLOCATION FOR ZONE VALET
    public void fillAvailableSlotZoneValet()
    {
        Fstore.collection("Parking Lot").document("UOWD").collection("Zone Valet")
                .whereEqualTo("status", true)
                .whereEqualTo("reservationType", "valet")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Toast.makeText(getActivity(), "Spot Allocated " + document.getId(), Toast.LENGTH_SHORT).show();
                                String parkingSpot = document.getId();

                                Map<String, String> Notify = new HashMap<String, String>();
                                Notify.put("ParkingSpot", parkingSpot);
                                SREF.set(Notify);

                                ZREF = Fstore.collection("ValetTokens").document(parkingSpot);
                                Map<String, String> valettokens = new HashMap<String, String>();
                                valettokens.put("fcm_token", TOKEN);
                                ZREF.set(valettokens);

                                GREF = Fstore.collection("ValetCarDrop").document(userID);
                                Map<String, Object> valetdrop = new HashMap<>();
                                valetdrop.put("ParkingSpot", parkingSpot);
                                valetdrop.put("Cardropped", cardropped);
                                GREF.set(valetdrop);

                                Fstore.collection("Valet")
                                        .whereEqualTo("status", true)
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
                                                        String availableValet = document.getId();
                                                        Toast.makeText(getActivity(), "Valet Assigned " + availableValet, Toast.LENGTH_SHORT).show();

                                                        Fstore.collection("Users").document(userID)
                                                                .update("parkingSpot", parkingSpot);
                                                        Fstore.collection("Users").document(userID)
                                                                .update("parkingZone", "Zone Valet");
                                                        Fstore.collection("Users").document(userID)
                                                                .update("type", "Valet");
                                                        Fstore.collection("Users").document(userID)
                                                                .update("checkInTime", Time);

                                                        Fstore.collection("Parking Lot").document("UOWD").collection("Zone Valet").document(parkingSpot)
                                                                .update("status", false);
                                                        Fstore.collection("Parking Lot").document("UOWD").collection("Zone Valet").document(parkingSpot)
                                                                .update("reservationId", licensePlateNumber);

                                                        //UPDATING FIRST AVAILABLE VALET WITH LICENSE PLATE AND ASSIGNED SPOT
                                                        Fstore.collection("Valet").document(availableValet)
                                                                .update("assignedPlate", licensePlateNumber);
                                                        Fstore.collection("Valet").document(availableValet)
                                                                .update("assignedSpot", parkingSpot);
                                                        Fstore.collection("Valet").document(availableValet)
                                                                .update("status", false);

                                                        break;
                                                    }
                                                }
                                                else
                                                {
                                                    Log.d("Error", "Valet Unavailable");
                                                }
                                            }
                                        });
                                break;
                            }
                        }
                        else
                        {
                            Log.d("Error", "No spots available");
                        }
                    }
                });
    }


    //DISPLAY FUNCTION
    public void displayAvailableSlots(String zone, String type)
    {
        counter = 0;

        Fstore.collection("Parking Lot").document("UOWD").collection(zone)
                .whereEqualTo("status", true)
                .whereEqualTo("reservationType", type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                counter++;
                                availableSpotsTextView.setText("Available spots: "+String.valueOf(counter));
                            }
                        }
                    }
                });
    }

}
