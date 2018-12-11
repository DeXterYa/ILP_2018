package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.dexter.informatics_large_practicaltest.Adapter.CoinAdapter;
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

public class UsermarketActivity extends AppCompatActivity implements ActionMode.Callback{

    Intent intent;
    private String userid;

    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private List<Integer> selectedIds = new ArrayList<>();
    private List<Coin> mcoin;
    private CoinAdapter adapter;
    private FirebaseUser firebaseUser;
    private int count;

    private Double valueOfDolr;
    private Double valueOfPeny;
    private Double valueOfQuid;
    private Double valueOfShil;

    private Double valueOfDolrChange;
    private Double valueOfPenyChange;
    private Double valueOfQuidChange;
    private Double valueOfShilChange;

    private Double rateOfDolr;
    private Double rateOfPeny;
    private Double rateOfQuid;
    private Double rateOfShil;

    private Double GoldOfBuyer;
    private Double GoldOfSeller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermarket);

        intent = getIntent();
        userid = intent.getStringExtra("userid");

        count = 0;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        adapter = new CoinAdapter(this, getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Coins on sale");
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));


        actionMode = startActionMode(UsermarketActivity.this);
        if (actionMode != null) {
            actionMode.setTitle("Choose coins");
        }

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode ==null) {
                        actionMode = startActionMode(UsermarketActivity.this);
                    }

                }
                multiSelect(position);
            }
        }));


        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("Rates").document("rate");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    Rate rate = documentSnapshot.toObject(Rate.class);
                    if (rate != null) {
                        rateOfDolr = rate.getDOLR();
                        rateOfPeny = rate.getPENY();
                        rateOfQuid = rate.getQUID();
                        rateOfShil = rate.getSHIL();
                    }
                }
            }
        });

        DocumentReference documentReference1 = FirebaseFirestore.getInstance()
                .collection("User").document(firebaseUser.getUid());
        documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        GoldOfBuyer = user.getGOLD();
                    }
                }
            }
        });

        DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                .collection("User").document(userid);
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user1 = documentSnapshot.toObject(User.class);
                    if (user1 != null) {
                        GoldOfSeller = user1.getGOLD();
                    }
                }
            }
        });
    }

    private void multiSelect(int position) {
        Coin coin = adapter.getItem(position);
        if (coin != null) {
            if (actionMode != null) {
                if (selectedIds.contains(coin.getId()))
                    selectedIds.remove(Integer.valueOf(coin.getId()));
                else
                    selectedIds.add(coin.getId());

                if (selectedIds.size() > 0) {
                    actionMode.setTitle(String.valueOf(selectedIds.size()));
                } else {
                    actionMode.setTitle("Choose coins");
//                    actionMode.finish();
                }
                adapter.setSelectedIds(selectedIds);
            }
        }
    }

    // From Firebase get the coins on sale
    private List<Coin> getList(){

        List<Coin> list = new ArrayList<>();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(userid)
                .collection("features");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    count = 0;
                    list.clear();
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                        if ((markersonmap2.getIsCollected_1() == 1)&&(markersonmap2.getIsStored() == 0)&& (markersonmap2.getIsInMarket() == 1)&&(markersonmap2.getCurrency().equals("DOLR"))) {
                            count += 1;
                            list.add(new Coin(markersonmap2.getCurrency(), markersonmap2.getValue(), count, d.getId()));
                        }

                    }

                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                        if ((markersonmap2.getIsCollected_1() == 1)&&(markersonmap2.getIsStored() == 0)&& (markersonmap2.getIsInMarket() == 1)&&(markersonmap2.getCurrency().equals("PENY"))) {
                            count += 1;
                            list.add(new Coin(markersonmap2.getCurrency(), markersonmap2.getValue(), count, d.getId()));
                        }

                    }
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                        if ((markersonmap2.getIsCollected_1() == 1)&&(markersonmap2.getIsStored() == 0)&& (markersonmap2.getIsInMarket() == 1)&&(markersonmap2.getCurrency().equals("QUID"))) {
                            count += 1;
                            list.add(new Coin(markersonmap2.getCurrency(), markersonmap2.getValue(), count, d.getId()));
                        }

                    }
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                        if ((markersonmap2.getIsCollected_1() == 1)&&(markersonmap2.getIsStored() == 0)&& (markersonmap2.getIsInMarket() == 1)&&(markersonmap2.getCurrency().equals("SHIL"))) {
                            count += 1;
                            list.add(new Coin(markersonmap2.getCurrency(), markersonmap2.getValue(), count, d.getId()));
                        }

                    }
                    mcoin = new ArrayList<>(list);

                }
            }
        });
        return list;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_marketfriend, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_buy:

                for (Coin coin_market : mcoin) {
                    if (GoldOfBuyer > 0) {
                        if (selectedIds.contains(coin_market.getId())) {


                            DocumentReference documentReference = FirebaseFirestore.getInstance()
                                    .collection("User").document(firebaseUser.getUid());
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    valueOfDolr = 0.0;
                                    valueOfPeny = 0.0;
                                    valueOfQuid = 0.0;
                                    valueOfShil = 0.0;
                                    valueOfDolrChange = 0.0;
                                    valueOfPenyChange = 0.0;
                                    valueOfQuidChange = 0.0;
                                    valueOfShilChange = 0.0;
                                    User user = documentSnapshot.toObject(User.class);
                                    if (user != null) {
                                        switch (coin_market.getTitle()) {
                                            case "DOLR":
                                                valueOfDolr = user.getDOLR();
                                                valueOfDolr += Double.parseDouble(coin_market.getValue());
                                                valueOfDolrChange = Double.parseDouble(coin_market.getValue());
                                                break;
                                            case "PENY":
                                                valueOfPeny = user.getPENY();
                                                valueOfPeny += Double.parseDouble(coin_market.getValue());
                                                valueOfPenyChange = Double.parseDouble(coin_market.getValue());
                                                break;
                                            case "QUID":
                                                valueOfQuid = user.getQUID();
                                                valueOfQuid += Double.parseDouble(coin_market.getValue());
                                                valueOfQuidChange = Double.parseDouble(coin_market.getValue());
                                                break;
                                            case "SHIL":
                                                valueOfShil = user.getSHIL();
                                                valueOfShil += Double.parseDouble(coin_market.getValue());
                                                valueOfShilChange = Double.parseDouble(coin_market.getValue());
                                                break;
                                        }
                                    }

                                    uploadChange();

                                    DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                                            .collection("Icons").document(userid)
                                            .collection("features").document(coin_market.getTitle2());
                                    HashMap<String, Object> update2 = new HashMap<>();
                                    update2.put("isInMarket", 0);
                                    update2.put("isStored", 1);
                                    documentReference2.update(update2);
                                }
                            });




                        }
                    }
                }
                finish();
                startActivity(getIntent());
                return true;
        }
        return false;
    }

    private void uploadChange() {

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("User").document(firebaseUser.getUid());
        HashMap<String, Object> update = new HashMap<>();
        update.put("DOLR", valueOfDolr);
        update.put("PENY", valueOfPeny);
        update.put("QUID", valueOfQuid);
        update.put("SHIL", valueOfShil);
        documentReference.update(update);

        DocumentReference documentReference1 = FirebaseFirestore.getInstance()
                .collection("User").document(firebaseUser.getUid());
        Double cost = valueOfDolrChange*(rateOfDolr - 8) + valueOfPenyChange*(rateOfPeny - 8) + valueOfQuidChange* (rateOfQuid - 8) + valueOfShilChange * (rateOfShil - 8);
        Double goldChanged = GoldOfBuyer - cost;
        HashMap<String, Object> update2 = new HashMap<>();
        update2.put("GOLD", goldChanged);
        documentReference1.update(update2);

        DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                .collection("User").document(userid);
        Double cost1 = valueOfDolrChange*(rateOfDolr - 8) + valueOfPenyChange*(rateOfPeny - 8) + valueOfQuidChange* (rateOfQuid - 8) + valueOfShilChange * (rateOfShil - 8);
        Double goldChanged1 = GoldOfSeller + cost1;
        HashMap<String, Object> update1 = new HashMap<>();
        update1.put("GOLD", goldChanged1);
        documentReference2.update(update1);


    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<>());
    }

}
