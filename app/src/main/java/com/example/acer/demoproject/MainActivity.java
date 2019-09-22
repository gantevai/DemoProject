package com.example.acer.demoproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.AccountActivity.LoginActivity;
import com.example.acer.demoproject.Adapter.AreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.RecommendedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.RelatedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.AreaOnClick.PlaceDescription;
import com.example.acer.demoproject.Model.RecommendedPlaces;
import com.example.acer.demoproject.Model.Users;
import com.example.acer.demoproject.RecommendedAreaActivity.RecommendedAreaRequest;
import com.example.acer.demoproject.SearchActivity.SenderReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout nav_drawerLayout;
    ActionBarDrawerToggle nav_toggle;
    Spinner areaSpinner;
    NavigationView navigationView;
    //Search ko lagi
    String urlAddress = "http://pasang1422.000webhostapp.com/searcher.php";
    SearchView searchView;
    ListView listView;
    ImageView noDataImageView, noNetworkImageView;

    List<RecommendedPlaces> placeList;
    public RecyclerView recyclerView;
    private RelatedAreaRecyclerViewAdapter adapter;
    private String query;
    private RequestQueue queue;
    String username, name, address;
    int userID, placeLength, userLength, placeToRate;
    double[][] ratingMatrix;
    HashMap<Integer, Double> predictedRating = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recommendedAreaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        queue = Volley.newRequestQueue(this);
        placeList = new ArrayList<>();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        address = intent.getStringExtra("address");
        userID = Integer.parseInt(intent.getStringExtra("userID"));

        initSearchBar();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SenderReceiver senderReceiver = new SenderReceiver(MainActivity.this,userID,urlAddress,query,listView,noDataImageView,noNetworkImageView);
                senderReceiver.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                SenderReceiver senderReceiver = new SenderReceiver(MainActivity.this,userID,urlAddress,query,listView,noDataImageView,noNetworkImageView);
                senderReceiver.execute();
                return false;
            }
        });
        initSpinner();
        initNavigationView(name, username);
        initAreaRecyclerView();
        initRecommendedAreaRecyclerView();


    }

    private void initSearchBar() {
        listView = (ListView) findViewById(R.id.search_content_ListView);
        searchView = (SearchView) findViewById(R.id.searchView);
        noDataImageView = (ImageView) findViewById(R.id.search_content_no_dataImageView);
        noNetworkImageView = (ImageView) findViewById(R.id.search_content_no_serverImageView);

    }

    private void initSpinner() {
        areaSpinner = (Spinner) findViewById(R.id.selectAreaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.area_StringArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter);
        areaSpinner.setOnItemSelectedListener(this);

    }

    private void initNavigationView(String name, String username) {
        navigationView = (NavigationView) findViewById(R.id.navigation_header_container); //for navigation view item action
        nav_drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        nav_toggle = new ActionBarDrawerToggle(this, nav_drawerLayout, R.string.open, R.string.close);
        nav_drawerLayout.addDrawerListener(nav_toggle);
        nav_toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);     //for navigation view item action

        View navHeader = navigationView.getHeaderView(0);
        TextView profileName = (TextView) navHeader.findViewById(R.id.profile_name);
        TextView profileUsername = (TextView) navHeader.findViewById(R.id.profile_username);
        profileName.setText(name);
        profileUsername.setText(username);
    }

    private void initAreaRecyclerView() {
        int[] images = {R.drawable.religious, R.drawable.historical, R.drawable.natural, R.drawable.adventurous};
        String[] titles = {"Religious Places", "Historical Places", "Natural Places", "Adventurous Places"};
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.areaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        AreaRecyclerViewAdapter adapter = new AreaRecyclerViewAdapter(images, titles,userID);
        recyclerView.setAdapter(adapter);
    }

    private void initRecommendedAreaRecyclerView() {
        final String retreiveRatingURL = "http://pasang1422.000webhostapp.com/retreive_rating.php";
        query = "select place_id,placeName,place_description,placeType,placeLat,placeLong,rating from places where ";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, retreiveRatingURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray ratings = new JSONArray(response);
                    JSONObject ratingsJSONObject = ratings.getJSONObject(0);
                    placeLength = ratingsJSONObject.getInt("place_count");
                    userLength = ratingsJSONObject.getInt("user_count");
                    ratingMatrix = new double[userLength][placeLength];
                    boolean similar = true;

                    for (int i = 0; i < ratings.length(); i++) {
                        if (i > 0)
                            ratingsJSONObject = ratings.getJSONObject(i);
                        int user_id = ratingsJSONObject.getInt("user_id");
                        int place_id = ratingsJSONObject.getInt("place_id");
                        double ratingValue = ratingsJSONObject.getDouble("rating");
                        ratingMatrix[user_id - 1][place_id - 1] = ratingValue;
                    }

                    Users[] user = new Users[userLength];
                    for (int i = 0; i < userLength; i++) {
                        user[i] = new Users(ratingMatrix[i], placeLength);
                        user[i].setRatedItem();
                    }

                    for (placeToRate = 0; placeToRate < placeLength; placeToRate++) {
                        int knn=5;
                        if (user[userID - 1].ratedItem[placeToRate] == false) {

                            HashMap<Integer, Double> similarAndRated = new HashMap<>();

                            for (int i = 0; i < userLength; i++) {
                                if (i != userID - 1) {
                                    if (user[i].ratedItem[placeToRate]) {
                                        double sim = getUserSimilarity(ratingMatrix[userID - 1], ratingMatrix[i]);
                                        if(sim>0){
                                            similarAndRated.put(i + 1,sim);
                                            knn--;
                                            if(knn==0)
                                                break;
                                        }
                                    }
                                }
                            }
                            if(similarAndRated.isEmpty()){
                                similar = false;
                                break;
                            }


                            HashMap<Integer, Double> similarRatedAndSorted = similarAndRated
                                    .entrySet()
                                    .stream()
                                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                                    .collect(
                                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                    LinkedHashMap::new));

                            System.out.println("map after sorting by values in descending order: "
                                    + similarRatedAndSorted);

                            predictedRating.put(placeToRate + 1, getPredictedValue(similarRatedAndSorted, user, placeToRate));
                        }

                    }

                    if(similar){
                        HashMap<Integer, Double> predictedSortedRated = predictedRating
                                .entrySet()
                                .stream()
                                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                                .collect(
                                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                LinkedHashMap::new));

                        Set<Integer> keys = predictedSortedRated.keySet();
                        int count =0;
                        for (Integer key : keys) {
                            query += String.format("place_id = %d", key);
                            if (count < predictedSortedRated.size()-1) {
                                query += " or ";
                            } else {
                                query += ";";
                                break;
                            }
                            count++;
                        }
                        show(query);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No similar users!!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(stringRequest);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nav_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String itemPosition = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), itemPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.dashboard:
                Toast.makeText(this, "This is dashboard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.wish_list:
                Toast.makeText(this, "This is wishlist", Toast.LENGTH_SHORT).show();
                break;
            case R.id.note:
                Toast.makeText(this, "This is note", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "This is settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rate_us:
                Toast.makeText(this, "This is rate us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.log_out:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;

        }
        return false;
    }

    public static double getUserSimilarity(double[] x, double[] y) {

        double avgX;
        double avgY;
        double sumX = 0, sumY = 0, sumSqX = 0, sumSqY = 0, sumXY = 0;
        int countX = 0, countY = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > 0) {
                sumX += x[i];
                countX++;
            }
            if (y[i] > 0) {
                sumY += y[i];
                countY++;
            }
        }
        avgX = sumX / countX;
        avgY = sumY / countY;

        double[] newX = new double[x.length];
        double[] newY = new double[y.length];
        sumX = 0;
        sumY = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] > 0) {
                newX[i] = x[i] - avgX;
            }
            if (y[i] > 0) {
                newY[i] = y[i] - avgY;
            }

            sumX += newX[i];
            sumY += newY[i];
            sumSqX += newX[i] * newX[i];
            sumSqY += newY[i] * newY[i];
            sumXY += newX[i] * newY[i];

        }
        double simValue = (x.length * sumXY - sumX * sumY) / (Math.sqrt((x.length * sumSqX - sumX * sumX) * (x.length * sumSqY - sumY * sumY)));
        return simValue;
    }

    public static double getPredictedValue(HashMap<Integer, Double> similarRatedAndSorted, Users[] user, int itemToRate) {
        double predictedRating;
        double topFormula = 0, lowerFormula = 0;
        Set<Integer> keys = similarRatedAndSorted.keySet();
        for (Integer key : keys) {
            topFormula += similarRatedAndSorted.get(key) * user[key - 1].ratesOfItem[itemToRate];
            lowerFormula += similarRatedAndSorted.get(key);
        }
        predictedRating = topFormula / lowerFormula;
        return predictedRating;
    }

    private void show(String query) {
        final String retreiveTitleURL = "http://pasang1422.000webhostapp.com/retreive_title.php";
        try {
            String url = String.format("%s?query=%s", retreiveTitleURL, query);
            final String requestQuery = query;
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            placeList.add(new RecommendedPlaces(
                                    jsonObject.getInt("place_id"),
                                    jsonObject.getString("placeName"),
                                    jsonObject.getString("place_description"),
                                    jsonObject.getString("placeType"),
                                    jsonObject.getDouble("placeLat"),
                                    jsonObject.getDouble("placeLong"),
                                    jsonObject.getDouble("rating")


                            ));
                        }
                        adapter = new RelatedAreaRecyclerViewAdapter(placeList,userID,getApplicationContext());
                        recyclerView.setAdapter(adapter);
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
