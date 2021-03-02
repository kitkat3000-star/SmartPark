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

public class ReceiptsFragment extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String Location;
    DocumentReference dREF;
    DocumentReference hREF;
    String Date;
    String Time;
    String Duration;
    String type;
    String carwash;

    double Amount;
    double servicecharges = 0;
    double VAT;
    double Totalbill;
    TextView locationdetailscity;
    TextView locationdetailsdate;
    TextView locationdetailstime;
    TextView duration;
    TextView amount;
    TextView service;
    TextView vat;
    TextView totalbill;

    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    DashboardActivity DA = new DashboardActivity();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipts, container, false);
        locationdetailscity = view.findViewById(R.id.locationdetailscity);
        locationdetailsdate = view.findViewById(R.id.locationdetailsdate);
        locationdetailstime = view.findViewById(R.id.locationdetailstime);
        duration = view.findViewById(R.id.Duration);
        amount = view.findViewById(R.id.amount);
        service = view.findViewById(R.id.servicecharges);
        vat = view.findViewById(R.id.VAT);
        totalbill = view.findViewById(R.id.totalbill);

        hREF = Fstore.collection("Users").document(userID);
        hREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                carwash = value.getString("carwashTime");
                if (!carwash.equals("")) {
                    servicecharges = 10;
                }
            }
        });

        dREF = Fstore.collection("Users").document(userID).collection("Booking History").document(DA.getDocumentID());
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                Date= value.getString("date");
                Duration= value.getString("duration");
                Time = value.getString("time");
                Location = value.getString("location");
                type = value.getString("type");



                if(type.equals("Standard"))
                {
                    Amount = 2*5;
                }
                else if(type.equals("Valet"))
                {
                    Amount = 2*20;
                    servicecharges += 10;
                }
                else if(type.equals("VIP"))
                {
                    Amount = 100;
                }

                VAT = (Amount*5)/100;
                Totalbill = Amount + VAT + servicecharges;

                locationdetailscity.setText(Location);
                locationdetailsdate.setText(Date);
                locationdetailstime.setText(Time);
                duration.setText(Duration);
                amount.setText(Amount + " Dhs");
                vat.setText(VAT + " Dhs");
                totalbill.setText(Totalbill + " Dhs");
                service.setText(servicecharges + " Dhs");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);


        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_receipts_to_navigation_home);
            }
        });



    }
}

