package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Activity_Two extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    Intent intent1 = new Intent(Activity_Two.this, Activity_One.class);
//                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent1,0);
//                    overridePendingTransition(0,0);
                    startActivity(intent1);
                    overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in);
                    break;

                case R.id.navigation_coins:
//                    Intent intent2 = new Intent(Activity_Two.this, Activity_Two.class);
//                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent2,0);
//                    overridePendingTransition(0,0);
                    break;

                case R.id.navigation_friends:
                    Intent intent3 = new Intent(Activity_Two.this, Acitivity_Three.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent3,0);
                    overridePendingTransition(0,0);
                    break;
                case R.id.navigation_welcome:
                    Intent intent4 = new Intent(Activity_Two.this, MainActivity.class);
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent4,0);
                    overridePendingTransition(0,0);
                    break;


            }
            return false;
        });




    }
}