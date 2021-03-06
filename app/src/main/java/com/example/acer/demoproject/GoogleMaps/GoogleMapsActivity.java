package com.example.acer.demoproject.GoogleMaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.acer.demoproject.DirectionHelper.FetchURL;
import com.example.acer.demoproject.DirectionHelper.TaskLoadedCallback;
import com.example.acer.demoproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    Button btnGetDirection;
    LatLng destinationLatLng;
    String apiKey;
    String directionMode;
    Polyline currentPolyline;
    int clickCount = 0;
    Double place_lat,place_long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        place_lat = Double.parseDouble(intent.getStringExtra("place_lat"));
        place_long = Double.parseDouble(intent.getStringExtra("place_long"));
        destinationLatLng = new LatLng(place_lat, place_long);
        directionMode = "driving";

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_google_maps);
            btnGetDirection = (Button) findViewById(R.id.btnGetDirection);
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
            final int totalKeys = Integer.parseInt(getString(R.string.key_count));
            btnGetDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    clickCount++;
//                    if (clickCount > totalKeys){
//                        Toast.makeText(getApplicationContext(), "ALL API KEY USED", Toast.LENGTH_SHORT).show();
//                    }
//
//                    switch (clickCount) {
//                        case 1:
//                            apiKey = getString(R.string.google_maps_key);
//                            break;
//                        case 2:
//                            apiKey = getString(R.string.google_maps_key);
//                            break;
//                        case 3:
//                            apiKey = getString(R.string.google_maps_key);
//                            break;
//                        case 4:
//                            apiKey = getString(R.string.google_maps_key);
//                            break;
//                        case 5:
//                            apiKey = getString(R.string.google_maps_key);
//                            break;
//                        default: break;
//                    }
                    apiKey = getString(R.string.google_maps_key);
                    Location myPosition = MapHelper.GetLastKnownLocation(GoogleMapsActivity.this);
                    double tempLat = myPosition.getLatitude();
                    double tempLong = myPosition.getLongitude();
                    String url = MapHelper.GetDirectionApiUrl(new LatLng(tempLat, tempLong), destinationLatLng, directionMode, apiKey);
                    new FetchURL(GoogleMapsActivity.this).execute(url, directionMode);
                }
            });
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapHelper.CreateMapHelperInstance(googleMap, (LocationManager) getSystemService(Context.LOCATION_SERVICE), this);
        MapHelper.GetMyLocation(GoogleMapsActivity.this, false);
        MapHelper.AddMarker(destinationLatLng, "Destination");
        MapHelper.GetMapInstance().animateCamera(
                CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15), 2000, null);

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = MapHelper.GetMapInstance().addPolyline((PolylineOptions) values[0]);
    }
}
