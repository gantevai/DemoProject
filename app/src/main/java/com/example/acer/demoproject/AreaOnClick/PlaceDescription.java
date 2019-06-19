package com.example.acer.demoproject.AreaOnClick;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.AccountActivity.LoginActivity;
import com.example.acer.demoproject.Adapter.PlaceDescriptionSliderAdapter;
import com.example.acer.demoproject.Adapter.RecommendedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.RelatedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.GoogleMaps.GoogleMapsActivity;
import com.example.acer.demoproject.MainActivity;
import com.example.acer.demoproject.Maps.SearchLocationActivity;
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class PlaceDescription extends AppCompatActivity {
    //Declarations
    private ViewPager place_descriptionViewPager;
    private LinearLayout place_descriptionDotsLayout;
    private PlaceDescriptionSliderAdapter sliderAdapter;

    private TextView[] slideLayoutDots;
    private Button nextBtn;
    private Button backBtn;
    private RatingBar ratingBar;
    private Button getlocationBtn,getlocationBtn2;
    private TextView place_description_heading,descriptionTextView;
    private int currentPage;

    String place_description,place_type,title;
    Double place_lat,place_long;
    double[][] ratingMatrix;
    int selectedPlace_id;
    int placeLength=7 , userLength=20;
    int[] recommendPlaces_Id, sortedPlacesImageId = new int[5];
    String[] recommendPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

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
                Toast.makeText(PlaceDescription.this,"Stars: "+rating,Toast.LENGTH_SHORT).show();
                ratingBar.setRating(rating);
            }
        });

//        getlocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SearchLocationActivity.class);
//                intent.putExtra("address",place_description_heading.getText());
//                v.getContext().startActivity(intent);
//            }
//        });
        getlocationBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GoogleMapsActivity.class);
                intent.putExtra("address",place_description_heading.getText());
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerView = findViewById(R.id.related_placesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ;
//        String[] titles = {"Religious Places","Historical Places","Natural Places","Adventurous Places","Religious Places","Historical Places","Natural Places","Adventurous Places"};
        //algorithm
        final String retreiveRatingURL = "http://pasang1422.000webhostapp.com/retreive_rating.php";
        final String retreiveTitleURL = "http://pasang1422.000webhostapp.com/retreive_title.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, retreiveRatingURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray ratings = new JSONArray(response);
                    ratingMatrix = new double[placeLength][userLength];

                    for (int i = 0; i < ratings.length(); i++) {
                        JSONObject ratingsJSONObject = ratings.getJSONObject(i);
                        int user_id = ratingsJSONObject.getInt("user_id");
                        int place_id = ratingsJSONObject.getInt("place_id");
                        double ratingValue = ratingsJSONObject.getDouble("rating");
                        ratingMatrix[place_id-1][user_id-1] = ratingValue;
                    }

                    double[] selectedplaceRow = ratingMatrix[selectedPlace_id-1];
                    double[] similarity = new double[placeLength];

                    for(int i=0;i<placeLength;i++){
                        if(i!=selectedPlace_id-1) {
                            similarity[i] = getSimilarityValue(selectedplaceRow, ratingMatrix[i]);
                        }
                        else
                            similarity[i] = 100; // to fix the position of the selected place.
                    }

                    double[] sortedSimilarity = similarity;
                    Arrays.sort(sortedSimilarity);
                    String query = "select * from places";
                    for(int i=placeLength-2;i>=0;i--){
                        for(int j=1;j<placeLength;j++){
                            if(sortedSimilarity[i]==similarity[j]){
                                sortedPlacesImageId[i]=j;
                                break;
                            }
                        }
                    }
                    for(int i=0;i<sortedPlacesImageId.length;i++) {
                        if(i==0) {
                            query.concat("where ");
                        }
                        query.concat(String.format("place_id = %d",sortedPlacesImageId[i]+1));
                        if(i<sortedPlacesImageId.length-1) {
                            query.concat(" or ");
                        } else {
                        query.concat(";");
                        }
                    }
                    Response.Listener responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray jsonArray = new JSONArray(response);
                                recommendPlaces_Id = new int[jsonArray.length()];
                                recommendPlaceName = new String[jsonArray.length()];
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                         recommendPlaces_Id[i] = jsonObject.getInt("place_id");
                                         recommendPlaceName[i] = jsonObject.getString("place_id");

                                    }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    CustomQueryRequest request = new CustomQueryRequest(query,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(PlaceDescription.this);
                    queue.add(request);



                    RelatedAreaRecyclerViewAdapter adapter = new RelatedAreaRecyclerViewAdapter(recommendPlaces_Id,recommendPlaceName);
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);


    }

    private double getSimilarityValue(double[] x, double[] y){
//        x = new double[]{1,0,3,0,0,5,0,0,5,0,4,0};
//        y = new double[]{0,0,5,4,0,0,4,0,0,2,1,3};
        double avgX = 0;
        double avgY = 0;
        double sumX=0,sumY=0,sumSqX=0,sumSqY=0,sumXY=0;
        int countX = 0,countY = 0;
        for(int i=0;i<x.length;i++){
            if(x[i] >0) {
                sumX += x[i];
                countX++;
            }
            if(y[i]>0) {
                sumY += y[i];
                countY++;
            }
        }
        avgX = sumX/countX;
        avgY = sumY/countY;

        double[] newX = new double[x.length];
        double[] newY = new double[y.length];

        for(int i=0;i<x.length;i++){
            if(x[i]>0)
                newX[i] = x[i] - avgX;
            if(y[i]>0)
                newY[i] = y[i] - avgY;

            sumX += newX[i];
            sumY += newY[i];
            sumSqX += newX[i]*newX[i];
            sumSqY += newY[i]*newY[i];
            sumXY += newX[i]*newY[i];

        }
        double simValue = (x.length*sumXY-sumX*sumY)/(Math.sqrt((x.length*sumSqX-sumX*sumX)*(x.length*sumSqY-sumY*sumY)));
        return simValue;
    }

    private void initializeSlideAdapter() {
        sliderAdapter = new PlaceDescriptionSliderAdapter(selectedPlace_id,this);
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
