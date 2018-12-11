package com.example.dexter.informatics_large_practicaltest;



import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.dexter.informatics_large_practicaltest.Model.Markersonmap;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

import javax.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DocumentReference documentReference;


    Button dolr_out;
    Button peny_out;
    Button quid_out;
    Button shil_out;

    Button collected;
    Button inMarket;

    TextView level;

    private Double gold;

    int numberOfDolr;
    int numberOfPeny;
    int numberOfQuid;
    int numberOfShil;

    int numberOfCollected;
    int numberOfInMarket;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }



        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        documentReference = FirebaseFirestore.getInstance().collection("User").document(firebaseUser.getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            username.setText(user.getUsername());
                        if (user.getImageURL().equals("default")) {
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);

                        }
                        }
                    }
                }
            }
        });






        // Bottom Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener((item) -> {

            switch (item.getItemId()) {
                case R.id.navigation_map:
                    Intent intent1 = new Intent(MainActivity.this, Activity_One.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent1,0);
                    overridePendingTransition(0,0);
                    finish();

                    break;

                case R.id.navigation_coins:
                    Intent intent2 = new Intent(MainActivity.this, Activity_Two.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent2,0);
                    overridePendingTransition(0,0);
                    finish();

                    break;

                case R.id.navigation_friends:
                    Intent intent3 = new Intent(MainActivity.this, Acitivity_Three.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent3,0);
                    overridePendingTransition(0,0);
                    finish();


                    break;


            }
            return false;
        });



        dolr_out = findViewById(R.id.dolr_out);
        peny_out = findViewById(R.id.peny_out);
        quid_out = findViewById(R.id.quid_out);
        shil_out = findViewById(R.id.shil_out);
        collected = findViewById(R.id.button_collected);
        inMarket = findViewById(R.id.button_inmarket);

        level = findViewById(R.id.level);

        DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                .collection("User").document(firebaseUser.getUid());
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        gold = user.getGOLD();
                        String string = "Level  " + String.format(Locale.getDefault(), "%.0f", gold / 2000);
                        level.setText(string);
                    }
                }
            }
        });

        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("features");
        collectionReference.addSnapshotListener(MainActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    numberOfDolr = 0;
                    numberOfPeny = 0;
                    numberOfQuid = 0;
                    numberOfShil = 0;
                    numberOfCollected = 0;
                    numberOfInMarket = 0;
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap1 = d.toObject(Markersonmap.class);
                        if ((markersonmap1.getIsCollected_1() == 0)&&(markersonmap1.getIsStored() == 0)&& (markersonmap1.getIsInMarket() == 0)) {
                            switch (markersonmap1.getCurrency()) {
                                case "DOLR":
                                    numberOfDolr += 1;
                                    break;
                                case "PENY":
                                    numberOfPeny += 1;
                                    break;
                                case "QUID":
                                    numberOfQuid += 1;
                                    break;
                                case "SHIL":
                                    numberOfShil += 1;
                                    break;
                            }
                        }

                        if ((markersonmap1.getIsCollected_1() == 1)&&(markersonmap1.getIsStored() == 0)&& (markersonmap1.getIsInMarket() == 0)) {
                            numberOfCollected += 1;
                        }

                        if ((markersonmap1.getIsCollected_1() == 1)&&(markersonmap1.getIsStored() == 0)&& (markersonmap1.getIsInMarket() == 1)) {
                            numberOfInMarket += 1;
                        }
                    }

                    String string_1 = "DOLR             NUMBER: " + Integer.toString(numberOfDolr);
                    String string_2 = "PENY             NUMBER: "+Integer.toString(numberOfPeny);
                    String string_3 = "QUID             NUMBER: "+Integer.toString(numberOfQuid);
                    String string_4 = "SHIL             NUMBER: "+Integer.toString(numberOfShil);
                    String string_5 = "Collected: "+Integer.toString(numberOfCollected);
                    String string_6 = "In market: "+Integer.toString(numberOfInMarket);

                    dolr_out.setText(string_1);
                    peny_out.setText(string_2);
                    quid_out.setText(string_3);
                    shil_out.setText(string_4);
                    collected.setText(string_5);
                    inMarket.setText(string_6);


                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuwelcome, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, ReglogActivity.class));
                finish();

                return true;

            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
        }

        return false;
    }
}




