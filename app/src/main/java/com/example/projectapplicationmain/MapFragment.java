package com.example.projectapplicationmain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arubanetworks.meridian.Meridian;
import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.editor.EditorKey.*;

import com.arubanetworks.meridian.maps.MapView;


public class MapFragment extends Fragment {


    private MapView mapView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Meridian.configure(getActivity(), "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0IjoxNjEwOTk0NTc5LCJ2YWx1ZSI6ImE4YTliNzYzNGJmMWE5ZDMxMzBiMzQ2YjM5OGVmOGRlNTk4ZWNkYTkifQ.y3Kng8S8V9_EWM-48CiWwjDitv-wBfvFmpx-GlZ0adY");
        NavController navController = Navigation.findNavController(view);

        EditorKey APP_KEY =  EditorKey.forApp("4721326715699200");
        EditorKey MAP_KEY = EditorKey.forMap("5668600916475904", APP_KEY);

//        mapView = new MapView(getActivity());
//        mapView.setAppKey(appKey);
   //mapView.setMapKey(EditorKey.forApp("5656090511540224", appKey.getId()));
//        mapView.setMapKey(EditorKey.forMap("5656090511540224", appKey.getId()));
        ImageView userprofile = view.findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                navController.navigate(R.id.action_navigation_map_to_navigation_profile);
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();

        // MapView needs to know when its host activity is resumed
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        // MapView needs to know when its host activity is paused
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // MapView needs to know when its host activity is destroyed (SDK 4.7.0 and above)
        mapView.onDestroy();
    }
}

