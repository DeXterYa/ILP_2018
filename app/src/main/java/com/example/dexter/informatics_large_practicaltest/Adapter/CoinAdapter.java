package com.example.dexter.informatics_large_practicaltest.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dexter.informatics_large_practicaltest.Model.Coin;
import com.example.dexter.informatics_large_practicaltest.R;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.MyViewHolder> {

    private Context context;
    private List<Coin> coins;
    private List<Integer> selectedIds = new ArrayList<>();

    public  CoinAdapter (Context context, List<Coin> coins) {
        this.context = context;
        this.coins = coins;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coin_item, viewGroup, false);
        return  new MyViewHolder(view);
    }
//    @TargetApi(23)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(coins.get(i).getTitle()+"  "+"Value: "+coins.get(i).getValue());
        int id = coins.get(i).getId();
        if (selectedIds.contains(id)) {
            myViewHolder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context, R.color.colorControlActivated)));
        } else {
            myViewHolder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        }
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    public Coin getItem(int position) {
        return coins.get(position);
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView title;
        TextView title2;

        FrameLayout rootView;
        MyViewHolder (View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titlecoin);

            rootView = itemView.findViewById(R.id.root_view);
        }
    }
}











