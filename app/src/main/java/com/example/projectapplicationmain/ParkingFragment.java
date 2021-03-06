    package com.example.projectapplicationmain;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Spinner;
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

    import java.util.Objects;

    public class ParkingFragment extends Fragment {

        private Spinner zonespinner;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String licensePlateNumber;
        DocumentReference dREF;
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String valetId = null;


        FirebaseFirestore Fstore = FirebaseFirestore.getInstance();

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_parking, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

    //ZONE SPINNER

            zonespinner = view.findViewById(R.id.zonespinner);
            NavController navController = Navigation.findNavController(view);
            Button button = view.findViewById(R.id.parkingbutton);

    //LOGGED-IN USER LICENSE PLATE EXTRACTION

            dREF = Fstore.collection("Users").document(userID);
            dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    assert value != null;
                    licensePlateNumber = value.getString("licensePlate");
                }
            });
//
    //VALET PARKING OPTION SELECTION

            Button button1 = view.findViewById(R.id.yesvalet);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Valet option selected", Toast.LENGTH_SHORT).show();
                    button.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            switch (zonespinner.getSelectedItemPosition())
                            {
                                case 0:
                                    fillAvailableSlotZoneA_Valet();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                                case 1:
                                    fillAvailableSlotZoneB_Valet();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                                case 2:
                                    fillAvailableSlotZoneC_Valet();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                                case 3:
                                    fillAvailableSlotZoneD_Valet();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                            }
                        }

                    });
                }
            });

    //STANDARD USER PARKING OPTION SELECTION

            Button button2 = view.findViewById(R.id.novalet);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Valet option deselected", Toast.LENGTH_SHORT).show();
                    button.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            switch (zonespinner.getSelectedItemPosition())
                            {
                                case 0:
                                    fillAvailableSlotZoneA_Standard();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                                case 1:
                                    fillAvailableSlotZoneB_Standard();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                                case 2:
                                    fillAvailableSlotZoneC_Standard();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                                case 3:
                                    fillAvailableSlotZoneD_Standard();
                                    navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                    break;
                            }
                        }

                    });
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Please select yes/no for Valet", Toast.LENGTH_SHORT).show();
                }
            });



            ImageView imageView = view.findViewById(R.id.imageView3);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_navigation_parking_to_navigation_verifyUser);
                }
            });

            ImageView userprofile = view.findViewById(R.id.userprofile);
            userprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_navigation_parking_to_navigation_profile);
                }
            });

        }


    //SPOT ALLOCATION FOR STANDARD ZONES

        public void fillAvailableSlotZoneA_Standard()
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

        public void fillAvailableSlotZoneB_Standard()
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
                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    Toast.makeText(getActivity(), "Spot Allocated " + document.getId(), Toast.LENGTH_SHORT).show();
                                    String parkingSpot = document.getId();
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

        public void fillAvailableSlotZoneC_Standard()
        {
            Fstore.collection("Parking Lot").document("UOWD").collection("Zone C")
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
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone C").document(parkingSpot)
                                            .update("status", false);
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone C").document(parkingSpot)
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

        public void fillAvailableSlotZoneD_Standard()
        {
            Fstore.collection("Parking Lot").document("UOWD").collection("Zone D")
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
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
                                            .update("status", false);
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
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

    //SPOT ALLOCATION FOR VALET ZONES

        public void fillAvailableSlotZoneA_Valet()
        {
            getAvailableValetId();

            Fstore.collection("Parking Lot").document("UOWD").collection("Zone A")
                    .whereEqualTo("status", true)
                    .whereEqualTo("reservationType", "valet")
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

                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone A").document(parkingSpot)
                                            .update("status", false);
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone A").document(parkingSpot)
                                            .update("reservationId", licensePlateNumber);

                                    //UPDATING FIRST AVAILABLE VALET WITH LICENSE PLATE AND ASSIGNED SPOT
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedPlate", licensePlateNumber);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedSpot", parkingSpot);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("status", false);

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

        public void fillAvailableSlotZoneB_Valet()
        {
            getAvailableValetId();

            Fstore.collection("Parking Lot").document("UOWD").collection("Zone B")
                    .whereEqualTo("status", true)
                    .whereEqualTo("reservationType", "valet")
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

                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone B").document(parkingSpot)
                                            .update("status", false);
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone B").document(parkingSpot)
                                            .update("reservationId", licensePlateNumber);

                                    //UPDATING FIRST AVAILABLE VALET WITH LICENSE PLATE AND ASSIGNED SPOT
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedPlate", licensePlateNumber);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedSpot", parkingSpot);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("status", false);

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

        public void fillAvailableSlotZoneC_Valet()
        {
            getAvailableValetId();

            Fstore.collection("Parking Lot").document("UOWD").collection("Zone C")
                    .whereEqualTo("status", true)
                    .whereEqualTo("reservationType", "valet")
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
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone C").document(parkingSpot)
                                            .update("status", false);
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone C").document(parkingSpot)
                                            .update("reservationId", licensePlateNumber);

                                    //UPDATING FIRST AVAILABLE VALET WITH LICENSE PLATE AND ASSIGNED SPOT
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedPlate", licensePlateNumber);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedSpot", parkingSpot);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("status", false);

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

        public void fillAvailableSlotZoneD_Valet()
        {
            getAvailableValetId();

            Fstore.collection("Parking Lot").document("UOWD").collection("Zone D")
                    .whereEqualTo("status", true)
                    .whereEqualTo("reservationType", "valet")
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
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
                                            .update("status", false);
                                    Fstore.collection("Parking Lot").document("UOWD").collection("Zone D").document(parkingSpot)
                                            .update("reservationId", licensePlateNumber);

                                    //UPDATING FIRST AVAILABLE VALET WITH LICENSE PLATE AND ASSIGNED SPOT
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedPlate", licensePlateNumber);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("assignedSpot", parkingSpot);
                                    Fstore.collection("Valet").document(valetId)
                                            .update("status", false);

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

    //GETTING FIRST AVAILABLE VALET

        public void getAvailableValetId()
        {
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
                                    valetId = document.getId();
                                    Toast.makeText(getActivity(), "Valet Assigned " + valetId, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            else
                            {
                                Log.d("Error", "Valet Unavailable");
                            }
                        }
                    });
        }

    }