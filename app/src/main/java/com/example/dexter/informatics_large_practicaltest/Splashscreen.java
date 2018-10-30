package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {

     ImageView logo;
     TextView motto1, motto2;
     static int splashTimeOut = 5000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        logo = (ImageView) findViewById(R.id.logo);
        motto1 = findViewById((R.id.name));
        motto2 = findViewById((R.id.textmotto));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen.this,ReglogActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplash);
        logo.startAnimation(myanim);
        motto1.startAnimation(myanim);
        motto2.startAnimation(myanim);


    }
}
