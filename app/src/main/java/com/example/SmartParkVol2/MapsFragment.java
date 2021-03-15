package com.example.SmartParkVol2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arubanetworks.meridian.editor.EditorKey;
import com.arubanetworks.meridian.editor.Placemark;
import com.arubanetworks.meridian.location.LocationRequest;
import com.arubanetworks.meridian.location.MeridianLocation;
import com.arubanetworks.meridian.location.MeridianOrientation;
import com.arubanetworks.meridian.maps.FlatPlacemarkMarker;
import com.arubanetworks.meridian.maps.MapFragment;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;
import com.arubanetworks.meridian.maps.Marker;
import com.arubanetworks.meridian.maps.directions.Directions;
import com.arubanetworks.meridian.maps.directions.DirectionsDestination;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class MapsFragment extends Fragment { /*implements MapView.DirectionsEventListener, MapView.MapEventListener, MapView.MarkerEventListener*/

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    DocumentReference dREF;
    FirebaseFirestore Fstore = FirebaseFirestore.getInstance();
    String ParkingSpot1;
    String ParkingZone;
    String placemark1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mapLayout = inflater.inflate(R.layout.fragment_map, container, false);

        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
                }
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }

        MapFragment.Builder builder = new MapFragment.Builder()
                .setAppKey(LandingActivity.appKey)
                .setMapKey(LandingActivity.mapKey);
        MapOptions mapOptions = MapOptions.getDefaultOptions();
        // Turn off the overview button (only shown if there is an overview map for the location)
        mapOptions.HIDE_OVERVIEW_BUTTON = true;
        // example: how to set placemark markers text size
        //mapOptions.setTextSize(14);
        builder.setMapOptions(mapOptions);



        final MapFragment mapFragment = builder.build();
        mapFragment.setMapEventListener(new MapView.MapEventListener() {

            @Override
            public void onMapLoadFinish() {
            }

            @Override
            public void onMapLoadStart() {
            }

            @Override
            public void onPlacemarksLoadFinish() {


                // example: how to start directions programmatically
                final int[] index = {0};
                for (Placemark placemark : mapFragment.getMapView().getPlacemarks()) {
                    dREF = Fstore.collection("Users").document(userID);
                    dREF.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                            ParkingSpot1 = value.getString("parkingSpot");
                            ParkingZone = value.getString("parkingZone");

                            if (ParkingZone.equals("Zone Valet")) {
                                placemark1 = "valetDropOff";
                            } else {
                                placemark1 = ParkingSpot1;
                            }

                            //Log.d("AAA", ParkingSpot1);
                            index[0]++;
                            if (placemark1.equals(placemark.getName())) {
                                placemarkcolor(index[0]);
                                mapFragment.startDirections(DirectionsDestination.forPlacemarkKey(placemark.getKey()));
                            }
                        }
                    });


                }

            }

            public void placemarkcolor(int index) {
                int i = 0;
                for (Marker marker : mapFragment.getMapView().getAllMarkers()) {
                    if (marker instanceof FlatPlacemarkMarker) {
                        i++;
                        if (i == index) {
                            ((FlatPlacemarkMarker) marker).setTintColor(Color.RED);
                            break;
                        }
                    }
                }
            }


            @Override
            public void onMapRenderFinish() {
            }

            @Override
            public void onMapLoadFail(Throwable tr) {

            }

            @Override
            public void onMapTransformChange(Matrix transform) {
            }

            @Override
            public void onLocationUpdated(MeridianLocation location) {
            }

            @Override
            public void onOrientationUpdated(MeridianOrientation orientation) {
            }
        });

        mapFragment.setMapClickEventListener(new MapView.MapClickEventListener() {
            @Override
            public boolean onLocationButtonClick() {
                // example of how to override the behavior of the location button
                // Note that the default behavior (return false) provides animation and error handling
                final MapView mapView = mapFragment.getMapView();
                MeridianLocation location = mapView.getUserLocation();
                mapView.setShowsUserLocation(true);
                if (location != null) {
                    mapView.showsUserLocation();
                    mapView.updateForLocation(location);

                } else {
                    LocationRequest.requestCurrentLocation(getActivity(), LandingActivity.appKey, new LocationRequest.LocationRequestListener() {
                        @Override
                        public void onResult(MeridianLocation location) {
                            mapView.updateForLocation(location);
                        }

                        @Override
                        public void onError(LocationRequest.ErrorType location) {
                            // handle the error
                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onOverviewButtonClick() {
                return false;
            }

            @Override
            public boolean onLongPress() {
                return false;
            }

            @Override
            public boolean onMapClick() {
                return false;
            }
        });
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.maplayout, mapFragment, null);
        transaction.commit();

        return mapLayout;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Begin monitoring for Aruba Beacon-based Campaign events
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // now request background permissions, should use a fancy UI for this
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String[] permissions2 = new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), permissions2, 1);
                return;
            }
        }

    }

    @Override
    public void onViewCreated(@NonNull android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        Button nextdashboard = view.findViewById(R.id.nextdashboard);
        nextdashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_navigation_map_to_navigation_home2);
            }
        });


    }

}





