    package com.example.projectapplicationmain;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Toast;

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

    public class VerifyUserFragment extends Fragment {

        FirebaseFirestore firenode = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID =  Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DocumentReference dREF;
        Boolean verifiedUser;



        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_verifyuser, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            NavController navController = Navigation.findNavController(view);

            dREF = firenode.collection("Users").document(userID);
            dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    // Only works if you have ocr input
                    verifiedUser = true; //value.getBoolean("enteredParking");
                }
            });


            Button button = view.findViewById(R.id.scanbutton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (verifiedUser.equals(true)) {

                        Toast.makeText(getActivity(), "Verification successful", Toast.LENGTH_SHORT).show();

                        navController.navigate(R.id.action_navigation_scanbarcode_to_navigation_parking);

                    }  else if (verifiedUser.equals(false)){

                        Toast.makeText(getActivity(), "Verification successful", Toast.LENGTH_SHORT).show();

                        navController.navigate(R.id.action_navigation_scanbarcode_to_navigation_parking);

                    } else {
                        Toast.makeText(getActivity(), "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            ImageView imageView = view.findViewById(R.id.imageView3);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_navigation_scanbarcode_to_navigation_home);
                }
            });

            ImageView userprofile = view.findViewById(R.id.userprofile);
            userprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_navigation_scanbarcode_to_navigation_profile);
                }
            });

        }
    }