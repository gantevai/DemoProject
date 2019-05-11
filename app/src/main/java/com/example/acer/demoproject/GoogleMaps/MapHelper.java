package com.example.acer.demoproject.GoogleMaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.acer.demoproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class MapHelper {
    private static LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static int DEFAULT_ZOOM = 18;
    private static MapHelper instance_MapHelper = null;
    public static GoogleMap instance_GMap = null;
    public static LocationManager instance_LM = null;
    private static String directionApiUrl = "https://maps.googleapis.com/maps/api/directions/";
    private static String defaultOutputFormat = "json";
    private static Context context = null;

    public static MapHelper CreateMapHelperInstance(GoogleMap gMap, LocationManager lm, Context currentContext) {
        if (instance_MapHelper == null)
            instance_MapHelper = new MapHelper();
        if (instance_GMap == null)
            instance_GMap = gMap;
        if (instance_LM == null)
            instance_LM = lm;
        if (context == null)
            context = currentContext;
        return instance_MapHelper;
    }

    public static GoogleMap GetMapInstance() {
        return instance_GMap;
    }

    public static LocationManager GetManagerInstance() {
        return instance_LM;
    }

    private static void GetLocationPermission(Context context, Activity activity) {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(!instance_GMap.isMyLocationEnabled())
                instance_GMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    public static Location GetLastKnownLocation(Activity activity) {
        Location location;
        final Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        location = instance_LM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = instance_LM.getBestProvider(criteria, true);
            location = instance_LM.getLastKnownLocation(provider);
        }
        return location;
    }

    public static LatLng GetMyLocation(Activity activity, boolean showMarker) {
        GetLocationPermission(context,activity);
        Location location  = GetLastKnownLocation(activity);
        if (location == null)
            return null;
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (showMarker)
            AddMarker(myLocation, "Origin");
        return myLocation;
    }

    public static void AddMarker(LatLng latLng, String title) {
        final MarkerOptions marker = new MarkerOptions().
                position(latLng).title(title);
        instance_GMap.addMarker(marker);
    }

    public static String GetDirectionApiUrl(String origin, String dest, String directionMode, String key) {
        return ManageUrlString(origin, dest, defaultOutputFormat, directionMode, key);
    }

    public static String GetDirectionApiUrl(LatLng origin, LatLng dest, String directionMode, String key) {
        String str_origin = origin.latitude + ","+origin.longitude;
        String str_dest = dest.latitude + ","+dest.longitude;

//        String str_origin = String.format("%d,%d", origin.latitude, origin.longitude);
//        String str_dest = String.format("%d,%d", dest.latitude, dest.longitude);
        return ManageUrlString(str_origin, str_dest, defaultOutputFormat, directionMode, key);

    }

    private static String ManageUrlString(String origin, String destination, String outputFormat, String mode, String key) {
        if (outputFormat == null)
            outputFormat = defaultOutputFormat;
        String url = String.format("%s%s?origin=%s&destination=%s&mode=%s&key=%s",
                directionApiUrl, outputFormat, origin, destination,mode, key);
        return url;
    }
}