//-----------------------------------------------------------------------------------------------------------------------


//    private static final String TAG = "MyActivity";
//    public static final EditorKey placemark = EditorKey.forPlacemark("6487331234250752_4725445002133504", LandingActivity.mapKey);
//    private MapView mapView;
//    private static final String PENDING_DESTINATION_KEY = "meridianSamples.PendingDestinationKey";
//    private static final int SOURCE_REQUEST_CODE = "meridianSamples.source_request".hashCode() & 0xFF;
//    private Directions directions;
//    private LocationRequest locationRequest;
//    final EditorKey directionsDestination = EditorKey.forPlacemark("6487331234250752_4725445002133504", LandingActivity.mapKey);
//    MeridianLocation Source;
//    private BroadcastReceiver receiver;
// examples: how to set directions options.
                /*DirectionsOptions directionsOptions = DirectionsOptions.getDefaultOptions();
                directionsOptions.DIRECTIONS_CALLOUT_COLOR = ContextCompat.getColor(this, R.color.mr_color_primary);
                directionsOptions.DIRECTIONS_SLIDE_COLOR = ContextCompat.getColor(this, R.color.mr_color_primary);
                directionsOptions.ROUTE_OVERVIEW_BUTTON_COLOR = ContextCompat.getColor(this, R.color.mr_md_gray_600);
                directionsOptions.END_ROUTE_BUTTON_COLOR = ContextCompat.getColor(this, R.color.mr_md_gray_600);
                directionsOptions.ROUTE_OVERVIEW_BUTTON_TEXT_COLOR = (ContextCompat.getColor(this, R.color.mr_white));
                directionsOptions.END_ROUTE_BUTTON_TEXT_COLOR = ContextCompat.getColor(this, R.color.mr_color_primary);
                directionsOptions.REORIENTATION_BANNER_COLOR = ContextCompat.getColor(this, R.color.mr_md_gray_600);
                directionsOptions.DIRECTIONS_INSTRUCTIONS_ON_TOP = true;
                directionsOptions.DIRECTIONS_INSTRUCTIONS_TEXT_COLOR = ContextCompat.getColor(this, R.color.mr_md_gray_600);;
                builder.setDirectionsOptions(directionsOptions);*/

//                if (mapFragment.isAdded() && mapFragment.getActivity() != null) {
//                    String message = getString(com.arubanetworks.meridian.R.string.mr_error_invalid_map);
//                    if (tr != null) {
//                        if (tr instanceof VolleyError && ((VolleyError) tr).networkResponse != null && ((VolleyError) tr).networkResponse.statusCode == 401) {
//                            message = "HTTP 401 Error: Please verify the Editor token.";
//                        } else if (!Strings.isNullOrEmpty(tr.getLocalizedMessage())) {
//                            message = tr.getLocalizedMessage();
//                        }
//                    }
//                    new AlertDialog.Builder(mapFragment.getActivity())
//                            .setTitle(getString(com.arubanetworks.meridian.R.string.mr_error_title))
//                            .setMessage(message)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setPositiveButton(com.arubanetworks.meridian.R.string.mr_ok, null)
//                            .show();
//                }


//
//            mapView.setAppKey(LandingActivity.appKey);
//
//            // If you want to handle MapView events
//            mapView.setMapEventListener(this);
//
//            // If you want to handle directions events
//            mapView.setDirectionsEventListener(this);
//
//
//            // If you want to handle marker events
//            mapView.setMarkerEventListener(this);
//
//            // Set map options if desired
//            MapOptions mapOptions = mapView.getOptions();
//            mapOptions.HIDE_MAP_LABEL = true;
//            mapView.setOptions(mapOptions);ote
//
//            // Set which map to load
//            // It is recommended to do this after setting the map options
//            mapView.setMapKey(LandingActivity.mapKey);
//





//        @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // Clean up memory.
//        mapView.onDestroy();
//    }
//
//    //
//    // MapViewListener methods
//    //
//    @Override public void onMapLoadStart() { }
//    @Override public void onMapLoadFinish() { }
//    @Override public void onPlacemarksLoadFinish() {}
//    @Override public void onMapLoadFail(Throwable tr) { }
//    @Override public void onMapRenderFinish() { }
//    @Override public void onMapTransformChange(Matrix transform) { }
//    @Override public void onLocationUpdated(MeridianLocation location) { }
//    @Override public void onOrientationUpdated(MeridianOrientation orientation) { }

