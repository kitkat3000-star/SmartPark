package com.example.projectapplicationmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.arubanetworks.meridian.location.LocationRequest;
import com.arubanetworks.meridian.maps.directions.Directions;

public class MapsFragment extends FragmentActivity {
  //  MapView mapView;

    private static final String PENDING_DESTINATION_KEY = "meridianSamples.PendingDestinationKey";
    private static final int SOURCE_REQUEST_CODE = "meridianSamples.source_request".hashCode() & 0xFF;
    private Directions directions;
    private LocationRequest locationRequest;
    private WebView webView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View mapLayout = inflater.inflate(R.layout.fragment_map, container, false);
        return mapLayout;
    }
}
//

//      //WEB VIEW MAP DISPLAY
//      webView = (WebView) mapLayout.findViewById(R.id.navigatemap);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webView.loadUrl("file:///android_asset/www.html");

        // Map View
//        Meridian.getShared().showBlueDot();
//        mapView = (MapView) mapLayout.findViewById(R.id.demo_mapview);
//        EditorKey appKEY = new EditorKey("5656090511540224");
//        mapView = new MapView(getActivity());
//        mapView.setAppKey(appKEY);
//
//        // If you want to handle MapView events
//        //mapView.setMapEventListener(this);
//
//        MapOptions mapOptions = mapView.getOptions();
//        mapOptions.HIDE_MAP_LABEL = true;
//        mapView.setOptions(mapOptions);
//        mapView.setMapKey(EditorKey.forMap("6487331234250752", appKEY.getId()));

