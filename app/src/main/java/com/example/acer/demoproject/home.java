package com.example.acer.demoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Acer on 4/22/2019.
 */

public class home extends AppCompatActivity {

    Button logoutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,MainActivity.class));
            }
        });
    }

    private void initialize() {
        logoutBtn = (Button) findViewById(R.id.logoutButton);
    }
}
