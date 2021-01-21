package com.example.projectapplicationmain;

import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arubanetworks.meridian.location.LocationRequest;
import com.arubanetworks.meridian.location.MeridianLocation;
import com.arubanetworks.meridian.location.MeridianOrientation;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;
import com.arubanetworks.meridian.maps.directions.Directions;



public class MapsFragment extends Fragment  implements MapView.MapEventListener {

    private MapView mapView;
    private static final String PENDING_DESTINATION_KEY = "meridianSamples.PendingDestinationKey";
    private static final int SOURCE_REQUEST_CODE = "meridianSamples.source_request".hashCode() & 0xFF;
    private Directions directions;
    private LocationRequest locationRequest;
 //   EditorKey appKEY =  EditorKey.forApp("4721326715699200");
//    EditorKey mapKEY = EditorKey.forMap("6487331234250752", appKEY.getId());


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View mapLayout = inflater.inflate(R.layout.fragment_map, container, false);
//
//        MapFragment mapFragment = new MapFragment.Builder()
//                .setAppKey(LandingActivity.appKEY)
//                .setMapKey(LandingActivity.mapKEY)
//                .build();
//
//        mapFragment.onMapLoadStart();

        mapView = (MapView) mapLayout.findViewById(R.id.demo_mapview);
        mapView = new MapView(getActivity());
        mapView.setAppKey(LandingActivity.appKEY);



        mapView.setMapKey(LandingActivity.mapKEY);
        // If you want to handle MapView events
        mapView.setMapEventListener(this);

        // If you want to handle directions events

//        mapView.setMapEventListener(this);
//        mapView.setDirectionsEventListener(this);
//        mapView.setMarkerEventListener(this);
        MapOptions mapOptions = mapView.getOptions();
        mapOptions.HIDE_MAP_LABEL = true;
        mapView.setOptions(mapOptions);

//        NavController navController = Navigation.findNavController(mapLayout);
//        ImageView userprofile = mapLayout.findViewById(R.id.userprofile);
//        userprofile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navController.navigate(R.id.action_navigation_map_to_navigation_profile);
//            }
//        });
         return mapLayout;
    }
//


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up memory.
        mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onMapLoadStart() {

    }

    @Override
    public void onMapLoadFinish() {

    }

    @Override
    public void onPlacemarksLoadFinish() {

    }

    @Override
    public void onMapRenderFinish() {

    }

    @Override
    public void onMapLoadFail(Throwable throwable) {

    }

    @Override
    public void onMapTransformChange(Matrix matrix) {

    }

    @Override
    public void onLocationUpdated(MeridianLocation meridianLocation) {

    }

    @Override
    public void onOrientationUpdated(MeridianOrientation meridianOrientation) {

    }
}

