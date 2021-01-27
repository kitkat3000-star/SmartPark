package com.example.projectapplicationmain;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.arubanetworks.meridian.editor.Placemark;
import com.arubanetworks.meridian.location.LocationRequest;
import com.arubanetworks.meridian.location.MeridianLocation;
import com.arubanetworks.meridian.location.MeridianOrientation;
import com.arubanetworks.meridian.maps.ClusteredMarker;
import com.arubanetworks.meridian.maps.MapOptions;
import com.arubanetworks.meridian.maps.MapView;
import com.arubanetworks.meridian.maps.Marker;
import com.arubanetworks.meridian.maps.directions.Directions;
import com.arubanetworks.meridian.maps.directions.DirectionsDestination;
import com.arubanetworks.meridian.maps.directions.DirectionsResponse;
import com.arubanetworks.meridian.maps.directions.DirectionsSource;
import com.arubanetworks.meridian.maps.directions.TransportType;
import com.arubanetworks.meridian.search.SearchActivity;

public class MapsFragment extends Fragment implements MapView.DirectionsEventListener, MapView.MapEventListener, MapView.MarkerEventListener {



    private MapView mapView;
    private static final String PENDING_DESTINATION_KEY = "meridianSamples.PendingDestinationKey";
    private static final int SOURCE_REQUEST_CODE = "meridianSamples.source_request".hashCode() & 0xFF;
    private Directions directions;
    private LocationRequest locationRequest;
 //   public static final EditorKey placemark = EditorKey.forPlacemark("6487331234250752_4725445002133504", LandingActivity.mapKey);
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_map, container, false);
        //   View layout = inflater.inflate(R.layout.fragment_map, container, false);
        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                    permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
                }
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }


            mapView = layout.findViewById(R.id.demo_mapview);

            mapView.setAppKey(LandingActivity.appKey);

            // If you want to handle MapView events
            mapView.setMapEventListener(this);

            // If you want to handle directions events
            mapView.setDirectionsEventListener(this);


            // If you want to handle marker events
            mapView.setMarkerEventListener(this);

            // Set map options if desired
            MapOptions mapOptions = mapView.getOptions();
            mapOptions.HIDE_MAP_LABEL = true;
            mapView.setOptions(mapOptions);

            // Set which map to load
            // It is recommended to do this after setting the map options
            mapView.setMapKey(LandingActivity.mapKey);


            return layout;
        }




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

    //
    // MapViewListener methods
    //
    @Override public void onMapLoadStart() { }
    @Override public void onMapLoadFinish() { }
    @Override public void onPlacemarksLoadFinish() {}
    @Override public void onMapLoadFail(Throwable tr) { }
    @Override public void onMapRenderFinish() { }
    @Override public void onMapTransformChange(Matrix transform) { }
    @Override public void onLocationUpdated(MeridianLocation location) { }
    @Override public void onOrientationUpdated(MeridianOrientation orientation) { }

    //
    // DirectionsEventListener methods
    //
    @Override public void onDirectionsReroute() { }
    @Override public boolean onDirectionsClick(Marker marker) {
        if (getActivity() != null) {
           // EditorKey p = EditorKey.forPlacemark("6487331234250752_4725445002133504", LandingActivity.mapKey);
            Placemark p = mapView.getAssociatedPlacemark(marker);
            if (p != null) {
               startDirections(DirectionsDestination.forPlacemarkKey(p.getKey()));
              //  startDirections(DirectionsDestination.forPlacemarkKey(p));
            } else {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Directions only implemented for placemarks.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
        return true;
    }
    @Override public boolean onDirectionsStart() { return false; }
    @Override public boolean onRouteStepIndexChange(int index) { return false; }
    @Override public boolean onDirectionsClosed() {return false; }
    @Override public boolean onDirectionsError(Throwable tr) { return false; }
    @Override public void onUseAccessiblePathsChange() { }
    //
    // MarkerEventListener methods
    //
    @Override
    public boolean onMarkerSelect(Marker marker) {
        // prevent clustered markers from being selected
        return (marker instanceof ClusteredMarker);
    }
    @Override
    public boolean onMarkerDeselect(Marker marker) {
        return false;
    }

    @Override
    public Marker markerForPlacemark(Placemark placemark) {
        return null;
    }

    @Override
    public Marker markerForSelectedMarker(Marker markerToSelect) {
        return null;
    }
    @Override
    public boolean onCalloutClick(Marker marker) {
        return false;
    }

    /// Directions support

    private void startDirections(final DirectionsDestination destination) {
        // check if we already have started directions
        if (directions != null) directions.cancel();

        // if we have requested the user location and its still running, keep it
        if (locationRequest != null && locationRequest.isRunning()) return;

        if (getActivity() == null) {
            return;
        }
        mapView.onDirectionsRequestStart();

        // Lets see if we can get the users location
        locationRequest =
                LocationRequest.requestCurrentLocation(getActivity(), LandingActivity.appKey, new LocationRequest.LocationRequestListener() {
                    @Override
                    public void onResult(MeridianLocation location) {
                        if (location == null) {
                            startSearchActivity(destination);
                            return;
                        }
                        // Looks like we got a good location
                        onSourceResult(destination, DirectionsSource.forMapPoint(location.getMapKey(), location.getPoint()));
                    }

                    @Override
                    public void onError(LocationRequest.ErrorType errorType) {
                        if (errorType != LocationRequest.ErrorType.CANCELED) {
                            startSearchActivity(destination);
                        }
                    }
                });
    }

    private void startSearchActivity(DirectionsDestination destination) {
        // Location is unknown so use the Search activity to get a
        // start location from the user.

        // Handle any exclusions
        Intent i = SearchActivity.createIntent(getActivity(), LandingActivity.appKey,
                destination == null ? null : destination.getSearchExclusions());
        i.putExtra(PENDING_DESTINATION_KEY, destination);
        startActivityForResult(i, SOURCE_REQUEST_CODE);
    }

    private void startDirections(final DirectionsDestination destination, final DirectionsSource source) {
        if (directions != null) directions.cancel();
        if (getActivity() == null) {
            return;
        }

        directions = new Directions.Builder()
                .setAppKey(LandingActivity.appKey)
                .setDestination(destination)
                .setListener(new Directions.DirectionsRequestListener() {
                    @Override
                    public void onDirectionsRequestStart() {
                        if (mapView != null) {
                            mapView.onDirectionsRequestStart();
                        }
                    }

                    @Override
                    public void onDirectionsRequestComplete(DirectionsResponse response) {
                        if (mapView != null) {
                            mapView.onDirectionsRequestComplete(response);
                        }
                    }

                    @Override
                    public void onDirectionsRequestError(final Throwable th) {
                        if (mapView != null) {
                            mapView.onDirectionsRequestError(th);
                        }
                    }

                    @Override
                    public void onDirectionsRequestCanceled() {
                        if (mapView != null) {
                            mapView.onDirectionsRequestCanceled();
                        }
                    }
                })
                .setTransportType(TransportType.WALKING)
                .setSource(source).build();
        directions.calculate();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOURCE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DirectionsDestination destination = (DirectionsDestination) data.getSerializableExtra(PENDING_DESTINATION_KEY);
                Placemark result = SearchActivity.getSearchResult(data).getPlacemark();
                DirectionsSource source = result.isInvalid() ?
                        DirectionsSource.forPlacemarkKey(result.getKey()) :
                        DirectionsSource.forMapPoint(result.getKey().getParent(), new PointF(result.getX(), result.getY()));
                onSourceResult(destination, source);
            }
            if (mapView != null) {
                mapView.onDirectionsRequestCanceled();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onSourceResult(DirectionsDestination destination, DirectionsSource source) {
        startDirections(destination, source);
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

        ImageView userprofile = view.findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_map_to_navigation_profile);
            }
        });


    }

}
