package com.example.acer.demoproject.GoogleMaps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.acer.demoproject.DirectionHelper.FetchURL;
import com.example.acer.demoproject.DirectionHelper.TaskLoadedCallback;
import com.example.acer.demoproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap gMap;
    Button btnGetDirection;
    MarkerOptions origin, destination;
    double originLatLng[] = new double[2];

    Polyline currentPolyline;
    LatLng destLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_google_maps);
            btnGetDirection = (Button) findViewById(R.id.btnGetDirection);
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
//        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location1");
            destLatLng = new LatLng(27.7105, 85.3487);
            destination = new MarkerOptions().position(destLatLng).title("Destination");

//        Intent intent = getIntent();
//        final String destination = intent.getStringExtra("address");
            btnGetDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLng myPosition = getMyCurrentLocation();
                    String url = getUrl(myPosition, destination.getPosition(), "driving");
                    new FetchURL(GoogleMapsActivity.this).execute(url, "driving");
                }
            });
        } catch (Exception ex) {
            throw ex;
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        //Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Mode eg:walking,driving,cycling
        String mode = "mode=" + directionMode;
        //Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        //Output format
        String output = "json";
        //Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng myPosition = getMyCurrentLocation();
        originLatLng = getMyLocation();
        origin = new MarkerOptions().position(new LatLng(originLatLng[0], originLatLng[1])).title("Origin");
//        origin = new MarkerOptions().position(myPosition).title("Origin");
//        gMap.addMarker(origin);
        gMap.addMarker(destination);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 15), 2000, null);
    }

    private double[] getMyLocation() {
        double d[] = new double[2];
        final Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(GoogleMapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return getMyLocation();
        } else {
            if (!gMap.isMyLocationEnabled())
                gMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }
            if (myLocation != null) {
//            LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                d[0] = myLocation.getLatitude();
                d[1] = myLocation.getLongitude();
            }
            return d;
        }
    }


    private LatLng getMyCurrentLocation() {
       final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location oldloc;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (!gMap.isMyLocationEnabled())
                gMap.setMyLocationEnabled(true);
        }
        while (true) {
            oldloc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (oldloc == null) {
                continue;
            } else {
                break;
            }
        }
        if (oldloc != null) {
            return new LatLng(oldloc.getLatitude(), oldloc.getLongitude());
        }
        return null;
    }

//    // this function throws out the dialog box and
//    // click: Yes => forcefully enables location
//    // click: No => closes dialog
//    private void buildAlertMessageNoGps(Context context) {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setMessage("Write your message here.");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();
////        new AlertDialog.Builder(context).setTitle("GPS Permission")
////                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
//////                .setCancelable(false)
////                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
////                    }
////                })
////                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////                        dialog.cancel();
////                    }
////                }).show();
//    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = gMap.addPolyline((PolylineOptions) values[0]);
    }
}
