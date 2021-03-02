package com.example.projectapplicationmain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BookingHistoryFragment extends Fragment {

    ListView bookinghistorylist;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String DocumentId;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> documentarray = new ArrayList<>();
    String Duration;
    String Location;
    String Parking;
    String Type;
    String Zone;
    String level;
    DashboardActivity DA = new DashboardActivity();
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    CollectionReference cREF = Fstore.collection("Users").document(userID).collection("Booking History");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bookinghistory, container, false);
        bookinghistorylist = view.findViewById(R.id.Bookinghistorylist);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cREF.addSnapshotListener( getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    DocumentId = documentSnapshot.getId();
                    Duration = documentSnapshot.getString("duration");
                    Location = documentSnapshot.getString("location");
                    Parking = documentSnapshot.getString("parking");
                    Type = documentSnapshot.getString("type");
                    Zone = documentSnapshot.getString("zone");
                    level = documentSnapshot.getString("level");
                    String date =documentSnapshot.getString("date");
                    String time = documentSnapshot.getString("time");
                    data = "\n" + Location + "\n\n" + date + "\t\t" + time ;
                    arrayList.add(data);
                    documentarray.add(DocumentId);
                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                bookinghistorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        for(int i=0; i<documentarray.size(); i++) {
                            if(position == i ) {
                                NavController navController = Navigation.findNavController(view);
                                DA.setDocumentID(documentarray.get(i));
                                navController.navigate(R.id.action_navigation_bookinghistory_to_navigation_bookingdetails);
                            }
                        }

                    }
                });

                bookinghistorylist.setAdapter(arrayAdapter);
            }
        });
    }



    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        ImageView imageView = view.findViewById(R.id.imageView3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_bookinghistory_to_navigation_home);
            }
        });


    }
}









//    private void addbooking(String duration, String location, String parking, String type, String zone, String level) {
//        bookingHistory booking = new bookingHistory(duration, location, parking, type, zone, level);
//        cREF.add(booking);
//    }

//booking = documentSnapshot.toObject(bookingHistory.class);
//booking.setDocumentId(documentSnapshot.getId());

//   dREF = Fstore.collection("Users").document(userID);
//           dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//@Override
//public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//        assert value != null;
//        licensePlateNumber = value.getString("licensePlate");
//        parkingSpot = value.getString("parkingSpot");
//        parkingZone = value.getString("parkingZone");
//        checkInTime = value.getString("checkInTime");
//        leftparkinglot = value.getBoolean("leftParkingLot");
//        }
//        });
//        Calendar calendar = Calendar.getInstance();
//        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//        bookinghistorylist = view.findViewById(R.id.Bookinghistorylist);
//        ArrayList<String> arrayList = new ArrayList<>();
//        if(leftparkinglot)
//        {
//        arrayList.add("UOWD /n" + currentdate );
//        }

//       Button button = view.findViewById(R.id.bookingdetails);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_navigation_bookinghistory_to_navigation_bookingdetails);
//            }
//        });


// locationdetailscity = view.findViewById(R.id.locationdetailscity);
//         locationdetailsdate = view.findViewById(R.id.locationdetailsdate);
//         locationdetailstime = view.findViewById(R.id.locationdetailstime);
//         zone = view.findViewById(R.id.zone);
//         leveldetail = view.findViewById(R.id.level);
//         duration = view.findViewById(R.id.duration);
//         parking = view.findViewById(R.id.parking);
//         typedetail = view.findViewById(R.id.type);

//    NavController navController = Navigation.findNavController(view);
//                        dREF = Fstore.collection("Users").document(userID).collection("Booking History").document();
//                                dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//@Override
//public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//        assert value != null;
//        locationdetailscity.setText(Location);
//        // locationdetailsdate.setText(Date);
//        //locationdetailstime.setText((CharSequence) Time);
//        zone.setText(Zone);
//        leveldetail.setText(level);
//        duration.setText(Duration);
//        parking.setText(Parking);
//        typedetail.setText(Type);
