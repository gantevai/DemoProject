package com.example.acer.demoproject.AccountActivity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.acer.demoproject.MainActivity;
import com.example.acer.demoproject.Model.User;
import com.example.acer.demoproject.Model.UserLocalStore;
import com.example.acer.demoproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText unameEditText, pwEditText;
    TextView registerLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User(null, null);
                userLocalStore.setUserLoggedIn(true);
                userLocalStore.storeUserData(user);

                final String username = unameEditText.getText().toString();
                final String password = pwEditText.getText().toString();

                if (username.isEmpty() && password.isEmpty())
                    Toast.makeText(getApplicationContext(), "Field should not be empty", Toast.LENGTH_SHORT).show();
                else if(username.isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter the Username", Toast.LENGTH_SHORT).show();
                else if(password.isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter the Password", Toast.LENGTH_SHORT).show();
                else {
                    Response.Listener responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    String name = jsonResponse.getString("name");
                                    String address = jsonResponse.getString("address");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("username", username);
                                    intent.putExtra("address", address);
                                    LoginActivity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }


            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, register.class));
            }
        });


    }

    private void initialize() {
        loginBtn = (Button) findViewById(R.id.loginButton);
        unameEditText = (EditText) findViewById(R.id.unameEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);
        registerLink = (TextView) findViewById(R.id.registerlinkTextView);
        userLocalStore = new UserLocalStore(this);
    }

}
