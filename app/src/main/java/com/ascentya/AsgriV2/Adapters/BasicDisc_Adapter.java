package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Interfaces_Class.MyInterface;
import com.ascentya.AsgriV2.Models.BasicInfo_Model;
import com.ascentya.AsgriV2.Models.Taxanomy_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BasicDisc_Adapter extends RecyclerView.Adapter<BasicDisc_Adapter.ViewHolder> {

    private List<Taxanomy_Model> items = new ArrayList<>();
    private Context ctx;
    private MyInterface listener;
    Boolean hide;


    public BasicDisc_Adapter(Context context, List<BasicInfo_Model> items, MyInterface listners, String check) {
        this.items = items.get(0).getData();
        this.hide = items.get(0).getExpand();
        ctx = context;
        this.listener = listners;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.basicinfo_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            if (hide) {
                view.c_name.setMaxLines(1);
                view.c_name.setEllipsize(TextUtils.TruncateAt.END);
            } else {
                view.c_name.setMaxLines(1000);
            }

            view.c_name.setText(items.get(position).getDisc());

            Glide.with(ctx).load(Webservice.Searchicon).into(view.icon);

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Taxanomy_Model objone = new Taxanomy_Model();


//                    Dialog_Master obj = new Dialog_Master();
//                    obj.dialog(ctx,items.get(position).getDisc(),);


                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}
