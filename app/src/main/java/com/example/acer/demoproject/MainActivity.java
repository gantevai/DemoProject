package com.example.acer.demoproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    EditText unameEditText,pwEditText;
    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname, pw;
                uname = unameEditText.getText().toString().trim();
                pw = pwEditText.getText().toString().trim();
                if(uname == "" || pw == ""){
                    Toast.makeText(getApplicationContext(),"Field should not be empty",Toast.LENGTH_SHORT).show();
                }
                else{

                }


            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,register.class));
            }
        });
    }

    private void initialize() {
        loginBtn = (Button) findViewById(R.id.loginButton);
        unameEditText = (EditText) findViewById(R.id.unameEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);
        registerLink = (TextView) findViewById(R.id.registerlinkTextView);
    }

}
