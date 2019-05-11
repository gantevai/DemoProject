package com.example.acer.demoproject.GoogleMaps;

import com.example.acer.demoproject.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Acer on 5/10/2019.
 */

public class UrlHelper {

    private static String getUrl(LatLng origin, LatLng dest, String directionMode) {
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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=";
        //+ getString(R.string.google_maps_key);
        return url;
    }
}
