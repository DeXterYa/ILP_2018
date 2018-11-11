package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dexter.informatics_large_practicaltest.Adapter.MessageAdapter;
import com.example.dexter.informatics_large_practicaltest.Model.Chat;
import com.example.dexter.informatics_large_practicaltest.Model.Chatlist;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    CircleImageView profile_image;
    TextView username;

    FirebaseUser fUser;
    DatabaseReference reference;
    DocumentReference documentReference;
    CollectionReference collectionReference;
    Firebase firebase;


    ImageButton btn_send;
    EditText text_send;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;
    @ServerTimestamp  public Date time;


    private int hint = 0;
    private int is_seen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


            recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        if (hint == 1) {
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                }
                            });
                        }
                    }
                }
            });





        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        fUser = FirebaseAuth.getInstance().getCurrentUser();



        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Please input something",
                            Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });



        documentReference = FirebaseFirestore.getInstance().collection("User").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                User user = documentSnapshot.toObject(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into (profile_image);

                }

                readMessages(fUser.getUid(), userid, user.getImageURL());

            }
        });

        seenMessage(userid);

//        reference = FirebaseDatabase.getInstance().getReference("User").child(userid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                username.setText(user.getUsername());
//                if (user.getImageURL().equals("default")){
//                    profile_image.setImageResource(R.mipmap.ic_launcher);
//                } else{
//                    Glide.with(MessageActivity.this).load(user.getImageURL()).into (profile_image);
//
//                }
//
//                readMessages(fUser.getUid(), userid, user.getImageURL());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


    public static Date getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return now;
    }



    private void seenMessage(String userid) {
        collectionReference = FirebaseFirestore.getInstance().collection("Chats");
        Query query =  collectionReference.whereEqualTo("receiver", fUser.getUid())
                .whereEqualTo("sender", userid);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (is_seen == 0) {
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            Chat chat = d.toObject(Chat.class);
                            String id = d.getId();
                            documentReference = FirebaseFirestore.getInstance().collection("Chats").document(id);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("isseen", true);
                            documentReference.update(map);

                        }
                    }
                }
            }
        });
    }


    private void sendMessage(String sender, String receiver, String message) {



        collectionReference = FirebaseFirestore.getInstance().collection("Chats");
        Date time_stamp = getCurrentTimeStamp();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("time_stamp", FieldValue.serverTimestamp());
        hashMap.put("isseen", false);
        collectionReference.add(hashMap);


        DocumentReference chatting1 = FirebaseFirestore.getInstance().collection("Chatlist").document(sender);
        HashMap<String, String> Map1 = new HashMap<>();
        chatting1.set(Map1);


        DocumentReference chatRef1 = FirebaseFirestore.getInstance()
                .collection("Chatlist").document(sender)
                .collection("Users").document(receiver);

        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("id", receiver);
        hashMap1.put("time_stamp", FieldValue.serverTimestamp());
        chatRef1.set(hashMap1);


        DocumentReference chatting2 = FirebaseFirestore.getInstance().collection("Chatlist").document(receiver);
        HashMap<String, String> Map2 = new HashMap<>();
        chatting2.set(Map2);

        DocumentReference chatRef2 = FirebaseFirestore.getInstance()
                .collection("Chatlist").document(receiver)
                .collection("Users").document(sender);

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("id", sender);
        hashMap2.put("time_stamp", FieldValue.serverTimestamp());
        chatRef2.set(hashMap2);


//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("receiver", receiver);
//        hashMap.put("message", message);
//        collectionReference.add(hashMap);


//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("receiver", receiver);
//        hashMap.put("message", message);
//
//        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages (String myid, String userid, String imageURL) {
        mChat = new ArrayList<>();



        collectionReference = FirebaseFirestore.getInstance().collection("Chats");
        Query order = collectionReference.orderBy("time_stamp",Query.Direction.ASCENDING);
        order.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                mChat.clear();
                if (queryDocumentSnapshots != null) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                    hint = 1;


                    for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Chat chat = d.toObject(Chat.class);
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            mChat.add(chat);

                        }

                        messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageURL);
                        recyclerView.setAdapter(messageAdapter);
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());

                    }

                } else {
                        hint = 0;

                    }
                }






            }
        });

//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mChat.clear();
//                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
//                    Chat chat = snapshot.getValue(Chat.class);
//                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
//                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
//                        mChat.add(chat);
//
//                    }
//
//                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageURL);
//                    recyclerView.setAdapter(messageAdapter);
//                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }


    private void status(String status) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("User").document(fUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        documentReference.update(map);


    }


    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        is_seen = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        is_seen = 1;
    }
}
