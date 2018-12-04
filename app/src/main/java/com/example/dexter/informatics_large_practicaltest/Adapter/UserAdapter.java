package com.example.dexter.informatics_large_practicaltest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.dexter.informatics_large_practicaltest.MessageActivity;
import com.example.dexter.informatics_large_practicaltest.Model.Chat;
import com.example.dexter.informatics_large_practicaltest.Model.Markersonmap;
import com.example.dexter.informatics_large_practicaltest.R;
import com.example.dexter.informatics_large_practicaltest.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context  mContext;
    private List<User> mUsers;
    private boolean ischat;
    private boolean ifShown;
    private Double valueOfDolr;
    private Double valueOfPeny;
    private Double valueOfQuid;
    private Double valueOfShil;


    String theLastMessage;

    public UserAdapter(Context mContext, List<User> mUsers, boolean ischat, boolean ifShown) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;
        this.ifShown = ifShown;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        User user = mUsers.get(i);
        viewHolder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_image);
        }


        if (ischat) {
            lastMessage(user.getId(), viewHolder.last_msg);
        } else {
            viewHolder.last_msg.setVisibility(View.GONE);
        }



        if (ischat) {
            if (user.getStatus().equals("online")) {
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            } else {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });

        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Icons").document(user.getId())
                .collection("features");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (ifShown) {
                    if (queryDocumentSnapshots != null) {
                        valueOfDolr = 0.0;
                        valueOfPeny = 0.0;
                        valueOfQuid = 0.0;
                        valueOfShil = 0.0;
                        for (QueryDocumentSnapshot d : queryDocumentSnapshots) {
                            Markersonmap markersonmap2 = d.toObject(Markersonmap.class);
                            if ((markersonmap2.getIsCollected_1() == 1) && (markersonmap2.getIsStored() == 0) && (markersonmap2.getIsInMarket() == 1)) {
                                switch (markersonmap2.getCurrency()) {
                                    case "DOLR":
                                        valueOfDolr += Double.parseDouble(markersonmap2.getValue());
                                        break;
                                    case "PENY":
                                        valueOfPeny += Double.parseDouble(markersonmap2.getValue());
                                        break;
                                    case "QUID":
                                        valueOfQuid += Double.parseDouble(markersonmap2.getValue());
                                        break;
                                    case "SHIL":
                                        valueOfShil += Double.parseDouble(markersonmap2.getValue());
                                        break;
                                }

                            }
                        }
                        viewHolder.dolrimg.setVisibility(View.VISIBLE);
                        viewHolder.penyimg.setVisibility(View.VISIBLE);
                        viewHolder.quidimg.setVisibility(View.VISIBLE);
                        viewHolder.shilimg.setVisibility(View.VISIBLE);
                        viewHolder.dolrvalue.setVisibility(View.VISIBLE);
                        viewHolder.penyvalue.setVisibility(View.VISIBLE);
                        viewHolder.quidvalue.setVisibility(View.VISIBLE);
                        viewHolder.shilvalue.setVisibility(View.VISIBLE);
                        viewHolder.dolrvalue.setText(String.format("%.1f", valueOfDolr));
                        viewHolder.penyvalue.setText(String.format("%.1f", valueOfPeny));
                        viewHolder.quidvalue.setText(String.format("%.1f", valueOfQuid));
                        viewHolder.shilvalue.setText(String.format("%.1f", valueOfShil));

                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private  ImageView img_on;
        private  ImageView img_off;
        private TextView last_msg;
        private TextView dolrvalue;
        private TextView penyvalue;
        private TextView quidvalue;
        private TextView shilvalue;
        private ImageView dolrimg;
        private ImageView penyimg;
        private ImageView quidimg;
        private ImageView shilimg;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            dolrvalue = itemView.findViewById(R.id.dolrvalue);
            penyvalue = itemView.findViewById(R.id.penyvalue);
            quidvalue = itemView.findViewById(R.id.quidvalue);
            shilvalue = itemView.findViewById(R.id.shilvalue);
            dolrimg = itemView.findViewById(R.id.dolrimage);
            penyimg = itemView.findViewById(R.id.penyimage);
            quidimg = itemView.findViewById(R.id.quidimage);
            shilimg = itemView.findViewById(R.id.shilimage);

        }
    }

    private void lastMessage(String userid, TextView last_msg) {
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Chats");
        Query order = collectionReference.orderBy("time_stamp",Query.Direction.DESCENDING);
        order.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    int i = 0;
                    for(QueryDocumentSnapshot d : queryDocumentSnapshots) {
                        Chat chat = d.toObject(Chat.class);
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            if (i == 0) {
                                theLastMessage = chat.getMessage();
                                i = 1;
                            }
                        }

                    }
                }
                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }
        });
    }

}
