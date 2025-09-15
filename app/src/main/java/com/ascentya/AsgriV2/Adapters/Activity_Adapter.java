package com.ascentya.AsgriV2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.Edit_Activity;
import com.ascentya.AsgriV2.Interfaces_Class.MyInterface;
import com.ascentya.AsgriV2.Models.Activity_Cat_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Activity_Details_DIalog;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ProductUtils;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Activity_Adapter extends RecyclerView.Adapter<Activity_Adapter.ViewHolder> {

    private Action action;

    public Activity_Adapter(Action action){
        this.action = action;
    }

    private List<Activity_Cat_Model> items = new ArrayList<>();
    private Context ctx;
    private MyInterface listener;
    ImageView icon;
    Boolean hide;
    String image_icon;
    int itemLayoutResId = R.layout.activity_row;

    public Activity_Adapter(Context context, List<Activity_Cat_Model> items, String image_icon) {
        this.items = items;
        ctx = context;
        this.image_icon = image_icon;

    }

    public Activity_Adapter(Context context, Action action, List<Activity_Cat_Model> items, int itemLayoutResId) {
        this.items = items;
        ctx = context;
        this.image_icon = image_icon;
        this.itemLayoutResId = itemLayoutResId;
        this.action = action;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView cat_type, activity_type, totalcost, date;
        ImageView info, edit;




        public ViewHolder(Action action, View v) {
            super(v);
            cat_type = (TextView) v.findViewById(R.id.cat_type);
            activity_type = (TextView) v.findViewById(R.id.activity_type);
            totalcost = (TextView) v.findViewById(R.id.totalcost);
            date = (TextView) v.findViewById(R.id.date);
            info = (ImageView) v.findViewById(R.id.info);
            icon = (ImageView) v.findViewById(R.id.icon);
            edit = (ImageView) v.findViewById(R.id.edit);

//            itemView.findViewById(R.id.edit).setVisibility(action.canUpdateActivity() ? View.VISIBLE : View.VISIBLE);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayoutResId, parent, false);
        vh = new ViewHolder(action, v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof ViewHolder) {

            ViewHolder view = (ViewHolder) holder;

            if (items.get(position).getService_id().equalsIgnoreCase("1")) {
//
            }

            view.cat_type.setText(items.get(position).getPrepare_type());
            view.totalcost.setText(ProductUtils.getPrice(items.get(position).getTotal_amount()));
            view.date.setText(DateUtils.splitDate(items.get(position).getStart_date()));
            view.activity_type.setText(items.get(position).getCrop_type());
            Glide.with(ctx).load(image_icon).into(icon);

            if (!action.canUpdateActivity()) view.edit.setVisibility(View.INVISIBLE);

            view.edit.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {

                   // if (!action.canEdit()) return;
                    System.out.println("Item: " + GsonUtils.getGson().toJson(items.get(position)));
                    Intent i = new Intent(ctx, Edit_Activity.class);
                    i.putExtra("act", items.get(position));
                    i.putExtra("crop", items.get(position).getCrop_id());
                    i.putExtra("land", items.get(position).getLand_id());
                    i.putExtra("lc_id", items.get(position).getLc_id());
                    i.putExtra("cat", items.get(position).getService_id());
                    i.putExtra("crop_type", items.get(position).getCrop_type());
                    ctx.startActivity(i);
                }
            });

            view.info.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    Activity_Details_DIalog obj = new Activity_Details_DIalog();
                    obj.dialog(ctx, items.get(position).getPrepare_type(), items.get(position));
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    public interface Action{
        boolean canUpdateActivity();
        boolean canEdit();
    }
}