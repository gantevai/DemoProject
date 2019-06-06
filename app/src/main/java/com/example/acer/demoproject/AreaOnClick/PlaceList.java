package com.example.acer.demoproject.AreaOnClick;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.Adapter.AreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.PlaceListRecyclerViewAdapter;
import com.example.acer.demoproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceList extends AppCompatActivity {
    String[] titles, subtitles,placeDescription,placeType;
    Double[] placeLat,placeLong;
    int[] place_id;
    ArrayList<String> imagesUrl;
    Context context;
    private static final String IMAGE_URL = "http://pasang1422.000webhostapp.com/place_images/";
    private static final String RELIGIOUS_PLACE_URL = "http://pasang1422.000webhostapp.com/religiousplaces.php";
    private static final String HISTORICAL_PLACE_URL = "http://pasang1422.000webhostapp.com/historicalplaces.php";
    private static final String NATURAL_PLACE_URL = "http://pasang1422.000webhostapp.com/naturalplaces.php";
    private static final String ADVENTURE_PLACE_URL = "http://pasang1422.000webhostapp.com/adventureplaces.php";
    private static String PLACE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Intent intent = getIntent();
        String area_category = intent.getStringExtra("area_category");
        if(area_category.equalsIgnoreCase("Religious Places")) {
            PLACE_URL = RELIGIOUS_PLACE_URL;
        }
        else if(area_category.equalsIgnoreCase("Historical Places")) {
            PLACE_URL = HISTORICAL_PLACE_URL;
        }
        else if(area_category.equalsIgnoreCase("Natural Places")) {
            PLACE_URL = NATURAL_PLACE_URL;
        }
        else if(area_category.equalsIgnoreCase("Adventurous Places")){
            PLACE_URL = ADVENTURE_PLACE_URL;
        }
        TextView area_category_heading = (TextView) findViewById(R.id.area_category_heading);
        area_category_heading.setText(area_category);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.place_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadPlaces(recyclerView);
    }


    private void loadPlaces(final RecyclerView recyclerView) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PLACE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray places = new JSONArray(response);
                    imagesUrl = new ArrayList<String>();
                    titles = new String[places.length()];
                    subtitles = new String[places.length()];
                    place_id = new int[places.length()];
                    placeDescription = new String[places.length()];
                    placeType = new String[places.length()];
                    placeLat = new Double[places.length()];
                    placeLong = new Double[places.length()];

                    for (int i = 0; i < places.length(); i++) {
                        JSONObject placeObject = places.getJSONObject(i);

                        place_id[i] = placeObject.getInt("place_id");
                        titles[i] = placeObject.getString("placeName");
                        placeLat[i] = placeObject.getDouble("placeLat");
                        placeLong[i] = placeObject.getDouble("placeLong");
                        placeDescription[i] = placeObject.getString("place_description");
                        placeType[i] = placeObject.getString("placeType");
                        String temporary_url = IMAGE_URL + String.valueOf(place_id[i]) + ".png";
                        subtitles[i] = placeObject.getString("place_region");
                        imagesUrl.add(temporary_url);
                    }
                    PlaceListRecyclerViewAdapter adapter = new PlaceListRecyclerViewAdapter(
                            imagesUrl, titles, subtitles,place_id,placeLat,placeLong,placeDescription,placeType,context);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PlaceList.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}

