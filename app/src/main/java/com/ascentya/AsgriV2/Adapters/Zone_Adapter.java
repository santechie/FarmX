package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ascentya.AsgriV2.Models.Zone_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Remedy_Dialog;
import com.ascentya.AsgriV2.Utils.Reports_Dialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.ascentya.AsgriV2.farmx.my_lands.ReportHistory_Activity;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Zone_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Zone_Model> items = new ArrayList<>();
    private Context ctx;
    private Boolean masterslave;
    String land_id, user_id;


    public Zone_Adapter(Context context, List<Zone_Model> items, Boolean master, String land, String user_id) {
        this.items = items;
        ctx = context;
        this.masterslave = master;
        this.land_id = land;
        this.user_id = user_id;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, status, crop_name;
        FloatingActionButton pests, remedy, history;
        CircleImageView crop_icon;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            crop_name = (TextView) v.findViewById(R.id.crop_name);
            status = (TextView) v.findViewById(R.id.status);
            pests = (FloatingActionButton) v.findViewById(R.id.pests);
            remedy = (FloatingActionButton) v.findViewById(R.id.remedy);
            history = (FloatingActionButton) v.findViewById(R.id.history);
            crop_icon = (CircleImageView) v.findViewById(R.id.crop_icon);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Zone_Model Position_Object = items.get(position);

            view.title.setText("Zone name : " + Position_Object.getZone_name());


            view.crop_name.setText("Crop : " + Webservice.Data_crops.get(searchname(Integer.parseInt(Position_Object.getCrop_name()))).getName());
            Glide.with(ctx).load("https://vrjaitraders.com/ard_farmx/" + Position_Object.getCrop_icons_images()).into(view.crop_icon);


            view.remedy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent i = new Intent(ctx, Farmx_Mycro.class);
//                    ctx.startActivity(i);
                    Remedy_Dialog obj = new Remedy_Dialog();
                    obj.dialog(ctx, "Remedy", land_id, items.get(position).getZone_id(), user_id);

                }
            });

            view.history.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    Intent i = new Intent(ctx, ReportHistory_Activity.class);
                    i.putExtra("zone", items.get(position).getZone_id());
                    i.putExtra("land", land_id);
                    i.putExtra("crop_name", view.crop_name.getText().toString());
                    i.putExtra("zone_name", view.title.getText().toString());
                    ctx.startActivity(i);

                }
            });

            view.pests.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    Reports_Dialog obj = new Reports_Dialog();
                    obj.dialog(ctx, "Report", land_id, null, items.get(position).getZone_id(), user_id);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private Integer searchname(Integer data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            Integer unitString = Integer.parseInt(Webservice.Data_crops.get(i).getCrop_id());


            if (unitString.equals(data)) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getCrop_id();


            if (unitString.equals(data.toLowerCase())) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }
}