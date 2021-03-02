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

public class CarRequestFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Spinner zonespinner;
    String licensePlateNumber;
    String parkingSpot;
    String parkingZone;
    DocumentReference dREF;
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carrequest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        zonespinner = view.findViewById(R.id.carrequestselectedzone);

        Button button = view.findViewById(R.id.bookcarrequestbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarRequest();
                navController.navigate(R.id.action_navigation_carrequest_to_navigation_home2);
            }
        });


        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_carrequest_to_navigation_home2);
            }
        });

    }

    public void CarRequest()
    {
        dREF = Fstore.collection("Users").document(userID);
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                licensePlateNumber = value.getString("licensePlate");
                parkingSpot = value.getString("parkingSpot");
                parkingZone = value.getString("parkingZone");

                Fstore.collection("Parking Lot").document("UOWD").collection(parkingZone)
                        .whereEqualTo("reservationType", "valet")
                        .whereEqualTo("reservationId", licensePlateNumber)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    for (QueryDocumentSnapshot document : task.getResult())
                                    {
                                        Toast.makeText(getActivity(), "Your car request is confirmed, Valet will arrive at " + zonespinner.getSelectedItem() + " entrance", Toast.LENGTH_SHORT).show();
                                        Fstore.collection("Parking Lot").document("UOWD").collection(parkingZone).document(parkingSpot).update("carRequest", zonespinner.getSelectedItem());
                                        break;
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Your car request could not be proceeded", Toast.LENGTH_SHORT).show();
                                    Log.d("Error", "couldn't complete task");
                                }
                            }
                        });
            }
        });


    }


}
