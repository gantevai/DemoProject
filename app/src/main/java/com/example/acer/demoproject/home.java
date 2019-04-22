package com.example.acer.demoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.acer.demoproject.Model.User;
import com.example.acer.demoproject.Model.UserLocalStore;

/**
 * Created by Acer on 4/22/2019.
 */

public class home extends AppCompatActivity {

    Button logoutBtn;
    EditText display;
    UserLocalStore userLocalStore;

    @Override
    public void onStart(){
        super.onStart();
        if(authenticate()==true)
            displayUserDetails();
    }

    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }

    private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();

        display.setText("hi user");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(home.this,MainActivity.class));
            }
        });
    }

    private void initialize() {
        logoutBtn = (Button) findViewById(R.id.logoutButton);
        userLocalStore = new UserLocalStore(this);
        display = (EditText) findViewById(R.id.displayEditText);
    }
}
