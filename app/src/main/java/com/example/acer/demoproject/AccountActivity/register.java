package com.example.acer.demoproject.AccountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acer on 4/22/2019.
 */

public class register extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Button registerBtn = (Button) findViewById(R.id.registerButton);
        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final EditText addressEditText = (EditText) findViewById(R.id.addressEditText);
        final EditText unameEditText = (EditText) findViewById(R.id.unameEditText);
        final EditText pwEditText = (EditText) findViewById(R.id.pwEditText);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEditText.getText().toString();
                final String address = addressEditText.getText().toString();
                final String username = unameEditText.getText().toString();
                final String password = pwEditText.getText().toString();

                if (name.isEmpty() || address.isEmpty()|| username.isEmpty() || password.isEmpty())
                    Toast.makeText(getApplicationContext(), "Field should not be empty", Toast.LENGTH_SHORT).show();
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "Registration successful!!!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(register.this, LoginActivity.class);
                                    register.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                                    builder.setMessage("Register Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(name, address, username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(register.this);
                    queue.add(registerRequest);
                }


            }
        });

    }


}
