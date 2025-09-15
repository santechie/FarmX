package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Interfaces_Class.Suggest_Interface;
import com.ascentya.AsgriV2.Models.SuggetstedCrops_Model;
import com.ascentya.AsgriV2.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SuggestedCrops_Adapter extends RecyclerView.Adapter<SuggestedCrops_Adapter.ViewHolder> {

    private List<SuggetstedCrops_Model> items = new ArrayList<>();
    private Context ctx;

    private Suggest_Interface listener;

    public SuggestedCrops_Adapter(Context context, List<SuggetstedCrops_Model> items, Suggest_Interface listner) {
        this.items = items;
        ctx = context;
        this.listener = listner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView icon_name;
        ImageView icon;

        public ViewHolder(View v) {
            super(v);
            icon_name = (TextView) v.findViewById(R.id.icon_name);
            icon = (ImageView) v.findViewById(R.id.icon);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestcrop_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (holder != null) {
            ((ViewHolder) holder).icon_name.setText(items.get(position).getName());
            System.out.println("jkdbjkdshfs"+items.get(position).getName());
           // view.icon.setBackgroundResource(Integer.parseInt(items.get(position).getIcon()));
            Glide.with(ctx).load(items.get(position).getIcon()).into(((ViewHolder) holder).icon);

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.crop_suggest(items.get(position));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
