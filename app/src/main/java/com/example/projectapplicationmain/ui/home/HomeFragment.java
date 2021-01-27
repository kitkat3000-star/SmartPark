package com.example.projectapplicationmain.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.projectapplicationmain.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    String userID,UserName;
    TextView username;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       return inflater.inflate(R.layout.fragment_home,container,false);
}


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mAuth = FirebaseAuth.getInstance();
//        userID = mAuth.getCurrentUser().getUid();
//        username = (TextView) view.findViewById(R.id.username);
//        DocumentReference dRef = Fstore.collection("Users").document(userID);
//        dRef.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                // Only works if you have ocr input
//                UserName = value.getString("name");
//            }
//        });
//
//        username.setText(UserName);

        NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.Book);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_navigation_verifyUser);
            }
        });

        Button button1 = view.findViewById(R.id.vipbook);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_navigation_vipbook);
            }
        });

        Button button2 = view.findViewById(R.id.bookhistory);
        button2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navController.navigate(R.id.action_navigation_home_to_navigation_bookinghistory);
        }
    });

        Button button3 = view.findViewById(R.id.Receipts);
        button3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        navController.navigate(R.id.action_navigation_home_to_navigation_receipts);
        }
        });

        Button userprofile = view.findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_navigation_profile);
            }
        });

    }
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