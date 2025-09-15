package com.ascentya.AsgriV2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.SchemeBanks;
import com.ascentya.AsgriV2.Models.Scheme_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Scheme_Adapter extends RecyclerView.Adapter<Scheme_Adapter.ViewHolder> {

    private List<Scheme_Model> items;
    private Context ctx;


    public Scheme_Adapter(Context context, List<Scheme_Model> items) {
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
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;

            view.c_name.setText(items.get(position).getScheme_name());
//            view.icon.setImageResource(items.get(position).getScheme_icon());

            Glide.with(ctx).load(items.get(position).getScheme_icon()).into(view.icon);

            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {

                   /* ((BaseActivity) ctx).openWithSubscription(SchemeBanks.class,
                            Components.findComponent(items.get(position).getScheme_value(), Modules.SCHEME),
                            ModuleManager.ACCESS.VIEW,
                            Pair.create("scheme_id", items.get(position).getScheme_id()));*/


                    Intent i = new Intent(ctx, SchemeBanks.class);
                        i.putExtra("scheme_id", items.get(position).getScheme_id());
                        ctx.startActivity(i);


//                    if (((BaseActivity) ctx).getSubscriptionManager().canView(
//                            Components.findComponent(
//                                    items.get(position).getScheme_value(),
//                                    Modules.SCHEME))){
//                        Intent i = new Intent(ctx, SchemeBanks.class);
//                        i.putExtra("scheme_id", items.get(position).getScheme_id());
//                        ctx.startActivity(i);
//                    }

                   /* if (((BaseActivity) ctx).checkPrivilege(VIEW_SCHEME)){

                    }*/
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
