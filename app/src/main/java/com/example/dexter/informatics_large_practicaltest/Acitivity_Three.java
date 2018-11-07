package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dexter.informatics_large_practicaltest.Fragments.ChatsFragment;
import com.example.dexter.informatics_large_practicaltest.Fragments.UserFragment;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Acitivity_Three extends AppCompatActivity {
    private FirebaseAuth mAuth;

    FirebaseUser firebaseUser4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        mAuth = FirebaseAuth.getInstance();


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UserFragment(), "Friends");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);






        // Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    Intent intent1 = new Intent(Acitivity_Three.this, Activity_One.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent1,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;

                case R.id.navigation_coins:
                    Intent intent2 = new Intent(Acitivity_Three.this, Activity_Two.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent2,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;

                case R.id.navigation_friends:
//                    Intent intent3 = new Intent(Acitivity_Three.this, Acitivity_Three.class);
//                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivityForResult(intent3,0);
//                    overridePendingTransition(0,0);
                    break;
                case R.id.navigation_welcome:
                    Intent intent4 = new Intent(Acitivity_Three.this, MainActivity.class);
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent4,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;


            }
            return false;
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser4 = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser4 == null){
            Intent intent = new Intent(Acitivity_Three.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment (Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("User").document(firebaseUser4.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        documentReference.update(map);


    }


    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
