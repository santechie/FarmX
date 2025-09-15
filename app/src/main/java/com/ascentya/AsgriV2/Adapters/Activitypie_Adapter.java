package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Interfaces_Class.MyInterface;
import com.ascentya.AsgriV2.Models.Farmx_Finance;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Activitypie_Adapter extends RecyclerView.Adapter<Activitypie_Adapter.ViewHolder> {

    private List<Farmx_Finance> items = new ArrayList<>();
    private Context ctx;
    private MyInterface listener;
    Boolean hide;


    public Activitypie_Adapter(Context context, List<Farmx_Finance> items) {

        ctx = context;
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView name, activity;
        CardView card;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            activity = (TextView) v.findViewById(R.id.activity);
            card = (CardView) v.findViewById(R.id.card);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activitypie_items, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            view.name.setText(items.get(position).getName());
            view.activity.setText(String.format("%.2f", items.get(position).getCost()));
            view.card.setCardBackgroundColor(items.get(position).getColour());

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}
