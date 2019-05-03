package com.example.acer.demoproject.AreaOnClick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.acer.demoproject.Adapter.AreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.PlaceListRecyclerViewAdapter;
import com.example.acer.demoproject.R;

public class PlaceList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        Intent intent = getIntent();
        String area_category = intent.getStringExtra("area_category");

        TextView area_category_heading = (TextView)findViewById(R.id.area_category_heading);
        area_category_heading.setText(area_category);

        int[] images = {R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous,R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous,R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous};
        String[] titles = {"Religious Places","Historical Places","Natural Places","Adventurous Places","Religious Places","Historical Places","Natural Places","Adventurous Places","Religious Places","Historical Places","Natural Places","Adventurous Places"};
        String[] subtitles = {"Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu","Kathmandu"};


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.place_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        PlaceListRecyclerViewAdapter adapter = new PlaceListRecyclerViewAdapter(images,titles,subtitles);
        recyclerView.setAdapter(adapter);
    }
}

