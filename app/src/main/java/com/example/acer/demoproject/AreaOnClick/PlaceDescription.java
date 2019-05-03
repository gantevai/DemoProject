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
import com.example.acer.demoproject.Maps.SearchLocationActivity;
import com.example.acer.demoproject.R;

public class PlaceDescription extends AppCompatActivity {
    //Declarations
    private ViewPager place_descriptionViewPager;
    private LinearLayout place_descriptionDotsLayout;
    private PlaceDescriptionSliderAdapter sliderAdapter;

    private TextView[] slideLayoutDots;
    private Button nextBtn;
    private Button backBtn;
    private RatingBar ratingBar;
    private Button getlocationBtn;
    private TextView place_description_heading;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

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

        getlocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchLocationActivity.class);
                intent.putExtra("address",place_description_heading.getText());
                v.getContext().startActivity(intent);
            }
        });

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
        sliderAdapter = new PlaceDescriptionSliderAdapter(this);
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
        getlocationBtn = (Button) findViewById(R.id.getlocationBtn);
        place_description_heading = (TextView) findViewById(R.id.place_description_heading);
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
