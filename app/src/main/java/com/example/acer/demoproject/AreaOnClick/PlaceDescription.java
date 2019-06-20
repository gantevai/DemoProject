package com.example.acer.demoproject.AreaOnClick;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.AccountActivity.LoginActivity;
import com.example.acer.demoproject.Adapter.PlaceDescriptionSliderAdapter;
import com.example.acer.demoproject.Adapter.RecommendedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.RelatedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.GoogleMaps.GoogleMapsActivity;
import com.example.acer.demoproject.MainActivity;
import com.example.acer.demoproject.Maps.SearchLocationActivity;
import com.example.acer.demoproject.Model.RecommendedPlaces;
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PlaceDescription extends AppCompatActivity {
    List<RecommendedPlaces> placeList;
    private String query;
    private RequestQueue queue;
    public RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    //Declarations
    private ViewPager place_descriptionViewPager;
    private LinearLayout place_descriptionDotsLayout;
    private PlaceDescriptionSliderAdapter sliderAdapter;

    private TextView[] slideLayoutDots;
    private Button nextBtn;
    private Button backBtn;
    private RatingBar ratingBar;
    private Button getlocationBtn, getlocationBtn2;
    private TextView place_description_heading, descriptionTextView;
    private int currentPage;

    String place_description, place_type, title;
    Double place_lat, place_long;
    double[][] ratingMatrix;
    int selectedPlace_id;
    int placeLength, userLength;
    int totalRecommendedPlaces = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);
        recyclerView = findViewById(R.id.related_placesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        queue = Volley.newRequestQueue(this);
        placeList = new ArrayList<>();

        Intent intent = getIntent();
        title = intent.getStringExtra("title_name");
        selectedPlace_id = Integer.parseInt(intent.getStringExtra("place_id"));
        place_lat = Double.parseDouble(intent.getStringExtra("place_lat"));
        place_long = Double.parseDouble(intent.getStringExtra("place_long"));
        place_description = intent.getStringExtra("place_description");
        place_type = intent.getStringExtra("place_type");
        initialize();
        initializeSlideAdapter();
        initializeRelatedPlaceRecyclerView();


        recyclerView.setAdapter(adapter);
        //OnClickListeners
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_descriptionViewPager.setCurrentItem(currentPage - 1);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_descriptionViewPager.setCurrentItem(currentPage + 1);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(PlaceDescription.this, "Stars: " + rating, Toast.LENGTH_SHORT).show();
                ratingBar.setRating(rating);
            }
        });

        getlocationBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GoogleMapsActivity.class);
                intent.putExtra("address", place_description_heading.getText());
                intent.putExtra("place_lat", String.valueOf(place_lat));
                intent.putExtra("place_long", String.valueOf(place_long));
                intent.putExtra("place_description", place_description);
                intent.putExtra("place_type", place_type);
                v.getContext().startActivity(intent);
            }
        });

    }

    private void initializeIntent() {

    }

    private void initializeRelatedPlaceRecyclerView() {
        final String retreiveRatingURL = "http://pasang1422.000webhostapp.com/retreive_rating.php";
        query = "select place_id,placeName,place_description,placeType,placeLat,placeLong from places where ";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, retreiveRatingURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray ratings = new JSONArray(response);
                    JSONObject ratingsJSONObject = ratings.getJSONObject(0);
                    placeLength = ratingsJSONObject.getInt("place_count");
                    userLength = ratingsJSONObject.getInt("user_count");
                    ratingMatrix = new double[placeLength][userLength];


                    for (int i = 0; i < ratings.length(); i++) {
                        if (i > 0)
                            ratingsJSONObject = ratings.getJSONObject(i);
                        int user_id = ratingsJSONObject.getInt("user_id");
                        int place_id = ratingsJSONObject.getInt("place_id");
                        double ratingValue = ratingsJSONObject.getDouble("rating");
                        ratingMatrix[place_id - 1][user_id - 1] = ratingValue;
                    }

                    double[] selectedplaceRow = ratingMatrix[selectedPlace_id - 1];
                    HashMap<Integer, Double> similarity = new HashMap<>();

                    for (int i = 0; i < placeLength; i++) {
                        if (i != selectedPlace_id - 1) {
                            similarity.put(i + 1, getSimilarityValue(selectedplaceRow, ratingMatrix[i]));
                        }
                    }
                    List<Map.Entry<Integer, Double>> greatest = findGreatest(similarity, totalRecommendedPlaces);
                    query += "";
                    int count = 1;
                    for (Map.Entry<Integer, Double> entry : greatest) {
                        query += String.format("place_id = %d", entry.getKey());
                        if (count <= totalRecommendedPlaces - 1) {
                            query += " or ";
                        } else {
                            query += ";";
                        }
                        count++;
                    }
                    show(query);
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

    private static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>>
    findGreatest(Map<K, V> map, int n) {
        Comparator<? super Map.Entry<K, V>> comparator =
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e0, Map.Entry<K, V> e1) {
                        V v0 = e0.getValue();
                        V v1 = e1.getValue();
                        return v0.compareTo(v1);
                    }
                };
        PriorityQueue<Map.Entry<K, V>> highest =
                new PriorityQueue<Map.Entry<K, V>>(n, comparator);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            highest.offer(entry);
            while (highest.size() > n) {
                highest.poll();
            }
        }

        List<Map.Entry<K, V>> result = new ArrayList<Map.Entry<K, V>>();
        while (highest.size() > 0) {
            result.add(highest.poll());
        }
        return result;
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
                                    jsonObject.getDouble("placeLong")


                            ));
                        }
                        adapter = new RelatedAreaRecyclerViewAdapter(placeList, getApplicationContext());
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

    private double getSimilarityValue(double[] x, double[] y) {
//        x = new double[]{1,0,3,0,0,5,0,0,5,0,4,0};
//        y = new double[]{0,0,5,4,0,0,4,0,0,2,1,3};
        double avgX = 0;
        double avgY = 0;
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
            if (x[i] > 0)
                newX[i] = x[i] - avgX;
            if (y[i] > 0)
                newY[i] = y[i] - avgY;

            sumX += newX[i];
            sumY += newY[i];
            sumSqX += newX[i] * newX[i];
            sumSqY += newY[i] * newY[i];
            sumXY += newX[i] * newY[i];

        }
        double simValue = (x.length * sumXY - sumX * sumY) / (Math.sqrt((x.length * sumSqX - sumX * sumX) * (x.length * sumSqY - sumY * sumY)));
        return simValue;
    }

    private void initializeSlideAdapter() {
        sliderAdapter = new PlaceDescriptionSliderAdapter(selectedPlace_id, this);
        place_descriptionViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        place_descriptionViewPager.addOnPageChangeListener(viewListener);

    }

    private void initialize() {
        place_descriptionViewPager = (ViewPager) findViewById(R.id.place_descriptionViewPager);
        place_descriptionDotsLayout = (LinearLayout) findViewById(R.id.place_descriptionDotsLayout);

        backBtn = (Button) findViewById(R.id.backBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        getlocationBtn = (Button) findViewById(R.id.getlocationBtn);
        getlocationBtn2 = (Button) findViewById(R.id.getlocationBtn2);
        place_description_heading = (TextView) findViewById(R.id.place_description_heading);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        place_description_heading.setText(title);
        descriptionTextView.setText(place_description);
    }

    public void addDotsIndicator(int position) {
        slideLayoutDots = new TextView[3];
        place_descriptionDotsLayout.removeAllViews();

        for (int i = 0; i < slideLayoutDots.length; i++) {
            slideLayoutDots[i] = new TextView(this);
            slideLayoutDots[i].setText(Html.fromHtml("&#8226"));
            slideLayoutDots[i].setTextSize(35);
            slideLayoutDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));

            place_descriptionDotsLayout.addView(slideLayoutDots[i]);
        }

        if (slideLayoutDots.length > 0) {
            slideLayoutDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;

            if (position == 0) {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("");
            } else if (position == slideLayoutDots.length - 1) {
                nextBtn.setEnabled(false);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);

                nextBtn.setText("");
                backBtn.setText("Back");
            } else {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("Next");
                backBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}



