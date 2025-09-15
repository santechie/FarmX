package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.CropEssentials_Model;
import com.ascentya.AsgriV2.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Mycrops_EssentialAdapter extends RecyclerView.Adapter<Mycrops_EssentialAdapter.ViewHolder> {

    private List<CropEssentials_Model> items = new ArrayList<>();
    private Context ctx;


    public Mycrops_EssentialAdapter(Context context, List<CropEssentials_Model> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView title, value;
        ImageView icon;
        ImageView status;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            icon = (ImageView) v.findViewById(R.id.icon);
            status = (ImageView) v.findViewById(R.id.status);
            value = (TextView) v.findViewById(R.id.value);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycrops_essential_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;


            view.value.setText("Actual value " + items.get(position).getActual_value());
            view.title.setText(items.get(position).getTitle());

            view.icon.setBackgroundResource(items.get(position).getIcon());
//            view.status.setBackgroundResource(items.get(position).getStatusicon());
//            if (items.get(position).isSuccess()) {
//                view.status.setBackground(ContextCompat.getDrawable(ctx, R.drawable.success));
//
//            } else {
//                view.status.setBackground(ContextCompat.getDrawable(ctx, R.drawable.highlevel));
////                    view.icon.setBackgroundResource(R.drawable.failure);
//            }
//            if(position %2 == 1)
//            {
//                holder.c_name.setTextColor(ctx.getResources().getColor(R.color.subheadlines_colour));
//                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }
//            else
//            {
//                holder.c_name.setTextColor(ctx.getResources().getColor(R.color.orange_delete));
//                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
//            }


//            Glide.with(ctx).load(Webservice.Searchicon).into(view.icon);


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
