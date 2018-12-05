package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.dexter.informatics_large_practicaltest.Adapter.RadioUserAdapter;
import com.example.dexter.informatics_large_practicaltest.Model.Coin;
import com.example.dexter.informatics_large_practicaltest.Model.Markersonmap;
import com.example.dexter.informatics_large_practicaltest.Model.Rate;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class ConversionActivity extends AppCompatActivity {

    Intent intent;
    String currency;
    private RecyclerView offerRecyclerView;
    int count;
    private FirebaseUser firebaseUser;

    Coin selectedCoin;


    private List<Coin> mcoin;

    private Double rateDOLR;
    private Double ratePENY;
    private Double rateQUID;
    private Double rateSHIL;

    Double value;
    Double gold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        currency = intent.getStringExtra("currency");



        offerRecyclerView = findViewById(R.id.recycle_view);

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        offerRecyclerView.setLayoutManager(recyclerLayoutManager);

        RadioUserAdapter recyclerViewAdapter = new
                RadioUserAdapter(getList(),this);
        offerRecyclerView.setAdapter(recyclerViewAdapter);



//        DividerItemDecoration dividerItemDecoration =
//                new DividerItemDecoration(offerRecyclerView.getContext(),
//                        recyclerLayoutManager.getOrientation());
//        offerRecyclerView.addItemDecoration(dividerItemDecoration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currency + " in your wallet");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DocumentReference documentReference1 = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("Rates").document("rate");
        documentReference1.addSnapshotListener(ConversionActivity.this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Rate rate = documentSnapshot.toObject(Rate.class);
                    rateDOLR = rate.getDOLR();
                    ratePENY = rate.getPENY();
                    rateQUID = rate.getQUID();
                    rateSHIL = rate.getSHIL();
                }
            }
        });

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("User").document(firebaseUser.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    gold = user.getGOLD();
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedCoin != null) {

                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                            .collection("User").document(firebaseUser.getUid());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null) {
                                User user = documentSnapshot.toObject(User.class);
                                switch (currency) {
                                    case "DOLR":
                                        value = 0.0;
                                        value = user.getDOLR() + Double.parseDouble(selectedCoin.getValue());
                                        value = value * rateDOLR;
                                        HashMap<String, Object> update1 = new HashMap<>();
                                        update1.put("DOLR", 0.0);
                                        update1.put("GOLD", (gold + value));
                                        documentReference.update(update1);
                                        break;

                                    case "PENY":
                                        value = 0.0;
                                        value = user.getPENY() + Double.parseDouble(selectedCoin.getValue());
                                        value = value * ratePENY;
                                        HashMap<String, Object> update2 = new HashMap<>();
                                        update2.put("PENY", 0.0);
                                        update2.put("GOLD", (gold + value));
                                        documentReference.update(update2);
                                        break;

                                    case "QUID":
                                        value = 0.0;
                                        value = user.getQUID() + Double.parseDouble(selectedCoin.getValue());
                                        value = value * rateQUID;
                                        HashMap<String, Object> update3 = new HashMap<>();
                                        update3.put("QUID", 0.0);
                                        update3.put("GOLD", (gold + value));
                                        documentReference.update(update3);
                                        break;

                                    case "SHIL":
                                        value = 0.0;
                                        value = user.getSHIL() + Double.parseDouble(selectedCoin.getValue());
                                        value = value * rateSHIL;
                                        HashMap<String, Object> update4 = new HashMap<>();
                                        update4.put("SHIL", 0.0);
                                        update4.put("GOLD", (gold + value));
                                        documentReference.update(update4);
                                        break;
                                }

                                DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                                        .collection("Icons").document(firebaseUser.getUid())
                                        .collection("features").document(selectedCoin.getTitle());
                                HashMap<String, Object> update = new HashMap<>();
                                update.put("isStored", 1);
                                documentReference2.update(update);


                            }
                        }
                    });
                    finish();
                    startActivity(getIntent());
                }
            }
        });


        offerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, offerRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedCoin = recyclerViewAdapter.getItem(position);
                recyclerViewAdapter.setSelectedcoin(selectedCoin);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                selectedCoin = recyclerViewAdapter.getItem(position);
                recyclerViewAdapter.setSelectedcoin(selectedCoin);
            }
        }));
    }



    private List<Coin> getList() {
        List<Coin> list = new ArrayList<>();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("features");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    list.clear();
                    count = 0;
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                        if ((markersonmap2.getIsCollected_1() == 1)&&(markersonmap2.getIsStored() == 0)&& (markersonmap2.getIsInMarket() == 0) && (Double.parseDouble(markersonmap2.getValue()) >= 5.0)) {
                            if(markersonmap2.getCurrency().equals(currency)) {
                                count += 1;
                                list.add(new Coin(d.getId(), markersonmap2.getValue(), count, ""));
                            }
                        }

                    }
                    mcoin = new ArrayList<>(list);

                }
            }
        });
        return list;
    }

}
