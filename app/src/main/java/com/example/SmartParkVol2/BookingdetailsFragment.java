package com.example.SmartParkVol2;

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

public class BookingdetailsFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String Location;
    String type;
    DocumentReference dREF;
    String Date;
    String Duration;
    String Parking;
    String Time;
    String Zone;
    String level;
    TextView locationdetailscity;
    TextView locationdetailsdate;
    TextView locationdetailstime;
    TextView zone;
    TextView leveldetail;
    TextView parking;
    TextView duration;
    TextView typedetail;

    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    DashboardActivity DA = new DashboardActivity();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookingdetails, container, false);
        locationdetailscity = view.findViewById(R.id.locationdetailscity);
        locationdetailsdate = view.findViewById(R.id.locationdetailsdate);
        locationdetailstime = view.findViewById(R.id.locationdetailstime);
        zone = view.findViewById(R.id.zone);
        leveldetail = view.findViewById(R.id.level);
        duration = view.findViewById(R.id.duration);
        parking = view.findViewById(R.id.parking);
        typedetail = view.findViewById(R.id.type);


        dREF = Fstore.collection("Users").document(userID).collection("Booking History").document(DA.getDocumentID());
        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                Date= value.getString("date");
                Duration= value.getString("duration");
                Parking= value.getString("parking");
                Time = value.getString("time");
                Zone= value.getString("zone");
                level= value.getString("level");
                Location = value.getString("location");
                type = value.getString("type");

                locationdetailscity.setText(Location);
                locationdetailsdate.setText(Date);
                locationdetailstime.setText(Time);
                zone.setText(Zone);
                leveldetail.setText(level);
                duration.setText(Duration);
                parking.setText(Parking);
                typedetail.setText(type);
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
                navController.navigate(R.id.action_navigation_bookingdetails_to_navigation_bookinghistory);
            }
        });


        TextView receipts = (TextView) view.findViewById(R.id.viewReceipt);
        receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_bookingdetails_to_navigation_receipts);
            }
        });


    }
}
