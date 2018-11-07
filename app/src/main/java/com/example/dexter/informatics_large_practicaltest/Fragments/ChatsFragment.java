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

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        collectionReference = FirebaseFirestore.getInstance().collection("Chats");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                usersList.clear();
                if (queryDocumentSnapshots != null) {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            Chat chat = d.toObject(Chat.class);
                            if (chat.getReceiver().equals(fuser.getUid()) ) {
                                usersList.add(chat.getSender());
                            }

                            if (chat.getSender().equals(fuser.getUid())){
                                usersList.add(chat.getReceiver());
                            }
                        }
                        readChats();
                    }
                }

            }
        });


//        documentReference.collection("Chats").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (!queryDocumentSnapshots.isEmpty()) {
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for (DocumentSnapshot d : list) {
//                                Chat chat = d.toObject(Chat.class);
//                                if (chat.getReceiver().equals(fuser.getUid()) ) {
//                                    usersList.add(chat.getSender());
//                                }
//
//                                if (chat.getSender().equals(fuser.getUid())){
//                                    usersList.add(chat.getReceiver());
//                                }
//                            }
//                            readChats();
//                        }
//
//                    }
//                });


//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//
//                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if (chat.getReceiver().equals(fuser.getUid()) ) {
//                        usersList.add(chat.getSender());
//                    }
//
//                    if (chat.getSender().equals(fuser.getUid())){
//                        usersList.add(chat.getReceiver());
//                    }
//                }
//
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return view;
    }


    private void readChats() {
        mUser  =  new ArrayList<>();


        collectionReference = FirebaseFirestore.getInstance().collection("User");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mUser.clear();
                if (queryDocumentSnapshots != null) {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            User user = new User ();
                            user.setId(d.toObject(User.class).getId());
                            user.setImageURL(d.toObject(User.class).getImageURL());
                            user.setStatus(d.toObject(User.class).getStatus());
                            user.setUsername(d.toObject(User.class).getUsername());


                            for (String id :usersList) {
                                if (user.getId().equals(id)) {
                                    if (mUser.size() != 0) {
                                        int i = 0;
                                        int m = 0;
                                        while (i<mUser.size()) {
                                            User user1 = mUser.get(i);
                                            if (user.getId().equals(user1.getId())) {
                                                m=1;
                                            }

                                            i++;
                                        }
                                        if (m == 0) {
                                            mUser.add(user);
                                        }




//                                        for (int i=0; i<mUser.size();i++) {
//                                            User user1 = mUser.get(i);
//                                            if (!user.getId().equals(user1.getId())) {
//                                                mUser.add(user);
//                                            }
//                                        }
//                                        for (User userl : mUser) {
//                                            if (!user.getId().equals(userl.getId())) {
//                                            mUser.add(user);
//                                            }
//                                         }
                                    } else {
                                        mUser.add(user);
                                    }
                                }
                            }
                        }
                        userAdapter = new UserAdapter(getContext(), mUser, true);
                        recyclerView.setAdapter(userAdapter);


                    }
                }

            }
        });

//        reference = FirebaseDatabase.getInstance().getReference("User");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUser.clear();
//                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//
//                    for (String id :usersList) {
//                        if (user.getId().equals(id)) {
//                            if (mUser.size() != 0) {
//                                for (User userl : mUser) {
//                                    if (!user.getId().equals(userl.getId())) {
//                                        mUser.add(user);
//                                    }
//                                }
//                            } else {
//                                mUser.add(user);
//                            }
//                        }
//                    }
//
//                }
//                userAdapter = new UserAdapter(getContext(), mUser);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


}
