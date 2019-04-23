package com.example.acer.demoproject;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.acer.demoproject.AccountActivity.LoginActivity;

/**
 * Created by Acer on 4/21/2019.
 */

public class SplashScreen extends AppCompatActivity{

    private ImageView logo;
    private static int splashTimeOut = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.logoImageView);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.my_splash_animation);
        logo.startAnimation(myanim);
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(in);
                finish();

            }
        },splashTimeOut);

    }

}
