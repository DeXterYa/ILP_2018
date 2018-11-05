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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;

    FirebaseUser fuser;
    DatabaseReference reference;

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

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();

                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) ) {
                        usersList.add(chat.getSender());
                    }

                    if (chat.getSender().equals(fuser.getUid())){
                        usersList.add(chat.getReceiver());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }


    private void readChats() {
        mUser  =  new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (String id :usersList) {
                        if (user.getId().equals(id)) {
                            if (mUser.size() != 0) {
                                for (User userl : mUser) {
                                    if (!user.getId().equals(userl.getId())) {
                                        mUser.add(user);
                                    }
                                }
                            } else {
                                mUser.add(user);
                            }
                        }
                    }

                }
                userAdapter = new UserAdapter(getContext(), mUser);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}