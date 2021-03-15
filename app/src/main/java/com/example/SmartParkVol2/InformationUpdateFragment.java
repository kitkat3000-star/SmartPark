package com.example.SmartParkVol2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;


public class InformationUpdateFragment extends Fragment {
    FirebaseFirestore firenode = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userID =  Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    DocumentReference dREF;
    String Newname, Newemail, Newmobile;
    TextView username;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_updateinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username =  view.findViewById(R.id.username);
        dREF = firenode.collection("Users").document(userID);
        dREF.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // Only works if you have ocr input
                String name = value.getString("name");
                username.setText(name);
            }
        });

        NavController navController = Navigation.findNavController(view);
        EditText userName = (EditText) view.findViewById(R.id.usernameEdit);
        EditText userMobile = (EditText) view.findViewById(R.id.userMobileEdit);
        Button updateinfo = (Button) view.findViewById(R.id.updateinfoBtn);

        updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Newname = userName.getText().toString();
                Newmobile= userMobile.getText().toString();
                dREF = firenode.collection("Users").document(userID);
                dREF.update("name",Newname,"phoneNumber",Newmobile);
                navController.navigate(R.id.action_navigation_updateinfo_to_navigation_information);
            }
        });






        ImageView back = view.findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_updateinfo_to_navigation_information);
            }
        });


    }
}