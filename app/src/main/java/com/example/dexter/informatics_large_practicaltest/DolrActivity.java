package com.example.dexter.informatics_large_practicaltest;

import android.app.ActionBar;
import android.os.AsyncTask;
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

public class DolrActivity extends AppCompatActivity implements ActionMode.Callback{


    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private List<Integer> selectedIds = new ArrayList<>();
    List<Coin> mcoin;
    private CoinAdapter adapter;
    FirebaseUser firebaseUser;
    int count;
    Double rate_dolr;

    Double Gold;

    boolean ifaddgold;

    HashMap<String, Object> update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ifaddgold = true;
        count = 0;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.activity_dolr);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        adapter = new CoinAdapter(this, getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DOLR");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionMode = startActionMode(DolrActivity.this);
        actionMode.setTitle("Choose coins");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                        actionMode = startActionMode(DolrActivity.this);
                    }

                }
                multiSelect(position);
            }
        }));

        DocumentReference documentReference6 = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("Rates").document("rate");
        documentReference6.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    Rate rate = documentSnapshot.toObject(Rate.class);
                    rate_dolr = rate.getDOLR();
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

    private List<Coin> getList(){
        count = 0;
        List<Coin> list = new ArrayList<>();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(firebaseUser.getUid())
                .collection("features");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {

                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                        if ((markersonmap2.getIsCollected_1() == 1)&&(markersonmap2.getIsStored() == 0)) {
                            if(markersonmap2.getCurrency().equals("DOLR")) {
                                count += 1;
                                list.add(new Coin(d.getId(), markersonmap2.getValue(), count));
                            }
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
        inflater.inflate(R.menu.menu_select, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_store:
                for (Coin coin_dolr : mcoin) {
                        if (selectedIds.contains(coin_dolr.getId())) {
                            DocumentReference documentReference88 = FirebaseFirestore.getInstance()
                                    .collection("User").document(firebaseUser.getUid());
                            documentReference88.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    if (documentSnapshot != null) {
                                        if (ifaddgold) {
                                            User user = documentSnapshot.toObject(User.class);
                                            Gold = user.getGOLD() + Double.parseDouble(coin_dolr.getValue()) * rate_dolr;
                                            uploadgold();
                                            ifaddgold = false;
                                        }
                                    }
                                }
                            });



                            DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                                    .collection("Icons").document(firebaseUser.getUid())
                                    .collection("features").document(coin_dolr.getTitle());
                            HashMap<String, Object> update2 = new HashMap<>();
                            update2.put("isStored", 1);
                            documentReference2.update(update2);

                    }

                }
                break;
        }



        return true;
    }

    private void uploadgold() {
        DocumentReference documentReference99 = FirebaseFirestore.getInstance()
                .collection("User").document(firebaseUser.getUid());
        update = new HashMap<>();
        update.put("GOLD", Gold);
        documentReference99.update(update);
    }



    private class CalculateGold extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {



            return null;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<Integer>());
    }
}
