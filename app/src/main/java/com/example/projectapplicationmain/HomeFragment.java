package com.example.projectapplicationmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.projectapplicationmain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    DocumentReference dREF;
    TextView username;
    Boolean parked = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//
//        username =  view.findViewById(R.id.username);
//        dREF = Fstore.collection("Users").document(userID);
//        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                // Only works if you have ocr input
//                String name = value.getString("name");
//                username.setText(name);
//            }
//        });

        NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.Book);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dREF = Fstore.collection("Users").document(userID);
                                        dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                         parked = value.getBoolean("parked");
                        if(parked.equals(false)) {
                            navController.navigate(R.id.action_navigation_home_to_navigation_verifyUser);
                        }
                        else
                            Toast.makeText(getActivity(), "You have already parked!", Toast.LENGTH_SHORT).show();
                    }
                });
            }        });

        Button button1 = view.findViewById(R.id.vipbook);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_navigation_home_to_navigation_vipbook);

            }
        });

        Button bookhistory = view.findViewById(R.id.bookhistory);
        bookhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_navigation_bookinghistory);
            }
        });





        Button services = view.findViewById(R.id.services);
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dREF = Fstore.collection("Users").document(userID);
                dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        Boolean parked = value.getBoolean("parked");
                        if(parked) {
                            navController.navigate(R.id.action_navigation_home_to_navigation_home2);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Services not available yet as you have not parked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        registration.remove();
//    }
}


//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;