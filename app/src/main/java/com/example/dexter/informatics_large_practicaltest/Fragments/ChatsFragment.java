package com.example.dexter.informatics_large_practicaltest.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dexter.informatics_large_practicaltest.Adapter.UserAdapter;
import com.example.dexter.informatics_large_practicaltest.Model.Chat;
import com.example.dexter.informatics_large_practicaltest.Model.Chatlist;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.example.dexter.informatics_large_practicaltest.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;

    FirebaseUser fuser;
    DatabaseReference reference;

    CollectionReference collectionReference;

    DocumentReference documentReference;

    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        collectionReference = FirebaseFirestore.getInstance().collection("Chatlist")
                .document(fuser.getUid())
                .collection("Users");
        Query order = collectionReference.orderBy("time_stamp",Query.Direction.DESCENDING);

        order.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                usersList.clear();
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Chatlist chatlist = d.toObject(Chatlist.class);
                        usersList.add(chatlist);
                    }

                    chatlist();
                }

            }
        });




        return view;
    }

    private void chatlist() {
        mUser = new ArrayList<>();
        collectionReference = FirebaseFirestore.getInstance().collection("User");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mUser.clear();
                if (queryDocumentSnapshots != null) {
                    for (Chatlist chatlist : usersList) {
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            User user = d.toObject(User.class);
                            if (chatlist.getId().equals(user.getId())) {
                                mUser.add(user);
                            }
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), mUser, true);
                    recyclerView.setAdapter(userAdapter);
                }
            }
        });
    }






}
