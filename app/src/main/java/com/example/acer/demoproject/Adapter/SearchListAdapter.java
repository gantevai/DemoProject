package com.example.acer.demoproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.AreaOnClick.PlaceDescription;
import com.example.acer.demoproject.Model.RecommendedPlaces;
import com.example.acer.demoproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Acer on 8/14/2019.
 */

public class SearchListAdapter extends BaseAdapter{

    private ArrayList<String> placeList;
    Context context;
    int userID;

    public SearchListAdapter(ArrayList<String> placeList, Context context, int userID) {
        this.placeList = placeList;
        this.context = context;
        this.userID = userID;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context,R.layout.search_item_list,null);
        TextView tvItems = (TextView) view.findViewById(R.id.searchItemName);

        //Set text for TextView
        tvItems.setText((CharSequence) placeList.get(position));
        tvItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "select place_id,placeName,place_description,placeType,placeLat,placeLong,rating from places where placeName = '"+placeList.get(position)+"'";
                show(query,v);
            }
        });
        return view;
    }

    private void show(String query, View v) {
        RequestQueue queue= Volley.newRequestQueue(context);
        final String retreiveTitleURL = "http://pasang1422.000webhostapp.com/search_data_retrieve.php";
        try {
            String url = String.format("%s?query=%s", retreiveTitleURL, query);
            final String requestQuery = query;
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    final int place_id;
                    final String placeName;
                    final String place_description;
                    final String placeType;
                    final Double placeLat;
                    final Double placeLong;
                    final Double rating;
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            place_id = jsonObject.getInt("place_id");
                            placeName = jsonObject.getString("placeName");
                            place_description = jsonObject.getString("place_description");
                            placeType = jsonObject.getString("placeType");
                            placeLat = jsonObject.getDouble("placeLat");
                            placeLong = jsonObject.getDouble("placeLong");
                            rating = jsonObject.getDouble("rating");

                        Intent intent = new Intent(v.getContext(), PlaceDescription.class);
                        intent.putExtra("title_name", placeName);
                        intent.putExtra("place_id", String.valueOf(place_id));
                        intent.putExtra("place_lat", String.valueOf(placeLat));
                        intent.putExtra("place_long", String.valueOf(placeLong));
                        intent.putExtra("place_description", place_description);
                        intent.putExtra("place_type", placeType);
                        intent.putExtra("rating_value", String.valueOf(rating));
                        intent.putExtra("userID", String.valueOf(userID));
                        v.getContext().startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("query", requestQuery);
                    return params;
                }
            };
            queue.add(stringRequest);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
