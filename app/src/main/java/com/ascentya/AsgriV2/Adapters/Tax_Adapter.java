package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Tax_Adapter extends RecyclerView.Adapter<Tax_Adapter.ViewHolder> {

    private List<String> items;
    private Context ctx;


    public Tax_Adapter(Context context, List<String> items) {
        this.items = items;
        this.ctx = context;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView c_name;
        ImageView icon;

        public ViewHolder(View v) {
            super(v);
            c_name = (TextView) v.findViewById(R.id.disc);
            icon = (ImageView) v.findViewById(R.id.icon);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.taxanomy_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;


            view.c_name.setText(items.get(position));

            Glide.with(ctx).load(Webservice.Searchicon).into(view.icon);


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
