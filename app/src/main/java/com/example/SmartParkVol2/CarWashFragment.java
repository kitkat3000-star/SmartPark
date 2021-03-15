package com.example.SmartParkVol2;

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

import lucifer.org.snackbartest.MySnack;

public class CarWashFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Spinner timecarwash;
    int licensePlateNumber;
    DocumentReference dREF;
    TextView location;
    String carlocation;
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carwash, container, false);
        location = view.findViewById(R.id.carlocation);
        dREF = Fstore.collection("Users").document(userID);
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                licensePlateNumber =value.getLong("licensePlate").intValue();
                carlocation = value.getString("parkingSpot");
                location.setText(carlocation);
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        timecarwash = view.findViewById(R.id.timecarwash);

        Button button = view.findViewById(R.id.bookcarwashbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MySnack.SnackBuilder(getActivity())
                        .setText("Are you sure?")
                        .setTextColor("#ffffff")   //optional
                        .setTextSize(20)           //optional
                        .setBgColor("#2196F3")      //optional
                        .setDurationInSeconds(10)  //will display for 10 seconds
                        .setActionBtnColor("#f44336") //optional
                        .setIcon(R.drawable.ic_info_black_24dp)
                        .setActionListener("Ok", new View.OnClickListener() {  //optional
                            @Override
                            public void onClick(View v) {
                                CarWashTime();
                                navController.navigate(R.id.action_navigation_carwash_to_navigation_home2);
                            }
                        })
                        .build();
            }
        });


        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_carwash_to_navigation_home2);
            }
        });




    }


    public void CarWashTime()
    {
        Fstore.collection("Users")
                .whereEqualTo("licensePlate", licensePlateNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Toast.makeText(getActivity(), "Your car wash is confirmed", Toast.LENGTH_SHORT).show();
                                String userID = document.getId();
                                Fstore.collection("Users").document(userID)
                                        .update("carwashTime", timecarwash.getSelectedItem());
                                break;
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Your car wash request could not be proceeded", Toast.LENGTH_SHORT).show();
                            Log.d("Error", "couldn't complete task");
                        }
                    }
                });

    }

}
