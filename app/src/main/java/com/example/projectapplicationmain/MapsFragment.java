
package com.example.projectapplicationmain;

        import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arubanetworks.meridian.Meridian;
import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.maps.MapFragment;
import com.arubanetworks.meridian.maps.directions.DirectionsDestination;
import com.arubanetworks.meridian.maps.directions.DirectionsSource;


public class MapsFragment extends Fragment {


    final EditorKey appKey = new EditorKey("5656090511540224");
    final EditorKey mapKey = EditorKey.forMap("6487331234250752", appKey.getId());
    final EditorKey directionsSource = EditorKey.forPlacemark("6487331234250752_5225694472830976", mapKey);
    final EditorKey directionsDestination = EditorKey.forPlacemark("6487331234250752_4725445002133504", mapKey);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View mapLayout = inflater.inflate(R.layout.fragment_map, container, false);
        Meridian.getShared().setForceSimulatedLocation(true);
        //MapFragment mapFragment = new MapFragment.Builder().setMapKey(EditorKey.forMap("6487331234250752", appKey.getId())).build();
        MapFragment mapFragment;

        mapFragment = new MapFragment
                .Builder()
                .setMapKey(mapKey)
                .setSource(DirectionsSource
                        .forPlacemarkKey(directionsSource))
                .setDestination(DirectionsDestination
                        .forPlacemarkKey(directionsDestination))
                .build();

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.maplayout, mapFragment, null);
        transaction.commit();

        return mapLayout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        ImageView userprofile = view.findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_map_to_navigation_profile);
            }
        });

//        ImageView imageView = view.findViewById(R.id.imageView3);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_navigation_map_to_navigation_profile);
//            }
//        });
    }

}
