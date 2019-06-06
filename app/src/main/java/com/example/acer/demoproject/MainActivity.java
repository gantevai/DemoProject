package com.example.acer.demoproject;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.acer.demoproject.AccountActivity.LoginActivity;
import com.example.acer.demoproject.Adapter.AreaRecyclerViewAdapter;
import com.example.acer.demoproject.Adapter.RecommendedAreaRecyclerViewAdapter;
import com.example.acer.demoproject.RecommendedAreaActivity.RecommendedAreaRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//import com.example.acer.demoproject.Model.User;
//import com.example.acer.demoproject.Model.UserLocalStore;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    // UserLocalStore userLocalStore;
    DrawerLayout nav_drawerLayout;
    ActionBarDrawerToggle nav_toggle;
    Spinner areaSpinner;
    NavigationView navigationView;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

//    @Override
//    public void onStart(){
//        super.onStart();
//        if(authenticate()==true)
//            displayUserDetails();
//    }
//
//    private boolean authenticate() {
//        return userLocalStore.getUserLoggedIn();
//    }
//
//    private void displayUserDetails(){
//        User user = userLocalStore.getLoggedInUser();
//
//        //display.setText("hi user");
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        String address = intent.getStringExtra("address");

        initSpinner();
        initNavigationView(name,username);
        initAreaRecyclerView();
        initRecommendedAreaRecyclerView();


    }

    private void initSpinner() {
        areaSpinner = (Spinner) findViewById(R.id.selectAreaSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.area_StringArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        areaSpinner.setAdapter(adapter);
        areaSpinner.setOnItemSelectedListener(this);

    }

    private void initNavigationView(String name ,String username){
        navigationView = (NavigationView) findViewById(R.id.navigation_header_container); //for navigation view item action
        nav_drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        nav_toggle = new ActionBarDrawerToggle(this, nav_drawerLayout, R.string.open, R.string.close);
        nav_drawerLayout.addDrawerListener(nav_toggle);
        nav_toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);     //for navigation view item action

        View navHeader = navigationView.getHeaderView(0);
        TextView profileName = (TextView)navHeader.findViewById(R.id.profile_name);
        TextView profileUsername = (TextView)navHeader.findViewById(R.id.profile_username);
        profileName.setText(name);
        profileUsername.setText(username);
    }

    private void initAreaRecyclerView() {
        int[] images = {R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous};
        String[] titles = {"Religious Places","Historical Places","Natural Places","Adventurous Places"};
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.areaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        AreaRecyclerViewAdapter adapter = new AreaRecyclerViewAdapter(images,titles);
        recyclerView.setAdapter(adapter);
    }

    private void initRecommendedAreaRecyclerView() {
        int[] images = {R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous,R.drawable.religious,R.drawable.historical,R.drawable.natural,R.drawable.adventurous};
        String[] titles = {"Religious Places","Historical Places","Natural Places","Adventurous Places","Religious Places","Historical Places","Natural Places","Adventurous Places"};
        final String url;
        final String place_name;

        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        String place_name = jsonResponse.getString("place_name");
                        String url = jsonResponse.getString("url");

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        //RecommendedAreaRequest recommendedAreaRequest = new RecommendedAreaRequest();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recommendedAreaRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecommendedAreaRecyclerViewAdapter adapter = new RecommendedAreaRecyclerViewAdapter(images,titles);
        recyclerView.setAdapter(adapter);
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
}
