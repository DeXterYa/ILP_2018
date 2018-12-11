package com.example.dexter.informatics_large_practicaltest.Fragments;



import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.dexter.informatics_large_practicaltest.Adapter.UserAdapter;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.example.dexter.informatics_large_practicaltest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import java.lang.CharSequence;


public class UserFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_users;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String s) {

        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseFirestore.getInstance().collection("User").orderBy("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mUsers.clear();
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        User user = d.toObject(User.class);
                        if (fuser != null) {
                            if (!user.getId().equals(fuser.getUid())) {
                                mUsers.add(user);
                            }
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers,false, true);
                    recyclerView.setAdapter(userAdapter);
                }
            }
        });
    }

    private void readUsers() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("User");
        collectionReference.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots,  FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    if (search_users.getText().toString().equals("")) {
                        mUsers.clear();
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            User user = d.toObject(User.class);
                            assert user != null;
                            assert firebaseUser != null;
                            String try1 = user.getId();
                            String try2 = firebaseUser.getUid();
                            if (!try1.equals(try2)) {
                                mUsers.add(user);
                            }
                        }
                        userAdapter = new UserAdapter(getContext(), mUsers, false, true);
                        recyclerView.setAdapter(userAdapter);
                    }
                }


                }


            });






}
}