//
// DirectionsEventListener methods
//
//    @Override public void onDirectionsReroute() { }
//    @Override public boolean onDirectionsClick(Marker marker) {
//        if (getActivity() != null) {
//           // EditorKey p = EditorKey.forPlacemark("6487331234250752_4725445002133504", LandingActivity.mapKey);
//            Placemark p = mapView.getAssociatedPlacemark(marker);
//            if (p != null) {
//               startDirections(DirectionsDestination.forPlacemarkKey(p.getKey()));
//              //  startDirections(DirectionsDestination.forPlacemarkKey(p));
//            } else {
//                new AlertDialog.Builder(getActivity())
//                        .setMessage("Directions only implemented for placemarks.")
//                        .setPositiveButton("OK", null)
//                        .show();
//            }
//        }
//        return true;
//    }
//    @Override public boolean onDirectionsStart() { return false; }
//    @Override public boolean onRouteStepIndexChange(int index) { return false; }
//    @Override public boolean onDirectionsClosed() {return false; }
//    @Override public boolean onDirectionsError(Throwable tr) { return false; }
//    @Override public void onUseAccessiblePathsChange() { }
//    //
//    // MarkerEventListener methods
//    //
//    @Override
//    public boolean onMarkerSelect(Marker marker) {
//        // prevent clustered markers from being selected
//        return (marker instanceof ClusteredMarker);
//    }
//    @Override
//    public boolean onMarkerDeselect(Marker marker) {
//        return false;
//    }
//
//    @Override
//    public Marker markerForPlacemark(Placemark placemark) {
//        return null;
//    }
//
//    @Override
//    public Marker markerForSelectedMarker(Marker markerToSelect) {
//        return null;
//    }
//    @Override
//    public boolean onCalloutClick(Marker marker) {
//        return false;
//    }
//
//    /// Directions support
//
//    private void startDirections(final DirectionsDestination destination) {
//        // check if we already have started directions
//        if (directions != null) directions.cancel();
//
//        // if we have requested the user location and its still running, keep it
//        if (locationRequest != null && locationRequest.isRunning()) return;
//
//        if (getActivity() == null) {
//            return;
//        }
//        mapView.onDirectionsRequestStart();
//
//        // Lets see if we can get the users location
//        locationRequest =
//                LocationRequest.requestCurrentLocation(getActivity(), LandingActivity.appKey, new LocationRequest.LocationRequestListener() {
//                    @Override
//                    public void onResult(MeridianLocation location) {
//                        if (location == null) {
//                            startSearchActivity(destination);
//                            return;
//                        }
//                        // Looks like we got a good location
//                        onSourceResult(destination, DirectionsSource.forMapPoint(location.getMapKey(), location.getPoint()));
//                    }
//
//                    @Override
//                    public void onError(LocationRequest.ErrorType errorType) {
//                        if (errorType != LocationRequest.ErrorType.CANCELED) {
//                            startSearchActivity(destination);
//                        }
//                    }
//                });
//    }
//
//    private void startSearchActivity(DirectionsDestination destination) {
//        // Location is unknown so use the Search activity to get a
//        // start location from the user.
//
//        // Handle any exclusions
//        Intent i = SearchActivity.createIntent(getActivity(), LandingActivity.appKey,
//                destination == null ? null : destination.getSearchExclusions());
//        i.putExtra(PENDING_DESTINATION_KEY, destination);
//        startActivityForResult(i, SOURCE_REQUEST_CODE);
//    }
//
//    private void startDirections(final DirectionsDestination destination, final DirectionsSource source) {
//        if (directions != null) directions.cancel();
//        if (getActivity() == null) {
//            return;
//        }
//
//        directions = new Directions.Builder()
//                .setAppKey(LandingActivity.appKey)
//                .setDestination(destination)
//                .setListener(new Directions.DirectionsRequestListener() {
//                    @Override
//                    public void onDirectionsRequestStart() {
//                        if (mapView != null) {
//                            mapView.onDirectionsRequestStart();
//                        }
//                    }
//
//                    @Override
//                    public void onDirectionsRequestComplete(DirectionsResponse response) {
//                        if (mapView != null) {
//                            mapView.onDirectionsRequestComplete(response);
//                        }
//                    }
//
//                    @Override
//                    public void onDirectionsRequestError(final Throwable th) {
//                        if (mapView != null) {
//                            mapView.onDirectionsRequestError(th);
//                        }
//                    }
//
//                    @Override
//                    public void onDirectionsRequestCanceled() {
//                        if (mapView != null) {
//                            mapView.onDirectionsRequestCanceled();
//                        }
//                    }
//                })
//                .setTransportType(TransportType.WALKING)
//                .setSource(source).build();
//        directions.calculate();
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SOURCE_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                DirectionsDestination destination = (DirectionsDestination) data.getSerializableExtra(PENDING_DESTINATION_KEY);
//                Placemark result = SearchActivity.getSearchResult(data).getPlacemark();
//                DirectionsSource source = result.isInvalid() ?
//                        DirectionsSource.forPlacemarkKey(result.getKey()) :
//                        DirectionsSource.forMapPoint(result.getKey().getParent(), new PointF(result.getX(), result.getY()));
//                onSourceResult(destination, source);
//            }
//            if (mapView != null) {
//                mapView.onDirectionsRequestCanceled();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void onSourceResult(DirectionsDestination destination, DirectionsSource source) {
//        startDirections(destination, source);
//    }
//
