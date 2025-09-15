package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.ZoneRemedy;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Remedy_Adapter extends RecyclerView.Adapter<Remedy_Adapter.ViewHolder> {

    private List<ZoneRemedy> items = new ArrayList<>();
    private Context ctx;


    public Remedy_Adapter(Context context, List<ZoneRemedy> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView order_id, date, fertilizerTv;


        public ViewHolder(View view) {
            super(view);
            order_id = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            fertilizerTv = view.findViewById(R.id.fertilizer);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.remedy_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final ZoneRemedy history = items.get(position);
            holder.order_id.setText(history.getActivityType());
            holder.date.setText(history.getDateAction());
            holder.fertilizerTv.setText(history.getFertilizerUsed());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
