package com.example.acer.demoproject.AreaOnClick;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.*;

import com.example.acer.demoproject.Adapter.PlaceDescriptionSliderAdapter;
import com.example.acer.demoproject.Adapter.RecommendedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.RelatedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.GoogleMaps.GoogleMapsActivity;
import com.example.acer.demoproject.Maps.SearchLocationActivity;
import com.example.acer.demoproject.R;
import com.squareup.picasso.Picasso;

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
    int place_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

        Intent intent = getIntent();
        title = intent.getStringExtra("title_name");
        place_id = Integer.parseInt(intent.getStringExtra("place_id"));
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
        int[] images = {R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous,R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous};
        String[] titles = {"Religious Places","Historical Places","Natural Places","Adventurous Places","Religious Places","Historical Places","Natural Places","Adventurous Places"};


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.related_placesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RelatedAreaRecyclerViewAdapter adapter = new RelatedAreaRecyclerViewAdapter(images,titles);
        recyclerView.setAdapter(adapter);
    }

    private void initializeSlideAdapter() {
        sliderAdapter = new PlaceDescriptionSliderAdapter(place_id,this);
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
