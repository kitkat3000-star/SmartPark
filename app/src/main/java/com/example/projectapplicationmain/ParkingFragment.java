package com.example.projectapplicationmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.HashMap;
import java.util.Map;

public class ParkingFragment extends Fragment {

    private Spinner zonespinner;
    Map<Integer, Slot> slots;
    String licensePlateNumber = "A 69435";
    int exitConfirmation = 0;
    ParkingFragment parkinglot;
    int zoneUOWD = 10;
    int zoneHeriottWatt = 10;
    int totalSpots = zoneUOWD + zoneHeriottWatt;


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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // navController.navigate(R.id.action_navigation_parking_to_navigation_map);
                zonespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        createParkingLot();

                        switch(position){

                            case 1: Toast.makeText(getActivity(), zonespinner.getSelectedItem().toString() + " Allocated spot" + fillAvailableSlotUOWD(licensePlateNumber), Toast.LENGTH_SHORT).show();
                                break;
                            case 2: Toast.makeText(getActivity(), zonespinner.getSelectedItem().toString() + " Allocated spot" + fillAvailableSlotHeriott(licensePlateNumber), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        //END OF CODE

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

    //CLASSES
    class Slot{

        String reservationId;
        char zone;
        int slotNumber;
        boolean status;

        Slot(char zone, int slotNumber)
        {
            this.reservationId = null;
            this.zone = zone;
            this.slotNumber = slotNumber;
            this.status = true;

        }
    }

    ParkingFragment() 														//Parking lot constructor
    {

        slots = new HashMap<Integer, Slot>();

        for (int i = 1; i <= zoneUOWD; i++)
        {
            slots.put(i, new Slot('A', i));
        }

        for (int i = 1; i <= zoneHeriottWatt; i++)
        {
            slots.put(i+zoneUOWD, new Slot('H', i));
        }

    }

    public void createParkingLot()
    {
        parkinglot = new ParkingFragment();
    }


    public int fillAvailableSlotUOWD(String userID){
        int nextAvailableSlotNumber = -1;

        for (int i = 1; i <= slots.size(); i++)
        {
            Slot s = slots.get(i);

            if (s.zone == 'U' && s.status)
            {
                nextAvailableSlotNumber = s.slotNumber;
                s.reservationId = userID;
                s.status = false;
                break;
            }
        }
            return nextAvailableSlotNumber;
    }

    public int fillAvailableSlotHeriott(String userID){
        int nextAvailableSlotNumber = -1;

        for (int i = 1; i <= slots.size(); i++)
        {
            Slot s = slots.get(i);

            if (s.zone == 'H' && s.status)
            {
                nextAvailableSlotNumber = s.slotNumber;
                s.reservationId = userID;
                s.status = false;
                break;
            }
        }
        return nextAvailableSlotNumber;
    }


}
