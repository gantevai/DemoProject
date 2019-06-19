package com.example.acer.demoproject.AreaOnClick;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 6/19/2019.
 */

public class CustomQueryRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://pasang1422.000webhostapp.com/retreive_title.php";
    private Map<String,String> params;

    public CustomQueryRequest(String query, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener,null);
        params = new HashMap<>();
        params.put("query", query);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
