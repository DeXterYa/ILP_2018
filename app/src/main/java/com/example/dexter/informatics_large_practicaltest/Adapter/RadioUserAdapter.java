package com.example.dexter.informatics_large_practicaltest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.dexter.informatics_large_practicaltest.Model.Coin;
import com.example.dexter.informatics_large_practicaltest.R;

import java.util.List;

public class RadioUserAdapter extends RecyclerView.Adapter<RadioUserAdapter.ViewHolder> {
    private List<Coin> offersList;
    private Context context;
    private Coin selectedCoin;
    private int postionNow;

    private int lastSelectedPosition = -1;

    public  RadioUserAdapter (List<Coin> offersListIn, Context ctx) {
        this.offersList = offersListIn;
        this.context = ctx;
    }


    public RadioUserAdapter.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.coinradio_item, parent, false);

        RadioUserAdapter.ViewHolder viewHolder =
                new RadioUserAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RadioUserAdapter.ViewHolder viewHolder, int i) {
        Coin coins = offersList.get(i);
        int id = coins.getId();
        if (selectedCoin != null) {
            if (selectedCoin.getId() == id) {
                viewHolder.selectionState.setChecked(true);
                postionNow = id;
            }
            else {
                viewHolder.selectionState.setChecked(false);
            }
        }
        String string = coins.getTitle() + "   Value: " + coins.getValue();
        viewHolder.title.setText(string);

    }


    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public int getPostionNow() {
        return postionNow;
    }

    public Coin getItem(int position) {
        return offersList.get(position);
    }

    public void setSelectedcoin(Coin coins) {
        this.selectedCoin = coins;
        notifyDataSetChanged();
    }

    public  class  ViewHolder extends  RecyclerView.ViewHolder {
        public TextView title;
        private RadioButton selectionState;

        private ViewHolder (View view) {
            super(view);
            title = view.findViewById(R.id.title);
            selectionState = view.findViewById(R.id.offer_select);

            selectionState.setOnClickListener((View v) ->  {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
            });
        }
    }
}
