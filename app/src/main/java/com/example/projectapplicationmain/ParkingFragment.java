package com.example.projectapplicationmain;

import android.os.Bundle;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ParkingFragment extends Fragment {

    private Spinner zonespinner;
  //  Map<Integer, Slot> slots;
    String licensePlateNumber = "A 69435";
    int exitConfirmation = 0;
    ParkingFragment parkinglot;
    final int Zone_A = 5;
   final  int Zone_B = 5;
    final int Zone_C = 5;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userID =  mAuth.getCurrentUser().getUid();
    DocumentReference dREF;
    String Verified;
//


   // int totalSpots = zoneUOWD + zoneHeriottWatt;
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();

    CollectionReference cREFZB =  Fstore.collection("Parking Lot").document("UOWD").collection("Zone B");
    String TESTER;


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

        //END OF CODE

        Button button1 = view.findViewById(R.id.yesvalet);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Valet option selected", Toast.LENGTH_SHORT).show();
            }
        });

        Button button2 = view.findViewById(R.id.novalet);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Valet option deselected", Toast.LENGTH_SHORT).show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        dREF = Fstore.collection("users").document(userID);
//                        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                Verified =  value.getString("License_Num");
//                            }
//                        });
//                        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                TESTER =  value.getString("reservationId");
//                                Toast.makeText(getActivity(), TESTER, Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        switch (zonespinner.getSelectedItemPosition()) {
                            case 0:
                                 Fstore.collection("/Parking Lot/UOWD/Zone A")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Toast.makeText(getActivity(), document.getId()  , Toast.LENGTH_LONG).show();
//                                                        Log.d("Success", document.getId() + " => " + document.getData());
                                                    }
                                                } else {
                                                   // Toast.makeText(getActivity(), "AAAHHHHHHHH NO" , Toast.LENGTH_LONG).show();
//                                                    Log.d("Error", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                              //
                              //  fillAvailableSlotZoneA(licensePlateNumber);
//                                Toast.makeText(getActivity(), zonespinner.getSelectedItem().toString() + " Allocated spot " + , Toast.LENGTH_LONG).show();
                                navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                                break;
                            case 1:
//                                Toast.makeText(getActivity(), zonespinner.getSelectedItem().toString() + " Allocated spot" + fillAvailableSlotHeriott(licensePlateNumber) , Toast.LENGTH_LONG).show();
//                                navController.navigate(R.id.action_navigation_parking_to_navigation_map);
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
                navController.navigate(R.id.action_navigation_parking_to_navigation_scanbarcode);
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
//    public int fillAvailableSlotZoneA(String userID){
//        //CollectionReference cREFZA =  Fstore.collection("Parking Lot").document("UOWD").collection("Zone A");
////        cREFZA.get();
//        Object Fobj =  Fstore.collectionGroup("/Parking Lot/UOWD/Zone A").whereEqualTo("status", true).get();
//
//        Toast.makeText(getActivity(), Fobj.toString(), Toast.LENGTH_SHORT).show();
//
//
//        int nextAvailableSlotNumber = -1;
//
//
//        for (int i = 1; i <= slots.size(); i++)
//        //
//        {
//            Slot s = slots.get(i);
//
//            if (s.zone == 'U' && s.status)
//            {
//                nextAvailableSlotNumber = s.slotNumber;
//                s.reservationId = userID;
//                s.status = false;
//                break;
//            }
//        }
//        return nextAvailableSlotNumber;
//    }
//
//    public int fillAvailableSlotHeriott(String userID){
//
//        int nextAvailableSlotNumber = -1;
//
//        for (int i = 1; i <= slots.size(); i++)
//        {
//            Slot s = slots.get(i);
//
//            if (s.zone == 'H' && s.status)
//            {
//                nextAvailableSlotNumber = s.slotNumber;
//                s.reservationId = userID;
//                s.status = false;
//                break;
//            }
//        }
//        return nextAvailableSlotNumber;
//    }
//
//    //CLASSES
////    class Slot{
////
////        String reservationId;
////        char zone;
////        int slotNumber;
////        boolean status;
////
////        Slot(char zone, int slotNumber)
////        {
////            this.reservationId = null;
////            this.zone = zone;
////            this.slotNumber = slotNumber;
////            this.status = true;
////
////        }
////    }
//
//
////    public void createParkingLot()
////    {
////        parkinglot = new ParkingFragment();
////    }
//
//
////    public ParkingFragment() 														//Parking lot constructor
////    {
////
////        slots = new HashMap<Integer, Slot>();
////
////        for (int i = 1; i <= zoneUOWD; i++)
////        {
////            slots.put(i, new Slot('A', i));
////        }
////
////        for (int i = 1; i <= zoneHeriottWatt; i++)
////        {
////            slots.put(i+zoneUOWD, new Slot('H', i));
////        }
////
////    }



}
