package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.Varieties_Models;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.Dialog_Master;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class Varieties_Adapter extends RecyclerView.Adapter<Varieties_Adapter.ViewHolder> {

    private List<Varieties_Models> items = new ArrayList<>();
    private Context ctx;
    String lang_id;


    public Varieties_Adapter(Context context, List<Varieties_Models> items) {
        this.items = items;
        this.lang_id = lang_id;
        ctx = context;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView title, disc;
        public ImageView icons;


        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            disc = (TextView) v.findViewById(R.id.disc);
            icons = (ImageView) v.findViewById(R.id.variety_pic);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.varieties_row, parent, false);


        vh = new ViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Varieties_Models Position_Object = items.get(position);


            view.title.setText(Position_Object.getTitle());

            if (Position_Object.getDisc() != null) {
                view.disc.setText(Position_Object.getDisc());
                Drawable img = ctx.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp);
                img.setBounds(0, 0, 60, 60);
                view.disc.setCompoundDrawables(null, null, img, null);
            } else {
                view.disc.setText("");
                view.disc.setCompoundDrawables(null, null, null, null);

            }

            Glide.with(ctx).load(Position_Object.getImages()).error(R.drawable.leaf).into(view.icons);


            view.itemView.setOnClickListener(new DebouncedOnClickListener(1500) {
                @Override
                public void onDebouncedClick(View v) {
                    List<String> Data;

                    try {
                        JSONArray jsonArray = new JSONArray(Position_Object.getDetailed_des());
                        Data = new ArrayList<>();

                        if (jsonArray.length() > 0) {
                            if (!jsonArray.get(0).toString().equalsIgnoreCase("")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Data.add(jsonArray.get(i).toString().trim());
                                }

                                Dialog_Master obj = new Dialog_Master();
                                obj.dialog(ctx, Data, Position_Object.getTitle());
                            } else {
                                Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ctx, R.string.discnotavailable, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<Varieties_Models> list) {
        items = list;
        notifyDataSetChanged();
    }


}
