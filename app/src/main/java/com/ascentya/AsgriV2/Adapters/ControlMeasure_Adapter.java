package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.ControlMeasure_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Dialog_Master;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ControlMeasure_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ControlMeasure_Model> items = new ArrayList<>();
    private Context ctx;


    public ControlMeasure_Adapter(Context context, List<ControlMeasure_Model> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image, variety_pic;
        public TextView name;
        public FloatingActionButton controlmeasure;


        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.variety_pic);

            name = (TextView) v.findViewById(R.id.title);
            controlmeasure = (FloatingActionButton) v.findViewById(R.id.controlmeasure);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.controlmeasure_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final ControlMeasure_Model Position_Object = items.get(position);
            view.name.setText(Position_Object.getTitle());

//                Glide.with(ctx).load(Position_Object.getCover_image() ).error(R.drawable.leaf).into(view.variety_pic);


            Glide.with(ctx).load(Position_Object.getCover_image()).error(R.drawable.leaf).into(view.image);

            view.controlmeasure.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    if (Position_Object.getDesc().size() > 0) {
                        Dialog_Master obj = new Dialog_Master();
                        obj.dialog(ctx, Position_Object.getDesc(), Position_Object.getTitle());
                    } else {
                        Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();
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
