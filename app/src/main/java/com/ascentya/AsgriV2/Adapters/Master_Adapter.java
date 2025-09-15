package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.MylandMaster_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.farmx.my_lands.MyLand_Slaves;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Master_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MylandMaster_Model> items = new ArrayList<>();
    private Context ctx;
    private Boolean masterslave;
    String land_id;


    public Master_Adapter(Context context, List<MylandMaster_Model> items, Boolean master, String land_id) {
        this.items = items;
        ctx = context;
        this.masterslave = master;
        this.land_id = land_id;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, status;
        ImageView status_icon;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            status = (TextView) v.findViewById(R.id.status);
            status_icon = (ImageView) v.findViewById(R.id.status_icon);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final MylandMaster_Model Position_Object = items.get(position);

            view.title.setText(Position_Object.getMastername());

            if (Position_Object.getMaster_status().equalsIgnoreCase("Failure")) {
                view.status.setTextColor(ctx.getResources().getColor(R.color.colorAccentRed));
                view.status_icon.setImageResource(R.drawable.failure_master);
            } else {
                view.status.setTextColor(ctx.getResources().getColor(R.color.green_farmx));
                view.status_icon.setImageResource(R.drawable.success_master);
            }
            view.status.setText(Position_Object.getMaster_status());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(ctx, MyLand_Slaves.class);
                    i.putExtra("pos", items.get(position).getMastername());
                    i.putExtra("land_id", land_id);
                    ctx.startActivity(i);


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}