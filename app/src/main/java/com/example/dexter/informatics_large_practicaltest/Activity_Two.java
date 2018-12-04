package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dexter.informatics_large_practicaltest.Model.Markersonmap;
import com.example.dexter.informatics_large_practicaltest.Model.Rate;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Activity_Two extends AppCompatActivity {

    FirebaseUser firebaseUser3;
    FirebaseUser firebaseUser;
    Button buttonOfDOLR;
    Button buttonOfPENY;
    Button buttonOfQUID;
    Button buttonOfSHIL;
    Button buttonOfGOLD;

    TextView rateOfDOLR;
    TextView rateOfPENY;
    TextView rateOfQUID;
    TextView rateOfSHIL;
    TextView rateOfInterest;

    int numberOfDOLR;
    int numberOfPENY;
    int numberOfQUID;
    int numberOfSHIL;


    Double rateDOLR;
    Double ratePENY;
    Double rateQUID;
    Double rateSHIL;
    Double rateINTEREST;

    Double floatValueOfDolr;
    Double floatValueOfPeny;
    Double floatValueOfQuid;
    Double floatValueOfShil;
    private Toolbar mTopToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberOfDOLR = 0;
        numberOfPENY = 0;
        numberOfQUID = 0;
        numberOfSHIL = 0;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_two);
        buttonOfDOLR = findViewById(R.id.button_dolr);
        buttonOfPENY = findViewById(R.id.button_peny);
        buttonOfQUID = findViewById(R.id.button_quid);
        buttonOfSHIL = findViewById(R.id.button_shil);
        buttonOfGOLD = findViewById(R.id.button_gold);

        rateOfDOLR = findViewById(R.id.dolr_rate);
        rateOfPENY = findViewById(R.id.peny_rate);
        rateOfQUID = findViewById(R.id.quid_rate);
        rateOfSHIL = findViewById(R.id.shil_rate);
        rateOfInterest = findViewById(R.id.interest_rate);

        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);






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
                    overridePendingTransition(0,0);
                    finish();
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
                    finish();
                    break;
                case R.id.navigation_welcome:
                    Intent intent4 = new Intent(Activity_Two.this, MainActivity.class);
                    intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent4,0);
                    overridePendingTransition(0,0);
                    finish();
                    break;


            }
            return false;
        });

        ShowValues showValues = new ShowValues();
        showValues.execute();

        buttonOfDOLR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Two.this, DolrActivity.class));
            }
        });

        buttonOfPENY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Two.this, PenyActivity.class));
            }
        });

        buttonOfQUID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Two.this, QuidActivity.class));
            }
        });

        buttonOfSHIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_Two.this, ShilActivity.class));
            }
        });




    }

    private class ShowValues extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {


            CollectionReference informationOfCoins = FirebaseFirestore.getInstance()
                    .collection("Icons").document(firebaseUser.getUid())
                    .collection("features");
            informationOfCoins.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        numberOfDOLR = 0;
                        numberOfPENY = 0;
                        numberOfQUID = 0;
                        numberOfSHIL = 0;
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            Markersonmap markersonmap1 = d.toObject(Markersonmap.class);
                            if ((markersonmap1.getIsCollected_1() == 1)&&(markersonmap1.getIsStored()==0)&& (markersonmap1.getIsInMarket() == 0)) {
                                switch (markersonmap1.getCurrency()) {
                                    case "DOLR":
                                        numberOfDOLR += 1;
                                        break;
                                    case "PENY":
                                        numberOfPENY += 1;
                                        break;
                                    case "QUID":
                                        numberOfQUID += 1;
                                        break;
                                    case "SHIL":
                                        numberOfSHIL += 1;
                                        break;
                                }
                            }
                        }
                        buttonOfDOLR.setText("Value: "+String.format("%.2f", floatValueOfDolr)+"  "+"Number: "+Integer.toString(numberOfDOLR));
                        buttonOfPENY.setText("Value: "+String.format("%.2f", floatValueOfPeny)+"  "+"Number: "+Integer.toString(numberOfPENY));
                        buttonOfQUID.setText("Value: "+String.format("%.2f", floatValueOfQuid)+"  "+"Number: "+Integer.toString(numberOfQUID));
                        buttonOfSHIL.setText("Value: "+String.format("%.2f", floatValueOfShil)+"  "+"Number: "+Integer.toString(numberOfSHIL));
                    }
                }
            });


            CollectionReference collectionReference3 = FirebaseFirestore.getInstance()
                    .collection("Icons").document(firebaseUser.getUid())
                    .collection("features");
            collectionReference3.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null) {
                        floatValueOfDolr = 0.0;
                        floatValueOfPeny = 0.0;
                        floatValueOfQuid = 0.0;
                        floatValueOfShil = 0.0;
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            Markersonmap markersonmap1 = d.toObject(Markersonmap.class);
                            if ((markersonmap1.getIsCollected_1() == 1)&&(markersonmap1.getIsStored() == 0)&& (markersonmap1.getIsInMarket() == 0)) {
                                switch (markersonmap1.getCurrency()) {
                                    case "DOLR":
                                        floatValueOfDolr += Double.parseDouble(markersonmap1.getValue());
                                        break;

                                    case "PENY":
                                        floatValueOfPeny += Double.parseDouble(markersonmap1.getValue());
                                        break;

                                    case "QUID":
                                        floatValueOfQuid += Double.parseDouble(markersonmap1.getValue());
                                        break;

                                    case "SHIL":
                                        floatValueOfShil += Double.parseDouble(markersonmap1.getValue());
                                        break;

                                }
                            }
                        }
                        buttonOfDOLR.setText("Value: "+String.format("%.2f", floatValueOfDolr)+"  "+"Number: "+Integer.toString(numberOfDOLR));
                        buttonOfPENY.setText("Value: "+String.format("%.2f", floatValueOfPeny)+"  "+"Number: "+Integer.toString(numberOfPENY));
                        buttonOfQUID.setText("Value: "+String.format("%.2f", floatValueOfQuid)+"  "+"Number: "+Integer.toString(numberOfQUID));
                        buttonOfSHIL.setText("Value: "+String.format("%.2f", floatValueOfShil)+"  "+"Number: "+Integer.toString(numberOfSHIL));
                    }
                }
            });



            DocumentReference documentReference1 = FirebaseFirestore.getInstance()
                    .collection("Icons").document(firebaseUser.getUid())
                    .collection("Rates").document("rate");
            documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        Rate rate = documentSnapshot.toObject(Rate.class);
                        rateDOLR = rate.getDOLR();
                        ratePENY = rate.getPENY();
                        rateQUID = rate.getQUID();
                        rateSHIL = rate.getSHIL();
                        rateOfDOLR.setText(String.format("%.4f", rateDOLR));
                        rateOfPENY.setText(String.format("%.4f", ratePENY));
                        rateOfQUID.setText(String.format("%.4f", rateQUID));
                        rateOfSHIL.setText(String.format("%.4f", rateSHIL));
                    }
                }
            });

            DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                    .collection("User").document(firebaseUser.getUid());
            documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        buttonOfGOLD.setText("Gold   value: "+user.getGOLD());
                    }
                }
            });



            buttonOfDOLR.setText("Value: "+String.format("%.2f", floatValueOfDolr)+"  "+"Number: "+Integer.toString(numberOfDOLR));
            buttonOfPENY.setText("Value: "+String.format("%.2f", floatValueOfPeny)+"  "+"Number: "+Integer.toString(numberOfPENY));
            buttonOfQUID.setText("Value: "+String.format("%.2f", floatValueOfQuid)+"  "+"Number: "+Integer.toString(numberOfQUID));
            buttonOfSHIL.setText("Value: "+String.format("%.2f", floatValueOfShil)+"  "+"Number: "+Integer.toString(numberOfSHIL));

            return null;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser3 = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser3 == null){
            Intent intent = new Intent(Activity_Two.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_market, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(Activity_Two.this, MarketActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
