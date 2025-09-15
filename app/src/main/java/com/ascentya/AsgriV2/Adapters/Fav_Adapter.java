package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.Fav_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Dialog_Master;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Fav_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Fav_Model> items = new ArrayList<>();
    private Context ctx;


    public Fav_Adapter(Context context, List<Fav_Model> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, disc;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            disc = (TextView) v.findViewById(R.id.disc);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favcondition_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Fav_Model Position_Object = items.get(position);

            if (Position_Object.getDescription().size() > 0) {


                view.disc.setText(Position_Object.getDescription().get(0).trim());
                view.title.setText(Position_Object.getTitle());
            }


            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    if (Position_Object.getDescription().size() > 0) {
                        Dialog_Master obj = new Dialog_Master();
                        obj.dialog(ctx, Position_Object.getDescription(), Position_Object.getTitle());
                    } else {
                        Toast.makeText(ctx, "Content are not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
