package com.example.acer.demoproject.RecommendedAreaActivity;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 4/28/2019.
 */

public class RecommendedAreaRequest extends StringRequest {

    private static final String AREA_REQUEST_URL = "http://pasang1422.000webhostapp.com/recommendarearequest.php";
    private Map<String,String> params;

    public RecommendedAreaRequest(String url, String place_name, Response.Listener<String> listener){
        super(Method.POST, AREA_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("url", url);
        params.put("place_name", place_name);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
