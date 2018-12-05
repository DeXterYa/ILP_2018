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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class MarketActivity extends AppCompatActivity implements ActionMode.Callback{


    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private List<Integer> selectedIds = new ArrayList<>();
    private List<Coin> mcoin;
    private CoinAdapter adapter;
    private FirebaseUser firebaseUser;
    private int count;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        count = 0;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        adapter = new CoinAdapter(this, getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Market");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));

        actionMode = startActionMode(MarketActivity.this);
        actionMode.setTitle("Choose coins");

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
                        actionMode = startActionMode(MarketActivity.this);
                    }

                }
                multiSelect(position);
            }
        }));

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
                    list.clear();
                    count = 0;
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
        inflater.inflate(R.menu.menu_wallet, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_wallet:
                for (Coin coin_market : mcoin) {
                    if (selectedIds.contains(coin_market.getId())) {
                        DocumentReference documentReference2 = FirebaseFirestore.getInstance()
                                .collection("Icons").document(firebaseUser.getUid())
                                .collection("features").document(coin_market.getTitle2());
                        HashMap<String, Object> update2 = new HashMap<>();
                        update2.put("isInMarket", 0);
                        documentReference2.update(update2);
                    }
                }
                finish();
                startActivity(getIntent());
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<Integer>());
    }


}
