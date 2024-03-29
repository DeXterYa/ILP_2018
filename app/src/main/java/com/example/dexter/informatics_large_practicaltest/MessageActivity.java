package com.example.dexter.informatics_large_practicaltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.dexter.informatics_large_practicaltest.Adapter.MessageAdapter;
import com.example.dexter.informatics_large_practicaltest.Model.Chat;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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
    private String userid;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }





        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Keep the latest message showing at the bottom
            recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        if (hint == 1) {
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (recyclerView.getAdapter() != null) {
                                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                    }
                                }
                            });
                        }
                    }
                }
            });




        //noinspection CodeBlock2Expr
        toolbar.setNavigationOnClickListener((View v) -> {
                finish();
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        fUser = FirebaseAuth.getInstance().getCurrentUser();



        intent = getIntent();
        userid = intent.getStringExtra("userid");

        btn_send.setOnClickListener((View v) -> {
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fUser.getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "Please input something",
                            Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
        });


        // Show your friend's display image
        documentReference = FirebaseFirestore.getInstance().collection("User").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        username.setText(user.getUsername());
                        if (user.getImageURL().equals("default")) {
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);

                        }

                        readMessages(fUser.getUid(), userid, user.getImageURL());
                    }
                }

            }
        });

        seenMessage(userid);


    }





    // Check if the message has been seen by your friend
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

        // Meanwhile, send the time of the conversation to Firebase
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



    }

    private void readMessages (String myid, String userid, String imageURL) {
        mChat = new ArrayList<>();


        // Order the message according to the time
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
                        if (recyclerView.getAdapter() != null) {
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                        }
                    }

                } else {
                        hint = 0;

                    }
                }






            }
        });


    }


    private void status(String status) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("User").document(fUser.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("status", status);
        documentReference.update(map);


    }

    // Upload the status of the user
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_marketdark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MessageActivity.this, UsermarketActivity.class);
        intent.putExtra("userid",userid);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
