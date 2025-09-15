package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Cultivation_Disc_Model;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PreCultivation_Sub_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cultivation_Disc_Model> items = new ArrayList<>();
    private Context ctx;
    Boolean check;


    public PreCultivation_Sub_Adapter(Context context, List<Cultivation_Disc_Model> items, Boolean check) {
        this.items = items;
        this.check = check;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView c_name;


        public ViewHolder(View v) {
            super(v);
            c_name = (TextView) v.findViewById(R.id.disc);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.precultivation_row, parent, false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Cultivation_Disc_Model Position_Object = items.get(position);
            view.c_name.setText(Position_Object.getTitle());


            if (check) {

//                view.c_name.setMaxLines(1);
//                ObjectAnimator animation = ObjectAnimator.ofInt(
//                        view.c_name,
//                        "maxLines",
//                        25);
//                animation.setDuration(1000);
//                animation.start();
                view.c_name.setMaxLines(25);
            } else {

//                ObjectAnimator animation = ObjectAnimator.ofInt(
//                        view.c_name,
//                        "maxLines",
//                        1);
//                animation.setDuration(1000);
//                animation.start();
                view.c_name.setMaxLines(1);
            }


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
