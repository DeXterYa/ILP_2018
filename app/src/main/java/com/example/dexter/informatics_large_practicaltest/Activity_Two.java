package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

    TextView rateOfDOLR;
    TextView rateOfPENY;
    TextView rateOfQUID;
    TextView rateOfSHIL;
    TextView rateOfInterest;

    int numberOfDOLR;
    int numberOfPENY;
    int numberOfQUID;
    int numberOfSHIL;

    Double valueOfDOLR;
    Double valueOfPENY;
    Double valueOfQUID;
    Double valueOfSHIL;

    Double rateDOLR;
    Double ratePENY;
    Double rateQUID;
    Double rateSHIL;
    Double rateINTEREST;


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

        rateOfDOLR = findViewById(R.id.dolr_rate);
        rateOfPENY = findViewById(R.id.peny_rate);
        rateOfQUID = findViewById(R.id.quid_rate);
        rateOfSHIL = findViewById(R.id.shil_rate);
        rateOfInterest = findViewById(R.id.interest_rate);



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

            }
        });

        buttonOfPENY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonOfQUID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonOfSHIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            Markersonmap markersonmap1 = d.toObject(Markersonmap.class);
                            String fkdlsf = d.getId();
                            Double fdgg = markersonmap1.getLatitude();
                            int yuir = markersonmap1.getIsCollected_1();
                            if (markersonmap1.getIsCollected_1() == 1) {
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
                        buttonOfDOLR.setText("Value: "+String.format("%.2f", valueOfDOLR)+"  "+"Number: "+Integer.toString(numberOfDOLR));
                        buttonOfPENY.setText("Value: "+String.format("%.2f", valueOfPENY)+"  "+"Number: "+Integer.toString(numberOfPENY));
                        buttonOfQUID.setText("Value: "+String.format("%.2f", valueOfQUID)+"  "+"Number: "+Integer.toString(numberOfQUID));
                        buttonOfSHIL.setText("Value: "+String.format("%.2f", valueOfSHIL)+"  "+"Number: "+Integer.toString(numberOfSHIL));
                    }
                }
            });

            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("User").document(firebaseUser.getUid());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        valueOfDOLR = user.getDOLR();
                        valueOfPENY = user.getPENY();
                        valueOfQUID = user.getQUID();
                        valueOfSHIL = user.getSHIL();
                        buttonOfDOLR.setText("Value: "+String.format("%.2f", valueOfDOLR)+"  "+"Number: "+Integer.toString(numberOfDOLR));
                        buttonOfPENY.setText("Value: "+String.format("%.2f", valueOfPENY)+"  "+"Number: "+Integer.toString(numberOfPENY));
                        buttonOfQUID.setText("Value: "+String.format("%.2f", valueOfQUID)+"  "+"Number: "+Integer.toString(numberOfQUID));
                        buttonOfSHIL.setText("Value: "+String.format("%.2f", valueOfSHIL)+"  "+"Number: "+Integer.toString(numberOfSHIL));
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



            buttonOfDOLR.setText("Value: "+String.format("%.2f", valueOfDOLR)+"  "+"Number: "+Integer.toString(numberOfDOLR));
            buttonOfPENY.setText("Value: "+String.format("%.2f", valueOfPENY)+"  "+"Number: "+Integer.toString(numberOfPENY));
            buttonOfQUID.setText("Value: "+String.format("%.2f", valueOfQUID)+"  "+"Number: "+Integer.toString(numberOfQUID));
            buttonOfSHIL.setText("Value: "+String.format("%.2f", valueOfSHIL)+"  "+"Number: "+Integer.toString(numberOfSHIL));

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
}
