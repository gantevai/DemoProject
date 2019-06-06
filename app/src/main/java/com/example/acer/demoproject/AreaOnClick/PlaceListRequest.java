package com.example.acer.demoproject.AreaOnClick;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 5/27/2019.
 */

public class PlaceListRequest extends StringRequest {
    private static final String PLACE_REQUEST_URL = "http://pasang1422.000webhostapp.com/places.php";
    private Map<String,String> params;

    public PlaceListRequest( Response.Listener<String> listener){
        super(Method.POST, PLACE_REQUEST_URL, listener,null);
        params = new HashMap<>();
//        params.put("place_id", place_id);
//        params.put("placeName", placeName);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
